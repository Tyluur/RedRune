package org.redrune.network.master.server.network.channel;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.master.network.channel.MasterChannelRegistrar;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.world.MSRepository;
import org.redrune.network.master.server.world.MSWorld;

import java.util.Optional;

/**
 * This class handles the registration of connections with the master server.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Sharable
public class MSChannelRegistrar extends MasterChannelRegistrar<MSSession> {
	
	/**
	 * The {@link Multiset} of connections currently active within the server.
	 */
	private final Multiset<String> connections = ConcurrentHashMultiset.create();
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		connections.add(ctx.channel().attr(SESSION_KEY).get().getIp());
	}
	
	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		super.channelUnregistered(ctx);
		// grab the session
		MSSession session = (MSSession) ctx.channel().attr(SESSION_KEY).get();
		
		// get the ip of the host
		String host = session.getIp();
		
		// remove the host from the connection list
		connections.remove(host);
		
		// only handle un-registration event for verified sessions
		if (!session.isVerified()) {
			return;
		}
		
		// the session is verified, on un-registration events we must empty the world
		byte worldId = session.getWorld().getId();
		if (worldId < 0) {
			System.err.println("Unable to unregister world # " + worldId + "! " + session);
		} else {
			final Optional<MSWorld> optional = MSRepository.getWorld(worldId);
			if (optional.isPresent()) {
				MSRepository.unregister(optional.get());
			} else {
				System.err.println("Unable to unregister world # " + worldId + "! " + session);
			}
		}
	}
	
}