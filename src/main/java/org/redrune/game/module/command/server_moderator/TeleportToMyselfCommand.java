package org.redrune.game.module.command.server_moderator;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.world.World;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/30/2017
 */
@CommandManifest(description = "Teleports a player to you", types = {String.class})
public class TeleportToMyselfCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("teletome");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		String username = args[1].replaceAll("_", " ");
		Optional<Player> optional = World.get().getPlayerByUsername(username);
		if (!optional.isPresent()) {
			player.getTransmitter().sendMessage("Couldn't find a play by name '" + username + "', try again.");
			return;
		}
		Player target = optional.get();
		target.teleport(player.getLocation());
		
		target.getTransmitter().sendMessage(player.getDetails().getDisplayName() + " teleported you to them.");
		player.getTransmitter().sendMessage(target.getDetails().getDisplayName() + " was teleported to you.");
	}
}
