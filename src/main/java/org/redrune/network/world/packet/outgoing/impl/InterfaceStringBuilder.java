package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class InterfaceStringBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The id of the interface to change text on
	 */
	private final int interfaceId;
	
	/**
	 * The component id of the interface to change text on
	 */
	private final int componentId;
	
	/**
	 * The text we will store
	 */
	private final String text;
	
	public InterfaceStringBuilder(int interfaceId, int componentId, String text) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.text = text;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(69, PacketType.VAR_SHORT);
		bldr.writeInt(interfaceId << 16 | componentId);
		bldr.writeRS2String(text);
		return bldr.toPacket();
	}
}
