package org.redrune.network.master.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public abstract class OutgoingPacket extends Packet implements PacketConstants {
	
	/**
	 * Constructs a new outgoing packet
	 *
	 * @param id
	 * 		The id of the packet
	 */
	public OutgoingPacket(int id) {
		super(id);
	}
	
	/**
	 * Writes a byte on the buffer
	 *
	 * @param value
	 * 		The value
	 */
	protected OutgoingPacket writeByte(byte value) {
		buffer.writeByte(value);
		return this;
	}
	
	/**
	 * Writes a short
	 *
	 * @param value
	 * 		The value of the short
	 */
	protected OutgoingPacket writeShort(short value) {
		buffer.writeShort(value);
		return this;
	}
	
	/**
	 * Writes an integer on the buffer
	 *
	 * @param value
	 * 		The value of the int
	 */
	protected OutgoingPacket writeInt(int value) {
		buffer.writeInt(value);
		return this;
	}
	
	/**
	 * Writes a long on the buffer
	 *
	 * @param value
	 * 		The value of the long
	 */
	protected OutgoingPacket writeLong(long value) {
		buffer.writeLong(value);
		return this;
	}
	
	/**
	 * Writes a string on the buffer.
	 *
	 * @param text
	 * 		The text on the string.
	 */
	protected OutgoingPacket writeString(String text) {
		for (byte b : text.getBytes()) {
			writeByte(b);
		}
		writeByte((byte) 0);
		return this;
	}
	
	/**
	 * Encodes the packet
	 */
	public ByteBuf encode() {
		ByteBuf response = Unpooled.buffer(buffer.readableBytes() + 3);
		
		response.writeByte(getId());
		response.writeInt(length());
		response.writeBytes(buffer);
		
		return response;
	}
}
