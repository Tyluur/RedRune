package org.redrune.network.lobby.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.WorldSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LobbyChannelRegistrar extends ChannelInboundHandlerAdapter {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		NetworkSession networkSession = ctx.channel().attr(NetworkConstants.SESSION_KEY).get();
		// we don't care if people unregister while downloading files
		if (!(networkSession instanceof WorldSession)) {
			return;
		}
		WorldSession session = (WorldSession) networkSession;
		session.disconnect();
	}
	
}
