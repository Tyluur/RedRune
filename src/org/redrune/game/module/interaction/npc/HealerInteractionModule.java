package org.redrune.game.module.interaction.npc;

import org.redrune.game.content.dialogue.impl.misc.HealingNurseDialogue;
import org.redrune.game.content.dialogue.impl.misc.SimpleNPCMessage;
import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class HealerInteractionModule implements NPCInteractionModule {
	
	/**
	 * The delay between the time regular players can use the nurse (seconds)
	 */
	public static final long DELAY_BETWEEN = TimeUnit.SECONDS.toMillis(180);
	
	@Override
	public int[] npcSubscriptionIds() {
		return arguments(961);
	}
	
	@Override
	public boolean handle(Player player, NPC npc, InteractionOption option) {
		if (option == InteractionOption.SECOND_OPTION) {
			healPlayer(player, npc);
			return true;
		} else {
			player.getManager().getDialogues().startDialogue(new HealingNurseDialogue(), npc);
		}
		return false;
	}
	
	/**
	 * Heals the player
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc healing the player
	 */
	public static void healPlayer(Player player, NPC npc) {
		if (npc != null) {
			npc.sendAnimation(12575);
		}
		player.restoreAll();
		player.sendGraphics(1314);
		player.getManager().getDialogues().startDialogue(new SimpleNPCMessage(), 961, "I have restored your character to extreme health!");
	}
}
