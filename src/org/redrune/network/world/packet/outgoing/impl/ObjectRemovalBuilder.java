package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.object.GameObject;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class ObjectRemovalBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The object that is being sent to the client
	 */
	private final GameObject object;
	
	/**
	 * Constructs a new removal instance
	 *
	 * @param object
	 * 		The object being removed
	 */
	public ObjectRemovalBuilder(GameObject object) {
		this.object = object;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(110);
		int localX = object.getLocation().getX() - (player.getLastLoadedLocation().getRegionX() - 6) * 8;
		int localY = object.getLocation().getY() - (player.getLastLoadedLocation().getRegionY() - 6) * 8;
		player.getTransmitter().send(new TileLocationUpdate(object.getLocation()).build(player));
		bldr.writeByte(((localX - ((localX >> 3) << 3)) << 4) | ((localY - ((localY >> 3) << 3)) & 0x7));
		bldr.writeByteC((object.getType() << 2) + (object.getRotation() & 3));
		return bldr.toPacket();
	}
}
