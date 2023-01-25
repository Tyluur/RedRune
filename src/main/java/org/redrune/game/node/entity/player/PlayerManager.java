package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.link.*;
import org.redrune.game.node.entity.player.link.ContactManager;
import org.redrune.game.node.entity.player.link.prayer.PrayerManager;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class PlayerManager {
	
	/**
	 * The note manager instance for the player
	 */
	@Getter
	private final NoteManager notes;
	
	/**
	 * The prayer manager
	 */
	@Getter
	private final PrayerManager prayers;
	
	/**
	 * The chat manager
	 */
	@Getter
	private final ContactManager contacts;
	
	/**
	 * The activity manager instance
	 */
	@Getter
	private final ActivityManager activities;
	
	/**
	 * The interface manager of the player
	 */
	@Getter
	@Setter
	private transient InterfaceManager interfaces;
	
	/**
	 * The hint icon manager of the player
	 */
	@Getter
	@Setter
	private transient HintIconManager hintIcons;
	
	/**
	 * The lock manager object
	 */
	@Getter
	@Setter
	private transient LockManager locks;
	
	/**
	 * The action manager object
	 */
	@Getter
	@Setter
	private transient ActionManager actions;
	
	/**
	 * The dialogue manager object
	 */
	@Getter
	@Setter
	private transient DialogueManager dialogues;
	
	/**
	 * The web manager
	 */
	@Getter
	private transient WebManager webManager;
	
	PlayerManager() {
		this.notes = new NoteManager();
		this.prayers = new PrayerManager();
		this.contacts = new ContactManager();
		this.activities = new ActivityManager();
		this.webManager = new WebManager();
	}
	
	/**
	 * Registers the transient variables
	 *
	 * @param player
	 * 		The player
	 */
	void registerTransients(Player player) {
		this.setInterfaces(new InterfaceManager());
		this.setActions(new ActionManager());
		this.setLocks(new LockManager());
		this.setDialogues(new DialogueManager(player));
		this.setHintIcons(new HintIconManager());
		this.activities.setPlayer(player);
		this.webManager.setPlayer(player);
		this.interfaces.setPlayer(player);
		this.notes.setPlayer(player);
		this.actions.setPlayer(player);
		this.hintIcons.setPlayer(player);
		this.prayers.setPlayer(player);
		this.contacts.setPlayer(player);
		player.getSkills().setLevelsAdvanced(new HashMap<>());
	}
	
}