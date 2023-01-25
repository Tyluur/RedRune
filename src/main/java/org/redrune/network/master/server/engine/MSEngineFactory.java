package org.redrune.network.master.server.engine;

import org.redrune.core.system.SystemManager;
import org.redrune.network.master.server.engine.worker.MSLoginWorker;
import org.redrune.network.master.utility.rs.LoginRequest;
import org.redrune.utility.backend.RS2ThreadFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class MSEngineFactory {
	
	/**
	 * The scheduled executor service
	 */
	private static final ScheduledExecutorService SCHEDULED = (ScheduledExecutorService) Services.SCHEDULED.getService();
	
	/**
	 * The login worker
	 */
	private static MSLoginWorker loginWorker;
	
	/**
	 * Loads all engine factory workers
	 */
	public static void startUp() {
		(loginWorker = new MSLoginWorker()).schedule(SCHEDULED);
	}
	
	/**
	 * Adds a request to the worker's queue
	 *
	 * @param request
	 * 		The request to add
	 */
	public static void addLoginRequest(LoginRequest request) {
		loginWorker.addRequest(request);
	}
	
	/**
	 * The enum of services we may have
	 */
	private enum Services {
		
		SCHEDULED {
			@Override
			ExecutorService getService() {
				return SystemManager.PROCESSOR_COUNT >= 6 ? Executors.newScheduledThreadPool(SystemManager.PROCESSOR_COUNT >= 12 ? 4 : 2, new RS2ThreadFactory("Scheduled-Worker")) : Executors.newSingleThreadScheduledExecutor(new RS2ThreadFactory("Scheduled-Worker"));
			}
		};
		
		abstract ExecutorService getService();
	}
	
}
