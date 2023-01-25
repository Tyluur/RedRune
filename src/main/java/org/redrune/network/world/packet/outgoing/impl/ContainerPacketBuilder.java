package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class ContainerPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The key of the container.
	 */
	private final int key;
	
	/**
	 * The items of items to send.
	 */
	private final Item[] items;
	
	/**
	 * If split interfaces.
	 */
	private final boolean split;
	
	public ContainerPacketBuilder(int key, Item[] items) {
		this(key, items, key < 0);
	}
	
	public ContainerPacketBuilder(int key, Item[] items, boolean split) {
		this.key = key;
		this.items = items;
		this.split = split;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(122, PacketType.VAR_SHORT);
		
		bldr.writeShort(key);
		bldr.writeByte(split ? 1 : 0);
		bldr.writeShort(items.length);
		for (Item item : items) {
			int id, amt;
			if (item == null) {
				id = -1;
				amt = 0;
			} else {
				id = item.getId();
				amt = item.getAmount();
			}
			bldr.writeByte(amt > 254 ? 255 : amt);
			if (amt > 254) {
				bldr.writeInt1(amt);
			}
			bldr.writeLEShortA(id + 1);
		}
		return bldr.toPacket();
	}
}
