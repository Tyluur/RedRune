package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Shows an interface by its id", types = { Integer.class })
public class SendInterfaceCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("interface", "inter");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		try {
			player.getManager().getInterfaces().sendInterface(intParam(args, 1), true);
		} catch (Exception e) {
			sendResponse(player, "Verify that the interface id exists.", console);
		}
	}
}
