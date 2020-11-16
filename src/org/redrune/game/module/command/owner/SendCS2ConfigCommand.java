package org.redrune.game.module.command.owner;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.network.world.packet.outgoing.impl.CS2ConfigBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
@CommandManifest(description = "Sends a CS2 Config", types = { Integer.class, Integer.class })
public class SendCS2ConfigCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("sendcs2config");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getTransmitter().send(new CS2ConfigBuilder(intParam(args, 1), intParam(args, 2)).build(player));
	}
}
