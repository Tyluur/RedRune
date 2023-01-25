package org.redrune.network.master.network.channel;

import com.google.common.base.Objects;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.packet.IncomingPacket;

/**
 * This class handles the reading of different events from the channel that is connected to the master server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public abstract class MasterChannelReader extends SimpleChannelInboundHandler<IncomingPacket> implements MasterConstants {
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
		if (NetworkConstants.IGNORED_EXCEPTIONS.stream().noneMatch($it -> Objects.equal($it, e.getMessage()))) {
			e.printStackTrace();
		}
		ctx.channel().close();
	}
	
}
