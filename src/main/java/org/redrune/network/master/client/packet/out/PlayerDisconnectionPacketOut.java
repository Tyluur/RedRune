package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class PlayerDisconnectionPacketOut extends WritablePacket {
	
	/**
	 * The name of the player who disconnected
	 */
	private final String username;
	
	/**
	 * The id of the world we're on
	 */
	private final byte worldId;
	
	/**
	 * If the player disconnected from the lobby
	 */
	private final boolean lobby;
	
	public PlayerDisconnectionPacketOut(byte worldId, boolean lobby, String username) {
		super(PLAYER_DISCONNECTION_PACKET_ID);
		this.username = username;
		this.worldId = worldId;
		this.lobby = lobby;
	}
	
	@Override
	public WritablePacket create() {
		writeByte(worldId);
		writeByte((byte) (lobby ? 1 : 0));
		writeString(username);
		return this;
	}
}
