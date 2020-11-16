package org.redrune.game.node.entity.data;

import org.redrune.utility.rs.Hit;

/**
 * This class contains methods that are necessary for the abstraction of entities.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public interface EntityDetails {
	
	/**
	 * Gets the maximum hitpoints of the entity
	 */
	int getMaxHealth();
	
	/**
	 * Sets the amount of health points we're at
	 *
	 * @param healthPoints
	 * 		The amount of health points we're at
	 */
	void setHealthPoints(int healthPoints);
	
	/**
	 * What is executed on the tick of an entity.
	 */
	void tick();
	
	/**
	 * Receives the hit and makes any modification [soaking] necessary, as well as secondary listeners [prayer
	 * deflections]
	 *
	 * @param hit
	 * 		The hit received
	 */
	void receiveHit(Hit hit);
	
	/**
	 * If we are fighting
	 */
	boolean fighting();
	
	/**
	 * Fires the death event
	 */
	void fireDeathEvent();
	
	/**
	 * Restores to default settings
	 */
	void restoreAll();
	
}
