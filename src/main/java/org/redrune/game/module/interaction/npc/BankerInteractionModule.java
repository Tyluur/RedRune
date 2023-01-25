package org.redrune.game.module.interaction.npc;

import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class BankerInteractionModule implements NPCInteractionModule {
	
	@Override
	public int[] npcSubscriptionIds() {
		return arguments(45, 44);
	}
	
	@Override
	public boolean handle(Player player, NPC npc, InteractionOption option) {
		player.getBank().open();
		return true;
	}
}
