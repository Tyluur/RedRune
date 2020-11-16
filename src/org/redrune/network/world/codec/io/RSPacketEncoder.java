package org.redrune.network.world.codec.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;

/**
 * This encodes a packet going to the rs client. All packets have a specific header and data is encoded differently
 * based on the type of packet being encoded. This class handles all said operations.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
@Sharable
public final class RSPacketEncoder extends MessageToByteEncoder<Packet> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
		try {
			// the session
			NetworkSession session = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
			// the encoded response
			ByteBuf response;
			if (packet.isRaw()) {
				response = packet.getBuffer();
			} else {
				// the length of the packet
				final int length = packet.getBuffer().readableBytes();
				// create the buffer
				response = Unpooled.buffer(length + 3);
				// the id of the packet
				int opcode = packet.getOpcode();
				// the isaac cipher
				final ISAACCipher outCipher = session == null ? null : session.getOutCipher();
				// if there was no cipher
				if (outCipher == null) {
					writeSmart(response, opcode);
	//				System.out.println("encoded opcode " + opcode + " WITHOUT isaac cipher");
				} else {
					if (opcode >= 128) {
						response.writeByte((opcode >> 8) + 128 + outCipher.take());
					}
					response.writeByte(opcode + outCipher.take());
	//				System.out.println("encoded opcode " + opcode + " WITH isaac cipher");
				}
				if (packet.getType() == PacketType.VAR_BYTE) {
					if (length > 255) { // Stack overflow.
						throw new IllegalStateException("Could not send a packet with " + length + " bytes within 8 bits.");
					}
					response.writeByte(packet.getBuffer().readableBytes());
				} else if (packet.getType() == PacketType.VAR_SHORT) {
					if (length > 65535) { // Stack overflow.
						throw new IllegalStateException("Could not send a packet with " + length + " bytes within 16 bits.");
					}
					response.writeByte((byte) (packet.getBuffer().readableBytes() >> 8));
					response.writeByte((byte) packet.getBuffer().readableBytes());
				}
				response.writeBytes(packet.getBuffer());
			}
			ctx.writeAndFlush(response);
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes a smart byte to the buffer
	 *
	 * @param buffer
	 * 		The buffer
	 * @param value
	 * 		The value to write
	 */
	private void writeSmart(ByteBuf buffer, int value) {
		if (value >= 128) {
			buffer.writeByte(128);
		}
		buffer.writeByte(value);
	}
	
}