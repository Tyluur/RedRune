package org.redrune.game.module.command.owner;

import org.redrune.utility.backend.Priority;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
@CommandManifest(description = "Plays an animation", types = { Integer.class })
public class PlayAnimationCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("anim", "animate");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.sendAnimation(intParam(args, 1), intParamOrDefault(args, 2, 0), Priority.NORMAL);
	}
}
