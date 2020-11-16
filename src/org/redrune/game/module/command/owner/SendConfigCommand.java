package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Sends a config to the client", types = { Integer.class, Integer.class })
public class SendConfigCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("sendconfig");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getTransmitter().send(new ConfigPacketBuilder(intParam(args, 1), intParam(args, 2)).build(player));
	}
}
