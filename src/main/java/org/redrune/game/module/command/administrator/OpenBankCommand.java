package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class OpenBankCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("bank");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getBank().open();
	}
}
