package org.redrune.network.world.packet.incoming;

import org.redrune.game.GameFlags;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.utility.rs.NetworkUtils;
import org.redrune.utility.tool.Misc;

import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class IncomingPacketRepository {
	
	/**
	 * The map of {@code IncoingPacketStructure}s
	 */
	private final ConcurrentHashMap<Integer, IncomingPacketDecoder> decoderMap = new ConcurrentHashMap<>();
	
	/**
	 * The name of the package that classes are stored
	 */
	private final String packageName;
	
	public IncomingPacketRepository(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Stores all {@code IncoingPacketStructure}s
	 */
	public void storeAll() {
		NetworkUtils.loadPacketLengths();
		Misc.getClassesInDirectory(packageName).stream().filter(IncomingPacketDecoder.class::isInstance).forEach((clazz) -> include((IncomingPacketDecoder) clazz));
		System.out.println("Number of incoming packets that are handled on world " + GameFlags.worldId + " = " + decoderMap.size());
	}
	
	/**
	 * Includes the decoder
	 *
	 * @param decoder
	 * 		The decoder instance
	 */
	private void include(IncomingPacketDecoder decoder) {
		Arrays.stream(decoder.bindings()).forEach(key -> {
			if (decoderMap.containsKey(key)) {
				throw new IllegalStateException("Defined incoming packet [" + decoderMap.get(key) + "] #" + key + " already and attempted to store " + decoder + " ahead of it.");
			}
			decoderMap.put(key, decoder);
		});
	}
	
	/**
	 * Handles an incoming packet
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	public void handlePacket(Player player, Packet packet) {
		final int opcode = packet.getOpcode();
		try {
			IncomingPacketDecoder structure = decoderMap.get(opcode);
			if (structure == null) {
				System.out.println("Received game packet #" + opcode + ", unidentified handler.");
				return;
			}
			structure.read(player, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}