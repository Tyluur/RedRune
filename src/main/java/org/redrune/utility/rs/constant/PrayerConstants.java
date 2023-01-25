package org.redrune.utility.rs.constant;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public interface PrayerConstants {
	
	/**
	 * The slot numbers for prayer stat adjustments
	 */
	int ATTACK_SLOT = 0, STRENGTH_SLOT = 1, DEFENCE_SLOT = 2, RANGE_SLOT = 3, MAGIC_SLOT = 4, ENERGY_SLOT = 5, SPECIAL_ENERGY_SLOT = 6;
	
	/**
	 * The configuration ids of the prayers when they're on
	 */
	int[][] ACTIVATED_CONFIGURATION_IDS = {
			// normal prayers
			{ 1, 2, 4, 262144, 524288, 8, 16, 32, 64, 128, 256, 1048576, 2097152, 512, 1024, 2048, 16777216, 4096, 8192, 16384, 4194304, 8388608, 32768, 65536, 131072, 33554432, 134217728, 67108864, 268435456 * 2, 268435456 },
			// curses
			{ 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288 }
			// end
	};
	
	/**
	 * The array of prayers to close, the same types of prayers are collapsed together
	 */
	int[][][] PRAYER_CLOSE_IDS = { { // normal prayer book
			{ 0, 5, 13 }, // Skin prayers 0
			{ 1, 6, 14 }, // Strength prayers 1
			{ 2, 7, 15 }, // Attack prayers 2
			{ 3, 11, 20, 28 }, // Range prayers 3
			{ 4, 12, 21, 29 }, // Magic prayers 4
			{ 8, 9, 26 }, // Restore prayers 5
			{ 10 }, // Protect item prayers 6
			{ 17, 18, 19 }, // Protect prayers 7
			{ 16 }, // Other protect prayers 8
			{ 22, 23, 24 }, // Other special prayers 9
			{ 25, 27 } // Other prayers 10
	}, { // ancient prayer book
			{ 0 }, // Protect item prayers 0
			{ 1, 2, 3, 4 }, // sap prayers 1
			{ 5 }, // other prayers 2
			{ 7, 8, 9, 17, 18 }, // protect prayers 3
			{ 6, 17 }, // other protect prayers 4
			{ 10, 11, 12, 13, 14, 15, 16 }, // leech prayers 5
			{ 19 }, // other prayers
	} };
	
	/**
	 * The prayer levels needed to use prayers
	 */
	int[][] LEVEL_REQUIREMENTS = {
			// normal prayer book
			{ 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 35, 37, 40, 43, 44, 45, 46, 49, 52, 60, 65, 70, 74, 77 },
			// ancient prayer book
			{ 50, 50, 52, 54, 56, 59, 62, 65, 68, 71, 74, 76, 78, 80, 82, 84, 86, 89, 92, 95 }
			// end
	};
	
	/**
	 * The drain rates for prayers
	 */
	double[][] DRAIN_RATES = {
			// regular prayers
			{ 1.2, 1.2, 1.2, 1.2, 1.2, 0.6, 0.6, 0.6, 3.6, 1.8, 1.8, 0.6, 0.6, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 0.3, 1.2, 0.6, 0.18, 0.18, 0.24, 0.15, 0.2, 0.18 },
			// curses
			{ 1.8, 0.24, 0.24, 0.24, 0.24, 1.8, 0.3, 0.3, 0.3, 0.3, 0.36, 0.36, 0.36, 0.36, 0.36, 0.36, 0.36, 1.2, 0.2, 0.2 }
			// end
	};
}
