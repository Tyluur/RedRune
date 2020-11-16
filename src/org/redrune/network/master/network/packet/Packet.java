package org.redrune.network.master.network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public abstract class Packet {
	
	/**
	 * The channel buffer.
	 */
	final ByteBuf buffer;
	
	/**
	 * The original length of the buffer
	 */
	private final int length;
	
	/**
	 * The id of the packet
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * Constructs a packet with an id and an undefined buffer
	 *
	 * @param id
	 * 		The id of the packet
	 */
	Packet(int id) {
		this(id, Unpooled.buffer());
	}
	
	/**
	 * Constructs a packet with a defined buffer
	 * @param id The id of the packet
	 * @param buffer The buffer
	 */
	Packet(int id, ByteBuf buffer) {
		this.id = id;
		this.buffer = buffer;
		this.length = buffer.readableBytes();
	}
	
	@Override
	public String toString() {
		return "Packet{" + "id=" + id + ", buffer=" + buffer + '}';
	}
	
	/**
	 * The length of the packet
	 */
	int length() {
		return buffer.readableBytes();
	}
}
