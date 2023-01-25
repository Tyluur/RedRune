package org.redrune.utility.rs;

import lombok.Getter;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public enum GameTab {
	
	COMBAT_STYLES(884, 203, 89),
	ACHIEVEMENT_TAB(930, 204, 90),
	STATS(320, 205, 91),
	QUEST_JOURNALS(506, 206, 92),
	INVENTORY(InterfaceConstants.INVENTORY_INTERFACE_ID, 207, 93),
	WORN_EQUIPMENT(387, 208, 94),
	PRAYER_LIST(271, 209, 95),
	MAGIC_SPELLBOOK(192, 210, 96),
	FRIENDS_LIST(550, 212, 98),
	FRIENDS_CHAT(1109, 213, 99),
	CLAN_CHAT(1110, 214, 100),
	OPTIONS(261, 215, 101),
	EMOTES(590, 216, 102),
	MUSIC_PLAYER(187, 217, 103),
	NOTES(34, 218, 104),
	LOGOUT(182, 221, 107);
	
	/**
	 * The interface id of the tab
	 */
	@Getter
	private final int interfaceId;
	
	/**
	 * The child id to display the tab on fixed mode
	 */
	@Getter
	private final int fixedChildId;
	
	/**
	 * The child id to display the tab on resized mode
	 */
	@Getter
	private final int resizedChildId;
	
	GameTab(int interfaceId, int fixedChildId, int resizedChildId) {
		this.interfaceId = interfaceId;
		this.fixedChildId = fixedChildId;
		this.resizedChildId = resizedChildId;
	}
}
