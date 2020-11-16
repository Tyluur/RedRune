package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class PrivateMessageAttemptPacketOut extends WritablePacket {
	
	/**
	 * The name of the player the message is from
	 */
	private final String sourceName;
	
	/**
	 * The id of the world the source player is in
	 */
	private final byte sourceWorld;
	
	/**
	 * The clint rights the source player is in
	 */
	private final byte sourceClientRights;
	
	/**
	 * The name of the player the message is going to
	 */
	private final String deliveryName;
	
	/**
	 * The message
	 */
	private final String message;
	
	public PrivateMessageAttemptPacketOut(String sourceName, byte sourceWorld, byte sourceClientRights, String deliveryName, String message) {
		super(PRIVATE_MESSAGE_ATTEMPT_PACKET_ID);
		this.sourceName = sourceName;
		this.sourceWorld = sourceWorld;
		this.sourceClientRights = sourceClientRights;
		this.deliveryName = deliveryName;
		this.message = message;
	}
	
	@Override
	public WritablePacket create() {
		writeString(sourceName);
		writeByte(sourceWorld);
		writeByte(sourceClientRights);
		writeString(deliveryName);
		writeString(message);
		return this;
	}
}
