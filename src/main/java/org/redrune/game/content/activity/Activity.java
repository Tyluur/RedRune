package org.redrune.game.content.activity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportType;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class Activity {
	
	/**
	 * The parameters of the activity
	 */
	@Getter
	@Setter
	protected transient Object[] parameters;
	
	/**
	 * The player doing the activity
	 */
	@Getter
	@Setter
	protected transient Player player;
	
	public Activity(Object... parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Handles the start of the activity
	 */
	public void start() {
	
	}
	
	/**
	 * Handles the change of location
	 */
	public void updateLocation() {
	
	}
	
	/**
	 * Each game tick in which the activity is set for a player, this method is called
	 */
	public void tick() {
	
	}
	
	/**
	 * Handles the end of an activity. This method call cancels the player's current activity
	 */
	public void end() {
		player.getManager().getActivities().end();
	}
	
	/**
	 * Checks if this activity handles the interaction with a node
	 *
	 * @param node
	 * 		The node
	 * @param option
	 * 		The option
	 */
	public boolean handleNodeInteraction(Node node, InteractionOption option) {
		if (node.isPlayer() && handlePlayerOption(node.toPlayer(), option)) {
			return true;
		} else if (node.isNPC() && handleNPCOption(node.toNPC(), option)) {
			return true;
		} else if (node.isGameObject() && handleObject(node.toGameObject(), option)) {
			return true;
		} else if (node.isItem() && handleItem(node.toItem(), option)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Checks if this teleporation type is allowed
	 *
	 * @param type
	 * 		The type
	 */
	public boolean teleportationAllowed(TeleportType type) {
		return true;
	}
	
	/**
	 * If the activity handles the player option
	 *
	 * @param target
	 * 		The target
	 * @param option
	 * 		The option
	 */
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		return false;
	}
	
	/**
	 * If the activity handles the npc option
	 *
	 * @param npc
	 * 		The npc
	 * @param option
	 * 		The option
	 */
	private boolean handleNPCOption(NPC npc, InteractionOption option) {
		return false;
	}
	
	/**
	 * Handles the object interaction
	 *
	 * @param object
	 * 		The object
	 * @param option
	 * 		The option clicked
	 */
	private boolean handleObject(GameObject object, InteractionOption option) {
		return false;
	}
	
	/**
	 * Handles the item interaction
	 *
	 * @param item
	 * 		The item
	 * @param option
	 * 		The option
	 */
	private boolean handleItem(Item item, InteractionOption option) {
		return false;
	}
	
	/**
	 * If the activity can move
	 *
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param dir
	 * 		The direction
	 */
	public boolean canMove(int x, int y, int dir) {
		return true;
	}
	
	/**
	 * Checks that combat can continue with the target
	 *
	 * @param target
	 * 		The target
	 */
	public boolean combatAcceptable(Entity target) {
		return true;
	}
	
	/**
	 * Checks if the activity saves on logout.
	 */
	public boolean savesOnLogout() {
		return false;
	}
	
	/**
	 * Handles the entity's death
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean handleEntityDeath(Entity entity) {
		return false;
	}
}