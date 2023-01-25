package org.redrune.cache;

import org.redrune.game.GameConstants;

import static org.redrune.cache.CacheFileStore.*;

/**
 * The cache loading class
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/17
 */
public class Cache {
	
	/**
	 * Initializes the cache
	 */
	public static void init() {
		try {
			CacheManager.load(GameConstants.CACHE_PATH);
			generateRepository();
			System.out.println("Cache loaded! [items=" + getItemDefinitionsSize() + ", interfaces=" + getInterfaceDefinitionsSize() + ", npcs=" + getNPCDefinitionsSize() + ", objects=" + getObjectDefinitionsSize() + ", anims=" + getAnimationDefinitionsSize() + ", graphics=" + getGraphicDefinitionsSize() + "]");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
