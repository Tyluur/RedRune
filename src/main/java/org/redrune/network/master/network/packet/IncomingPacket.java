package org.redrune.network.master.network.packet;

import io.netty.buffer.ByteBuf;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class IncomingPacket extends Packet {
	
	/**
	 * Constructs a new incoming packet
	 *
	 * @param id
	 * 		The id of the packet
	 * @param buf
	 * 		The buffer of the packet
	 */
	public IncomingPacket(int id, ByteBuf buf) {
		super(id, buf);
	}
	
	/**
	 * Reads an integer.
	 *
	 * @return An integer.
	 */
	public int readInt() {
		return buffer.readInt();
	}
	
	/**
	 * Reads a short
	 *
	 * @return A Short
	 */
	public int readShort() {
		return buffer.readShort();
	}
	
	/**
	 * Reads a byte from the channel buffer.
	 *
	 * @return The byte.
	 */
	public int readByte() {
		return buffer.readByte();
	}
	
	/**
	 * Reads a long.
	 *
	 * @return A long.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads a string
	 *
	 * @return A String
	 */
	public String readString() {
		int index;
		StringBuilder builder = new StringBuilder();
		while ((index = readByte()) != 0) {
			builder.append((char) index);
		}
		return builder.toString();
	}
	
}
