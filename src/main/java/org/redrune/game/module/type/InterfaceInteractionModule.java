package org.redrune.game.module.type;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.interaction.InteractionModule;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface InterfaceInteractionModule extends InteractionModule,InterfaceConstants {
	
	/**
	 * The ids of the interfaces that subscribe to the module.
	 */
	int[] interfaceSubscriptionIds();
	
	/**
	 * Handles the interface interaction
	 *
	 * @param player
	 * 		The player clicking the interface
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id of the interface
	 * @param itemId
	 * 		The item id on the interface, -1 if none.
	 * @param slotId
	 * 		The slot id on the interface, -1 if none.
	 * @param packetId
	 * 		The packet id of the click, different ids are used for different options
	 * @return {@code True} if it was handled successfully
	 */
	boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId);
}
