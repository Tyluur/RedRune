package org.redrune.game.content.dialogue;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.dialogue.messages.*;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.DialogueConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public abstract class Dialogue implements DialogueConstants {
	
	/**
	 * Generates the list of messages to use in this dialogue
	 *
	 * @param player
	 * 		The player
	 */
	public abstract void constructMessages(Player player);
	
	/**
	 * The list of messages that will be sent
	 */
	private final Map<Integer, DialogueMessage> messageMap = new HashMap<>();
	
	/**
	 * The id of the entity we are interacting with
	 */
	@Getter
	@Setter
	protected int chattingId;
	
	/**
	 * The stage of the dialogue
	 */
	@Getter
	@Setter
	private int stage;
	
	/**
	 * The parameters that are sent
	 */
	@Getter
	@Setter
	private Object[] parameters;
	
	/**
	 * Constructs a new dialogue
	 */
	public Dialogue() {
		this.stage = 1;
	}
	
	/**
	 * Constructs a new dialogue
	 *
	 * @param message
	 * 		The message to add
	 */
	protected void construct(DialogueMessage message) {
		int size = messageMap.size();
		messageMap.put(size + 1, message);
	}
	
	/**
	 * Gets the message the player should do at the stage
	 *
	 * @param stage
	 * 		The stage
	 */
	public Optional<DialogueMessage> getMessageAtStage(int stage) {
		DialogueMessage message = messageMap.get(stage);
		if (message == null) {
			return Optional.empty();
		} else {
			return Optional.of(message);
		}
	}
	
	/**
	 * Sends the stage to the next one
	 */
	public void nextStage() {
		stage++;
	}
	
	/**
	 * Gets a parameter from the {@link #parameters} array and casts the type to it
	 *
	 * @param indexId
	 * 		The index in the parameters array
	 */
	@SuppressWarnings("unchecked")
	protected <K> K parameter(int indexId) {
		return (K) parameters[indexId];
	}
	
	/**
	 * Ends the dialogue
	 */
	public void end(Player player) {
		player.getManager().getInterfaces().closeChatboxInterface();
		stage = -1;
	}
	
	/**
	 * If the dialogue is now over.
	 */
	public boolean isOver() {
		return stage > getLastStage();
	}
	
	/**
	 * Gets the last stage
	 */
	public int getLastStage() {
		return messageMap.size();
	}
	
	/**
	 * Executes a player dialogue
	 *
	 * @param animation
	 * 		The animation to use
	 * @param messages
	 * 		The messages to send
	 */
	protected void player(int animation, String... messages) {
		construct(new PlayerDialogueMessage(animation, messages));
	}
	
	/**
	 * Executes an npc dialogue
	 *
	 * @param npcId
	 * 		The id of the npc
	 * @param animation
	 * 		The animation of the npc
	 * @param messages
	 * 		The messages of the npc
	 */
	protected void npc(int npcId, int animation, String... messages) {
		construct(new NPCDialogueMessage(npcId, animation, messages));
	}
	
	/**
	 * Executes an options dialogue
	 *
	 * @param title
	 * 		The title of the dialgoue
	 * @param messages
	 * 		The messages of the dialogue
	 * @param tasks
	 * 		The tasks to execute when we click successfully
	 */
	protected void options(String title, String[] messages, Runnable... tasks) {
		construct(new OptionDialogueMessage(title, messages, tasks));
	}
	
	/**
	 * Executes a chatbox dialogue
	 *
	 * @param messages
	 * 		The messages on the dialogue
	 */
	protected void chatbox(String... messages) {
		construct(new ChatDialogueMessage(messages));
	}
	
	/**
	 * Executes an item dialogue
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param messages
	 * 		The messages to send
	 */
	protected void item(int itemId, int itemAmount, String... messages) {
		construct(new ItemDialogueMessage(itemId, itemAmount, messages));
	}
	
	/**
	 * Adds an action dialogue
	 *
	 * @param action
	 * 		The action to perform
	 */
	protected void action(Runnable action) {
		construct(new ActionDialogueMessage(action));
	}
}

