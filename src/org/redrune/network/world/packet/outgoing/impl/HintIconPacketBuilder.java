package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.HintIcon;
import org.redrune.utility.rs.HintIcon.HintIconType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class HintIconPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The hint icon
	 */
	private final HintIcon icon;
	
	public HintIconPacketBuilder(HintIcon icon) {
		this.icon = icon;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(116);
		// icon encoding
		bldr.writeByte((icon.getIconType().getValue() & 0x1f) | (icon.getSlot() << 5));
		// arrow stuff
		bldr.writeByte(icon.getArrowType().getValue());
		
		// the removal overrides
		if (icon.getIconType() == HintIconType.REMOVAL) {
			bldr.skip(11);
		} else if (icon.getLocation() == null) {
			// icon is being sent to an entity
			bldr.writeShort(icon.getTargetIndex());
			// how often the arrow [non-minimap] flashes, [2500 ideal, 0 never]
			bldr.writeShort(2500);
			// skip
			bldr.skip(4);
		} else {
			// unknown
			bldr.writeByte(0);
			// location
			bldr.writeShort(icon.getLocation().getX());
			bldr.writeShort(icon.getLocation().getY());
			// distance from floor
			bldr.writeByte(icon.getFloorDistance() * 4 >> 2);
			// distance to start showing on minimap [0 doesnt show, -1 infinite]
			bldr.writeShort(-1);
		}
		// model stuff
		bldr.writeShort(icon.getModelId());
		return bldr.toPacket();
	}
}
