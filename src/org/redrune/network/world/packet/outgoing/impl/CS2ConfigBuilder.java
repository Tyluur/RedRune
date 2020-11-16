package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class CS2ConfigBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The config id.
	 */
	private final int id;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	/**
	 * Constructs a new cs2 config builder
	 *
	 * @param id
	 * 		The id of the config
	 * @param value
	 * 		The value of the config
	 */
	public CS2ConfigBuilder(int id, int value) {
		this.id = id;
		this.value = value;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE) {
			bldr = new PacketBuilder(49);
			bldr.writeShortA(id);
			bldr.writeByteC(value);
		} else {
			bldr = new PacketBuilder(37);
			bldr.writeInt2(value);
			bldr.writeLEShort(id);
		}
		return bldr.toPacket();
	}
}
