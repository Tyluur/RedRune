package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.constant.GameBarStatus;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class PrivateStatusBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The value to send
	 */
	private final int value;
	
	public PrivateStatusBuilder(int value) {
		this.value = value;
	}
	
	public PrivateStatusBuilder(GameBarStatus status) {
		this.value = status.getValue();
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(104);
		bldr.writeByte(value);
		return bldr.toPacket();
	}
}
