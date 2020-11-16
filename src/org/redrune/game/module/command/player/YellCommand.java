package org.redrune.game.module.command.player;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.utility.tool.ColorConstants;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/20/2017
 */
@CommandManifest(description = "Yells a message", types = { String.class })
public class YellCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("yell");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		String message = getCompleted(args, 1);
		if (message == null || message.equalsIgnoreCase("null")) {
			return;
		}
		sendYellMessage(player, message);
	}
	
	/**
	 * Sends a yell message
	 *
	 * @param player
	 * 		The player yelling
	 * @param message
	 * 		The message
	 */
	public static void sendYellMessage(Player player, String message) {
		message = Misc.fixChatMessage(message.replaceAll("<", "")).trim();
		
		StringBuilder tag = new StringBuilder();
		
		tag.append("[<col=" + ColorConstants.BLUE + ">RR</col>] ");
		tag.append(player.getDetails().getDisplayName()).append(": ").append(message);
		for (Player pl : World.get().getPlayers()) {
			if (pl == null) {
				continue;
			}
			pl.getTransmitter().sendMessage(tag.toString());
		}
	}
}
