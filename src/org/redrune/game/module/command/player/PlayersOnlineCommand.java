package org.redrune.game.module.command.player;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "View the amount of players online")
public class PlayersOnlineCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("players", "size");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		sendResponse(player, "There are currently " + World.get().getPlayers().size() + " players online.", console);
	}
}
