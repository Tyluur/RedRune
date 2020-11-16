package org.redrune.utility.rs;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @author Matrix Team
 * @since 6/30/2017
 */
public interface Graphics {
	
	/**
	 * The id of the graphic
	 */
	int id();
	
	/**
	 * If the graphic is for an npc
	 */
	boolean npc();
	
	/**
	 * The height of the graphic
	 */
	default int height() {
		return 0;
	}
	
	/**
	 * The speed of the graphic
	 */
	default int speed() {
		return 0;
	}
	
	/**
	 * The rotation of the graphic
	 */
	default int rotation() {
		return 0;
	}
	
	/**
	 * Gets the primary settings hash
	 */
	default int getPrimarySettings() {
		return (speed() & 0xffff) | (height() << 16);
	}
	
	/**
	 * Gets the secondary settings hash
	 */
	default int getSecondarySettings() {
		int hash = 0;
		hash |= rotation() & 0x7;
		return hash;
	}
	
}