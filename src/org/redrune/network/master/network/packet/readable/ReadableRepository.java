package org.redrune.network.master.network.packet.readable;

import org.redrune.network.master.network.MasterSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.utility.Utility;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class ReadableRepository {
	
	/**
	 * The name of the package with readable packets
	 */
	private final String packageName;
	
	/**
	 * The map of readable packets
	 */
	private final Map<Integer, ReadablePacket> packets = new HashMap<>();
	
	/**
	 * Constructs a new readable packet repository instance
	 *
	 * @param packageName
	 * 		The name of the package this instance is for
	 */
	public ReadableRepository(String packageName) {
		this.packageName = packageName;
		loadAll();
	}
	
	/**
	 * Loads all readable packets
	 */
	private void loadAll() {
		Utility.getClassesInDirectory(packageName).stream().filter(ReadablePacket.class::isInstance).forEach(clazz -> {
			ReadablePacket packet = (ReadablePacket) clazz;
			if (!packet.getClass().isAnnotationPresent(Readable.class)) {
				throw new IllegalStateException("Readable class " + clazz + " must have Readable annotation present.");
			}
			include(packet);
		});
		System.out.println("Prepared " + packets.size() + " readable packets.");
	}
	
	/**
	 * Includes the readable packet into the mapping
	 *
	 * @param packet
	 * 		The packet
	 */
	public void include(ReadablePacket packet) {
		Readable readable = packet.getClass().getAnnotation(Readable.class);
		for (int key : readable.packetIds()) {
			if (packets.containsKey(key)) {
				throw new IllegalStateException("Attempted duplicate key insertion for key " + key);
			}
			packets.put(key, packet);
		}
	}
	
	/**
	 * Reads a packet
	 *
	 * @param session
	 * 		The session the packet came from
	 * @param packet
	 * 		The packet
	 */
	@SuppressWarnings("unchecked")
	public void read(MasterSession session, IncomingPacket packet) {
		int packetId = packet.getId();
		ReadablePacket readable = packets.get(packetId);
		if (readable == null) {
			System.out.println("Unable to find master packet with id " + packetId);
			return;
		}
		// so we can only read verification packets before we're verified
		if ((packetId != PacketConstants.SUCCESSFUL_VERIFICATION_PACKET_ID && packetId != PacketConstants.VERIFICATION_ATTEMPT_PACKET_ID) && !session.isVerified()) {
			System.out.println("Attempted to receive master packet #" + packet + " before session was verified.");
			return;
		}
		readable.read(session, packet);
//		System.out.println("Received master packet #" + packetId + " [" + readable.getClass().getSimpleName() + "]");
	}
	
}
