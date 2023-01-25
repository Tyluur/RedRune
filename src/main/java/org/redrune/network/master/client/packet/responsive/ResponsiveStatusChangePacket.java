package org.redrune.network.master.client.packet.responsive;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;
import org.redrune.utility.rs.constant.GameBarStatus;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class ResponsiveStatusChangePacket extends ResponsiveGamePacket {
	
	/**
	 * The name of the user who updated their status
	 */
	private final String name;
	
	/**
	 * The status that the user updated to
	 */
	private final byte status;
	
	/**
	 * The id of the world that the user who updated their status was in
	 */
	private final byte worldId;
	
	/**
	 * If the user who updated their status was online
	 */
	private final boolean online;
	
	/**
	 * If the user who updated their status was in the lobby
	 */
	private final boolean lobby;
	
	public ResponsiveStatusChangePacket(String name, byte status, byte worldId, boolean online, boolean lobby) {
		this.name = name;
		this.status = status;
		this.worldId = worldId;
		this.online = online;
		this.lobby = lobby;
	}
	
	@Override
	public void read() {
		if (status >= GameBarStatus.values().length) {
			return;
		}
		GameBarStatus barStatus = GameBarStatus.values()[status];
		
		for (Player player : World.get().getPlayers()) {
			if (player == null) {
				continue;
			}
			// we skip the players that aren't renderable if they aren't in lobby
			// lobby players are always unrenderable
			if (!player.isRenderable() && !player.getSession().isInLobby()) {
				continue;
			}
			// the player never sees themselves on their own list
			if (name.equals(player.getDetails().getUsername())) {
				continue;
			}
			// we don't have that person on our friends list so we don't care their status was changed
			if (!player.getManager().getContacts().hasFriend(name)) {
				continue;
			}
			if (barStatus == GameBarStatus.ON) {
				player.getManager().getContacts().updateFriendStatus(name, worldId, lobby, online, true);
			} else if (barStatus == GameBarStatus.FRIENDS) {
				// TODO: friend status requires us to know if the person who changed their status has us on their list
				// if they don't, we show them as offline
				player.getManager().getContacts().updateFriendStatus(name, worldId, lobby, online, true);
			} else if (barStatus == GameBarStatus.OFF) {
				player.getManager().getContacts().updateFriendStatus(name, worldId, false, false, true);
			}
		}
	}
}
