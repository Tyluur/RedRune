package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class PlayerSavingTask extends ScheduledTask {
	
	public PlayerSavingTask() {
		super(1000, -1);
	}
	
	@Override
	public void run() {
		for (Player player : World.get().getPlayers()) {
			if (player == null) {
				continue;
			}
			player.save();
		}
	}
}
