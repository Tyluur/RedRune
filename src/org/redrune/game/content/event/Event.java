package org.redrune.game.content.event;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.event.EventPolicy.*;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public abstract class Event<T extends EventContext> {
	
	/**
	 * Handles the running of the event
	 *
	 * @param player
	 * 		The player
	 * @param context
	 * 		The context of the event
	 */
	public abstract void run(Player player, T context);
	
	/**
	 * The policy for walking
	 */
	@Getter
	@Setter
	private WalkablePolicy walkablePolicy = WalkablePolicy.NONE;
	
	/**
	 * The policy for interfaces
	 */
	@Getter
	@Setter
	private InterfacePolicy interfacePolicy = InterfacePolicy.NONE;
	
	/**
	 * The policy for stacking events
	 */
	@Getter
	@Setter
	private StackPolicy stackPolicy = StackPolicy.STACK;
	
	/**
	 * The policy for animations
	 */
	@Getter
	@Setter
	private AnimationPolicy animationPolicy = AnimationPolicy.NONE;
	
	/**
	 * The policy for actions
	 */
	@Getter
	@Setter
	private ActionPolicy actionPolicy = ActionPolicy.NONE;
	
	/**
	 * If the event can be executed, defaults to true.
	 *
	 * @param player
	 * 		The player executing the event
	 * @param context
	 * 		The context
	 */
	public boolean canStart(Player player, T context) {
		return !player.isDead();
	}
}
