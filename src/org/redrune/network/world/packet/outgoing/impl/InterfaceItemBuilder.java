package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class InterfaceItemBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The id of the interface
	 */
	private final int interfaceId;
	
	/**
	 * The interface component to use
	 */
	private final int componentId;
	
	/**
	 * The id of the item
	 */
	private final int itemId;
	
	/**
	 * The amount of the item
	 */
	private final int amount;
	
	public InterfaceItemBuilder(int interfaceId, int componentId, int itemId, int amount) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.itemId = itemId;
		this.amount = amount;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(64);
		bldr.writeInt(amount);
		bldr.writeShortA(itemId);
		bldr.writeInt1(interfaceId << 16 | componentId);
		return bldr.toPacket();
	}
}
