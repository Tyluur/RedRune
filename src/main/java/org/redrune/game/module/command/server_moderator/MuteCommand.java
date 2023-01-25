package org.redrune.game.module.command.server_moderator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.punishment.PunishmentHandler;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.utility.rs.input.InputResponse;
import org.redrune.utility.rs.input.InputType;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
@CommandManifest(description = "Mutes a player on any world", types = { String.class })
public class MuteCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("mute");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final String name = Misc.formatPlayerNameForProtocol(getCompleted(args, 1));
		player.getTransmitter().requestInput(input -> PunishmentHandler.addPunishment(player, name, InputResponse.getInput(input), PunishmentType.MUTE), InputType.INTEGER, "Enter hours (0 = infinite):");
	}
}
