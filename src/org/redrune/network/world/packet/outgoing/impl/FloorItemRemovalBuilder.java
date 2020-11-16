package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemRemovalBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The floor item
	 */
	private final FloorItem item;
	
	public FloorItemRemovalBuilder(FloorItem item) {
		this.item = item;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(46);
		int deltaX = item.getLocation().getX() - (player.getLastLoadedLocation().getRegionX() << 3);
		int deltaY = item.getLocation().getY() - (player.getLastLoadedLocation().getRegionY() << 3);
		player.getTransmitter().send(new TileLocationUpdate(item.getLocation()).build(player));
		bldr.writeShortA(item.getId());
		bldr.writeByteS((deltaX & 0x7) << 4 | deltaY & 0x7);
		return bldr.toPacket();
	}
}
