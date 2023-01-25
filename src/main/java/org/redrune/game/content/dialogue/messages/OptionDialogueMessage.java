package org.redrune.game.content.dialogue.messages;

import com.google.common.base.Preconditions;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.DialogueConstants;
import org.redrune.utility.tool.Misc;
import org.redrune.game.content.dialogue.DialogueMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class OptionDialogueMessage extends DialogueMessage {
	
	/**
	 * The title of the option dialogue
	 */
	private final String title;
	
	/**
	 * The messages to execute
	 */
	private final String[] messages;
	
	/**
	 * The tasks to execute
	 */
	private final Runnable[] tasks;
	
	/**
	 * Constructs a new option dialogue
	 *
	 * @param messages
	 * 		The messages
	 * @param tasks
	 * 		The tasks to execute for the selected message
	 */
	public OptionDialogueMessage(String[] messages, Runnable... tasks) {
		this(DialogueConstants.DEFAULT_OPTION, messages, tasks);
	}
	
	/**
	 * Constructs a new option dialogue
	 *
	 * @param title
	 * 		The title of the option
	 * @param messages
	 * 		The messages
	 * @param tasks
	 * 		The tasks to execute for the selected message
	 */
	public OptionDialogueMessage(String title, String[] messages, Runnable... tasks) {
		Preconditions.checkArgument(messages.length == tasks.length, "The amount of messages must be the same as the amount of tasks.");
		this.title = title;
		this.messages = messages;
		this.tasks = tasks;
	}
	
	@Override
	public void send(Player player) {
		int length = this.messages.length;
		int interfaceId = (length == 5 ? 238 : length == 4 ? 237 : length == 3 ? 230 : 236);
		
		List<String> messages = new ArrayList<>();
		messages.add(title);
		messages.addAll(Arrays.asList(this.messages));
		sendDialogue(player, interfaceId, messages);
	}
	
	@Override
	public void handleOption(Player player, int option) {
		int slot = option - 1;
		Runnable runnable = Misc.getArrayEntry(tasks, slot);
		if (runnable == null) {
			return;
		}
		runnable.run();
	}
	
	/**
	 * Sends an optoin dialogue
	 *
	 * @param player
	 * 		The player
	 * @param interfaceId
	 * 		The id of the dialogue
	 * @param text
	 * 		The messages
	 */
	private void sendDialogue(Player player, int interfaceId, List<String> text) {
		int[] componentOptions = getIComponentsIds(interfaceId);
		if (componentOptions == null) {
			return;
		}
		int properLength = (interfaceId > 213 ? text.size() : text.size() - 1);
		if (properLength != componentOptions.length) {
			return;
		}
		for (int childOptionId = 0; childOptionId < componentOptions.length; childOptionId++) {
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, componentOptions[childOptionId], text.get(childOptionId));
		}
		player.getManager().getInterfaces().sendChatboxInterface(interfaceId);
	}
}
