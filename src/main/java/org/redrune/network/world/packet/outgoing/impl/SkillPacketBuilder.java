package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class SkillPacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The skill slot id.
	 */
	private final int slot;
	
	public SkillPacketBuilder(int slot) {
		this.slot = slot;
	}
	
	@Override
	public Packet build(Player player) {
		final PacketBuilder bldr = new PacketBuilder(8);
		final PlayerSkills skills = player.getSkills();
		
		bldr.writeByteC(skills.getLevel(slot));
		bldr.writeByte(slot);
		bldr.writeInt2((int) skills.getExperience(slot));
		return bldr.toPacket();
	}
}
