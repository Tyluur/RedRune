package org.redrune.network.master.client.network.channel;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.redrune.network.master.client.network.MCNetworkStatus;
import org.redrune.network.master.client.network.MCNetworkSystem;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

public class MCChannelFuture implements ChannelFutureListener {
	
	/**
	 * The system
	 */
	private final MCNetworkSystem system;
	
	/**
	 * The address to reconnect to
	 */
	private final InetSocketAddress address;
	
	public MCChannelFuture(MCNetworkSystem system, InetSocketAddress address) {
		this.system = system;
		this.address = address;
	}
	
	@Override
	public void operationComplete(ChannelFuture future) throws Exception {
		if (!future.isSuccess()) {
			// closes the channel
			future.channel().close();
			// re-adds the listener
			system.getBootstrap().connect(address).addListener(this);
			
			// updates the status
			system.setStatus(MCNetworkStatus.DISCONNECTED);
			
			System.out.println("We were not able to connect....");
		} else {
			// after we successfully connect we attempt to verify the session
			system.attemptVerification();
			// the close event will then have an operation complete listener that
			// will use this to handle it
			future.channel().closeFuture().addListener((ChannelFutureListener) futureListener -> {
				// now that we've closed, we schedule a reconnect
				system.getBootstrap().config().group().schedule(() -> {
					// the bootstrap will have this class as a listener
					return system.getBootstrap().connect(address).addListener(MCChannelFuture.this);
				}, 100, TimeUnit.MILLISECONDS);
			});
			
			// updates the status
			if (system.getStatus().equals(MCNetworkStatus.NOT_CONNECTED)) {
				system.setStatus(MCNetworkStatus.CONNECTED);
			} else {
				system.setStatus(MCNetworkStatus.RECONNECTED);
			}
		}
	}
}