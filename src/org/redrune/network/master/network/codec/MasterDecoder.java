package org.redrune.network.master.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.master.network.packet.IncomingPacket;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MasterDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			while (in.readableBytes() > 0 && ctx.channel().isActive()) {
				// marks the index to read
				in.markReaderIndex();
				
				// the id of the packet
				int id = in.readByte();
				
				// decode the buffer data
				int length = in.readInt();
				
				// check for contents readability
				if (length > in.readableBytes()) {
					in.resetReaderIndex();
					return;
				}
				
				// construct the payload
				byte[] payload = new byte[length];
				
				try {
					// store the buffer into the payload
					in.readBytes(payload);
				} catch (Exception e) {
					throw new RuntimeException("Error decoding packet " + id, e);
				}
				
				// convert the buffer to a packet object now.
				out.add(new IncomingPacket(id, Unpooled.wrappedBuffer(payload)));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
