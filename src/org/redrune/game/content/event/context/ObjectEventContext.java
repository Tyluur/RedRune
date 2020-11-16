package org.redrune.game.content.event.context;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.event.EventContext;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class ObjectEventContext implements EventContext {
	
	/**
	 * The game object
	 */
	@Getter
	private final GameObject object;
	
	/**
	 * The option
	 */
	@Getter
	private final InteractionOption option;
	
	/**
	 * The item used, if the packet is an item on object packet
	 */
	@Getter
	@Setter
	private Item item;
	
	public ObjectEventContext(GameObject object, InteractionOption option, Item item) {
		this.object = object;
		this.option = option;
		this.item = item;
	}
	
	public ObjectEventContext(GameObject object, InteractionOption option) {
		this.object = object;
		this.option = option;
	}
}
