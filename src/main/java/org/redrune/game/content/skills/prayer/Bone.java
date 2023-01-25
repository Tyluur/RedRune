package org.redrune.game.content.skills.prayer;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public enum Bone {
	
	NORMAL(526, 100),
	
	BURNT(528, 100),
	
	WOLF(2859, 100),
	
	MONKEY(3183, 125),
	
	BAT(530, 125),
	
	BIG(532, 200),
	
	JOGRE(3125, 200),
	
	ZOGRE(4812, 250),
	
	SHAIKAHAN(3123, 300),
	
	BABY(534, 350),
	
	WYVERN(6812, 400),
	
	DRAGON(536, 500),
	
	FAYRG(4830, 525),
	
	RAURG(4832, 550),
	
	DAGANNOTH(6729, 650),
	
	OURG(4834, 750),
	
	FROST_DRAGON(18830, 850);
	
	/**
	 * The map of all the BONE_MAP
	 */
	private static final Map<Integer, Bone> BONE_MAP = new HashMap<>();
	
	static {
		for (Bone bone : Bone.values()) {
			BONE_MAP.put(bone.getItemId(), bone);
		}
	}
	
	/**
	 * The item id of the bone
	 */
	@Getter
	private final int itemId;
	
	/**
	 * The amount of experience the player gets for burying the bone.
	 */
	@Getter
	private final double experience;
	
	Bone(int itemId, double experience) {
		this.itemId = itemId;
		this.experience = experience;
	}
	
	/**
	 * Constructs an array of all the item ids of all the bones in this enum.
	 */
	public static int[] getItemIds() {
		int[] itemIds = new int[values().length];
		Bone[] values = values();
		for (int i = 0; i < values.length; i++) {
			Bone bone = values[i];
			itemIds[i] = bone.getItemId();
		}
		return itemIds;
	}
	
	/**
	 * Gets the bone by the item id
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	public static Bone getBone(int itemId) {
		return BONE_MAP.get(itemId);
	}
	
}
