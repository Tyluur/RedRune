package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.constant.GameBarStatus;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class PrivateSetupBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The flag to send
	 */
	private final byte flag;
	
	public PrivateSetupBuilder(GameBarStatus bar) {
		this.flag = (byte) bar.getValue();
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(104);
		bldr.writeByte(flag);
		return bldr.toPacket();
	}
}
