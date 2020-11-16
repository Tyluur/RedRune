package org.redrune.core.task;

import com.google.common.base.Preconditions;
import lombok.Getter;

/**
 * A game-related task that is scheduled to run in the future.
 *
 * @author Graham
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public abstract class ScheduledTask {
	
	/**
	 * The maximum amount of ticks that can be ran on this task
	 */
	@Getter
	private final int goalTicks;
	
	/**
	 * The delay between executions of the task, in ticks.
	 */
	private final int delay;
	
	/**
	 * The number of ticks remaining until the task is next executed.
	 */
	@Getter
	private int ticks;
	
	/**
	 * The amount of times this task has been pulsed
	 */
	@Getter
	private int ticksPassed = 0;
	
	/**
	 * The delayed tick count, this is incremented each time the task is pulsed. The task is pulsed based on the delay
	 * set in the constructor.
	 */
	@Getter
	private int delayedTickCount = 0;
	
	/**
	 * A flag indicating if the task is running.
	 */
	private boolean running = true;
	
	/**
	 * Constructs a scheduled task with a tick delay
	 */
	public ScheduledTask() {
		this(1);
	}
	
	/**
	 * Creates a new scheduled task.
	 *
	 * @param delay
	 * 		The delay between executions of the task, in ticks.
	 * @throws IllegalArgumentException
	 * 		If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay) {
		this(delay, 1);
	}
	
	/**
	 * Creates a new scheduled task.
	 *
	 * @param delay
	 * 		The delay between executions of the task, in ticks.
	 * @param goalTicks
	 * 		The maximum amount of ticks that this task can go through
	 * @throws IllegalArgumentException
	 * 		If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay, int goalTicks) {
		Preconditions.checkArgument(delay >= 0, "Delay cannot be negative");
		this.delay = delay;
		this.ticks = delay;
		this.goalTicks = goalTicks;
	}
	
	@Override
	public String toString() {
		return "ScheduledTask{" + "goalTicks=" + goalTicks + ", delay=" + delay + ", ticks=" + ticks + ", ticksPassed=" + ticksPassed + ", running=" + running + '}';
	}
	
	/**
	 * Checks if this task is running.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isRunning() {
		return running;
	}
	
	/**
	 * Stops the task.
	 */
	protected void stop() {
		running = false;
	}
	
	/**
	 * Pulses this task: updates the delay and calls {@link Runnable#run()} )} if necessary.
	 */
	final void pulse() {
		// task wasnt forced to stop
		if (!running) {
			return;
		}
		// reduce delay
		ticks--;
		
		// ticks passed increments
		ticksPassed++;
		
		// time until the next one has lapsed
		if (ticks <= 0) {
			// increases the delayed tick count
			delayedTickCount++;
			// reset in the case of infinite looping pulse
			ticks = delay;
			// runs the task
			run();
		}
	}
	
	/**
	 * Runs the task
	 */
	public abstract void run();
}