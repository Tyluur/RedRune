package org.redrune.network.master.client.network.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.channel.MasterChannelRegistrar;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MCChannelRegistrar extends MasterChannelRegistrar<MCSession> {
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
	}
	
}