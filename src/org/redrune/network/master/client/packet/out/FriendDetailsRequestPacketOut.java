package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class FriendDetailsRequestPacketOut extends WritablePacket {
	
	/**
	 * The name of the user who requested the friend's details
	 */
	private final String owner;
	
	/**
	 * The id of the world that the user who requested the details was on
	 */
	private final byte requestWorldId;
	
	/**
	 * The username of the user who requested the friend's details
	 */
	private final String requestedName;
	
	public FriendDetailsRequestPacketOut(String owner, byte requestWorldId, String requestedName) {
		super(FRIEND_DETAILS_REQUEST_PACKET_ID);
		this.owner = owner;
		this.requestWorldId = requestWorldId;
		this.requestedName = requestedName;
	}
	
	@Override
	public WritablePacket create() {
		writeString(owner);
		writeByte(requestWorldId);
		writeString(requestedName);
		return this;
	}
}
