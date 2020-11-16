package org.redrune.core.boot;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 3/25/2016
 */
class BootTask {
	
	/**
	 * The task to run
	 */
	@Getter
	private final Runnable task;
	
	/**
	 * The identification number of the task
	 */
	@Getter
	@Setter
	private int taskNumber;

	BootTask(Runnable task, int taskNumber) {
		this.task = task;
		this.taskNumber = taskNumber;
	}
}
