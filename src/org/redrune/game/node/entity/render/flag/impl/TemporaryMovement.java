package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class TemporaryMovement extends UpdateFlag {
	
	/**
	 * The speed to move.
	 */
	private final int type;
	
	public TemporaryMovement(int type) {
		this.type = type;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder packet) {
		packet.writeByteC(type);
	}
	
	@Override
	public int getOrdinal() {
		return 11;
	}
	
	@Override
	public int getMaskData() {
		return 0x400;
	}
}
