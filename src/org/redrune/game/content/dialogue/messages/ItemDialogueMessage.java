package org.redrune.game.content.dialogue.messages;

import org.redrune.game.content.dialogue.DialogueMessage;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.InterfaceItemBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class ItemDialogueMessage extends DialogueMessage {
	
	/**
	 * The id of the item
	 */
	private final int itemId;
	
	/**
	 * The amount of the item
	 */
	private final int itemAmount;
	
	/**
	 * The messages
	 */
	private final String[] messages;
	
	public ItemDialogueMessage(int itemId, int itemAmount, String... messages) {
		this.itemId = itemId;
		this.itemAmount = itemAmount;
		this.messages = messages;
	}
	
	@Override
	public void send(Player player) {
		int length = messages.length;
		int interfaceId = (length == 1 ? 241 : length == 2 ? 242 : length == 3 ? 243 : 244);
		List<String> text = new ArrayList<>();
		text.add("");
		Collections.addAll(text, messages);
		String[] message = text.toArray(new String[text.size()]);
		sendItemDialogue(player, interfaceId, message, itemId, itemAmount);
	}
	
	/**
	 * Sends the item dialogue
	 *
	 * @param player
	 * 		The player to send to
	 * @param interfaceId
	 * 		The id of the interface
	 * @param text
	 * 		The text to send
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item
	 */
	private void sendItemDialogue(Player player, int interfaceId, String[] text, int itemId, int amount) {
		int[] componentOptions = getIComponentsIds(interfaceId);
		if (componentOptions == null) {
			return;
		}
		if (text.length != componentOptions.length) {
			return;
		}
		for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++) {
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, componentOptions[childOptionId], text[childOptionId]);
		}
		player.getTransmitter().send(new InterfaceItemBuilder(interfaceId, 2, itemId, amount).build(player));
		player.getManager().getInterfaces().sendChatboxInterface(interfaceId);
	}
	
}
