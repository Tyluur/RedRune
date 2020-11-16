package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class SystemUpdateBuilder implements OutgoingPacketBuilder {
	
	/**
	 * Updating time tick.
	 */
	private final int time;
	
	public SystemUpdateBuilder(int time) {
		this.time = time;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(10);
		bldr.writeShort(125);
		bldr.writeShort((int) (time * 1.6));
		return bldr.toPacket();
	}
}
