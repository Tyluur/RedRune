package org.redrune.utility.backend;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class RS2ThreadFactory implements ThreadFactory {
	
	/**
	 * The name of the factory
	 */
	private final String name;
	
	/**
	 * The priority of the thread in the factory
	 */
	private final int priority;
	
	/**
	 * The amount of threads in the factory
	 */
	private final AtomicInteger threadCount = new AtomicInteger();
	
	public RS2ThreadFactory(String name) {
		this(name, Thread.NORM_PRIORITY);
	}
	
	public RS2ThreadFactory(String name, int priority) {
		this.name = name;
		this.priority = priority;
	}
	
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, name + "-" + threadCount.getAndIncrement());
		thread.setPriority(priority);
		return thread;
	}
	
}
