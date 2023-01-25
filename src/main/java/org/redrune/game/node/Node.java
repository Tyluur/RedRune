package org.redrune.game.node;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;

/**
 * This is the parent class of all game nodes. Nodes are anything in the game which undergoes
 * registration/de-registration and is interactible.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Node {
	
	/**
	 * Handles the registration of the node
	 */
	public abstract void register();
	
	/**
	 * Handles the de-registration of the node
	 */
	public abstract void deregister();
	
	/**
	 * The location of the node
	 */
	@Getter
	@Setter
	private Location location;
	
	/**
	 * If the entity has been renderable
	 */
	@Getter
	@Setter
	private transient boolean renderable;
	
	/**
	 * The construction of a new {@code Node} instance
	 *
	 * @param location
	 * 		The location of the node.
	 */
	protected Node(Location location) {
		this.location = location;
	}
	
	/**
	 * Constructs a regional optional for the {@code Location} the node is in.
	 */
	public Region getRegion() {
		return RegionManager.getRegion(location.getRegionId());
	}
	
	/**
	 * Verifies if this entity is a player
	 *
	 * @return A {@code Boolean} flag
	 */
	public boolean isPlayer() {
		return toPlayer() != null;
	}
	
	/**
	 * Converts this node to a {@code Player} {@code Object}
	 *
	 * @return A {@code Player}
	 */
	public Player toPlayer() {
		return null;
	}
	
	/**
	 * Verifies if this node is an npc
	 *
	 * @return A {@code Boolean} flag
	 */
	public boolean isNPC() {
		return toNPC() != null;
	}
	
	/**
	 * Converts this entity to a {@code NPC} {@code Object}
	 *
	 * @return A {@code NPC}
	 */
	public NPC toNPC() {
		return null;
	}
	
	/**
	 * Checks if this node is an item
	 */
	public boolean isItem() {
		return toItem() != null;
	}
	
	/**
	 * Converts this node to a {@code Item} {@code Object}
	 */
	public Item toItem() {
		return null;
	}
	
	/**
	 * Checks if this node is a game object
	 */
	public boolean isGameObject() {
		return toGameObject() != null;
	}
	
	/**
	 * Converts this node to a {@code GameObject} {@code Object}
	 */
	public GameObject toGameObject() {
		return null;
	}
	
	/**
	 * Gets the center location.
	 *
	 * @return The center location.
	 */
	public Location getCenterLocation() {
		int offset = getSize() >> 1;
		return location.transform(offset, offset, 0);
	}
	
	/**
	 * Gets the size of the node
	 *
	 * @return The size of the node
	 */
	public abstract int getSize();
	
}