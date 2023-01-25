package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsiveFriendDetailsPacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.FRIEND_DETAILS_COMPLETE_PACKET_ID})
public class FriendDetailsCompletePacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String owner = packet.readString();
		String requestedName = packet.readString();
		byte requestedWorldId = (byte) packet.readByte();
		boolean online = packet.readByte() == 1;
		boolean lobby = packet.readByte() == 1;
		
		MasterCommunication.read(new ResponsiveFriendDetailsPacket(owner, requestedName, requestedWorldId, online, lobby));
	}
}
