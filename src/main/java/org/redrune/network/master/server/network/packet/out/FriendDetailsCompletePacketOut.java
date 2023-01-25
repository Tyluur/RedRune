package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class FriendDetailsCompletePacketOut extends WritablePacket {
	
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
	
	public FriendDetailsCompletePacketOut(String owner, String requestedUsername, byte requestedWorldId, boolean online, boolean lobby) {
		super(FRIEND_DETAILS_COMPLETE_PACKET_ID);
		this.owner = owner;
		this.requestedUsername = requestedUsername;
		this.requestedWorldId = requestedWorldId;
		this.online = online;
		this.lobby = lobby;
	}
	
	@Override
	public WritablePacket create() {
		writeString(owner);
		writeString(requestedUsername);
		writeByte(requestedWorldId);
		writeByte((byte) (online ? 1 : 0));
		writeByte((byte) (lobby ? 1 : 0));
		return this;
	}
}
