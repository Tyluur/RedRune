package org.redrune.network.lobby.packet.readable;

import org.redrune.game.world.list.WorldList;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/25/2017
 */
@Readable(packetIds = { PacketConstants.REPOSITORY_UPDATE_PACKET_ID })
public class LobbyRepositoryPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		byte id = (byte) packet.readByte();
		int size = packet.readInt();
		
		// updates the size of the world
		WorldList.updateSize(id, size);
	}
}
