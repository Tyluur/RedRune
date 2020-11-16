package org.redrune.network.master.client.packet.responsive;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class ResponsiveFriendDetailsPacket extends ResponsiveGamePacket {
	
	/*
	
		String owner = packet.readString();
		String requestedName = packet.readString();
		byte requestedWorldId = (byte) packet.readByte();
		boolean online = packet.readByte() == 1;
		boolean lobby = packet.readByte() == 1;
		
	 */
	
	/**
	 * The username of the player who requested the details
	 */
	private final String owner;
	
	/**
	 * The username of the player whose details were requested
	 */
	private final String requestedUsername;
	
	/**
	 * The id of the world that the requested player was in
	 */
	private final byte requestedWorldId;
	
	/**
	 * If the requested player was online
	 */
	private final boolean online;
	
	/**
	 * If the requested player was in the lobby
	 */
	private final boolean lobby;
	
	public ResponsiveFriendDetailsPacket(String owner, String requestedUsername, byte requestedWorldId, boolean online, boolean lobby) {
		this.owner = owner;
		this.requestedUsername = requestedUsername;
		this.requestedWorldId = requestedWorldId;
		this.online = online;
		this.lobby = lobby;
	}
	
	@Override
	public void read() {
		Optional<Player> optional = World.get().getPlayerByUsername(owner);
		if (!optional.isPresent()) {
			return;
		}
		Player player = optional.get();
		player.getManager().getContacts().updateFriendStatus(requestedUsername, requestedWorldId, lobby, online, false);
	}
}
