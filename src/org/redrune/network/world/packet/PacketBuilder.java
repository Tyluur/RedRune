package org.redrune.network.world.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.utility.tool.BufferUtils;

/**
 * @author 'Mystic Flow
 */
public final class PacketBuilder {
	
	public static final int[] BIT_MASK_OUT = new int[32];
	
	static {
		for (int i = 0; i < BIT_MASK_OUT.length; i++) {
			BIT_MASK_OUT[i] = (1 << i) - 1;
		}
	}
	
	private final int opcode;
	
	private final PacketType type;
	
	private final ByteBuf buffer = Unpooled.buffer();
	
	private int bitPosition;
	
	public PacketBuilder() {
		this(-1);
	}
	
	public PacketBuilder(int opcode) {
		this(opcode, PacketType.STANDARD);
	}
	
	public PacketBuilder(int opcode, PacketType type) {
		this.opcode = opcode;
		this.type = type;
	}
	
	public PacketBuilder writeBytes(ByteBuf other) {
		buffer.writeBytes(other);
		return this;
	}
	
	public PacketBuilder writeLong(long l) {
		buffer.writeLong(l);
		return this;
	}
	
	public Packet toPacket() {
		return new Packet(opcode, type, Unpooled.copiedBuffer(buffer));
	}
	
	public PacketBuilder writeRS2String(String string) {
		buffer.writeBytes(string.getBytes());
		buffer.writeByte((byte) 0);
		return this;
	}
	
