package org.redrune.network.master.server.network.packet.in;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.PunishmentAdditionSuccessDeliveryPacketOut;
import org.redrune.network.master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
@Readable(packetIds = { PacketConstants.PUNISHMENT_ADDITION_SUCCESS_ALERT_PACKET_ID })
public class PunishmentAdditionSuccessPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		byte worldId = (byte) packet.readByte();
		String message = packet.readString();
		String punisher = packet.readString();
		String punished = packet.readString();
		byte type = (byte) packet.readByte();
		long time = packet.readLong();
		
		if (type < 0 || type >= PunishmentType.values().length) {
			System.out.println("Invalid punishment type received.");
			return;
		}
		
		// the instance of the punishment
		Punishment punishment = new Punishment(punisher, punished, PunishmentType.values()[type], time);
		
		// sends the successful delivery back to the punisher
		MSRepository.getSessionByUsername(punisher).ifPresent(worldSession -> {
			worldSession.write(new PunishmentAdditionSuccessDeliveryPacketOut(worldId, punishment, message));
		});
	}
}
