package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class MinimapFlagBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The x to write the flag on
	 */
	private final int flagX;
	
	/**
	 * The y to write the flag on
	 */
	private final int flagY;
	
	public MinimapFlagBuilder(int flagX, int flagY) {
		this.flagX = flagX;
		this.flagY = flagY;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(16);
		bldr.writeByteC(flagX).writeByteC(flagY);
		return bldr.toPacket();
	}
}
