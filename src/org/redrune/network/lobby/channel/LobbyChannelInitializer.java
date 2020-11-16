package org.redrune.network.lobby.channel;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.NetworkConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.lobby.codec.RequestTypeVerificationDecoder;
import org.redrune.network.world.codec.io.RSPacketEncoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LobbyChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception {
		final ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("encoder", new RSPacketEncoder());
		pipeline.addLast("decoder", new RequestTypeVerificationDecoder());
		pipeline.addLast("handler", new LobbyChannelReader());
		pipeline.addLast("registrar", new LobbyChannelRegistrar());
		// sets the session
		pipeline.channel().attr(NetworkConstants.SESSION_KEY).set(new NetworkSession(channel));
	}
	
}
