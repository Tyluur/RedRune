package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.FriendStatusDeliveryPacketOut;
import org.redrune.network.master.server.world.MSRepository;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.FRIEND_STATUS_CHANGE_RECEIVE_PACKET_ID })
public class FriendStatusChangePacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String name = packet.readString();
		byte status = (byte) packet.readByte();
		byte worldId = (byte) packet.readByte();
		boolean online = packet.readByte() == 1;
		boolean lobby = packet.readByte() == 1;
		
		// sending the update to all worlds available
		Arrays.stream(MSRepository.getWorlds()).filter(Objects::nonNull).forEach(world -> world.getSession().write(new FriendStatusDeliveryPacketOut(name, status, worldId, online, lobby)));
	}
}
