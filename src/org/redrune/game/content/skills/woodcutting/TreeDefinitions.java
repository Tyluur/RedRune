package org.redrune.game.content.skills.woodcutting;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public enum TreeDefinitions {
	
	NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0),
	
	EVERGREEN(1, 25, 1511, 20, 4, 57931, 8, 0),
	
	DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),
	
	OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15),
	
	WILLOW(30, 67.5, 1519, 60, 4, 1341, 51, 15),
	
	TEAK(30, 85, 6333, 60, 4, 1341, 51, 15),
	
	MAPLE(45, 100, 1517, 65, 12, 31057, 72, 10),
	
	MAHOGANY(50, 125, 6332, 83, 16, 1341, 51, 15),
	
	YEW(60, 175, 1515, 85, 13, 1341, 94, 10),
	
	FRUIT_TREES(1, 25, -1, 20, 4, 1341, 8, 0),
	
	IVY(68, 332.5, -1, 70, 17, 46319, 58, 10),
	
	MAGIC(75, 250, 1513, 110, 14, 37824, 121, 10),
	
	CURSED_MAGIC(82, 250, 1513, 80, 21, 37822, 121, 10);
	
	/**
	 * The level needed to chop this tree
	 */
	@Getter
	private final int level;
	
	/**
	 * The experience received from chopping this tree
	 */
	@Getter
	private final double xp;
	
	/**
	 * The id of the log received from this tree
	 */
	@Getter
	private final int logsId;
	
	/**
	 * The base time for the log
	 */
	@Getter
	private final int logBaseTime;
	
	/**
	 * The random time for the log
	 */
	@Getter
	private final int logRandomTime;
	
	/**
	 * The object id of the stump
	 */
	@Getter
	private final int stumpId;
	
	/**
	 * The respawn delay amount
	 */
	@Getter
	private final int respawnDelay;
	
	/**
	 * The probability for random life
	 */
	@Getter
	private final int randomLifeProbability;
	
	TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
		this.level = level;
		this.xp = xp;
		this.logsId = logsId;
		this.logBaseTime = logBaseTime;
		this.logRandomTime = logRandomTime;
		this.stumpId = stumpId;
		this.respawnDelay = respawnDelay;
		this.randomLifeProbability = randomLifeProbability;
	}
	
}