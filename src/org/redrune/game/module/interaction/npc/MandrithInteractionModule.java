package org.redrune.game.module.interaction.npc;

import org.redrune.game.module.interaction.rsinterface.SuppliesInterfaceInteractionModule;
import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/19/2017
 */
public class MandrithInteractionModule implements NPCInteractionModule {
	
	@Override
	public int[] npcSubscriptionIds() {
		return arguments(6537);
	}
	
	@Override
	public boolean handle(Player player, NPC npc, InteractionOption option) {
		if (option == InteractionOption.FIRST_OPTION) {
			SuppliesInterfaceInteractionModule.display(player);
			return true;
		} else if (option == InteractionOption.SECOND_OPTION) {
			return false;
		}
		return false;
	}
}
