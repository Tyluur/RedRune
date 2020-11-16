package org.redrune.game.node.entity.npc.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * Handles the NPC combat level update mask.
 *
 * @author Emperor
 */
public class CombatLevelUpdate extends UpdateFlag {

	/**
	 * The combat level to set.
	 */
	private final int level;
	
	/**
	 * Constructs a new {@code CombatLevelUpdate} {@code Object}.
	 *
	 * @param level
	 * 		The combat level to set.
	 */
	public CombatLevelUpdate(int level) {
		this.level = level;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		bldr.writeLEShort(level);
	}

	@Override
	public int getOrdinal() {
		return 4;
	}

	@Override
	public int getMaskData() {
		return 0x20000;
	}

}
