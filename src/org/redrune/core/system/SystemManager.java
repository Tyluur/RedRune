package org.redrune.core.system;

import org.redrune.core.MajorUpdateWorker;
import org.redrune.core.task.Scheduler;
import org.redrune.utility.backend.OutLogger;
import org.redrune.utility.backend.UnexpectedArgsException;

import static org.redrune.game.GameFlags.*;

/**
 * Manages all system operations.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class SystemManager {
	
	/**
	 * Gets the amount of processors on the computer
	 */
	public static final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();
	
	/**
	 * The update worker instance
	 */
	private static final MajorUpdateWorker MAJOR_UPDATE_WORKER = new MajorUpdateWorker();
	
	/**
	 * The system finalization instance
	 */
	private static final SystemFinalization FINALIZATION = new SystemFinalization();
	
	/**
	 * The instance of hte scheduler
	 */
	private static final Scheduler SCHEDULER = new Scheduler();
	
	/**
	 * Sets default system configuration values
	 *
	 * @throws IllegalStateException
	 * 		If we were unable to parse the arguments
	 */
	public static void setDefaults(String[] args) throws UnexpectedArgsException {
		if (args != null) {
			try {
				debugMode = Boolean.parseBoolean(args[0].trim());
				worldId = Byte.parseByte(args[1].trim());
				webIntegrated = Boolean.parseBoolean(args[2].trim());
			} catch (Throwable e) {
				throw new UnexpectedArgsException();
			}
		} else {
			throw new UnexpectedArgsException();
		}
		System.setOut(new OutLogger(System.out));
	}
	
	/**
	 * Starts the worker
	 */
	public static void start() {
		MAJOR_UPDATE_WORKER.start();
		Runtime.getRuntime().addShutdownHook(FINALIZATION);
	}
	
	/**
	 * Gets the scheduler
	 */
	public static Scheduler getScheduler() {
		return SCHEDULER;
	}
	
	/**
	 * Gets the update worker
	 */
	public static MajorUpdateWorker getUpdateWorker() {
		return MAJOR_UPDATE_WORKER;
	}
}