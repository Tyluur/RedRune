package org.redrune.game.content.event;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class EventPolicy {
	
	/**
	 * The policy types in relevancy to walking
	 */
	public enum WalkablePolicy {
		RESET,
		NONE
	}
	
	/**
	 * The policy types in relevancy to interfaces
	 */
	public enum InterfacePolicy {
		CLOSE,
		NONE
	}
	
	/**
	 * The policy types in relevancy to whether this action will stack with others, or will clear out all other actions
	 * queued.
	 */
	public enum StackPolicy {
		STACK,
		NONE
	}
	
	/**
	 * The policy types in relevancy to the current player's animation
	 */
	public enum AnimationPolicy {
		RESET,
		NONE
	}
	
	/**
	 * The policy types in relevancy to the action the player is doing
	 */
	public enum ActionPolicy {
		RESET,
		NONE
	}
}
