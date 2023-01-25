package org.redrune.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.impl.PingPacketBuilder;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The networkSession connected to the main game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class NetworkSession {
	
	/**
	 * The mapping of uids
	 */
	private static final Map<String, NetworkSession> UID_MAP = new ConcurrentHashMap<>();
	
	/**
	 * The uuid of the session
	 */
	@Getter
	@Setter
	private String uid;
	
	/**
	 * The channel instance.
	 */
	@Getter
	private Channel channel;
	
	/**
	 * If the networkSession is in the lobby
	 */
	@Getter
	@Setter
	private boolean inLobby;
	
	/**
	 * The ping count
	 */
	private byte pingCount;
	
	/**
	 * The ISAAC cipher for incoming data.
	 */
	@Getter
	@Setter
	private ISAACCipher inCipher;
	
	/**
	 * The ISAAC cipher for outgoing data
	 */
	@Getter
	@Setter
	private ISAACCipher outCipher;
	
	/**
	 * Constructs a session without a uid
	 *
	 * @param channel
	 * 		The channel of the session
	 */
	public NetworkSession(Channel channel) {
		this(channel, false);
	}
	
	/**
	 * Constructs a session with a uid
	 *
	 * @param channel
	 * 		The channel of the session
	 * @param generateUid
	 * 		If we should generate a uid for the session
	 */
	public NetworkSession(Channel channel, boolean generateUid) {
		this.channel = channel;
		// if we should create a uid for the session
		if (generateUid) {
			setUid(generateCollisionSafeUuid().toString());
			storeUid();
		}
	}
	
	/**
	 * Generates a collision-safe uuid. Although the chances of collision are insignificant we will be prepared for it.
	 */
	private UUID generateCollisionSafeUuid() {
		UUID uuid = UUID.randomUUID();
		while ((UID_MAP.containsKey(uuid.toString()))) {
			System.out.println("Generated a new uid [collision check]");
			uuid = UUID.randomUUID();
		}
		return uuid;
	}
	
	/**
	 * Handles the connection of a session
	 */
	private void storeUid() {
		UID_MAP.put(uid, this);
	}
	
	@Override
	public String toString() {
		return "NetworkSession{uid=" + uid + ", inLobby=" + inLobby + "}";
	}
	
	/**
	 * Finds a session by its uid
	 *
	 * @param uid
	 * 		The uid of the session
	 */
	public static Optional<NetworkSession> findByUid(String uid) {
		for (Entry<String, NetworkSession> entry : UID_MAP.entrySet()) {
			String entryUid = entry.getKey();
			if (Objects.equals(entryUid, uid)) {
				return Optional.ofNullable(entry.getValue());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets all the sessions that are connected
	 */
	public static Collection<NetworkSession> getAllSessions() {
		return UID_MAP.values();
	}
	
	/**
	 * Finds a session by the name of the player
	 *
	 * @param name
	 * 		The name
	 */
	public static Optional<NetworkSession> findByName(String name) {
		for (Player player : World.get().getPlayers()) {
			if (player.getDetails().getUsername().equals(name)) {
				return Optional.ofNullable(player.getSession());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Handles receiving a ping
	 */
	public void ping() {
		pingCount++;
		if (pingCount >= 5) {
			pingCount = 0;
			write(new PingPacketBuilder().build(null));
		}
	}
	
	/**
	 * Writes a packet to the channel
	 *
	 * @param packet
	 * 		The packet
	 */
	public synchronized ChannelFuture write(Packet packet) {
		try {
			return writeNoDelay(packet);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Writes a packet with no delay
	 *
	 * @param packet
	 * 		The packet to write
	 */
	protected synchronized ChannelFuture writeNoDelay(Packet packet) {
		return channel.writeAndFlush(packet);
	}
	
	/**
	 * Checking if the {@link #channel} is still active
	 */
	public boolean isActive() {
		return channel.isOpen() && channel.isWritable() && channel.isActive();
	}
	
	/**
	 * Handles the disconnection of a session
	 */
	public void disconnect() {
		UID_MAP.remove(uid);
	}
	
	/**
	 * Builds the ciphers
	 *
	 * @param inCipher
	 * 		The incoming cipher
	 * @param outCipher
	 * 		The outgoing cipher
	 */
	public void buildCiphers(ISAACCipher inCipher, ISAACCipher outCipher) {
		setInCipher(inCipher);
		setOutCipher(outCipher);
	}
	
}
