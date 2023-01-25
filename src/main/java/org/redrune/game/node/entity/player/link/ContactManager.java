package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.FriendDetailsRequestPacketOut;
import org.redrune.network.master.client.packet.out.FriendStatusChangePacketOut;
import org.redrune.network.master.client.packet.out.PrivateMessageAttemptPacketOut;
import org.redrune.network.world.packet.outgoing.impl.FriendsListBuilder;
import org.redrune.network.world.packet.outgoing.impl.IgnoreListBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.utility.tool.Misc;

import java.util.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ContactManager {
	
	/**
	 * The list of friends a player has
	 */
	@Getter
	private final Set<String> friendList = new LinkedHashSet<>();
	
	/**
	 * The list of usernames a player has on their ignore list
	 */
	@Getter
	private final Set<String> ignoreList = new LinkedHashSet<>();
	
	/**
	 * The map of ranks for the clan
	 */
	private final Map<String, Integer> clanRankMap = new HashMap<>();
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Handles the friend chat management when a user logs in
	 */
	public void sendLogin() {
		player.getTransmitter().send(new FriendsListBuilder().build(player));
		// unlocks the friends list
		requestAllFriendsDetails();
		// send all the users on our ignore list
		updateIgnoreList();
	}
	
	/**
	 * Requests details for all the friends in our list
	 */
	private void requestAllFriendsDetails() {
		friendList.forEach(this::requestFriendDetails);
	}
	
	/**
	 * Updates the ignore list
	 */
	private void updateIgnoreList() {
		player.getTransmitter().send(new IgnoreListBuilder(ignoreList, getDisplayNameList()).build(player));
	}
	
	/**
	 * Gets the list of all the players, with their display names matching
	 */
	private Map<String, String> getDisplayNameList() {
		return new HashMap<>();
	}
	
	/**
	 * Pushes the status change for the login
	 */
	public void pushLoginStatusChange() {
		// as long as we're not on appear offline, all our friends will know we updated our status
		if (getPrivateStatus() == 2) {
			return;
		}
		SystemManager.getScheduler().schedule(new ScheduledTask(5) {
			@Override
			public void run() {
				sendMyStatusChange(true);
			}
		});
	}
	
	/**
	 * Gets the current status of the player
	 */
	public byte getPrivateStatus() {
		Object barStatus = player.getVariables().getAttribute(AttributeKey.PRIVATE, GameBarStatus.ON);
		GameBarStatus status = GameBarStatus.ON;
		if (barStatus != null) {
			if (barStatus.getClass().equals(String.class)) {
				status = GameBarStatus.valueOf(barStatus.toString());
			} else {
				status = (GameBarStatus) barStatus;
			}
		}
		return status.getValue();
	}
	
	/**
	 * Shows the status of all my friends onto my friends list
	 *
	 * @param online
	 * 		If the player is online
	 */
	public void sendMyStatusChange(boolean online) {
		MasterCommunication.write(new FriendStatusChangePacketOut(player.getDetails().getUsername(), getPrivateStatus(), player.getWorld(), online, player.getSession().isInLobby()));
	}
	
	/**
	 * Handles the addition of a name to our friend list
	 *
	 * @param name
	 * 		The name of our friend
	 */
	public void addFriend(String name) {
		if (Misc.invalidAccountName(name)) {
			return;
		}
		if (friendList.size() >= 200) {
			player.getTransmitter().sendMessage("Your friends list is full.", false);
			return;
		}
		if (hasFriend(name)) {
			player.getTransmitter().sendMessage("This player is already on your friends list.");
			return;
		}
		friendList.add(name);
		requestFriendDetails(name);
	}
	
	/**
	 * Checks if we have a friend by the name
	 *
	 * @param name
	 * 		The name of the friend.
	 */
	public boolean hasFriend(String name) {
		return friendList.contains(name);
	}
	
	/**
	 * Requests details of a friend
	 *
	 * @param requested
	 * 		The name of the player whose details we requested
	 */
	private void requestFriendDetails(String requested) {
		MasterCommunication.write(new FriendDetailsRequestPacketOut(player.getDetails().getUsername(), player.getWorld(), requested));
	}
	
	/**
	 * Sends a private message
	 *
	 * @param name
	 * 		The name of the person we want to send a message to
	 * @param message
	 * 		The message we want to send
	 */
	public void sendPrivateMessage(String name, String message) {
		MasterCommunication.write(new PrivateMessageAttemptPacketOut(player.getDetails().getUsername(), player.getWorld(), player.getDetails().getDominantRight().getClientRight(), name, message));
	}
	
	/**
	 * Updates the status of a friend on our list
	 *
	 * @param friendUsername
	 * 		The name of the friend
	 * @param friendWorldId
	 * 		The id of the world the friend is on
	 * @param lobby
	 * 		If the friend is in the lobby
	 * @param online
	 * 		If the friend is online
	 * @param warn
	 * 		If we should be warned about their status update
	 */
	public void updateFriendStatus(String friendUsername, byte friendWorldId, boolean lobby, boolean online, boolean warn) {
		String displayName = Misc.formatPlayerNameForDisplay(friendUsername);
		player.getTransmitter().send(new FriendsListBuilder(displayName, "", friendWorldId, getClanRank(friendUsername), warn, lobby, online).build(player));
	}
	
	/**
	 * Gets the rank of a user in our clan
	 */
	public int getClanRank(String username) {
		return clanRankMap.getOrDefault(username, 0);
	}
	
	/**
	 * Handles the addition of a username to our ignore list
	 *
	 * @param name
	 * 		The name
	 */
	public void addIgnore(String name) {
		if (Misc.invalidAccountName(name)) {
			return;
		}
		if (ignoreList.size() >= 200) {
			player.getTransmitter().sendMessage("Your ignore list is full.", false);
			return;
		}
		if (ignoreList.contains(name)) {
			player.getTransmitter().sendMessage("This player is already on your ignore list.");
			return;
		}
		ignoreList.add(name);
		updateIgnoreList();
	}
	
	/**
	 * Handles the removal of a friends name
	 *
	 * @param name
	 * 		The name
	 */
	public void removeFriend(String name) {
		for (Iterator<String> iterator = friendList.iterator(); iterator.hasNext(); ) {
			String ignored = iterator.next();
			String protocolName = Misc.formatPlayerNameForProtocol(ignored);
			if (protocolName.equals(name)) {
				iterator.remove();
				break;
			}
		}
	}
	
	/**
	 * Handles the removal of a name from the ignore list
	 *
	 * @param name
	 * 		The name
	 */
	public void removeIgnore(String name) {
		for (Iterator<String> iterator = ignoreList.iterator(); iterator.hasNext(); ) {
			String ignored = iterator.next();
			String protocolName = Misc.formatPlayerNameForProtocol(ignored);
			if (protocolName.equals(name)) {
				iterator.remove();
				break;
			}
		}
	}
}
