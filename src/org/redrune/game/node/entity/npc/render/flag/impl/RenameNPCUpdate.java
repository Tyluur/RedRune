package org.redrune.game.node.entity.npc.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.render.flag.UpdateFlag;

/**
 * Represents the rename NPC update mask.
 *
 * @author Emperor
 */
public class RenameNPCUpdate extends UpdateFlag {
	
	/**
	 * The name to set.
	 */
	private final String name;
	
	/**
	 * Constructs a new {@code RenameNPCUpdate} {@code Object}.
	 *
	 * @param name
	 * 		The name to set.
	 */
	public RenameNPCUpdate(String name) {
		this.name = name;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		bldr.writeRS2String(name);
	}
	
	@Override
	public int getOrdinal() {
		return 17;
	}
	
	@Override
	public int getMaskData() {
		return 0x100000;
	}
	
}
