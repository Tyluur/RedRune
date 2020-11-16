package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class InterfaceDisplayBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The window id.
	 */
	private final int paneId;
	
	/**
	 * The interface id.
	 */
	private final int componentId;
	
	/**
	 * The child id.
	 */
	private final int interfaceId;
	
	/**
	 * If the interface is an overlay.
	 */
	private boolean walkable;
	
	/**
	 * Constructs a new interface display builder
	 *
	 * @param paneId
	 * 		The pane id of the interface
	 * @param componentId
	 * 		The component id of the interface (where to display it)
	 * @param interfaceId
	 * 		The id of the interface
	 * @param walkable
	 * 		If we should display the interface as walkable
	 */
	public InterfaceDisplayBuilder(int paneId, int componentId, int interfaceId, boolean walkable) {
		this.paneId = paneId;
		this.componentId = componentId;
		this.interfaceId = interfaceId;
		this.walkable = walkable;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(139);
		bldr.writeByteS(walkable ? 1 : 0);
		bldr.writeShortA(interfaceId);
		bldr.writeInt(paneId << 16 | componentId);
		return bldr.toPacket();
	}
}
