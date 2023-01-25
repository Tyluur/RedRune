package org.redrune.game.world.region.route.strategy;

import org.redrune.game.world.region.route.RouteStrategy;

public class FixedTileStrategy extends RouteStrategy {
	
	/**
	 * X position of tile.
	 */
	private int x;
	
	/**
	 * Y position of tile.
	 */
	private int y;
	
	public FixedTileStrategy(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
		return currentX == x && currentY == y;
	}
	
	@Override
	public int getApproxDestinationX() {
		return x;
	}
	
	@Override
	public int getApproxDestinationY() {
		return y;
	}
	
	@Override
	public int getApproxDestinationSizeX() {
		return 1;
	}
	
	@Override
	public int getApproxDestinationSizeY() {
		return 1;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof FixedTileStrategy)) {
			return false;
		}
		FixedTileStrategy strategy = (FixedTileStrategy) other;
		return x == strategy.x && y == strategy.y;
	}
	
}
