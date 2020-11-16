package org.redrune.network.lobby;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.redrune.core.system.SystemManager;
import org.redrune.network.NetworkConstants;
import org.redrune.network.lobby.channel.LobbyChannelInitializer;
import org.redrune.network.lobby.packet.incoming.WorldRequestPacketDecoder;
import org.redrune.network.world.packet.incoming.IncomingPacketRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LobbyNetwork {
	
	/**
	 * The instance of the packet repository
	 */
	public static final IncomingPacketRepository PACKET_REPOSITORY = new IncomingPacketRepository(WorldRequestPacketDecoder.class.getPackage().getName());
	
	/**
	 * Binds the lobby port
	 */
	public static void bind() throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		EventLoopGroup workerGroup = new NioEventLoopGroup(SystemManager.PROCESSOR_COUNT);
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			
			// builds the bootstrap
			bootstrap.group(bossGroup, workerGroup);
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childOption(ChannelOption.TCP_NODELAY, true);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			bootstrap.childHandler(new LobbyChannelInitializer());
			
			// Bind and start to accept incoming connections.
			ChannelFuture future = bootstrap.bind(NetworkConstants.LOBBY_PORT_ID).sync();
			
			// tell the console that we're bound
			System.out.println("Network bound to port: " + NetworkConstants.LOBBY_PORT_ID);
			
			// Wait until the server socket is closed.
			future.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
	
}
