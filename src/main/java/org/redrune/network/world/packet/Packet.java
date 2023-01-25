package org.redrune.network.world.packet;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import org.redrune.utility.tool.BufferUtils;

/**
 * Contains all the data inside of a packet. Netty 4 usage only.
 *
 * @author 'Mystic Flow
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class Packet {
	
	/**
	 * The opcode of the packet
	 */
	@Getter
	private final int opcode;
	
	/**
	 * The type of packet this is
	 */
	@Getter
	private final PacketType type;
	
	/**
	 * The buffer of the packet
	 */
	@Getter
	private final ByteBuf buffer;
	
	/**
	 * The length of the packet
	 */
	@Getter
	private final int length;
	
	/**
	 * Constructs a new packet
	 */
	public Packet(int opcode, PacketType type, ByteBuf buffer) {
		this.opcode = opcode;
		this.type = type;
		this.buffer = buffer;
		this.length = buffer.readableBytes();
	}
	
	@Override
	public String toString() {
		return "Packet{" + "opcode=" + opcode + ", type=" + type + ", length=" + length + '}';
	}
	
	/**
	 * If the packet is raw, meaning it was built with no opcode
	 */
	public boolean isRaw() {
		return opcode == -1;
	}
	
	/**
	 * Reads an unsigned short.
	 *
	 * @return An unsigned short.
	 */
	public int readUnsignedShort() {
		return buffer.readUnsignedShort();
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
	 * Reads a long.
	 *
	 * @return A long.
	 */
	public long readLong() {
		return buffer.readLong();
	}
	
	/**
	 * Reads a type C byte.
	 *
	 * @return A type C byte.
	 */
	public byte readByteC() {
		return (byte) (-readByte());
	}
	
	public byte readByte() {
		return buffer.readByte();
	}
	
	/**
	 * reads a type S byte.
	 *
	 * @return A type S byte.
	 */
	public byte readByteS() {
		return (byte) (128 - readByte());
	}
	
	/**
	 * Reads a little-endian type A short.
	 *
	 * @return A little-endian type A short.
	 */
	public int readLEShortA() {
		int i = (buffer.readByte() - 128 & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
		return i;
	}
	
	/**
	 * Reads a little-endian short.
	 *
	 * @return A little-endian short.
	 */
	public int readLEShort() {
		return (buffer.readByte() & 0xFF) | ((buffer.readByte() & 0xFF) << 8);
	}
	
	/**
	 * Reads a V1 integer.
	 *
	 * @return A V1 integer.
	 */
	public int readInt1() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return (b3 << 24 | b4 << 16 | b1 << 8 | b2);
	}
	
	/**
	 * Reads a V2 integer.
	 *
	 * @return A V2 integer.
	 */
	public int readInt2() {
		int b1 = buffer.readByte() & 0xFF;
		int b2 = buffer.readByte() & 0xFF;
		int b3 = buffer.readByte() & 0xFF;
		int b4 = buffer.readByte() & 0xFF;
		return (b2 << 24 | b1 << 16 | b4 << 8 | b3);
	}
	
	/**
	 * reads a 3-byte integer.
	 *
	 * @return The 3-byte integer.
	 */
	public int readTriByte() {
		return ((buffer.readByte() << 16) & 0xFF) | ((buffer.readByte() << 8) & 0xFF) | (buffer.readByte() & 0xFF);
	}
	
	/**
	 * Reads a type A short.
	 *
	 * @return A type A short.
	 */
	public int readShortA() {
		return ((buffer.readByte() & 0xFF) << 8) | (buffer.readByte() - 128 & 0xFF);
	}
	
	/**
	 * Reads a series of bytes in reverse.
	 *
	 * @param bytes
	 * 		The tarread byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void readReverse(byte[] bytes, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			bytes[i] = buffer.readByte();
		}
	}
	
	/**
	 * Reads a series of type A bytes in reverse.
	 *
	 * @param bytes
	 * 		The tarread byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void readReverseA(byte[] bytes, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			bytes[i] = readByteA();
		}
	}
	
	/**
	 * Reads a type A byte.
	 *
	 * @return A type A byte.
	 */
	public byte readByteA() {
		return (byte) (readByte() - 128);
	}
	
	/**
	 * Reads a series of bytes.
	 *
	 * @param bytes
	 * 		The to read byte array.
	 * @param offset
	 * 		The offset.
	 * @param length
	 * 		The length.
	 */
	public void read(byte[] bytes, int offset, int length) {
		for (int i = 0; i < length; i++) {
			bytes[offset + i] = buffer.readByte();
		}
	}
	
	/**
	 * reads a smart.
	 *
	 * @return The smart.
	 */
	public int readSmart() {
		int peek = buffer.getByte(buffer.readerIndex());
		if (peek < 128) {
			return (readByte() & 0xFF);
		} else {
			return (readShort() & 0xFFFF) - 32768;
		}
	}
	
	/**
	 * Reads a short.
	 *
	 * @return A short.
	 */
	public int readShort() {
		return buffer.readShort();
	}
	
	public int remaining() {
		return buffer.readableBytes();
	}
	
	public void readBytes(byte[] textBuffer, int length) {
		buffer.readBytes(textBuffer, 0, length);
	}
	
	public String readJagString() {
		readByte();
		return readRS2String();
	}
	
	/**
	 * Reads a RuneScape string.
	 *
	 * @return The string.
	 */
	public String readRS2String() {
		return BufferUtils.readRS2String(buffer);
	}
	
	public int readLEInt() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}
	
	public int readUnsignedByte() {
		return buffer.readUnsignedByte();
	}
	
	/**
	 * @author 'Mystic Flow
	 */
	public enum PacketType {
		STANDARD(0),
		VAR_BYTE(1),
		VAR_SHORT(2);
		
		@Getter
		private final int size;
		
		PacketType(int size) {
			this.size = size;
		}
		
	}
	
}
