package org.redrune.game.content.dialogue.messages;

import org.redrune.game.content.dialogue.DialogueMessage;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class ActionDialogueMessage extends DialogueMessage {
	
	/**
	 * The action to perform
	 */
	private final Runnable action;
	
	public ActionDialogueMessage(Runnable action) {
		this.action = action;
	}
	
	@Override
	public void send(Player player) {
		action.run();
	}
}
