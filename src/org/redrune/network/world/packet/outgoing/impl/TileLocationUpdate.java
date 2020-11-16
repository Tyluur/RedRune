package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.Location;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class TileLocationUpdate implements OutgoingPacketBuilder {
	
	/**
	 * The location to update
	 */
	private final Location location;
	
	public TileLocationUpdate(Location location) {
		this.location = location;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(77);
		int localX = location.getRegionX() - (player.getLastLoadedLocation().getRegionX() - 6);
		int localY = location.getRegionY() - (player.getLastLoadedLocation().getRegionY() - 6);
		bldr.writeByteS(location.getPlane());
		bldr.writeByteC(localX);
		bldr.writeByteA(localY);
		return bldr.toPacket();
	}
}
