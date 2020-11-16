package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
@CommandManifest(description = "Reloads all the commands dynamically")
public class ReloadCommandRepositoryCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("reloadcommands", "rlc");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		CommandRepository.populate(true);
	}
}
