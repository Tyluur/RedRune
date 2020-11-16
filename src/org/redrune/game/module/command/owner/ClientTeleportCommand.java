package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class ClientTeleportCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("tele");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int x, y, z;
		if (args[1].contains(",")) {
			String[] args2 = args[1].split(",");
			x = Integer.parseInt(args2[1]) << 6 | Integer.parseInt(args2[3]);
			y = Integer.parseInt(args2[2]) << 6 | Integer.parseInt(args2[4]);
			z = Integer.parseInt(args2[0]);
		} else {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			z = player.getLocation().getPlane();
			if (args.length > 3) {
				z = Integer.parseInt(args[3]);
			}
		}
		player.teleport(Location.create(x, y, z));
	}
	
	@Override
	public boolean consoleUsageOnly() {
		return true;
	}
}
