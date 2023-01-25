package org.redrune.network.master.client.packet.in;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsivePunishmentSuccessPacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
@Readable(packetIds = { PacketConstants.PUNISHMENT_ADDITION_SUCCESS_DELIVERY_PACKET_ID, PacketConstants.PUNISHMENT_REMOVAL_SUCCESS_DELIVERY_PACKET })
public class PunishmentStateSuccessDeliveryPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
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
		
		// read it now
		MasterCommunication.read(new ResponsivePunishmentSuccessPacket(worldId, punishment, message));
	}
}
