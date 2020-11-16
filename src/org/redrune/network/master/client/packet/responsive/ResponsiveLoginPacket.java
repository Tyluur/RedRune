package org.redrune.network.master.client.packet.responsive;

import io.netty.channel.ChannelFutureListener;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.NetworkSession;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.outgoing.impl.LoginResponseCodeBuilder;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.tool.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class ResponsiveLoginPacket extends ResponsiveGamePacket {
	
	/**
	 * The username of the login
	 */
	private final String username;
	
	/**
	 * The file text of the login request
	 */
	private final String fileText;
	
	/**
	 * The uid of the the login request
	 */
	private final String uid;
	
	/**
	 * If the login request was to the lobby
	 */
	private final boolean lobby;
	
	/**
	 * The response code of the login request
	 */
	private final byte responseCode;
	
	/**
	 * The id of the row that the player's sql data is in
	 */
	private final int rowId;
	
	public ResponsiveLoginPacket(String username, String fileText, String uid, boolean lobby, byte responseCode, int rowId) {
		this.username = username;
		this.fileText = fileText;
		this.uid = uid;
		this.lobby = lobby;
		this.responseCode = responseCode;
		this.rowId = rowId;
	}
	
	@Override
	public void read() {
		Optional<NetworkSession> optional = NetworkSession.findByUid(uid);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by id " + uid);
			return;
		}
		NetworkSession session = optional.get();
		
		try {
			// simply show the response
			if (responseCode != 2) {
				session.write(new LoginResponseCodeBuilder(responseCode).build(null));
				return;
			}
			
			Player player;
			// if we couldn't load the file or the file didn't exist
			if (fileText == null || fileText.equals("empty")) {
				if (fileText == null) {
					session.write(new LoginResponseCodeBuilder(ReturnCode.ERROR_LOADING_PROFILE).build(null));
				} else {
					session.write(new LoginResponseCodeBuilder(ReturnCode.INVALID_ACCOUNT_REQUESTED).build(null));
				}
				return;
			} else {
				player = Misc.loadPlayer(fileText);
			}
			
			// sends the response code
			session.write(new LoginResponseCodeBuilder(responseCode).build(null));
			
			if (player == null) {
				System.err.println("Unable to read file text for user '" + username + "'.");
				session.getChannel().close();
				return;
			}
			
			if (!(session instanceof WorldSession)) {
				System.out.println("Session was not a world session instance on login attempt...");
				return;
			}
			
			// syncs the session variables
			((WorldSession) session).sync(player);
			// sets the player's row
			player.getVariables().setRowId(rowId);
			
			if (lobby) {
				player.registerToLobby();
			} else {
				player.register();
			}
		} catch (Exception e) {
			session.write(new LoginResponseCodeBuilder(ReturnCode.ERROR_LOADING_PROFILE).build(null)).addListener(ChannelFutureListener.CLOSE);
			e.printStackTrace();
		}
	}
}
