package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
@CommandManifest(description = "Heals you to max health")
public class HealCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("heal");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.restoreAll();
	}
}
