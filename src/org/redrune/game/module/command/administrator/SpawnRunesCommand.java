package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
@CommandManifest(description = "Spawns every rune")
public class SpawnRunesCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("runes");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final int amount = 10_000;
		for (int i = 554; i <= 566; i++) {
			player.getInventory().addItem(i, amount);
		}
		player.getInventory().addItem(9075, amount);
	}
}
