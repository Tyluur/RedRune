package org.redrune.network.world.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.WorldSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelRegistrar extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		WorldSession session = (WorldSession) ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		if (session == null) {
			System.out.println("Channel disconnected with no session");
			return;
		}
		session.disconnect();
	}
}
