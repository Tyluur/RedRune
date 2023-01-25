package org.redrune.network.master.server.network.packet.in;

import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.server.network.packet.out.LobbyRepositoryPacketOut;
import org.redrune.network.master.server.world.MSRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PLAYER_DISCONNECTION_PACKET_ID })
public class PlayerDisconnectionPacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		byte worldId = (byte) packet.readByte();
		boolean lobby = packet.readByte() == 1;
		String username = packet.readString();
		
		MSRepository.getWorld(worldId).ifPresent(world -> {
			world.removePlayer(username);
			// sends the repository update as long as the world isn't a lobby
			if (!world.isLobby()) {
				MSRepository.getWorld(MasterConstants.LOBBY_WORLD_ID).ifPresent(lobbyWorld -> lobbyWorld.getSession().write(new LobbyRepositoryPacketOut(world)));
			}
		});
	}
}
