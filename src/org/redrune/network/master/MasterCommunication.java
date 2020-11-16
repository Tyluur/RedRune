package org.redrune.network.master;

import org.redrune.game.GameFlags;
import org.redrune.network.lobby.packet.readable.LobbyRepositoryPacketIn;
import org.redrune.network.master.client.network.MCNetworkSystem;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;
import org.redrune.network.master.network.packet.OutgoingPacket;
import org.redrune.network.master.network.packet.PacketConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class MasterCommunication implements PacketConstants {
	
	/**
	 * The instance of the network system
	 */
	private static final MCNetworkSystem SYSTEM = new MCNetworkSystem(GameFlags.worldId);
	
	/**
	 * Starts the communication
	 */
	public static void start() {
		SYSTEM.connect();
		if (GameFlags.worldId == MasterConstants.LOBBY_WORLD_ID) {
			MCSession.getReadableRepository().include(new LobbyRepositoryPacketIn());
		}
	}
	
	/**
	 * Writes an outgoing packet to the master server
	 *
	 * @param packet
	 * 		The packet to write
	 */
	public static void write(OutgoingPacket packet) {
		try {
			SYSTEM.write(packet);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Reads packets that the client receives back
	 *
	 * @param packet
	 * 		The responsive game packet that is read
	 */
	public static void read(ResponsiveGamePacket packet) {
		try {
			packet.read();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Checks that we are connected to the master server
	 */
	public static boolean isConnected() {
		if (SYSTEM.getSession() == null) {
			return false;
		}
		// if we had a session we need to make sure its still connected
		return SYSTEM.getSession().isConnected();
	}
	
}