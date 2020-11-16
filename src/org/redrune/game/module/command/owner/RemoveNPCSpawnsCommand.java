package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
public class RemoveNPCSpawnsCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("rspns");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final String key = "remove_npc_spawns";
		player.putAttribute(key, !player.getAttribute(key, false));
		player.getTransmitter().sendMessage("You are now " + (player.getAttribute(key, false) ? "removing" : "examining") + " npcs.");
	}
}
