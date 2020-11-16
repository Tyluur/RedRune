package org.redrune.game.node.entity.player.link.prayer;

import org.redrune.utility.rs.constant.PrayerConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public interface DrainPrayer extends PrayerConstants {
	
	/**
	 * The prayer that this is used for
	 */
	Prayer getPrayer();
	
	/**
	 * The start animation of the drain prayer
	 */
	int startAnimationId();
	
	/**
	 * The start graphics of the drain prayer
	 */
	int startGraphicsId();
	
	/**
	 * The projectile of the drain prayer
	 */
	int projectileId();
	
	/**
	 * The graphics applied when the effect hits
	 */
	int landingGraphicsId();
	
	/**
	 * The maximum the drain can reduce by
	 */
	double drainCap();
	
	/**
	 * The slots in the {@link PrayerManager#modifiers} array
	 */
	int[] prayerSlots();
	
	/**
	 * The amounts to drain by
	 */
	double[] amounts();
	
	/**
	 * If the source's {@link PrayerManager#modifiers} should be increased, for sap prayers this is untrue.
	 */
	default boolean raiseSource() {
		return raiseCap() != -1;
	}
	
	/**
	 * The maximum this drain can be raised to
	 */
	double raiseCap();
	
	/**
	 * Constructs an int array from a varargs int array
	 *
	 * @param args
	 * 		The varargs
	 */
	default int[] args(int... args) {
		return args;
	}
	
	/**
	 * Constructs a double array from a varargs double array
	 *
	 * @param args
	 * 		The varargs
	 */
	default double[] args(double... args) {
		return args;
	}
	
}

