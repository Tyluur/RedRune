package org.redrune.game.module.command.owner;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.repository.item.ItemRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class ReloadCombatRepositoryCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("reloadcombat");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		CombatRegistry.clearAll();
		CombatRegistry.registerAll();
		ItemRepository.initialize(true);
	}
}
