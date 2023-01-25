package org.redrune.game.node.entity.player.link.prayer;

import lombok.Getter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.utility.rs.constant.PrayerConstants;
import org.redrune.utility.tool.Misc;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public enum Prayer implements PrayerConstants {
	
	THICK_SKIN(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][10] }),
	BURST_OF_STRENGTH(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	CLARITY_OF_THOUGHT(new int[][] { PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	SHARP_EYE(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	MYSTIC_WILL(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	ROCK_SKIN(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][10] }),
	SUPERHUMAN_STRENGTH(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	IMPROVED_REFLEXES(new int[][] { PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	RAPID_RESTORE(new int[][] { PRAYER_CLOSE_IDS[0][5] }),
	RAPID_HEAL(new int[][] { PRAYER_CLOSE_IDS[0][5] }),
	PROTECT_ITEM(new int[][] { PRAYER_CLOSE_IDS[0][6] }),
	HAWK_EYE(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	MYSTIC_LORE(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	STEEL_SKIN(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][10] }),
	ULTIMATE_STRENGTH(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	INCREDIBLE_REFLEXES(new int[][] { PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	PROTECT_FROM_SUMMONING(new int[][] { PRAYER_CLOSE_IDS[0][8], PRAYER_CLOSE_IDS[0][9] }),
	PROTECT_FROM_MAGIC(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][9] }),
	PROTECT_FROM_MISSILES(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][9] }),
	PROTECT_FROM_MELEE(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][9] }),
	EAGLE_EYE(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	MYSTIC_MIGHT(new int[][] { PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	RETRIBUTION(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][8], PRAYER_CLOSE_IDS[0][9] }),
	REDEMPTION(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][8], PRAYER_CLOSE_IDS[0][9] }),
	SMITE(new int[][] { PRAYER_CLOSE_IDS[0][7], PRAYER_CLOSE_IDS[0][8], PRAYER_CLOSE_IDS[0][9] }),
	CHIVALRY(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }) {
		@Override
		public boolean canActivate(Player player) {
			if (player.getSkills().getLevelForXp(PlayerSkills.DEFENCE) < 60) {
				player.getTransmitter().sendMessage("You need a defence level of 60 to activate " + getName() + ".");
				return false;
			}
			return true;
		}
	},
	RAPID_RENEWAL(new int[][] { PRAYER_CLOSE_IDS[0][5] }),
	PIETY(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }) {
		@Override
		public boolean canActivate(Player player) {
			if (player.getSkills().getLevelForXp(PlayerSkills.DEFENCE) < 70) {
				player.getTransmitter().sendMessage("You need a defence level of 70 to activate " + getName() + ".");
				return false;
			}
			return true;
		}
	},
	RIGOUR(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	AUGURY(new int[][] { PRAYER_CLOSE_IDS[0][0], PRAYER_CLOSE_IDS[0][1], PRAYER_CLOSE_IDS[0][2], PRAYER_CLOSE_IDS[0][3], PRAYER_CLOSE_IDS[0][4], PRAYER_CLOSE_IDS[0][10] }),
	
	// curse books
	
	PROTECT_ITEM_CURSE(PrayerBook.CURSES, 0, new int[][] { PRAYER_CLOSE_IDS[1][0] }) {
		@Override
		public void activate(Player player) {
			player.sendAnimation(12567);
			player.sendGraphics(2213);
		}
	},
	SAP_WARRIOR(PrayerBook.CURSES, 1, new int[][] { PRAYER_CLOSE_IDS[1][5], PRAYER_CLOSE_IDS[1][6] }),
	SAP_RANGER(PrayerBook.CURSES, 2, new int[][] { PRAYER_CLOSE_IDS[1][5], PRAYER_CLOSE_IDS[1][6] }),
	SAP_MAGE(PrayerBook.CURSES, 3, new int[][] { PRAYER_CLOSE_IDS[1][5], PRAYER_CLOSE_IDS[1][6] }),
	SAP_SPIRIT(PrayerBook.CURSES, 4, new int[][] { PRAYER_CLOSE_IDS[1][5], PRAYER_CLOSE_IDS[1][6] }),
	BERSERKER(PrayerBook.CURSES, 5, new int[][] { PRAYER_CLOSE_IDS[1][2] }) {
		@Override
		public void activate(Player player) {
			player.sendAnimation(12589);
			player.sendGraphics(2266);
		}
	},
	DEFLECT_SUMMONING(PrayerBook.CURSES, 6, new int[][] { PRAYER_CLOSE_IDS[1][4] }),
	DEFLECT_MAGIC(PrayerBook.CURSES, 7, new int[][] { PRAYER_CLOSE_IDS[1][3] }),
	DEFLECT_MISSILES(PrayerBook.CURSES, 8, new int[][] { PRAYER_CLOSE_IDS[1][3] }),
	DEFLECT_MELEE(PrayerBook.CURSES, 9, new int[][] { PRAYER_CLOSE_IDS[1][3] }),
	LEECH_ATTACK(PrayerBook.CURSES, 10, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_RANGED(PrayerBook.CURSES, 11, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_MAGIC(PrayerBook.CURSES, 12, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_DEFENCE(PrayerBook.CURSES, 13, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_STRENGTH(PrayerBook.CURSES, 14, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_ENERGY(PrayerBook.CURSES, 15, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	LEECH_SPECIAL_ATTACK(PrayerBook.CURSES, 16, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][6] }),
	WRATH(PrayerBook.CURSES, 17, new int[][] { PRAYER_CLOSE_IDS[1][3], PRAYER_CLOSE_IDS[1][4] }),
	SOULSPLIT(PrayerBook.CURSES, 18, new int[][] { PRAYER_CLOSE_IDS[1][3] }),
	TURMOIL(PrayerBook.CURSES, 19, new int[][] { PRAYER_CLOSE_IDS[1][1], PRAYER_CLOSE_IDS[1][5], PRAYER_CLOSE_IDS[1][6] }) {
		@Override
		public void activate(Player player) {
			player.sendAnimation(12565);
			player.sendGraphics(2226);
		}
	};
	
	/**
	 * The book this prayer is for
	 */
	@Getter
	private final PrayerBook book;
	
	/**
	 * The slot id of the prayer in the tab
	 */
	@Getter
	private final int slotId;
	
	/**
	 * The prayers that will be closed when this is activated
	 */
	@Getter
	private final int[][] prayersToClose;
	
	/**
	 * Constructs a prayer on the regular book, and the slot type being the ordinal of the enum object
	 *
	 * @param prayersToClose
	 * 		The array of prayers to close
	 */
	Prayer(int[][] prayersToClose) {
		this.book = PrayerBook.REGULAR;
		this.slotId = ordinal();
		this.prayersToClose = prayersToClose;
	}
	
	/**
	 * Constructs a prayer
	 *
	 * @param book
	 * 		The book of the prayer
	 * @param slotId
	 * 		The slot the prayer is in
	 * @param prayersToClose
	 * 		The prayers to close
	 */
	Prayer(PrayerBook book, int slotId, int[][] prayersToClose) {
		this.book = book;
		this.slotId = slotId;
		this.prayersToClose = prayersToClose;
	}
	
	/**
	 * Finds a prayer by a slot
	 *
	 * @param slotId
	 * 		The slot
	 * @param book
	 * 		The book of the prayer
	 */
	public static Optional<Prayer> findPrayerBySlot(int slotId, PrayerBook book) {
		return Arrays.stream(values()).filter(prayer -> prayer.slotId == slotId && prayer.book == book).findFirst();
	}
	
	/**
	 * Handles the activation of the prayer
	 *
	 * @param player
	 * 		The player activating the prayer
	 */
	public void activate(Player player) {
	
	}
	
	/**
	 * If the player can activate the prayer. Some prayers have secondary requirements, so these are handled here.
	 *
	 * @param player
	 * 		The player
	 */
	public boolean canActivate(Player player) {
		return true;
	}
	
	/**
	 * Gets the prayer level required to activate a prayer.
	 */
	public int getPrayerLevelRequired() {
		return LEVEL_REQUIREMENTS[book.ordinal()][slotId];
	}
	
	/**
	 * Gets the configuration id for activating this prayer
	 */
	public int getActivationConfig() {
		return ACTIVATED_CONFIGURATION_IDS[book.ordinal()][slotId];
	}
	
	/**
	 * If the prayer is a drainer
	 */
	public boolean isDrainer() {
		return isSap() || isLeech();
	}
	
	/**
	 * If the prayer is a sap prayer
	 */
	public boolean isSap() {
		return getName().toLowerCase().contains("sap");
	}
	
	/**
	 * If the prayer is a leech prayer
	 */
	public boolean isLeech() {
		return getName().toLowerCase().contains("leech");
	}
	
	/**
	 * Gets the name of this prayer
	 */
	public String getName() {
		return Misc.formatPlayerNameForDisplay(name());
	}
	
}
