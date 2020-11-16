package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class ReconnectionPacketOut extends WritablePacket {
	
	/**
	 * The id of the world this packet is for
	 */
	private final byte worldId;
	
	/**
	 * The list of players
	 */
	private final List<String> playerData;
	
	/**
	 * Constructs a new outgoing packet
	 */
	public ReconnectionPacketOut(byte worldId, List<String> playerData) {
		super(RECONNECTION_PACKET_ID);
		this.worldId = worldId;
		this.playerData = playerData;
	}
	
	@Override
	public WritablePacket create() {
		writeByte(worldId);
		writeInt(playerData.size());
		for (String data : playerData) {
			writeString(data);
		}
		return this;
	}
}
