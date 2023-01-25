package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
@CommandManifest(description = "Plays a graphic", types = { Integer.class })
public class PlayGraphicCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("gfx");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.sendGraphics(intParam(args, 1), intParamOrDefault(args, 2, 0), intParamOrDefault(args, 3, 0));
	}
}
