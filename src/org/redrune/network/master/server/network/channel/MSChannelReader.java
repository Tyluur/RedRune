package org.redrune.network.master.server.network.channel;

import com.google.common.base.Preconditions;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.master.network.channel.MasterChannelReader;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.server.network.MSSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MSChannelReader extends MasterChannelReader {
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IncomingPacket msg) throws Exception {
		MSSession session = (MSSession) ctx.channel().attr(SESSION_KEY).get();
		Preconditions.checkArgument(session != null, "No session set for channel.");
		
		session.read(msg);
	}
	
}
