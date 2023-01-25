package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class ConfigFilePacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The file id of the config
	 */
	private final int fileId;
	
	/**
	 * The value to send.
	 */
	private final int value;
	
	public ConfigFilePacketBuilder(int fileId, int value) {
		this.fileId = fileId;
		this.value = value;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			bldr = new PacketBuilder(55);
			bldr.writeLEShort(fileId);
			bldr.writeByte(value);
		} else {
			bldr = new PacketBuilder(133);
			bldr.writeLEShortA(fileId);
			bldr.writeInt2(value);
		}
		return bldr.toPacket();
	}
}
