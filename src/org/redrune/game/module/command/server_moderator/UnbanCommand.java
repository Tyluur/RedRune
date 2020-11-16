package org.redrune.game.module.command.server_moderator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.punishment.PunishmentHandler;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
@CommandManifest(description = "Unbans a player on any world", types = { String.class })
public class UnbanCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("unban");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		final String name = Misc.formatPlayerNameForProtocol(getCompleted(args, 1));
		PunishmentHandler.removePunishment(player, name, PunishmentType.BAN);
	}
}
