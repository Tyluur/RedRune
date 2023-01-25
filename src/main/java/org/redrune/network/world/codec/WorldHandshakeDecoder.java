package org.redrune.network.world.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.lobby.ProtocolType;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.List;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class WorldHandshakeDecoder extends ByteToMessageDecoder {
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		// removes the pipeline
		final ChannelPipeline pipeline = ctx.pipeline().remove(this);
		// the protocol id
		final int id = in.readByte() & 0xFF;
		Optional<ProtocolType> optional = ProtocolType.getType(id);
		if (!optional.isPresent()) {
			System.out.println("Unable to find protocol to use for id " + id);
			return;
		}
		// the type of protocol we're using
		ProtocolType type = optional.get();
		
		// constructs a new builder
		PacketBuilder builder = new PacketBuilder();
		
		// we only care about the login request opcode to the world
		if (type == ProtocolType.LOGIN_REQUEST) {
			builder.writeByte(0);
			pipeline.addBefore("handler", "decoder", type.getDecoder(true));
		}
		ctx.writeAndFlush(builder.getBuffer());
	}
}
