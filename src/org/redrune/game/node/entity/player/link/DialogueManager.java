package org.redrune.game.node.entity.player.link;

import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.content.dialogue.messages.OptionDialogueMessage;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.AttributeKey;
import org.redrune.game.content.dialogue.DialogueMessage;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public final class DialogueManager {
	
	/**
	 * The player whos managing the dialogue
	 */
	private final Player player;
	
	/**
	 * The current dialogue
	 */
	private Dialogue dialogue;
	
	/**
	 * Constructing the instance of this manager
	 *
	 * @param player
	 * 		The player
	 */
	public DialogueManager(Player player) {
		this.player = player;
	}
	
	/**
	 * Starts a dialogue
	 *
	 * @param dialogue
	 * 		The dialogue
	 * @param parameters
	 * 		The parameters of the dialogue
	 */
	public void startDialogue(Dialogue dialogue, Object... parameters) {
		this.dialogue = dialogue;
		this.dialogue.setParameters(parameters);
		this.dialogue.constructMessages(player);
		this.start();
	}
	
	/**
	 * Starts the dialogue
	 */
	public void start() {
		if (dialogue == null) {
			throw new IllegalStateException("Attempted to process a dialogue when it was closed.");
		}
		int stage = dialogue.getStage();
		Optional<DialogueMessage> optional = dialogue.getMessageAtStage(stage);
		if (!optional.isPresent()) {
			dialogue.end(player);
			return;
		}
		DialogueMessage message = optional.get();
		player.putAttribute(AttributeKey.LAST_DIALOGUE_MESSAGE, message);
		message.send(player);
	}
	
	/**
	 * Processes the stage of the dialogue
	 *
	 * @param interfaceId
	 * 		The interface used
	 * @param componentId
	 * 		The component clicked
	 */
	public void handleOption(int interfaceId, int componentId) {
		if (dialogue == null) {
			return;
		}
		DialogueMessage lastDialogue = player.getAttribute(AttributeKey.LAST_DIALOGUE_MESSAGE);
		if (lastDialogue == null) {
			end();
			return;
		}
		dialogue.nextStage();
		int stage = dialogue.getStage();
		
		Optional<DialogueMessage> optional = dialogue.getMessageAtStage(stage);
		if (lastDialogue instanceof OptionDialogueMessage) {
			lastDialogue.handleOption(player, getOptionByComponent(interfaceId, componentId));
			optional = Optional.ofNullable(dialogue.getMessageAtStage(stage).orElse(null));
		}
		if (!optional.isPresent()) {
			dialogue.end(player);
			return;
		}
		DialogueMessage message = optional.get();
		player.putAttribute(AttributeKey.LAST_DIALOGUE_MESSAGE, message);
		message.send(player);
		if (dialogue == null || dialogue.isOver()) {
			end();
		}
	}
	
	/**
	 * Ends the current dialogue
	 */
	public void end() {
		this.dialogue = null;
		this.player.getManager().getInterfaces().closeChatboxInterface();
	}
	
	/**
	 * Gets the option clicked on the interface by the component id
	 *
	 * @param interfaceId
	 * 		The interface clicked
	 * @param componentId
	 * 		The component on the interface clicked
	 */
	private static int getOptionByComponent(int interfaceId, int componentId) {
		switch (interfaceId) {
			case 230:
				return componentId - 1;
			default:
				switch (componentId) {
					default:
						return componentId;
				}
		}
	}
	
}