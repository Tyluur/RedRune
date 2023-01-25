package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public final class ActionManager {
	
	/**
	 * The player
	 */
	@Setter
	private Player player;
	
	/**
	 * The action
	 */
	@Getter
	private Action action;
	
	/**
	 * The delay until the action is processed
	 */
	private int delay;
	
	/**
	 * Sets the action
	 *
	 * @param action
	 * 		The action
	 */
	public void startAction(Action action) {
		stopAction();
		if (!action.start(player)) {
			return;
		}
		this.action = action;
	}
	
	/**
	 * Forces the action to stop
	 */
	public void stopAction() {
		if (action == null) {
			return;
		}
		action.stop(player);
		action = null;
	}
	
	/**
	 * Handles the processing of the action
	 */
	public void process() {
		if (action != null) {
			if (player.isDead() || !action.process(player)) {
				stopAction();
			}
		}
		if (delay > 0) {
			--delay;
			return;
		}
		if (action == null) {
			return;
		}
		int tickDelay = action.processOnTicks(player);
		if (tickDelay == -1) {
			stopAction();
			return;
		}
		this.delay += tickDelay;
	}
	
	/**
	 * Adds onto the existing delay
	 *
	 * @param delay
	 * 		The delay
	 */
	public void addDelay(int delay) {
		this.delay = this.delay + delay;
	}
	
}