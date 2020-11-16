package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
@CommandManifest(description = "Toggles whether u can be hit.")
public class UnhittableCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("togglehittable");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.putAttribute("unhittable", !player.getAttribute("unhittable", false));
		player.getTransmitter().sendMessage("You are now " + (player.getAttribute("unhittable", false) ? "un" : "") + "hittable.");
	}
}
