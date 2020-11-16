package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.item.FloorItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemTask extends ScheduledTask {
	
	/**
	 * The floor item
	 */
	private final FloorItem item;
	
	public FloorItemTask(FloorItem item) {
		super(1, -1);
		this.item = item;
	}
	
	@Override
	public void run() {
		// make sure that the item still exists before any of this. if it doesn't we can stop the task
		if (!item.isRenderable()) {
			stop();
			System.out.println("stopped ticking an unrenedab");
			return;
		}
		item.addTicksPassed();
		// the time hasn't passed yet, hold on.
		if (!item.ticksElapsed()) {
			return;
		}
		// the item is a public item and its next phase is to delete
		if (item.isDefaultPublic()) {
			item.setRenderable(false);
			item.getRegion().removeFloorItem(item);
			stop();
			//			System.out.println("removed a public floor item	" + item);
		} else {
			// the item had an owner and its been alive for its destination ticks
			// the next phase is to remove after public for 3 minutes
			if (item.isOwnerVisibleOnly()) {
				// owner visible flag
				item.setOwnerVisibleOnly(false);
				
				// 300 ticks = 3 minutes
				item.setTicksPassed(0);
				item.setTargetTicks(300);
				
				// everyone else should see the item now
				item.getRegion().sendFloorItemToAll(item, true);
				//				System.out.println("made an item visible to the public	" + item);
			} else {
				// the item's phase for being public to the owner only has already lapsed... removal now
				item.setRenderable(false);
				item.getRegion().removeFloorItem(item);
				//				System.out.println("removed an owner floor item\t" + item);
				stop();
			}
			// TODO fix: ground items showing up for other players when removed
			// http://i.imgur.com/3YCLp7M.png
		}
	}
}