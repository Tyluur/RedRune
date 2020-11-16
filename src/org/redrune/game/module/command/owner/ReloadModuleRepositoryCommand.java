package org.redrune.game.module.command.owner;

import org.redrune.game.module.ModuleRepository;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
@CommandManifest(description = "Reloads all game modules")
public class ReloadModuleRepositoryCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("reloadmodules");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		ModuleRepository.registerAllModules(true);
	}
}
