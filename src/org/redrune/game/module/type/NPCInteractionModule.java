package org.redrune.game.module.type;

import org.redrune.game.module.interaction.InteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface NPCInteractionModule extends InteractionModule {
	
	/**
	 * The ids of the npcs that subscribe to the module
	 */
	int[] npcSubscriptionIds();
	
	/**
	 * Handles the interaction with an npc
	 *
	 * @param player
	 * 		The player interacting.
	 * @param npc
	 * 		The npc interacting with.
	 * @param option
	 * 		The option clicked on the npc.
	 * @return {@code True} if successfully interacted.
	 */
	boolean handle(Player player, NPC npc, InteractionOption option);
}
