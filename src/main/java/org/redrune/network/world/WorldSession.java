package org.redrune.network.world;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerViewComponents;
import org.redrune.network.NetworkSession;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.PlayerDisconnectionPacketOut;
import org.redrune.network.world.packet.Packet;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class WorldSession extends NetworkSession {
	
	/**
	 * The player affiliated with the networkSession
	 */
	@Getter
	private Player player;
	
	/**
	 * The components of the players client
	 */
	@Getter
	private final PlayerViewComponents viewComponents;
	
	/**
	 * The queue of outgoing packets to write
	 */
	private final ConcurrentLinkedQueue<Packet> packetQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * The time of creation, this is used to figure out if a session in the world has no player for a long period of
	 * time
	 */
	@Getter
	private final long creationTime;
	
	public WorldSession(Channel channel) {
		super(channel, true);
		this.viewComponents = new PlayerViewComponents();
		this.creationTime = System.currentTimeMillis();
	}
	
	/**
	 * Flushes all of the packets
	 */
	public void flushPackets() {
		try {
			Packet packet;
			while ((packet = packetQueue.poll()) != null) {
				writeNoDelay(packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Writes a packet to the channel
	 *
	 * @param packet
	 * 		The packet
	 */
	@Override
	public synchronized ChannelFuture write(Packet packet) {
		try {
			if (player != null && player.isRenderable() && !player.getSession().isInLobby()) {
				packetQueue.add(packet);
				return null;
			} else {
				return writeNoDelay(packet);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void disconnect() {
		try {
			if (player != null) {
				if (player.isRenderable()) {
					player.terminate();
				} else {
					player.save();
					player.deregisterLobby();
				}
			}
			this.player = null;
			super.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Syncs the session with the plyaer
	 *
	 * @param player
	 * 		The player
	 */
	public void sync(Player player) {
		this.player = player;
		player.setSession(this);
	}
	
	/**
	 * Sends a login response
	 *
	 * @param responseCode
	 * 		The response code
	 */
	public ChannelFuture sendLoginResponse(int responseCode) {
		return getChannel().writeAndFlush(Unpooled.buffer().writeByte(responseCode)).addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * Notifies the master server that a session has been disconnected
	 *
	 * @param worldId
	 * 		The world of the disconnected session
	 */
	public void notifyDisconnection(byte worldId) {
		if (player != null) {
			MasterCommunication.write(new PlayerDisconnectionPacketOut(worldId, isInLobby(), player.getDetails().getUsername()));
		}
	}
	
	@Override
	public String toString() {
		return "WorldSession{player=" + player + ", open=" + getChannel().isOpen() + ", active=" + getChannel().isOpen() + ", registered=" + getChannel().isRegistered() + ", writable=" + getChannel().isWritable() + "}";
	}
	
	/**
	 * Gets the time between creation and now
	 */
	public long getElapsedCreationTime() {
		return System.currentTimeMillis() - creationTime;
	}
}
