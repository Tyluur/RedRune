package org.redrune.game.content.activity.impl.pvp;

import lombok.Getter;
import org.redrune.game.node.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public enum PvPLocation {
	
	// safe zones
	ARDOUGNE_WEST_BANK(true, 2612, 3330, 2621, 3335),
	ARDOUGNE_EAST_BANK(true, 2649, 3280, 2658, 3287),
	FALADOR_WEST_BANK(true, 2943, 3368, 2947, 3373),
	FALADOR_WEST_BANK2(true, 2948, 3368, 2949, 3369),
	FALADOR_EAST_BANK(true, 3009, 3353, 3018, 3358),
	EDGEVILLE_BANK(true, 3091, 3488, 3098, 3499),
	GRAND_EXCHANGE(true, 3145, 3473, 3184, 3508),
	VARROCK_WEST_BANK(true, 3179, 3432, 3194, 3446),
	VARROCK_EAST_BANK(true, 3250, 3416, 3257, 3424),
	DRAYNOR_BANK(true, 3088, 3240, 3097, 3246),
	LUMBRIDGE_BANK(true, 3204, 3207, 3217, 3230);
	
	/**
	 * If this is a safe zone
	 */
	@Getter
	private boolean safeZone;
	
	/**
	 * The bottom x coordinate of the area
	 */
	@Getter
	private final int bottomX;
	
	/**
	 * The bottom y coordinate of the area
	 */
	private final int bottomY;
	
	/**
	 * The top x coordinate of the area
	 */
	private final int topX;
	
	/**
	 * The top y coordinate of the area
	 */
	private final int topY;
	
	PvPLocation(boolean safeZone, int bottomX, int bottomY, int topX, int topY) {
		this.safeZone = safeZone;
		this.bottomX = bottomX;
		this.bottomY = bottomY;
		this.topX = topX;
		this.topY = topY;
	}
	
	/**
	 * Checks if the tile is inside the safe zone
	 *
	 * @param location
	 * 		The tile
	 */
	public boolean inside(Location location) {
		return location.withinArea(bottomX, bottomY, topX, topY);
	}
	
	/**
	 * The list of areas that are safe to be inside
	 */
	private static final List<PvPLocation> SAFE_ZONES = new ArrayList<>();
	
	/**
	 * The list of areas that are dangerous to be inside
	 */
	private static final List<PvPLocation> DANGEROUS_ZONES = new ArrayList<>();
	
	static {
		for (PvPLocation zone : values()) {
			((zone.isSafeZone() ? SAFE_ZONES : DANGEROUS_ZONES)).add(zone);
		}
	}
	
	/**
	 * Checks if the location is at a pvp location
	 *
	 * @param location
	 * 		The location
	 */
	public static boolean isAtPvpLocation(Location location) {
		if (isAtSafeLocation(location)) {
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the location is a safe location
	 *
	 * @param location
	 * 		The location
	 */
	public static boolean isAtSafeLocation(Location location) {
		for (PvPLocation safe : SAFE_ZONES) {
			if (safe.inside(location)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If we are in the wilderness
	 *
	 * @param location
	 * 		The tile to check for
	 */
	public static boolean isAtWild(Location location) {
		return isAtWildSafe(location) || getWildLevel(location) > 0;
	}
	
	/**
	 * If the tile is at the safe area of the wilderness
	 */
	public static boolean isAtWildSafe(Location tile) {
		return (tile.getX() >= 2940 && tile.getX() <= 3395 && tile.getY() <= 3524 && tile.getY() >= 3523);
	}
	
	/**
	 * Gets the wilderness level at a lcoation
	 *
	 * @param location
	 * 		The location
	 */
	public static int getWildLevel(Location location) {
		int x = location.getX(), y = location.getY();
		int level = 0;
		if (y >= 10302 && y <= 10357) {
			level = (byte) ((y - 9912) / 8 + 1);
		}
		if (x > 2935 && x < 3400 && y > 3524 && y < 4000) {
			level = (byte) ((Math.ceil((y) - 3520D) / 8D) + 1);
		}
		if (y > 10050 && y < 10179 && x > 3008 && x < 3144) {
			level = (byte) ((Math.ceil((y) - 10048D) / 8D) + 17);
		}
		return level;
	}
}
