package org.redrune.game.world.region.route.strategy;

import org.redrune.game.node.item.FloorItem;
import org.redrune.game.world.region.route.RouteStrategy;

public class FloorItemStrategy extends RouteStrategy {
	
	/**
	 * Entity position x.
	 */
	private int x;
	
	/**
	 * Entity position y.
	 */
	private int y;
	
	public FloorItemStrategy(FloorItem entity) {
		this.x = entity.getLocation().getX();
		this.y = entity.getLocation().getY();
	}
	
	@Override
	public boolean canExit(int currentX, int currentY, int sizeXY, int[][] clip, int clipBaseX, int clipBaseY) {
		return RouteStrategy.checkFilledRectangularInteract(clip, currentX - clipBaseX, currentY - clipBaseY, sizeXY, sizeXY, x - clipBaseX, y - clipBaseY, 1, 1, 0);
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
		if (!(other instanceof FloorItemStrategy)) {
			return false;
		}
		FloorItemStrategy strategy = (FloorItemStrategy) other;
		return x == strategy.x && y == strategy.y;
	}
	
}
