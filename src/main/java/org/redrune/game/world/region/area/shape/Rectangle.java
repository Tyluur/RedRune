package org.redrune.game.world.region.area.shape;

import org.redrune.game.node.Location;
import org.redrune.game.world.region.area.Shape;

/**
 * Constructs a rectangular shape
 *
 * @author Viper
 * @author Tyluur <itstyluur@gmail.com>
 */
public class Rectangle extends Shape {
	
	public Rectangle(Location northEast, Location southWest) {
		areas(northEast, southWest);
	}
	
	@Override
	public boolean inside(Location location) {
		return !(getAreas()[0].getX() < location.getX() || getAreas()[1].getX() > location.getX()) && !(getAreas()[0].getY() < location.getY() || getAreas()[1].getY() > location.getY());
	}
	
}