package org.redrune.game.content.skills.woodcutting;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public enum HatchetDefinitions {
	
	BRONZE(1351, 1, 1, 879),
	
	IRON(1349, 1, 2, 877),
	
	STEEL(1353, 5, 3, 875),
	
	BLACK(1361, 11, 4, 873),
	
	MITHRIL(1355, 21, 5, 871),
	
	ADAMANT(1357, 31, 7, 869),
	
	RUNE(1359, 41, 10, 867),
	
	DRAGON(6739, 61, 13, 2846),
	
	INFERNO(13661, 61, 13, 10251);
	
	/**
	 * The id of the hatchet
	 */
	@Getter
	private final int itemId;
	
	/**
	 * The level required to use the hatchet
	 */
	@Getter
	private final int levelRequired;
	
	/**
	 * The speed modifier of the hatchet
	 */
	@Getter
	private final int speedModifier;
	
	/**
	 * The id of the animation the hatchet uses
	 */
	@Getter
	private final int animationId;
	
	HatchetDefinitions(int itemId, int levelRequired, int speedModifier, int animationId) {
		this.itemId = itemId;
		this.levelRequired = levelRequired;
		this.speedModifier = speedModifier;
		this.animationId = animationId;
	}
}
