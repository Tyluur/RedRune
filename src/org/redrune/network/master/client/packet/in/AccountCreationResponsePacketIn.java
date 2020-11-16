package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsiveCreationPacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
@Readable(packetIds = { PacketConstants.ACCOUNT_CREATION_RESPONSE_PACKET_ID })
public class AccountCreationResponsePacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String uid = packet.readString();
		byte responseId = (byte) packet.readByte();
		
		MasterCommunication.read(new ResponsiveCreationPacket(uid, responseId));
	}
}
