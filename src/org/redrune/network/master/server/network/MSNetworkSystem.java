package org.redrune.network.master.server.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.server.network.channel.MSChannelInitializer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MSNetworkSystem implements MasterConstants {
	
	/**
	 * The state of the network
	 */
	@Getter
	private MSNetworkStatus status;
	
	/**
	 * The constructor of a new network system
	 */
	public MSNetworkSystem() {
		this.status = MSNetworkStatus.NOT_CONNECTED;
	}
	
	/**
	 * Sets the status of the network
	 */
	private void setStatus(MSNetworkStatus status) {
		this.status = status;
		this.status.getEvent().execute();
	}
	
	/**
	 * Binds the socket that will listen.
	 */
	public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workerGroup = new NioEventLoopGroup(7);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			// builds the bootstrap
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			bootstrap.childHandler(new MSChannelInitializer());
			
			// Bind and start to accept incoming connections.
			ChannelFuture future = bootstrap.bind(PORT_ID).sync();
			
			// sets the status now that we have connected
			setStatus(MSNetworkStatus.CONNECTED);
			
			// Wait until the server socket is closed.
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
			
			// update the status now that we have disconnected.
			setStatus(MSNetworkStatus.CONNECTION_DROPPED);
		}
	}
	
}
