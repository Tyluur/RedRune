package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class CS2StringBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The config id
	 */
	private final int configId;
	
	/**
	 * The value of the config
	 */
	private final String configValue;
	
	public CS2StringBuilder(int configId, String configValue) {
		this.configId = configId;
		this.configValue = configValue;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(52, PacketType.VAR_BYTE);
		bldr.writeRS2String(configValue);
		bldr.writeShortA(configId);
		return bldr.toPacket();
	}
}
