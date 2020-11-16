package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.FriendDetailsCompletePacketOut;
import org.redrune.network.master.server.world.MSRepository;
import org.redrune.network.master.server.world.MSWorld;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.FRIEND_DETAILS_REQUEST_PACKET_ID })
public class FriendDetailsRequestPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		String owner = packet.readString();
		byte ownerWorldId = (byte) packet.readByte();
		String requestedName = packet.readString();
		
		Object[] details = MSRepository.getPlayerDetails(requestedName);
		boolean online = (boolean) details[1];
		byte requestedWorldId = (byte) details[2];
		
		Optional<MSWorld> optional = MSRepository.getWorld(ownerWorldId);
		if (optional.isPresent()) {
			optional.get().getSession().write(new FriendDetailsCompletePacketOut(owner, requestedName, requestedWorldId, online, (requestedWorldId == MasterConstants.LOBBY_WORLD_ID)));
		} else {
			System.out.println("Unable to find world by " + ownerWorldId);
		}
	}
}
