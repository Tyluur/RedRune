package org.redrune.network.lobby.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public abstract class PassableDecoder {
	
	/**
	 * Handles the decoding of a buffer payload
	 *
	 * @param ctx
	 * 		The context
	 * @param in
	 * 		The buffer
	 * @param builder
	 * 		The builder that will be sent back at the end of the decoding
	 */
	public abstract void decode(ChannelHandlerContext ctx, ByteBuf in, PacketBuilder builder) throws Exception;
}
