package org.redrune.network.world.codec.io;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.utility.rs.NetworkUtils;

import java.util.List;

/**
 * Decodes a received packet.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @author Emperor
 * @since 7/19/17
 */
public class RSPacketDecoder extends ByteToMessageDecoder {
	
	/**
	 * Constructs a new {@code RS2GameDecoder} {@code Object}.
	 *
	 * @param session
	 * 		The networkSession.
	 */
	public RSPacketDecoder(NetworkSession session) {
		session.getChannel().attr(NetworkConstants.SESSION_KEY).setIfAbsent(session);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			while (in.readableBytes() > 0 && ctx.channel().isActive()) {
				// the session
				WorldSession session = (WorldSession) ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
				
				// we always use the cipher
				if (session.getInCipher() == null) {
					return;
				}
				
				// the cipher
				final ISAACCipher cipher = session.getInCipher();
				
				// the opcode of the packet
				int opcode = (in.readByte() - cipher.take()) & 0xFF;
				
				// verify opcode in bounds
				if (opcode < 0 || opcode >= NetworkUtils.PACKET_SIZES.length) {
					System.out.println("packet_opcode [" + opcode + "] out of bounds");
					in.skipBytes(in.readableBytes());
					continue;
				}
				// grabs the expected length
				int length = NetworkUtils.PACKET_SIZES[opcode];
				//System.err.println("received_packet [opcode=" + opcode + ", length=" + length + "]");
				// modifies the length
				if (length == -1) {
					length = in.readByte() & 0xFF;
					//System.err.println("post_identification_length1 [opcode=" + opcode + ", length=" + length + "]");
				} else if (length == -2) {
					length = in.readShort() & 0xFFFF;
					//System.err.println("post_identification_length2 [opcode=" + opcode + ", length=" + length + "]");
				} else if (length == -3) {
					length = in.readInt();
					//System.err.println("post_identification_length3 [opcode=" + opcode + ", length=" + length + "]");
				} else if (length == -4) {
					length = in.readableBytes();
					//System.err.println("unknown_packet_size [opcode=" + opcode + ", length=" + length + ", guessedLength=" + in.readableBytes() + "]");
				}
				if (length > in.readableBytes()) {
					System.out.println("unexpected_packet_size [opcode=" + opcode + ", length=" + length + ", readable=" + in.readableBytes() + "]");
					continue;
				}
				byte[] payload = new byte[length];
				in.readBytes(payload, 0, length);
				out.add(new Packet(opcode, PacketType.STANDARD, Unpooled.wrappedBuffer(payload)));
				//				System.out.println("Received packet " + packet.getOpcode() + ", " + packet.getLength());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}