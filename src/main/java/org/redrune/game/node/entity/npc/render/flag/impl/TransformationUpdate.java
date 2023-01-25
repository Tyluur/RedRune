package org.redrune.game.node.entity.npc.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/25/2017
 */
public class TransformationUpdate extends UpdateFlag {
	
	/**
	 * The id of the npc to transform into
	 */
	private final int transformId;
	
	public TransformationUpdate(int transformId) {
		this.transformId = transformId;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder packet) {
		packet.writeShortA(transformId);
	}
	
	@Override
	public int getOrdinal() {
		return 19;
	}
	
	@Override
	public int getMaskData() {
		return 0x40;
	}
}
