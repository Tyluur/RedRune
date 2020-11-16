package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class MinimapFlagResetBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The map x type
	 */
	private final int mapX;
	
	/**
	 * The map y type
	 */
	private final int mapY;
	
	public MinimapFlagResetBuilder(int mapX, int mapY) {
		this.mapX = mapX;
		this.mapY = mapY;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(16);
		bldr.writeByteC(mapX);
		bldr.writeByteC(mapY);
		return bldr.toPacket();
	}
}
