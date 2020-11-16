package org.redrune.game.module.command.owner;

import org.redrune.cache.CacheFileStore;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.InterfaceChangeBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Debugs an interface", types = { Integer.class })
public class DebugInterfaceCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("dbi");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int interId = intParam(args, 1);
		int length = CacheFileStore.getAmountOfComponents(interId);
		for (int index = 0; index < length; index++) {
			player.getManager().getInterfaces().sendInterfaceText(interId, index, "" + index);
		}
		for (int index = 0; index < length; index++) {
			player.getTransmitter().send(new InterfaceChangeBuilder(interId, index, false).build(player));
		}
		player.getManager().getInterfaces().sendInterface(interId, true);
		String text = "Interface #" + interId + " has " + length + " component length";
		player.getTransmitter().sendMessage(text);
		System.out.println(text);
	}
}
