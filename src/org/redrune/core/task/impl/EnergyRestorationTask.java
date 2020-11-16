package org.redrune.core.task.impl;

import org.redrune.core.SequencialUpdate;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/26/2017
 */
public class EnergyRestorationTask extends ScheduledTask {
	
	/**
	 * The cycle count
	 */
	private long cycle;
	
	public EnergyRestorationTask() {
		super(1, -1);
	}
	
	@Override
	public void run() {
		for (Player player : SequencialUpdate.getRenderablePlayers()) {
			int amount = player.getAttribute("resting", false) ? 3 : ((180 - player.getSkills().getLevel(SkillConstants.AGILITY)) / 10);
			if (cycle % amount != 0) {
				continue;
			}
			player.restoreRunEnergy();
		}
		cycle++;
	}
}
