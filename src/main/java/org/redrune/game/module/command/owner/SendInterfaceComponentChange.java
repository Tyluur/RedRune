package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
@CommandManifest(description = "Changes an interface's component", types = { Integer.class, Integer.class, Boolean.class })
public class SendInterfaceComponentChange extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("changeic", "cic");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int interfaceId = intParam(args, 1);
		int componentId = intParam(args, 2);
		Boolean hide = boolParam(args, 3);
		player.getManager().getInterfaces().sendInterface(interfaceId, true);
		player.getManager().getInterfaces().sendInterfaceComponentChange(interfaceId, componentId, hide);
	}
	
}
