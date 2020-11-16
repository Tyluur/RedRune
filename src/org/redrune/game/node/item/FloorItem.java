package org.redrune.game.node.item;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.Location;

import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItem extends Item {
	
	/**
	 * The username of the owner
	 */
	@Getter
	private final String ownerUsername;
	
	/**
	 * The ticks that have passed that this item has been created for
	 */
	@Getter
	@Setter
	private int ticksPassed;
	
	/**
	 * The ticks until the next item stage is hit
	 */
	@Getter
	@Setter
	private int targetTicks;
	
	/**
	 * If we can only be seen by the owner at this present time. This changes when the item is sent public, or if the
	 * item has no owner.
	 */
	@Getter
	@Setter
	private boolean ownerVisibleOnly;
	
	/**
	 * The construction of a new {@code Node} instance
	 *
	 * @param id
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item
	 * @param location
	 * 		The location of the item.
	 */
	public FloorItem(String ownerUsername, int targetTicks, int id, int amount, Location location) {
		super(location);
		this.ownerUsername = ownerUsername;
		this.targetTicks = targetTicks;
		this.ownerVisibleOnly = ownerUsername != null;
		this.setId((short) id);
		this.setAmount(amount);
		this.setRenderable(true);
		this.setDefinitions(ItemDefinitionParser.forId(id));
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FloorItem)) {
			return false;
		}
		FloorItem floorItem = (FloorItem) o;
		return floorItem.getId() == getId() && floorItem.getAmount() == getAmount() && floorItem.getTargetTicks() == targetTicks && Objects.equals(floorItem.getOwnerUsername(), ownerUsername);
	}
	
	@Override
	public String toString() {
		return "FloorItem{" + "ownerUsername='" + ownerUsername + '\'' + ", ticksPassed=" + ticksPassed + ", targetTicks=" + targetTicks + ", ownerVisibleOnly=" + ownerVisibleOnly + ", item=['" + getId() + "', '" + getAmount() + "']}";
	}
	
	/**
	 * Gets the name of the item
	 */
	public String getName() {
		return getDefinitions().getName();
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	/**
	 * Tells whether the item's default type is public (the item has no owner)
	 */
	public boolean isDefaultPublic() {
		return ownerUsername == null;
	}
	
	/**
	 * Increments {@link #ticksPassed} by one.
	 */
	public void addTicksPassed() {
		ticksPassed++;
	}
	
	/**
	 * If the item has existed for long enough.
	 */
	public boolean ticksElapsed() {
		return ticksPassed >= targetTicks;
	}
	
	/**
	 * Checks if the item is visible for a player
	 *
	 * @param player
	 * 		The player
	 */
	public boolean visibleFor(Player player) {
		return isRenderable() && (isDefaultPublic() || !isOwnerVisibleOnly() || Objects.equals(ownerUsername, player.getDetails().getUsername()));
	}
}