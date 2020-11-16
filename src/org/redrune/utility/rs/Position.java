package org.redrune.utility.rs;

import lombok.Getter;

/**
 * Represents a position.
 *
 * @author Emperor
 */
public class Position {
	
	/**
	 * The x coordinate of the position
	 */
	@Getter
	final int x;
	
	/**
	 * The y coordinate of the position
	 */
	@Getter
	final int y;
	
	/**
	 * The z coordinate of the position
	 */
	@Getter
	final int z;
	
	/**
	 * Constructs a new position
	 *
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param z
	 * 		The z
	 */
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + ", z=" + z + "]";
	}
}
