package org.redrune.game.content.event.context.item;

import lombok.Getter;
import org.redrune.game.content.event.EventContext;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public final class ItemEventContext implements EventContext {
	
	/**
	 * The item
	 */
	@Getter
	private final Item item;
	
	/**
	 * The slot id of the item
	 */
	@Getter
	private final int slotId;
	
	/**
	 * The option clicked on the item
	 */
	@Getter
	private final InteractionOption option;
	
	public ItemEventContext(Item item, int slotId, InteractionOption option) {
		this.item = item;
		this.slotId = slotId;
		this.option = option;
	}
}
