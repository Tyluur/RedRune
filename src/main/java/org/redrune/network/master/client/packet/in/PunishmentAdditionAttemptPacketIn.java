package org.redrune.network.master.client.packet.in;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsivePunishmentPacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
@Readable(packetIds = { PacketConstants.PUNISHMENT_ADDITION_ATTEMPT_PACKET_ID })
public class PunishmentAdditionAttemptPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String punisher = packet.readString();
		String punished = packet.readString();
		byte type = (byte) packet.readByte();
		long time = packet.readLong();
		
		if (type < 0 || type >= PunishmentType.values().length) {
			System.out.println("Invalid punishment type received.");
			return;
		}
		
		// the punishment instance
		Punishment punishment = new Punishment(punisher, punished, PunishmentType.values()[type], time);
		
		// read the punishment now
		MasterCommunication.read(new ResponsivePunishmentPacket(punishment));
	}
}
