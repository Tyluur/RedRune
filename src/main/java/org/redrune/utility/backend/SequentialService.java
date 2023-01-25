package org.redrune.utility.backend;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/18/2017
 */
public interface SequentialService {
	
	/**
	 * Handles the start of a sequential service
	 */
	void start();
	
	/**
	 * Handles the execution of a sequential service
	 */
	void execute();
	
	/**
	 * Handles the last tasks that are done before the service is complete
	 */
	void end();
	
	/**
	 * Runs the service in sequence.
	 */
	default void run() {
		start();
		execute();
		end();
	}
	
}