package org.redrune.game.content.skills.firemaking;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public enum Fire {
	
	NORMAL(1511, 1, 30, 2732, 40),
	ACHEY(2862, 1, 30, 2732, 40),
	OAK(1521, 15, 45, 2732, 60),
	WILLOW(1519, 30, 45, 2732, 90),
	TEAK(6333, 35, 45, 2732, 105),
	ARCTIC_PINE(10810, 42, 50, 2732, 125),
	MAPLE(1517, 45, 50, 2732, 135),
	MAHOGANY(6332, 50, 70, 2732, 157.5),
	EUCALYPTUS(12581, 58, 70, 2732, 193.5),
	YEW(1515, 60, 80, 2732, 202.5),
	MAGIC(1513, 75, 90, 2732, 303.8),
	CURSED_MAGIC(13567, 82, 100, 2732, 303.8);
	
	/**
	 * The log id of the fire
	 */
	@Getter
	private int logId;
	
	/**
	 * The level required for this fire
	 */
	@Getter
	private int level;
	
	/**
	 * How many ticks the fire will appear for
	 */
	@Getter
	private int life;
	
	/**
	 * The object id of the fire
	 */
	@Getter
	private int objectId;
	
	/**
	 * The experience given from lighting this fire
	 */
	@Getter
	private double xp;
	
	Fire(int logId, int level, int life, int objectId, double xp) {
		this.logId = logId;
		this.level = level;
		this.life = life;
		this.objectId = objectId;
		this.xp = xp;
	}
	
	/**
	 * Gets the fire instance by the log
	 *
	 * @param logId
	 * 		The log
	 */
	public static Fire getFireInstance(int logId) {
		for (Fire fire : Fire.values()) {
			if (fire.getLogId() == logId) {
				return fire;
			}
		}
		return null;
	}
}