package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.network.world.packet.Packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class GameWindowBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The pane id.
	 */
	private final int paneId;
	
	/**
	 * The sub-window id.
	 */
	private final int subWindowId;
	
	public GameWindowBuilder(int paneId, int subWindowId) {
		this.paneId = paneId;
		this.subWindowId = subWindowId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(100);
		bldr.writeByteA(subWindowId);
		bldr.writeLEShortA(paneId);
		return bldr.toPacket();
	}
}
