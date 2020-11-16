package org.redrune.network.master.client.network.channel;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.master.client.network.MCNetworkSystem;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.channel.MasterChannelInitializer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MCChannelInitializer extends MasterChannelInitializer {
	
	/**
	 * The system
	 */
	private final MCNetworkSystem system;
	
	/**
	 * The master channel listener, used for when sessions register/deregister
	 */
	private final MCChannelRegistrar listener;
	
	/**
	 * The master channel reader, used for reading packets and when fires events for when the connection state should
	 * change.
	 */
	private final MCChannelReader reader = new MCChannelReader();
	
	public MCChannelInitializer(MCNetworkSystem system) {
		this.system = system;
		this.listener = new MCChannelRegistrar();
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		super.initChannel(channel);
		
		final ChannelPipeline pipeline = channel.pipeline();
		
		final MCSession session = new MCSession(channel);
		channel.attr(SESSION_KEY).setIfAbsent(session);
		system.setSession(session);
		
		pipeline.addLast("listener", listener);
		pipeline.addLast("reader", reader);
	}
	
}
