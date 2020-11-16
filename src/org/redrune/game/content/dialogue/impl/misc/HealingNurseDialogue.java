package org.redrune.game.content.dialogue.impl.misc;

import org.redrune.game.GameConstants;
import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.module.interaction.npc.HealerInteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class HealingNurseDialogue extends Dialogue {
	
	/**
	 * The npc that this dialogue is for
	 */
	private NPC npc;
	
	@Override
	public void constructMessages(Player player) {
		this.npc = parameter(0);
		this.chattingId = this.npc.getId();
		npc(chattingId, NORMAL, "Hello! I am the nurse here at " + GameConstants.SERVER_NAME + ".", "I've been healing travellers for years!", "How can I be of assistance?");
		sendDefaultOptions(player);
	}
	
	/**
	 * Sends the default options dialogue
	 *
	 * @param player
	 * 		The player to send it to
	 */
	private void sendDefaultOptions(Player player) {
		options(DEFAULT_OPTION, new String[] { "I would like to be healed?", "Weren't you originally at the duel arena?", "Nothing actually." }, () -> {
			player(NORMAL, "I would like to be healed.");
			npc(chattingId, NORMAL, "That's what I'm here for!");
			action(() -> HealerInteractionModule.healPlayer(player, npc));
		}, () -> {
			player(NORMAL, "Weren't you originally at the duel arena?");
			npc(chattingId, HAPPY, "Haha yes I was, but people needed more urgent care", "near the wilderness, so Tyluur brought me here.", "I am forever grateful.");
			sendDefaultOptions(player);
		}, () -> {
			player(NORMAL, "I don't need anything actually, thanks.");
		});
	}
}
