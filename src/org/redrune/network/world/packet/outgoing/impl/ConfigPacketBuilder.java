package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class ConfigPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The varp id.
	 */
	private final int id;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	public ConfigPacketBuilder(int id, int value) {
		this.id = id;
		this.value = value;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			bldr = new PacketBuilder(135);
			bldr.writeInt(value);
			bldr.writeLEShort(id);
		} else {
			bldr = new PacketBuilder(123);
			bldr.writeByte(value);
			bldr.writeShort(id);
		}
		return bldr.toPacket();
	}
}
