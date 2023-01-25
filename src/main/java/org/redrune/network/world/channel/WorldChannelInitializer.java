package org.redrune.network.world.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.world.codec.WorldHandshakeDecoder;
import org.redrune.network.world.codec.io.RSPacketEncoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
@Sharable
public class WorldChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("encoder", new RSPacketEncoder());
		pipeline.addLast("decoder", new WorldHandshakeDecoder());
		pipeline.addLast("handler", new WorldChannelReader());
		pipeline.addLast("registrar", new WorldChannelRegistrar());
	}
}
