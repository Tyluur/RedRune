package org.redrune.network.master.client.network.channel;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.channel.MasterChannelReader;
import org.redrune.network.master.network.packet.IncomingPacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MCChannelReader extends MasterChannelReader {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IncomingPacket msg) throws Exception {
		MCSession session = (MCSession) ctx.channel().attr(SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "No session set for channel.");
		
		session.read(msg);
	}
	
}