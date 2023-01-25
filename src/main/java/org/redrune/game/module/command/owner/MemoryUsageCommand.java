package org.redrune.game.module.command.owner;

import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
@CommandManifest(description = "Shows how much memory is used.")
public class MemoryUsageCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("memused");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		String info = Misc.getMemoryUsageInformation();
		System.out.println(info);
		player.getTransmitter().sendMessage(info, false);
	}
}
