package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.rs.constant.InterfaceConstants;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class CloseInterfaceBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The window id.
	 */
	private final int paneId;
	
	/**
	 * The child id.
	 */
	private final int componentId;
	
	/**
	 * Constructs a new close interface builder. The pane id is the pane the interface is displayed on. Most likely
	 * {@link InterfaceConstants#SCREEN_FIXED_WINDOW_ID}. The child id is the child id
	 * that the interface was displayed on. The actual interface id is irrelevant when closing.
	 *
	 * @param paneId
	 * 		The paneId
	 * @param componentId
	 * 		The component id to close the interface on
	 */
	public CloseInterfaceBuilder(int paneId, int componentId) {
		this.paneId = paneId;
		this.componentId = componentId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(29);
		bldr.writeInt2(paneId << 16 | componentId);
		return bldr.toPacket();
	}
}
