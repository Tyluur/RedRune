package org.redrune.network.master.server.engine;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public abstract class MSEngineWorker implements Runnable {
	
	/**
	 * Schedules the worker in this thread
	 */
	public abstract void schedule(ScheduledExecutorService service);
	
}
