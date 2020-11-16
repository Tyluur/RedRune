package org.redrune.game.content.dialogue;

import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.tool.Misc;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class DialogueRepository {
	
	/**
	 * The map of dialogues with a {@link DialogueSubscription} subscription
	 */
	private static Map<String, Dialogue> NPC_SUBSCRIPTION_DIALOGUES = new HashMap<>();
	
	/**
	 * The map of dialogues with a {@link DialogueSubscription} subscription
	 */
	private static Map<String, Dialogue> OBJECT_SUBSCRIPTION_DIALOGUES = new HashMap<>();
	
	/**
	 * Loads all the subscriptions
	 */
	public static void loadSubscriptions() {
		Misc.getClassesInDirectory(DialogueRepository.class.getPackage().getName() + ".impl.subscribe").stream().filter(clazz -> clazz.getClass().isAnnotationPresent(DialogueSubscription.class)).forEach(clazz -> {
			Dialogue dialogue = (Dialogue) clazz;
			DialogueSubscription subscription = dialogue.getClass().getAnnotation(DialogueSubscription.class);
			addEntry(dialogue, subscription);
		});
		System.out.println("Loaded " + NPC_SUBSCRIPTION_DIALOGUES.size() + "/" + OBJECT_SUBSCRIPTION_DIALOGUES.size() + " dialogues with npc/object subscriptions.");
	}
	
	/**
	 * Adds the dialogue entry
	 *
	 * @param dialogue
	 * 		The dialogue
	 * @param subscription
	 * 		The subscription
	 */
	private static void addEntry(Dialogue dialogue, DialogueSubscription subscription) {
		for (String name : subscription.npcNames()) {
			if (NPC_SUBSCRIPTION_DIALOGUES.containsKey(name)) {
				System.out.println("Unable to register dialogue with subscription: " + dialogue.getClass().getSimpleName() + "[" + subscription + "]");
				return;
			}
			NPC_SUBSCRIPTION_DIALOGUES.put(name, dialogue);
		}
		for (String name : subscription.objectNames()) {
			if (OBJECT_SUBSCRIPTION_DIALOGUES.containsKey(name)) {
				System.out.println("Unable to register dialogue with subscription: " + dialogue.getClass().getSimpleName() + "[" + subscription + "]");
				return;
			}
			OBJECT_SUBSCRIPTION_DIALOGUES.put(name, dialogue);
		}
	}
	
	/**
	 * Handles the interaction with the npc for a dialogue
	 *
	 * @param player
	 * 		The player
	 * @param npc
	 * 		The npc
	 */
	public static boolean handleNPC(Player player, NPC npc) {
		Dialogue dialogue = NPC_SUBSCRIPTION_DIALOGUES.get(npc.getDefinitions().getName());
		if (dialogue == null) {
			return false;
		}
		try {
			player.getManager().getDialogues().startDialogue(dialogue.getClass().newInstance(), npc.getId());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	/**
	 * Handles the interaction with the object for a dialogue
	 *
	 * @param player
	 * 		The player
	 * @param object
	 * 		The object
	 */
	public static boolean handleObject(Player player, GameObject object) {
		Dialogue dialogue = OBJECT_SUBSCRIPTION_DIALOGUES.get(object.getDefinitions().getName());
		if (dialogue == null) {
			return false;
		}
		try {
			player.getManager().getDialogues().startDialogue(dialogue.getClass().newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		return true;
	}
	
}
