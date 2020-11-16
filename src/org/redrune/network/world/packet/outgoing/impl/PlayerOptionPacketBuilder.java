package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public class PlayerOptionPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The option text.
	 */
	private final String option;
	
	/**
	 * If it's the first option.
	 */
	private final boolean first;
	
	/**
	 * The slot.
	 */
	private final int slot;
	
	/**
	 * Constructs a new {@code PlayerOptionPacket} {@code Object}.
	 *
	 * @param option
	 * 		The option text.
	 * @param first
	 * 		If the option is the first option.
	 * @param slot
	 * 		The option slot.
	 */
	public PlayerOptionPacketBuilder(String option, boolean first, int slot) {
		this.option = option;
		this.first = first;
		this.slot = slot;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(144, PacketType.VAR_BYTE);
		bldr.writeRS2String(option).writeByteC(first ? 1 : 0).writeByte(slot).writeShort(100);
		return bldr.toPacket();
	}
}
