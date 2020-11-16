package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class PrivateMessageDeliveredPacketOut extends WritablePacket {
	
	/**
	 * The name of the player the private message is coming from
	 */
	private final String sourceName;
	
	/**
	 * The name of the person the private message was sent to
	 */
	private final String deliveryName;
	
	/**
	 * The message in the private message
	 */
	private final String message;
	
	public PrivateMessageDeliveredPacketOut(String sourceName, String deliveryName, String message) {
		super(PRIVATE_MESSAGE_DELIVERY_PACKET_ID);
		this.sourceName = sourceName;
		this.deliveryName = deliveryName;
		this.message = message;
	}
	
	@Override
	public WritablePacket create() {
		writeString(sourceName);
		writeString(deliveryName);
		writeString(message);
		return this;
	}
}
