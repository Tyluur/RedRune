package org.redrune.network.master.network.channel;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.MasterSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class MasterChannelRegistrar<T extends MasterSession> extends ChannelInboundHandlerAdapter implements MasterConstants {
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		T session = (T) ctx.channel().attr(SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "No session set for channel.");
		
		super.channelRegistered(ctx);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		T session = (T) ctx.channel().attr(SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "No session set for channel.");
		
		super.channelUnregistered(ctx);
	}
}
