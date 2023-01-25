package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class PrivateMessageReceivedPacketOut extends WritablePacket {
	
	/**
	 * The name of the player that the private message is coming from
	 */
	private final String fromName;
	
	/**
	 * The name of the player that the message is going to
	 */
	private final String deliveryName;
	
	/**
	 * The rights of the player that the message is from
	 */
	private final byte fromClientRights;
	
	/**
	 * The message
	 */
	private final String message;
	
	
	public PrivateMessageReceivedPacketOut(String fromName, String deliveryName, byte fromClientRights, String message) {
		super(PRIVATE_MESSAGE_RECEIVED_PACKET_ID);
		this.fromName = fromName;
		this.deliveryName = deliveryName;
		this.fromClientRights = fromClientRights;
		this.message = message;
	}
	
	@Override
	public WritablePacket create() {
		writeString(fromName);
		writeString(deliveryName);
		writeByte(fromClientRights);
		writeString(message);
		return this;
	}
}
