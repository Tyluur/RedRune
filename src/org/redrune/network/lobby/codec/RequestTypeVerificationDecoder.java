package org.redrune.network.lobby.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.network.NetworkConstants;
import org.redrune.network.lobby.ProtocolType;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.List;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class RequestTypeVerificationDecoder extends ByteToMessageDecoder {
	
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
		switch (type) {
			case LOGIN_REQUEST:
				builder.writeByte(0);
				pipeline.addBefore("handler", "decoder", type.getDecoder(false));
				break;
			case JS5_REQUEST:
				int version = in.readInt();
				if (version == NetworkConstants.REVISION) {
					builder.writeByte((byte) 0);
					for (int i = 0; i < 27; i++) {
						builder.writeInt(NetworkConstants.UPDATE_SERVER_KEYS[i]);
					}
					pipeline.addBefore("handler", "decoder", type.getDecoder(false));
				} else {
					builder.writeByte((byte) 6);
				}
				break;
			case ACCOUNT_CREATION:
			case NAME_VERIFICATION:
				type.getPassableDecoder().decode(ctx, in, builder);
				break;
		}
		ctx.writeAndFlush(builder.getBuffer());
	}
}