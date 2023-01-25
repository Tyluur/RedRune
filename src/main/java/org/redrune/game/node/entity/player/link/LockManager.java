package org.redrune.game.node.entity.player.link;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class LockManager {
	
	/**
	 * The durations that each lock type is locked for
	 */
	private final long[] durations = new long[LockType.values().length];
	
	/**
	 * This method locks every {@link LockType} indefinitely
	 *
	 * @param time
	 * 		The amount of time in milliseconds to lock all actions for. If this param is blank, we lock indefinitely
	 */
	public void lockAll(long... time) {
		for (LockType type : LockType.values()) {
			durations[type.ordinal()] = time.length > 0 ? System.currentTimeMillis() + time[0] : Long.MAX_VALUE;
		}
	}
	
	/**
	 * This method locks several {@link LockType}s for the given delay
	 *
	 * @param delay
	 * 		The amount of time (in milliseconds) that we should lock the actions
	 * @param types
	 * 		The type of actions to lock
	 */
	public void lockActions(long delay, LockType... types) {
		for (LockType type : types) {
			durations[type.ordinal()] = System.currentTimeMillis() + delay;
		}
	}
	
	/**
	 * This method locks several {@link LockType}s forever
	 *
	 * @param types
	 * 		The type of actions to lock
	 */
	public void lockIndefinitely(LockType... types) {
		for (LockType type : types) {
			durations[type.ordinal()] = Long.MAX_VALUE;
		}
	}
	
	/**
	 * This method unlocks every action
	 */
	public void unlockAll() {
		for (LockType type : LockType.values()) {
			durations[type.ordinal()] = 0;
		}
	}
	
	/**
	 * This method unlocks all the actions parameterized
	 *
	 * @param types
	 * 		The types of locks to unlock
	 */
	public void unlock(LockType... types) {
		for (LockType type : types) {
			durations[type.ordinal()] = 0;
		}
	}
	
	/**
	 * Prints out debug information about the player's lock states
	 */
	public void debug() {
		for (LockType type : LockType.values()) {
			if (durations[type.ordinal()] > 0) {
				System.out.println(type + "\t is locked until: " + durations[type.ordinal()] + " ms...\t" + (durations[type.ordinal()] - System.currentTimeMillis()) + " more ms");
			}
		}
	}
	
	/**
	 * Checks if a type of lock is locked
	 *
	 * @param type
	 * 		The type of action
	 */
	public boolean isLocked(LockType type) {
		return durations[type.ordinal()] > System.currentTimeMillis();
	}
	
	/**
	 * This method checks if there is any {@code #lockDelays} index that is locked
	 *
	 * @return {@code True} if there is any index locked
	 */
	public boolean isAnyLocked() {
		for (long lockDelay : durations) {
			if (lockDelay > System.currentTimeMillis()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * The types of locks
	 */
	public enum LockType {
		NPC_INTERACTION,
		OBJECT_INTERACTION,
		ITEM_INTERACTION,
		PLAYER_INTERACTION,
		MOVEMENT
	}
}