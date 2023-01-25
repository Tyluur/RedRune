package org.redrune.network.master.client.network;

import com.google.common.base.Preconditions;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.client.MCFlags;
import org.redrune.network.master.client.network.MCNetworkStatus.StatusChangeEvent;
import org.redrune.network.master.client.network.channel.MCChannelFuture;
import org.redrune.network.master.client.network.channel.MCChannelInitializer;
import org.redrune.network.master.client.packet.out.VerificationPacketOut;
import org.redrune.network.master.network.packet.OutgoingPacket;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class MCNetworkSystem implements MasterConstants {
	
	/**
	 * The bootstrap
	 */
	@Getter
	private final Bootstrap bootstrap = new Bootstrap();
	
	/**
	 * The id of the world for this system
	 */
	@Getter
	private final byte worldId;
	
	/**
	 * The status of the system
	 */
	@Getter
	private MCNetworkStatus status = MCNetworkStatus.NOT_CONNECTED;
	
	/**
	 * The session
	 */
	@Getter
	@Setter
	private MCSession session;
	
	/**
	 * Constructs a new network system
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public MCNetworkSystem(byte worldId) {
		this.worldId = MCFlags.worldId = worldId;
	}
	
	/**
	 * Connects the system to the server
	 */
	public void connect() {
		// the address to connect to
		final InetSocketAddress address = new InetSocketAddress(IP, PORT_ID);
		
		// builds the bootstrap
		bootstrap.group(new NioEventLoopGroup(1));
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.handler(new MCChannelInitializer(this));
		
		// connects right after the bootstrap is built
		bootstrap.remoteAddress(address).connect().addListener(new MCChannelFuture(this, address));
	}
	
	/**
	 * Sets the status
	 *
	 * @param status
	 * 		The status to set to
	 */
	public void setStatus(MCNetworkStatus status) {
		// makes sure we don't set a null status
		Preconditions.checkArgument(status != null, "The status is not allowed to be null.");
		// are the statuses different
		boolean different = !Objects.equals(status, this.status);
		
		// the previous status
		final MCNetworkStatus oldStatus = this.status;
		
		// we change the status
		this.status = status;
		
		// statuses didn't change so we dont need to fire an update
		if (!different) {
			return;
		}
		
		status.setSystem(this);
		
		// executes the leave event
		Optional.ofNullable(oldStatus.leaveEvent()).ifPresent(StatusChangeEvent::execute);
		
		// executes the set event
		Optional.ofNullable(status.setEvent()).ifPresent(StatusChangeEvent::execute);
		
		System.out.println("Entered status " + status + " from " + oldStatus);
	}
	
	/**
	 * Writes the packet
	 *
	 * @param packet
	 * 		The packet
	 */
	public boolean write(OutgoingPacket packet) {
		if (session == null || !session.isConnected()) {
			System.err.println("Unable to write a packet, session: " + session);
			return false;
		}
			session.write(packet);
			return true;
	}
	
	/**
	 * Attempts to verify the session
	 */
	public void attemptVerification() {
		if (session.isVerified()) {
			return;
		}
		write(new VerificationPacketOut());
	}
}