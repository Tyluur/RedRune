package org.redrune.game.node.entity.player.link.prayer;

import org.redrune.utility.tool.Misc;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class PrayerEffectRepository {
	
	/**
	 * The list of drain prayers
	 */
	private static final Set<DrainPrayer> DRAIN_PRAYERS = new HashSet<>();
	
	/**
	 * Registers all drain prayers into the database
	 */
	public static void registerAll() {
		DRAIN_PRAYERS.clear();
		Misc.getClassesInDirectory(DrainPrayer.class.getPackage().getName() + ".drain").stream().filter(DrainPrayer.class::isInstance).forEach(clazz -> DRAIN_PRAYERS.add((DrainPrayer) clazz));
		System.out.println("Loaded " + DRAIN_PRAYERS.size() + " drain prayer effects.");
	}
	
	/**
	 * Gets a drain prayer
	 *
	 * @param prayer
	 * 		The prayer to find it by
	 */
	public static Optional<DrainPrayer> getDrainPrayer(Prayer prayer) {
		return DRAIN_PRAYERS.stream().filter(drainPrayer -> drainPrayer.getPrayer().equals(prayer)).findFirst();
	}
	
}
