package org.redrune.game.module.command.server_moderator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.MessageBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
@CommandManifest(description = "Teleports you to specific coordinates", types = { Integer.class, Integer.class })
public class TeleportCoordinatesCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("xtele");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		try {
			Integer x = intParam(args, 1);
			Integer y = intParam(args, 2);
			Integer plane = args.length == 4 ? intParam(args, 3) : player.getLocation().getPlane();
			
			player.teleport(Location.create(x, y, plane));
		} catch (NumberFormatException e) {
			player.getTransmitter().send(new MessageBuilder("Invalid parameters...").build(player));
		}
	}
}
