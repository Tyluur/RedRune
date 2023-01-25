package org.redrune.network.master.client.network;

import io.netty.channel.socket.SocketChannel;
import org.redrune.network.master.client.packet.in.SuccessfulVerificationIn;
import org.redrune.network.master.network.MasterSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.readable.ReadableRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MCSession extends MasterSession {
	
	/**
	 * The repository of readable packets
	 */
	private static final ReadableRepository READABLE_REPOSITORY = new ReadableRepository(SuccessfulVerificationIn.class.getPackage().getName());
	
	/**
	 * Constructs a new network session
	 *
	 * @param channel
	 * 		The channel of the session
	 */
	public MCSession(SocketChannel channel) {
		super(channel);
	}
	
	@Override
	public void read(IncomingPacket packet) {
		try {
			READABLE_REPOSITORY.read(this, packet);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * If the channel is still connected
	 */
	public boolean isConnected() {
		return channel.isWritable() && channel.isOpen() && channel.isActive();
	}
	
	/**
	 * Gets the readable repository
	 */
	public static ReadableRepository getReadableRepository() {
		return READABLE_REPOSITORY;
	}
}