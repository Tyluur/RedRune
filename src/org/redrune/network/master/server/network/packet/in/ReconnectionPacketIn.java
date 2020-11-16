package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.world.MSRepository;

import static org.redrune.network.master.network.packet.PacketConstants.RECONNECTION_PACKET_ID;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
@Readable(packetIds = { RECONNECTION_PACKET_ID })
public class ReconnectionPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		byte worldId = (byte) packet.readByte();
		int size = packet.readInt();
		for (int i = 0; i < size; i++) {
			String data = packet.readString();
			String[] split = data.split(":::::");
			String name = split[0];
			String uid = split[1];
			MSRepository.getWorld(worldId).ifPresent(world -> world.addPlayer(name, uid));
		}
	}
}
