package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class CombatStyleInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(884);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 4) {
			StaticCombatFormulae.submitSpecialRequest(player);
		} else if (componentId >= 11 && componentId <= 14) {
			player.getCombatDefinitions().changeAttackStyle((byte) (componentId - 11));
		} else if (componentId == 15) {
			player.getCombatDefinitions().toggleAutoRetaliate();
		}
		return true;
	}
}
