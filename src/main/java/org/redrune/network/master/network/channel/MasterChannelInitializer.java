package org.redrune.network.master.network.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.codec.MasterDecoder;
import org.redrune.network.master.network.codec.MasterEncoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MasterChannelInitializer extends ChannelInitializer<SocketChannel> implements MasterConstants {
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		final MasterDecoder decoder = new MasterDecoder();
		final MasterEncoder encoder = new MasterEncoder();
		
		pipeline.addLast("decoder", decoder);
		pipeline.addLast("encoder", encoder);
	}
}
