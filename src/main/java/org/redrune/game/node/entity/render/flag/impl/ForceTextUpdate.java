package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * Handles the chat update flag.
 *
 * @author Emperor
 */
public class ForceTextUpdate extends UpdateFlag {
	
	/**
	 * The message to send.
	 */
	private final String message;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code ChatUpdate} {@code Object}.
	 *
	 * @param message
	 * 		The message.
	 */
	public ForceTextUpdate(String message, boolean npc) {
		this.message = message;
		this.npc = npc;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		bldr.writeRS2String(message);
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 18 : 7;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x80 : 0x4000;
	}
	
}