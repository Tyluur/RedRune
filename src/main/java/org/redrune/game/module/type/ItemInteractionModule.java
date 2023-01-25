package org.redrune.game.module.type;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.module.interaction.InteractionModule;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface ItemInteractionModule extends InteractionModule {
	
	/**
	 * The item ids that subscribe to this module
	 */
	int[] itemSubscriptionIds();
	
	/**
	 * Handles the interaction with an item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item we're interacting with
	 * @param slotId
	 * 		The slot id in the inventory the item was clicked on
	 * @param option
	 * 		The option we clicked  @return {@code True} if successfully interacted.
	 */
	boolean handle(Player player, Item item, int slotId, InteractionOption option);
}
