package org.redrune.game.module.command.owner;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigFilePacketBuilder;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
@CommandManifest(types = { Integer.class, Integer.class })
public class SendConfigFileCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("sendconfigfile");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getTransmitter().send(new ConfigFilePacketBuilder(intParam(args, 1), intParam(args, 2)).build(player));
	}
}
