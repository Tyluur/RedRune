package org.redrune.network.master.server.engine.worker;

import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.MasterSession;
import org.redrune.network.master.server.engine.MSEngineWorker;
import org.redrune.network.master.server.network.packet.out.AccountCreationResponsePacketOut;
import org.redrune.network.master.server.network.packet.out.LobbyRepositoryPacketOut;
import org.redrune.network.master.server.network.packet.out.LoginResponsePacketOut;
import org.redrune.network.master.server.world.MSRepository;
import org.redrune.network.master.server.world.MSWorld;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.master.utility.rs.LoginConstants;
import org.redrune.network.master.utility.rs.LoginRequest;
import org.redrune.network.web.http.HTTPFunctions;
import org.redrune.network.web.sql.SQLFunctions;
import org.redrune.network.web.sql.impl.WebLoginDetail;
import org.redrune.utility.backend.CreationResponse;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public final class MSLoginWorker extends MSEngineWorker {
	
	/**
	 * The queue of login requests
	 */
	private final Queue<LoginRequest> loginRequests = new LinkedBlockingQueue<>();
	
	@Override
	public void schedule(ScheduledExecutorService service) {
		service.scheduleWithFixedDelay(this, 0, 100, TimeUnit.MILLISECONDS);
		
		System.out.println("Scheduled the login worker!");
	}
	
	@Override
	public void run() {
		try {
			LoginRequest request;
			while ((request = loginRequests.poll()) != null) {
				if (request.isCreation()) {
					handleCreationRequest(request);
					continue;
				}
				byte worldId = request.getWorldId();
				boolean lobby = request.isLobby();
				String username = request.getUsername();
				String password = request.getPassword();
				MasterSession session = request.getSession();
				String uid = request.getUuid();
				
				// if there is a user online with that name already
				boolean online;
				
				// the response code
				ReturnCode returnCode = ReturnCode.SUCCESSFUL;
				
				if (lobby) {
					online = MSRepository.isOnline(username, true);
				} else {
					online = MSRepository.isOnline(username, false);
				}
				
				// if the player is online we change the return code
				if (online) {
					session.write(new LoginResponsePacketOut(uid, ReturnCode.ALREADY_ONLINE.getValue(), "empty", username, lobby, 0));
					return;
				}
				
				// we must have the player file before this stage
				if (!Utility.playerFileExists(username)) {
					session.write(new LoginResponsePacketOut(uid, ReturnCode.INVALID_ACCOUNT_REQUESTED.getValue(), "empty", username, lobby, 0));
					return;
				}
				
				// the id of the row that the player's data is in
				int rowId = 0;
				
				// as long as we're web integrated we will check the credentials with the database
				if (GameFlags.webIntegrated) {
					WebLoginDetail loginDetail = SQLFunctions.getLoginDetail(username, password);
					switch(loginDetail.getResponse()) {
						case NON_EXISTENT_USERNAME:
							returnCode = ReturnCode.INVALID_ACCOUNT_REQUESTED;
							break;
						case WRONG_CREDENTIALS:
							returnCode = ReturnCode.INVALID_CREDENTIALS;
							break;
						case SQL_ERROR:
							returnCode = ReturnCode.ERROR_LOADING_PROFILE;
							break;
						case CORRECT:
							returnCode = ReturnCode.SUCCESSFUL;
							break;
					}
					rowId = loginDetail.getRowId();
				}
				
				// the player file existed, so we use the text in it
				String fileText = Utility.getCollapsedText(LoginConstants.getLocation(username));
				
				// the player , so we can load variables
				Player player = Misc.loadPlayer(fileText);
				
				if (player == null) {
					returnCode = ReturnCode.ERROR_LOADING_PROFILE;
				} else if (player.getVariables().hasPunishment(PunishmentType.BAN)) {
					returnCode = ReturnCode.ACCOUNT_DISABLED;
				}
				
				// the return code was not successful so we dont need to send the file over the network anyway
				if (returnCode != ReturnCode.SUCCESSFUL) {
					fileText = "empty";
				}
				
				// the byte value
				byte responseCode = returnCode.getValue();
				
				// if we had a successful login, we can then add the player to the world
				// as long as they are connecting to a world, not the lobby.
				if (returnCode == ReturnCode.SUCCESSFUL) {
					// add the player and update the lobby if we can
					Optional<MSWorld> optional = MSRepository.getWorld(worldId);
					
					if (optional.isPresent()) {
						optional.ifPresent(world -> {
							world.addPlayer(username, uid);
							// sends the repository update as long as the world isn't the lobby world
							if (!world.isLobby()) {
								MSRepository.getWorld(MasterConstants.LOBBY_WORLD_ID).ifPresent(lobbyWorld -> lobbyWorld.getSession().write(new LobbyRepositoryPacketOut(world)));
							}
						});
					}
				}
				
				// send the response now
				session.write(new LoginResponsePacketOut(uid, responseCode, fileText, username, lobby, rowId));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Handles the creation request of an account
	 *
	 * @param request
	 * 		The request
	 */
	private void handleCreationRequest(LoginRequest request) {
		// the username of the request
		final String username = request.getUsername();
		
		// the response
		CreationResponse response = CreationResponse.SUCCESSFUL;
		
		// there's already a file in the server by that name, we won't be making an account
		if (Utility.playerFileExists(username)) {
			request.getSession().write(new AccountCreationResponsePacketOut(request.getUuid(), CreationResponse.ALREADY_TAKEN.getValue()));
			return;
		}
		
		// as long as integration is on
		if (GameFlags.webIntegrated) {
			WebLoginDetail detail = SQLFunctions.getLoginDetail(request.getUsername(), request.getPassword());
			switch (detail.getResponse()) {
				case NON_EXISTENT_USERNAME:
					if (!HTTPFunctions.registerUser(request.getUsername(), request.getPassword())) {
						System.out.println("Unable to register user from request " + request);
						response = CreationResponse.BUSY_SERVER;
					} else {
						System.out.println("Successfully registered user from request " + request);
						response = CreationResponse.SUCCESSFUL;
					}
					break;
				case WRONG_CREDENTIALS:
					response = CreationResponse.ALREADY_TAKEN;
					break;
				case SQL_ERROR:
					response = CreationResponse.BUSY_SERVER;
					break;
			}
		}
		
		
		// if we could make the account successfully, we will create an account in the file server
		if (response == CreationResponse.SUCCESSFUL) {
			Player player = new Player(username);
			player.setCreationData();
			Utility.saveData(new File(LoginConstants.getLocation(username)), Utility.getJsonText(player, true));
			System.out.println("Created a new player because of request = " + request);
		}
		
		System.out.println("writing response " + response + ", " + request);
		request.getSession().write(new AccountCreationResponsePacketOut(request.getUuid(), response.getValue()));
	}
	
	/**
	 * Adds a request to the queue
	 *
	 * @param request
	 * 		The request to add
	 */
	public void addRequest(LoginRequest request) {
		try {
			loginRequests.add(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
