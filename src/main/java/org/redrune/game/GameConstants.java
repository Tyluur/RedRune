package org.redrune.game;

import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface GameConstants {
	
	/**
	 * The name of the server
	 */
	String SERVER_NAME = "RedRune";
	
	/**
	 * The path of the cache
	 */
	String CACHE_PATH = "./data/fs/";
	
	/**
	 * The key used for player-file encryption
	 */
	String FILE_ENCRYPTION_KEY = "MIGeMA0GCSqGSIb3DQEBAQUAA4GMADCBiAKBgFoPEhAf5C/G1AsCURrAXBAKKxLV";
	
	/**
	 * The path to the file with sql configuration
	 */
	String SQL_CONFIGURATION_FILE = "./data/sql.conf";
	
	/**
	 * The maximum amount of players allowed online
	 */
	int PLAYERS_LIMIT = 2048;
	
	/**
	 * The maximum amount of npcs allowed online
	 */
	int NPCS_LIMIT = Short.MAX_VALUE;
	
	/**
	 * The home location
	 */
	Location HOME_LOCATION = new Location(3092, 3503);
	
	/**
	 * The location players are sent to when they die
	 */
	Location DEATH_LOCATION = new Location(3102, 3492);
	
	/**
	 * The id of the pvp world
	 */
	int PVP_WORLD_ID = 2;
	
	/**
	 * The experience multiplier for combat skills
	 */
	int COMBAT_EXPERIENCE_MULTIPLIER = 1;
	
	/**
	 * The experience multiplier for non-combat skills (excluding prayer)
	 */
	int SKILL_EXPERIENCE_MULTIPLIER = 10;
	
	/**
	 * The experience multiplier for prayer
	 */
	int PRAYER_EXPERIENCE_MULTIPLIER = 5;
	
	/**
	 * The url that the players can change their email at
	 */
	String EMAIL_MODIFICATION_URL = "http://redrune.org/settings/email/";
	
	/**
	 * The url that players can see their inbox at
	 */
	String INBOX_URL = "http://redrune.org/messenger/";
	
	/**
	 * The url that players can donate at
	 */
	String DONATION_URL = "http://redrune.org/donate";
}
