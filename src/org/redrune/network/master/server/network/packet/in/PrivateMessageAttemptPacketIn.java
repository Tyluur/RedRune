package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.PrivateMessageDeliveredPacketOut;
import org.redrune.network.master.server.network.packet.out.PrivateMessageReceivedPacketOut;
import org.redrune.network.master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.PRIVATE_MESSAGE_ATTEMPT_PACKET_ID })
public class PrivateMessageAttemptPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String sourceName = packet.readString();
		byte sourceWorld = (byte) (packet.readByte());
		byte sourceClientRights = (byte) (packet.readByte());
		String deliveryName = packet.readString();
		String message = packet.readString();
		
		MSRepository.getSessionByUsername(deliveryName).ifPresent(receiveSession -> receiveSession.write(new PrivateMessageReceivedPacketOut(sourceName, deliveryName, sourceClientRights, message)));
		MSRepository.getWorld(sourceWorld).ifPresent(world -> world.getSession().write(new PrivateMessageDeliveredPacketOut(sourceName, deliveryName, message)));
	}
}
