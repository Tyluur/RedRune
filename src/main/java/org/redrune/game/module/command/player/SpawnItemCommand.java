package org.redrune.game.module.command.player;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Spawns an item by its id", types = { Integer.class })
public class SpawnItemCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("item");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final Integer amount = args.length == 3 ? intParam(args, 2) : 1;
		player.getInventory().addItem(intParam(args, 1), amount);
	}
}
