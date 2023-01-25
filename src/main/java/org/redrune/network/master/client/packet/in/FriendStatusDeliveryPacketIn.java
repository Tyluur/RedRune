package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsiveStatusChangePacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.FRIEND_STATUS_CHANGE_DELIVER_PACKET_ID })
public class FriendStatusDeliveryPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String name = packet.readString();
		byte status = (byte) packet.readByte();
		byte worldId = (byte) packet.readByte();
		boolean online = packet.readByte() == 1;
		boolean lobby = packet.readByte() == 1;
		
		MasterCommunication.read(new ResponsiveStatusChangePacket(name, status, worldId, online, lobby));
	}
}
