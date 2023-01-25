package org.redrune.network.master.network.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.redrune.network.master.network.packet.OutgoingPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MasterEncoder extends MessageToByteEncoder<OutgoingPacket> {
	
	@Override
	protected void encode(ChannelHandlerContext ctx, OutgoingPacket msg, ByteBuf out) throws Exception {
		ctx.writeAndFlush(msg.encode());
	}
}
