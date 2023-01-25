package org.redrune.utility.rs.constant;

import lombok.Getter;

/**
 * @author Jolt
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class HeadIcons {
	
	public enum PrayerIcon {
		
		NONE(-1),
		PROTECT_FROM_MELEE(0),
		PROTECT_FROM_RANGE(1),
		PROTECT_FROM_MAGIC(2),
		RETRIBUTION(3),
		SMITE(4),
		REDEMPTION(5),
		MAGIC_AND_RANGE(6),
		SUMMONING(7),
		MELEE_SUMMONING(8),
		RANGE_SUMMONING(9),
		MAGIC_SUMMONING(10),
		EMPTY(11),
		DEFLECT_MELEE(12),
		DEFLECT_MAGIC(13),
		DEFLECT_RANGE(14),
		DEFLECT_SUMMONING(15),
		DEFLECT_MELEE_AND_SUMMONING(16),
		DEFLECT_RANGE_AND_SUMMONING(17),
		DEFLECT_MAGE_AND_SUMMONING(18),
		WRATH(19),
		SOULSPLIT(20),
		REDSKULL(21),
		MELEE_RANGE(22),
		MELEE_MAGE(23),
		MELEE_MAGE_AND_RANGE(24);
		
		/**
		 * The id of the icon
		 */
		@Getter
		private final int id;
		
		PrayerIcon(int id) {
			this.id = id;
		}
	}
	
	public enum SkullIcon {
		NONE(-1),
		DEFAULT(0),
		DEFAULT_RED(1),
		BOUNTY_LEVEL_FIVE(2),
		BOUNTY_LEVEL_FOUR(3),
		BOUNTY_LEVEL_THREE(4),
		BOUNTY_LEVEL_TWO(5),
		BOUNTY_LEVEL_ONE(6),
		GOOL(7);
		
		/**
		 * The id of the icon
		 */
		@Getter
		private final int id;
		
		SkullIcon(int id) {
			this.id = id;
		}
	}
	
}
