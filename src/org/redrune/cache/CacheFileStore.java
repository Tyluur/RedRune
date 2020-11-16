package org.redrune.cache;

import com.alex.store.Store;
import org.redrune.game.GameConstants;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class CacheFileStore {
	
	/**
	 * The archive with all data.
	 */
	public static Store STORE;
	
	/**
	 * Private constructor
	 */
	private CacheFileStore() {
	
	}
	
	/**
	 * Generates the file store repostiory
	 */
	public static void generateRepository() throws IOException {
		STORE = new Store(GameConstants.CACHE_PATH);
	}
	
	/**
	 * Gets the size of the item definition index
	 */
	public static int getItemDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[19].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[19].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the interface definition index
	 */
	public static int getInterfaceDefinitionsSize() {
		return STORE.getIndexes()[3].getLastArchiveId() + 1;
	}
	
	/**
	 * Gets the size of the graphic definition index
	 */
	public static int getGraphicDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[21].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[21].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the animation definition index
	 */
	public static int getAnimationDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[20].getLastArchiveId();
		return lastArchiveId * 128 + STORE.getIndexes()[20].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the config definitions index
	 */
	public static int getConfigDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[22].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[22].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the object definitions index
	 */
	public static int getObjectDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[16].getLastArchiveId();
		return lastArchiveId * 256 + STORE.getIndexes()[16].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the size of the npc definitions index
	 */
	public static int getNPCDefinitionsSize() {
		int lastArchiveId = STORE.getIndexes()[18].getLastArchiveId();
		return lastArchiveId * 128 + STORE.getIndexes()[18].getValidFilesCount(lastArchiveId);
	}
	
	/**
	 * Gets the amount of components an interface has
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public static int getAmountOfComponents(int interfaceId) {
		return STORE.getIndexes()[3].getLastFileId(interfaceId) + 1;
	}
	
}
