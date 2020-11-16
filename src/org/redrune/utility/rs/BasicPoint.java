package org.redrune.utility.rs;

import lombok.Getter;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class BasicPoint {
	
	@Getter
	private int x;
	
	@Getter
	private int y;
	
	@Getter
	private int z;
	
	private BasicPoint(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BasicPoint) {
			BasicPoint p = (BasicPoint) other;
			return p.x == x && p.y == y && p.z == z;
		}
		return false;
	}
	
	public static BasicPoint create(int x, int y, int z) {
		return new BasicPoint(x, y, z);
	}
	
}
