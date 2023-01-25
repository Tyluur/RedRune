package org.redrune.game.world.region.area;

import lombok.Getter;
import org.redrune.game.node.Location;

/**
 * The class that defines a shape
 *
 * @author Viper
 * @author Tyluur <itstyluur@gmail.com>
 */
public abstract class Shape {
	
	/**
	 * The areas of the location
	 */
	@Getter
	private Location[] areas;
	
	/**
	 * If our location is inside the location
	 *
	 * @param location
	 * 		Our location
	 */
	public abstract boolean inside(Location location);
	
	public Shape areas(Location... areas) {
		this.areas = areas;
		return this;
	}
	
}