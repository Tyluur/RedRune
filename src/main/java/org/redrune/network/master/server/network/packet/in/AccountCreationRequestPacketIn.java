package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.engine.MSEngineFactory;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.utility.rs.LoginRequest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
@Readable(packetIds = { PacketConstants.ACCOUNT_CREATION_REQUEST_PACKET_ID })
public class AccountCreationRequestPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String username = packet.readString();
		String password = packet.readString();
		String uid = packet.readString();
		
		// adds a login request
		MSEngineFactory.addLoginRequest(new LoginRequest((byte) -1, true, username, password, session, uid));
	}
}
