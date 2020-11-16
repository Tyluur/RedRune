package org.redrune.game.content.dialogue.impl.subscribe;

import org.redrune.game.GameConstants;
import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;
import org.redrune.game.content.dialogue.DialogueSubscription;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
@DialogueSubscription(npcNames = { "Banker" }, objectNames = { "Counter", "Bank booth", "Bank" })
public class BankerNPCDialogue extends Dialogue {
	
	@Override
	public void constructMessages(Player player) {
		this.chattingId = Misc.findNPCByName(player.getRegion(), "banker", 44, player.getLocation());
		
		npc(chattingId, QUESTIONS, "Good day, How may I help you?");
		options("What would you like to say?", new String[] { "I'd like to access my bank account, please.", "I'd like to check my PIN settings.", "I'd like to see my collection box.", "What is this place?" }, () -> {
			player.getBank().open();
			end(player);
		}, () -> {
			player(NORMAL, "I like to set a bank pin.");
			npc(chattingId, NORMAL, "Sorry, bank pins are not yet ready.");
			player(NORMAL, "I will return another day.");
		}, () -> {
			player(NORMAL, "I would like to view my collection box.");
			npc(chattingId, NORMAL, "Sorry, the grand exchange is not ready.");
		}, () -> {
			player(NORMAL, "What is this place?");
			npc(chattingId, NORMAL, "This is a branch of the Bank of " + GameConstants.SERVER_NAME + ". We have", "branches in many towns.");
			options("What would you like to say?", new String[] { "And what do you do?", "Didnt you used to be called the Bank of Varrock?" }, () -> {
				player(NORMAL, "And what do you do?");
				npc(chattingId, NORMAL, "We will look after your items and money for you.", "Leave your valuables with us if you want to keep them", "safe.");
			}, () -> {
				player(NORMAL, "Didnt you used to be called the Bank of Varrock?");
				npc(chattingId, NORMAL, "Yes we did, but people kept on coming into our", "signs were wrong. They acted as if we didn't know", "what town we were in or something.");
			});
		});
	}
	
}