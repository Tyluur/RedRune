package org.redrune.game.content.action;

import org.redrune.game.node.entity.player.Player;

/**
 * This interface provides methods to handle an action that is processed at a continuous tick rate.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public interface Action {
	
	/**
	 * Handles the start of the event
	 *
	 * @param player
	 * 		The player
	 * @return True if we can start
	 */
	boolean start(Player player);
	
	/**
	 * Handles the processing of the action
	 *
	 * @param player
	 * 		The player
	 * @return True if we process successfully
	 */
	boolean process(Player player);
	
	/**
	 * Separately handles processing the action in a timely (ticks) fashion
	 *
	 * @param player
	 * 		The player
	 * @return -1 if we should stop
	 */
	int processOnTicks(Player player);
	
	/**
	 * Handles what to do when the event is stopped
	 *
	 * @param player
	 * 		The player
	 */
	void stop(Player player);
	
	/**
	 * Sets the delay
	 *
	 * @param player
	 * 		The player
	 * @param delay
	 * 		The delay
	 */
	default void setDelay(Player player, int delay) {
		player.getManager().getActions().addDelay(delay);
	}
	
}
