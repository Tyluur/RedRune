package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;

import static org.redrune.utility.rs.constant.MagicConstants.MagicBook.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/23/2017
 */
public class SpellbookInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(REGULAR.getInterfaceId(), ANCIENTS.getInterfaceId(), LUNARS.getInterfaceId());
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case 192: // regulars
				if (componentId == 2) {
					player.getCombatDefinitions().setDefensiveCasting(!player.getCombatDefinitions().isDefensiveCasting());
				} else if (componentId == 7) {
					player.getCombatDefinitions().switchShowCombatSpells();
				} else if (componentId == 9) {
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				} else if (componentId == 11) {
					player.getCombatDefinitions().switchShowMiscellaneousSpells();
				} else if (componentId == 13) {
					player.getCombatDefinitions().switchShowSkillSpells();
				} else if (componentId >= 15 & componentId <= 17) {
					player.getCombatDefinitions().setSortSpellBook(componentId - 15);
				} else {
					CombatRegistry.processSpell(player, componentId);
				}
				break;
			case 193: // ancients
				if (componentId == 5) {
					player.getCombatDefinitions().switchShowCombatSpells();
				} else if (componentId == 7) {
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				} else if (componentId >= 9 && componentId <= 11) {
					player.getCombatDefinitions().setSortSpellBook(componentId - 9);
				} else if (componentId == 18) {
					player.getCombatDefinitions().setDefensiveCasting(!player.getCombatDefinitions().isDefensiveCasting());
				} else {
					CombatRegistry.processSpell(player, componentId);
				}
				break;
			case 430: // lunars
				if (componentId == 5) {
					player.getCombatDefinitions().switchShowCombatSpells();
				} else if (componentId == 7) {
					player.getCombatDefinitions().switchShowTeleportSkillSpells();
				} else if (componentId == 9) {
					player.getCombatDefinitions().switchShowMiscellaneousSpells();
				} else if (componentId >= 11 & componentId <= 13) {
					player.getCombatDefinitions().setSortSpellBook(componentId - 11);
				} else if (componentId == 20) {
					player.getCombatDefinitions().setDefensiveCasting(!player.getCombatDefinitions().isDefensiveCasting());
				} else {
					CombatRegistry.processSpell(player, componentId);
				}
				break;
		}
		return true;
	}
}
