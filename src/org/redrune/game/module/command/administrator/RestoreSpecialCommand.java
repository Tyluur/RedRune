package org.redrune.game.module.command.administrator;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
@CommandManifest(description = "Restores your special to 100%")
public class RestoreSpecialCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("spec");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getCombatDefinitions().setSpecialEnergy((byte) 100);
	}
}
