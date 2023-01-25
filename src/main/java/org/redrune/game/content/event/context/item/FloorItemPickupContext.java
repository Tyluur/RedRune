package org.redrune.game.content.event.context.item;

import lombok.Getter;
import org.redrune.game.content.event.EventContext;
import org.redrune.game.node.item.FloorItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPickupContext implements EventContext {
	
	/**
	 * The item being picked up
	 */
	@Getter
	private final FloorItem floorItem;
	
	/**
	 * The distance from the player and the floor item
	 */
	@Getter
	private final int distance;
	
	public FloorItemPickupContext(FloorItem floorItem, int distance) {
		this.floorItem = floorItem;
		this.distance = distance;
	}
}
