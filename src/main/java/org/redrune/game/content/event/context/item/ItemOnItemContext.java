package org.redrune.game.content.event.context.item;

import lombok.Getter;
import org.redrune.game.content.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class ItemOnItemContext implements EventContext {
	
	/**
	 * The slot of the item used
	 */
	@Getter
	private final int usedSlot;
	
	/**
	 * The slot of the item used with
	 */
	@Getter
	private final int withSlot;
	
	public ItemOnItemContext(int usedSlot, int withSlot) {
		this.usedSlot = usedSlot;
		this.withSlot = withSlot;
	}
}
