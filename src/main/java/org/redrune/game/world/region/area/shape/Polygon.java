package org.redrune.game.world.region.area.shape;

import org.redrune.game.world.region.area.Shape;
import org.redrune.game.node.Location;

public class Polygon extends Shape {
	
	/**
	 * The amount of sides the shape has
	 */
	private int sides;
	
	/**
	 * The points of the shape
	 */
	private int[][] points;
	
	public Polygon(Location[] points) {
		setSides(points.length).areas(points);
		
		setPoints(new int[sides][2]);
		
		for (int i = 0; i < sides; i++) {
			getPoints()[i][0] = points[i].getX();
			getPoints()[i][1] = points[i].getY();
		}
		
	}
	
	@Override
	public boolean inside(Location location) {
		boolean inside = false;
		int y = location.getY(), x = location.getX();
		
		for (int i = 0, j = getSides() - 1; i < getSides(); j = i++) {
			if ((getPoints()[i][1] < y && getPoints()[j][1] >= y) || (getPoints()[j][1] < y && getPoints()[i][1] >= y)) {
				if (getPoints()[i][0] + (y - getPoints()[i][1]) / (getPoints()[j][1] - getPoints()[i][1]) * (getPoints()[j][0] - getPoints()[i][0]) < x) {
					inside = !inside;
				}
			}
		}
		
		return inside;
	}
	
	/**
	 * Gets the sides of the shape
	 */
	public int getSides() {
		return sides;
	}
	
	/**
	 * Sets the sides of the shape
	 *
	 * @param sides
	 * 		The sides
	 */
	public Shape setSides(int sides) {
		this.sides = sides;
		return this;
	}
	
	/**
	 * Gets the points
	 */
	public int[][] getPoints() {
		return points;
	}
	
	/**
	 * Sets the points
	 *
	 * @param points
	 * 		The points
	 */
	public Shape setPoints(int[][] points) {
		this.points = points;
		return this;
	}
	
}