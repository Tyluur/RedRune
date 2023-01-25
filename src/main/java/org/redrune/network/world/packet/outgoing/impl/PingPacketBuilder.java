package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class PingPacketBuilder implements OutgoingPacketBuilder {
	
	@Override
	public Packet build(Player player) {
		return new PacketBuilder(128).toPacket();
	}
}
