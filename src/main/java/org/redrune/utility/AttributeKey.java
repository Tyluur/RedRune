package org.redrune.utility;

/**
 * The map of all attribute keys. Keys here can be used in either the temporary attribute map or the saved attribute
 * map.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public enum AttributeKey {
	
	// MISC ATTRIBUTES
	COST_VALUE,
	
	FOOD_DELAY,
	
	FIND_TARGET_DELAY,
	
	LAST_HIT_BY_ENTITY,
	
	DESTROY_INTERFACE_TYPE,
	
	FROZEN_UNTIL,
	
	FREEZE_DELAY,
	
	FROZEN_BY,
	
	ATTACKED_BY_TIME,
	
	LAST_DIALOGUE_MESSAGE,
	
	SKILL_MENU,
	
	GOD_CHARGED,
	
	TELEBLOCKED_UNTIL,
	
	MIASMIC_EFFECT,
	
	MIASMIC_IMMUNITY,
	
	// UPDATING ATTRIBUTES
	MAP_REGION_CHANGED,
	
	TELEPORT_LOCATION,
	
	TELEPORTED,
	
	FORCE_NEXT_MAP_LOAD,
	
	// saved vars
	LAST_RESTORE_TIME,
	
	FILTERING_PROFANITY,
	
	DUAL_MOUSE_BUTTONS,
	
	CHAT_EFFECTS,
	
	ACCEPTING_AID,
	
	FORCE_MULTI_AREA,
	
	LAST_LONGIN_STAMP,
	
	LAST_UNCOLLAPSED_TELEPORT,
	
	LAST_SELECTED_TELEPORT,
	
	// game bar status
	
	FILTER,
	PUBLIC,
	PRIVATE,
	FRIENDS,
	CLAN,
	TRADE,
	ASSIST;
	
}
