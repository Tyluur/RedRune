package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class GameStatusBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The flag for the public bar
	 */
	private final byte publicFlag;
	
	/**
	 * The flag for the trade bar
	 */
	private final byte tradeFlag;
	
	public GameStatusBuilder(GameBarStatus publicBar, GameBarStatus tradeBar) {
		this.publicFlag = (byte) publicBar.getValue();
		this.tradeFlag = (byte) tradeBar.getValue();
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(58);
		bldr.writeByteA(publicFlag);
		bldr.writeByteA(tradeFlag);
		return bldr.toPacket();
	}
}
