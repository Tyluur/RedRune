package org.redrune.game.content.dialogue.messages;

import org.redrune.game.content.dialogue.DialogueMessage;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class ChatDialogueMessage extends DialogueMessage {
	
	/**
	 * The message to send
	 */
	private final String[] messages;
	
	public ChatDialogueMessage(String... messages) {
		this.messages = messages;
	}
	
	@Override
	public void send(Player player) {
		int length = messages.length;
		int interfaceId = (length == 4 ? 213 : length == 3 ? 212 : length == 2 ? 211 : 210);
		sendDialogue(player, interfaceId, messages);
	}
	
	private void sendDialogue(Player player, int interId, String... talkDefinitons) {
		int[] componentOptions = getIComponentsIds(interId);
		if (componentOptions == null) {
			return;
		}
		int properLength = talkDefinitons.length;
		if (properLength != componentOptions.length) {
			return;
		}
		for (int i = 0; i < componentOptions.length; i++) {
			player.getManager().getInterfaces().sendInterfaceText(interId, componentOptions[i], messages[i]);
		}
		player.getManager().getInterfaces().sendChatboxInterface(interId);
	}
}
