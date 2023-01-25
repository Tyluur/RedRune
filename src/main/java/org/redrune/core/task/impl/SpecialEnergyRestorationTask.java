package org.redrune.core.task.impl;

import org.redrune.core.SequencialUpdate;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class SpecialEnergyRestorationTask extends ScheduledTask{
	
	public SpecialEnergyRestorationTask() {
		super(50, -1);
	}
	
	@Override
	public void run() {
		for (Player player : SequencialUpdate.getRenderablePlayers()) {
			player.getCombatDefinitions().reduceSpecial(-10);
		}
	}
}
