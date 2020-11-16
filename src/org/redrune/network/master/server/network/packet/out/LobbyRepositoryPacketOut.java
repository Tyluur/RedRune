package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;
import org.redrune.network.master.server.world.MSWorld;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/25/2017
 */
public class LobbyRepositoryPacketOut extends WritablePacket {
	
	/**
	 * The world we're writing for
	 */
	private final MSWorld world;
	
	/**
	 * Constructs the repository outgoing packet
	 */
	public LobbyRepositoryPacketOut(MSWorld world) {
		super(REPOSITORY_UPDATE_PACKET_ID);
		this.world = world;
	}
	
	@Override
	public WritablePacket create() {
		writeByte(world.getId());
		writeInt(world.getId());
		return this;
	}
}
