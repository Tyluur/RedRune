package org.redrune.core.boot;

import lombok.Getter;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since 10/24/2015
 */
public class BootWorker extends Thread {
	
	/**
	 * The load of work we must complete
	 */
	@Getter
	private final CopyOnWriteArrayList<BootTask> workLoad = new CopyOnWriteArrayList<>();
	
	/**
	 * The number of the book worker
	 */
	private final int number;
	
	BootWorker(int number) {
		setName("Boot Worker #" + (number + 1));
		this.number = number;
	}
	
	@Override
	public void run() {
		for (BootTask work : new ArrayList<>(workLoad)) {
			new Thread(() -> {
				try {
					//					long start = System.currentTimeMillis();
					work.getTask().run();
					workLoad.remove(work);
					BootHandler.getCountDownLatch().countDown();
					//					long delay = System.currentTimeMillis() - start;
					//					System.out.println("Worker #" + number + ":\t\tFinished job " + work.getTaskNumber() + " in " + delay + " ms\t\t\tqueue=[" + BootHandler.workersLeftDetails() + "]");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
	
	/**
	 * Adds more work to the {@link #workLoad}
	 *
	 * @param work
	 * 		The work to add
	 * @param index
	 * 		The index of the work
	 */
	void addToWorkLoad(Runnable work, int index) {
		workLoad.add(new BootTask(work, index));
	}
	
	/**
	 * Gets the size of the workload
	 */
	int getWorkLoadSize() {
		return workLoad.size();
	}
}
