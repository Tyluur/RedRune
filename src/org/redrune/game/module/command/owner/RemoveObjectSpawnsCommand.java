package org.redrune.game.module.command.owner;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class RemoveObjectSpawnsCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("rmospns");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final String key = "remove_spawns";
		player.putAttribute(key, !player.getAttribute(key, false));
		player.getTransmitter().sendMessage("You are now " + (player.getAttribute(key, false) ? "removing" : "examining") + " objects.");
	}
}
