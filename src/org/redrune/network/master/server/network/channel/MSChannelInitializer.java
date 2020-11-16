package org.redrune.network.master.server.network.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.master.network.channel.MasterChannelInitializer;
import org.redrune.network.master.server.network.MSSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MSChannelInitializer extends MasterChannelInitializer {
	
	/**
	 * The master channel listener, used for when sessions register/deregister
	 */
	private final MSChannelRegistrar listener = new MSChannelRegistrar();
	
	/**
	 * The master channel reader, used for reading packets and when fires events for when the connection state should change.
	 */
	private final MSChannelReader reader = new MSChannelReader();
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		super.initChannel(channel);
		
		final ChannelPipeline pipeline = channel.pipeline();
		
		channel.attr(SESSION_KEY).setIfAbsent(new MSSession(channel));
		
		pipeline.addLast("listener", listener);
		pipeline.addLast("reader", reader);
	}
}
