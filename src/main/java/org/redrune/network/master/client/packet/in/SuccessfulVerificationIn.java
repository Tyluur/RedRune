package org.redrune.network.master.client.packet.in;

import org.redrune.network.master.client.MCFlags;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Readable(packetIds = { PacketConstants.SUCCESSFUL_VERIFICATION_PACKET_ID })
public class SuccessfulVerificationIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		byte successful = (byte) packet.readByte();
		String message = packet.readString();
		
		if (successful == 1) {
			System.out.println(message.replaceAll("#", "" + MCFlags.worldId));
			session.setVerified(true);
		} else {
			System.out.println("Unable to successfully verify our client. [" + successful + "]");
		}
	}
}
