package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class InterfaceAnimationBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The id of the interface to animate
	 */
	private final int interfaceId;
	
	/**
	 * The component to animate on the interface
	 */
	private final int componentId;
	
	/**
	 * The animation to animate with
	 */
	private final int animationId;
	
	public InterfaceAnimationBuilder(int interfaceId, int componentId, int animationId) {
		this.interfaceId = interfaceId;
		this.componentId = componentId;
		this.animationId = animationId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(131);
		bldr.writeShort(animationId);
		bldr.writeInt(interfaceId << 16 | componentId);
		return bldr.toPacket();
	}
}
