package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class InterfaceEntityBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The interface to show the entity on
	 */
	private final int interfaceId;
	
	/**
	 * The component of the interface to show the entity on
	 */
	private final int componentId;
	
	/**
	 * The id of the entity to draw
	 */
	private final int entityId;
	
	public InterfaceEntityBuilder(int interfaceId, int componentId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.entityId = -1;
	}
	
	public InterfaceEntityBuilder(int interfaceId, int componentId, int entityId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.entityId = entityId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr;
		if (entityId == -1) {
			bldr = new PacketBuilder(48);
			bldr.writeInt1(interfaceId << 16 | componentId);
		} else {
			bldr = new PacketBuilder(136);
			bldr.writeInt(interfaceId << 16 | componentId);
			bldr.writeLEShortA(entityId);
		}
		return bldr.toPacket();
	}
}
