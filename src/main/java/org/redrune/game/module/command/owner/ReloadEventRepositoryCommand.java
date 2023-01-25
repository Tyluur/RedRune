package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.event.EventRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
@CommandManifest(description = "Reloads all the events in the repository")
public class ReloadEventRepositoryCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("reloadevents");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		EventRepository.registerEvents(false);
	}
}
