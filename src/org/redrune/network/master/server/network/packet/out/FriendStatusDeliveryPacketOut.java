package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class FriendStatusDeliveryPacketOut extends WritablePacket {
	
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
	
	public FriendStatusDeliveryPacketOut(String name, byte status, byte worldId, boolean online, boolean lobby) {
		super(FRIEND_STATUS_CHANGE_DELIVER_PACKET_ID);
		this.name = name;
		this.status = status;
		this.worldId = worldId;
		this.lobby = lobby;
		this.online = online;
	}
	
	@Override
	public WritablePacket create() {
		writeString(name);
		writeByte(status);
		writeByte(worldId);
		writeByte((byte) (online ? 1 : 0));
		writeByte((byte) (lobby ? 1 : 0));
		return this;
	}
}
