package org.redrune.game.content.event.context;

import lombok.Getter;
import org.redrune.game.content.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class WalkEventContext implements EventContext {
	
	/**
	 * The destination x coordinate
	 */
	@Getter
	private final int x;
	
	/**
	 * The destination y coordinate
	 */
	@Getter
	private final int y;
	
	/**
	 * The buffer for the x
	 */
	@Getter
	private final int[] bufferX;
	
	/**
	 * The buffer for the y
	 */
	@Getter
	private final int[] bufferY;
	
	/**
	 * If ctrl was pressed when we started running
	 */
	@Getter
	private final boolean running;
	
	/**
	 * The steps
	 */
	@Getter
	private final int steps;
	
	public WalkEventContext(int x, int y, int[] bufferX, int[] bufferY, boolean running, int steps) {
		this.x = x;
		this.y = y;
		this.bufferX = bufferX;
		this.bufferY = bufferY;
		this.running = running;
		this.steps = steps;
	}
}
