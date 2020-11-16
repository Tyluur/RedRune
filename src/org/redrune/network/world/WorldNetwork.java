package org.redrune.network.world;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.redrune.core.system.SystemManager;
import org.redrune.network.NetworkConstants;
import org.redrune.network.world.channel.WorldChannelInitializer;

import java.io.IOException;

/**
 * This is the network handler for the main game protocol. This initializes the main game server and the update server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
@Sharable
public final class WorldNetwork {
	
	/**
	 * Binds the local address to port {@link NetworkConstants#WORLD_PORT_ID}
	 */
	public static void bind() throws IOException, InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		EventLoopGroup workerGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			// builds the bootstrap
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.option(ChannelOption.SO_REUSEADDR, true);
			bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, NetworkConstants.TIMEOUT_RATE);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.childHandler(new WorldChannelInitializer());
			
			// Bind and start to accept incoming connections.
			ChannelFuture future = bootstrap.bind(NetworkConstants.WORLD_PORT_ID).sync();
			
			// tell the console that we're bound
			System.out.println("Network bound to port: " + NetworkConstants.WORLD_PORT_ID);
			
			// Wait until the server socket is closed.
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
}
