package org.redrune.game.module.type;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.game.module.interaction.InteractionModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface ObjectInteractionModule extends InteractionModule {
	
	/**
	 * The object ids that are subscribed to the module.
	 */
	int[] objectSubscriptionIds();
	
	/**
	 * Handles the interaction with the module
	 *
	 * @param player
	 * 		The player interacting
	 * @param object
	 * 		The object interacting with
	 * @param option
	 * 		The option clicked on the object
	 * @return {@code True} if the interaction was successful
	 */
	boolean handle(Player player, GameObject object, InteractionOption option);
	
}
