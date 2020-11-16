package org.redrune.core.task;

import org.redrune.utility.tool.Misc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A class which manages {@link ScheduledTask}s.
 *
 * @author Graham
 */
public final class Scheduler {
	
	/**
	 * The Queue of tasks that are pending execution.
	 */
	private final Queue<ScheduledTask> pending = new LinkedBlockingQueue<>();
	
	/**
	 * The List of currently active tasks.
	 */
	private final List<ScheduledTask> active = new ArrayList<>();
	
	/**
	 * Pulses the {@link Queue} of {@link ScheduledTask}s, removing those that are no longer running.
	 */
	public void pulse() {
		try {
			Misc.pollAll(pending, active::add);
			
			for (Iterator<ScheduledTask> iterator = active.iterator(); iterator.hasNext(); ) {
				// the task from the list
				final ScheduledTask task = iterator.next();
				
				// pulsing the task
				task.pulse();
				
				// so if we've reached the amount of ticks to stop
				// or if the task was forced to stop
				final boolean shouldRemove = (task.getGoalTicks() != -1 && task.getDelayedTickCount() >= task.getGoalTicks()) || !task.isRunning();
				
				// removes the task from the list if its time
				if (shouldRemove) {
					iterator.remove();
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Schedules a new task.
	 *
	 * @param task
	 * 		The task to schedule.
	 */
	public void schedule(ScheduledTask task) {
		if (!pending.add(task)) {
			throw new IllegalStateException("Unable to add task " + task.getClass().getSimpleName() + " to the pending queue.");
		}
	}
	
}