	public PacketBuilder writeShortA(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val + 128));
		return this;
	}
	
	public PacketBuilder writeByteA(int val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}
	
	public PacketBuilder writeLEShortA(int val) {
		buffer.writeByte((byte) (val + 128));
		buffer.writeByte((byte) (val >> 8));
		return this;
	}
	
	public PacketBuilder startBitAccess() {
		bitPosition = buffer.writerIndex() * 8;
		return this;
	}
	
	public PacketBuilder finishBitAccess() {
		buffer.writerIndex((bitPosition + 7) / 8);
		return this;
	}
	
	public PacketBuilder writeBits(int numBits, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += numBits;
		int pos = (bitPosition + 7) / 8;
		buffer.ensureWritable(pos + 1); //pos + 1
		buffer.writerIndex(pos);
		byte b;
		for (; numBits > bitOffset; bitOffset = 8) {
			b = buffer.getByte(bytePos);
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos++, (byte) (b | (value >> (numBits - bitOffset)) & BIT_MASK_OUT[bitOffset]));
			numBits -= bitOffset;
		}
		b = buffer.getByte(bytePos);
		if (numBits == bitOffset) {
			buffer.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			buffer.setByte(bytePos, (byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			buffer.setByte(bytePos, (byte) (b & ~(BIT_MASK_OUT[numBits] << (bitOffset - numBits))));
			buffer.setByte(bytePos, (byte) (b | (value & BIT_MASK_OUT[numBits]) << (bitOffset - numBits)));
		}
		return this;
	}
	
	public PacketBuilder writeByteC(int val) {
		writeByte((byte) (-val));
		return this;
	}
	
	public PacketBuilder writeByte(byte b) {
		buffer.writeByte(b);
		return this;
	}
	
	public PacketBuilder writeLEShort(int val) {
		buffer.writeByte((byte) (val));
		buffer.writeByte((byte) (val >> 8));
		return this;
	}
	
	public PacketBuilder writeInt1(int val) {
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) (val >> 16));
		return this;
	}
	
	public PacketBuilder writeInt2(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) val);
		buffer.writeByte((byte) (val >> 8));
		return this;
	}
	
	public PacketBuilder writeLEInt(int val) {
		buffer.writeByte((byte) (val));
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		return this;
	}
	
	public PacketBuilder writeSomeInt(int val) {
		buffer.writeByte((byte) (val));
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 24));
		buffer.writeByte((byte) (val >> 8));
		return this;
	}
	
	public PacketBuilder writeByteC(byte val) {
		buffer.writeByte((byte) (-val));
		return this;
	}
	
	public PacketBuilder writeByteS(int val) {
		buffer.writeByte((byte) (128 - val));
		return this;
	}
	
	public PacketBuilder writeReverse(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			buffer.writeByte(is[i]);
		}
		return this;
	}
	
	public PacketBuilder writeReverseA(byte[] is, int offset, int length) {
		for (int i = (offset + length - 1); i >= offset; i--) {
			writeByteA(is[i]);
		}
		return this;
	}
	
	public PacketBuilder writeByteA(byte val) {
		buffer.writeByte((byte) (val + 128));
		return this;
	}
	
	public PacketBuilder writeMedium(int val) {
		buffer.writeByte((byte) (val >> 16));
		buffer.writeByte((byte) (val >> 8));
		buffer.writeByte((byte) val);
		return this;
	}
	
	public PacketBuilder writeSmart(int val) {
		if (val >= 128) {
			writeShort((val + 32768));
		} else {
			writeByte((byte) val);
		}
		return this;
	}
	
	public PacketBuilder writeShort(int s) {
		buffer.writeShort((short) s);
		return this;
	}
	
	/**
	 * Puts a smart.
	 *
	 * @param val
	 * 		The value.
	 * @return This instance for chaining.
	 */
	public PacketBuilder writeIntSmart(int val) {
		if (val >= 32768) {
			writeInt(val + 32768);
		} else {
			writeShort(val);
		}
		return this;
	}
	
	public PacketBuilder writeInt(int i) {
		buffer.writeInt(i);
		return this;
	}
	
	public PacketBuilder writeMediumInt(int i) {
		buffer.writeByte((byte) ((i << 16) & 0xFF));
		buffer.writeByte((byte) ((i << 8) & 0xFF));
		buffer.writeByte((byte) i);
		return this;
	}
	
	public PacketBuilder writeLEMedium(int i) {
		buffer.writeByte((byte) i);
		buffer.writeByte((byte) (i >> 8));
		buffer.writeByte((byte) (i >> 16));
		return this;
	}
	
	public void skip(int skip) {
		for (int i = 0; i < skip; i++) {
			buffer.writeByte((byte) 0);
		}
	}
	
	public PacketBuilder writeGJString2(String string) {
		byte[] packed = new byte[256];
		int length = BufferUtils.packGJString2(0, packed, string);
		writeByte(0).writeBytes(packed, 0, length).writeByte(0);
		return this;
	}
	
	public PacketBuilder writeByte(int i) {
		writeByte((byte) i);
		return this;
	}
	
	public PacketBuilder writeBytes(byte[] data, int offset, int length) {
		buffer.writeBytes(data, offset, length);
		return this;
	}
	
	/**
	 * Puts an GJ-String on the buffer.
	 *
	 * @param string
	 * 		The value.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public PacketBuilder writeGJString(String string) {
		writeByte(0);
		writeBytes(string.getBytes());
		writeByte(0);
		return this;
	}
	
	public PacketBuilder writeBytes(byte[] b) {
		buffer.writeBytes(b);
		return this;
	}
	
	/**
	 * Writes A-type bytes on the array.
	 *
	 * @param data
	 * 		The byte-array.
	 * @param offset
	 * 		The offset.
	 * @param len
	 * 		The length.
	 * @return This OutgoingPacket instance, for chaining.
	 */
	public PacketBuilder writeBytesA(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++) {
			buffer.writeByte((byte) (data[k] + 128));
		}
		return this;
	}
	
	public int position() {
		return buffer.writerIndex();
	}
	
	public ByteBuf getBuffer() {
		return buffer;
	}
	
	public void addBytes128(ByteBuf buffer) {
		for (int k = 0; k < buffer.writerIndex(); k++) {
			writeByte((byte) (buffer.readByte() + 128));
		}
	}
	
	public void writeByte5(long value) {
		writeByte((byte) (value >> 32));
		writeInt((int) (value));
	}
}
