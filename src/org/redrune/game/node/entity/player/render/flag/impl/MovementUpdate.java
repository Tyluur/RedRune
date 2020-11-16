package org.redrune.game.node.entity.player.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.render.flag.UpdateFlag;

/**
 * Represents the movement update flag.
 *
 * @author Emperor
 */
public class MovementUpdate extends UpdateFlag {
	
	/**
	 * The speed to move.
	 */
	private final int type;
	
	/**
	 * Constructs a new {@code MovementUpdate} {@code Object}.
	 *
	 * @param player
	 * 		The player.
	 */
	public MovementUpdate(Player player) {
		if (player.getMovement().getNextRunDirection() != -1) {
			type = 2;
		} else {
			type = 1;
		}
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder packet) {
		packet.writeByteS(type);
	}
	
	@Override
	public int getOrdinal() {
		return 17;
	}
	
	@Override
	public int getMaskData() {
		return 0x40;
	}
	
}