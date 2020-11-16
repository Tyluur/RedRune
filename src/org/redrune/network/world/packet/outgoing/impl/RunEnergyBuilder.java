package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class RunEnergyBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The amount of energy
	 */
	private final int energyAmount;
	
	public RunEnergyBuilder(int energyAmount) {
		this.energyAmount = energyAmount;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(18);
		bldr.writeByte(energyAmount);
		return bldr.toPacket();
	}
}
