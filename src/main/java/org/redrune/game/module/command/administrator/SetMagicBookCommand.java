package org.redrune.game.module.command.administrator;

import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
@CommandManifest(description = "Sets the magic book you're on", types = { Integer.class })
public class SetMagicBookCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("setmagicbook");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getCombatDefinitions().setSpellbook(MagicBook.values()[intParam(args, 1)]);
	}
}
