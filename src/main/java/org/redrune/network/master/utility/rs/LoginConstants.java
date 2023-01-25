package org.redrune.network.master.utility.rs;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public interface LoginConstants {
	
	/**
	 * The location where all player saves will be
	 */
	String SAVES_LOCATION = "./data/saves/";
	
	/**
	 * Gets the location the player file should be in
	 *
	 * @param username
	 * 		The name of the file
	 */
	static String getLocation(String username) {
		return SAVES_LOCATION + username + ".json";
	}
}
