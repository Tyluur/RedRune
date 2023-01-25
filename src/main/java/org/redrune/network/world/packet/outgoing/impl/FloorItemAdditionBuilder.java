package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemAdditionBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The floor item
	 */
	private final FloorItem item;
	
	public FloorItemAdditionBuilder(FloorItem item) {
		this.item = item;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(35);
		int deltaX = item.getLocation().getX() - (player.getLastLoadedLocation().getRegionX() << 3);
		int deltaY = item.getLocation().getY() - (player.getLastLoadedLocation().getRegionY() << 3);
		player.getTransmitter().send(new TileLocationUpdate(item.getLocation()).build(player));
		bldr.writeLEShort(item.getAmount());
		bldr.writeShort(0);
		bldr.writeByte((deltaX & 0x7) << 4 | deltaY & 0x7);
		bldr.writeShortA(item.getId());
		return bldr.toPacket();
	}
}
