package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.prayer.Prayer;
import org.redrune.game.world.World;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.tool.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class SkillRestorationTask extends ScheduledTask {
	
	public SkillRestorationTask() {
		super(100, -1);
	}
	
	@Override
	public void run() {
		for (Player player : World.get().getPlayers()) {
			if (player == null || !player.isRenderable()) {
				continue;
			}
			int restoreCount = player.getManager().getPrayers().prayerOn(Prayer.RAPID_RESTORE) ? 2 : 1;
			if (player.getAttribute("resting", false)) {
				restoreCount += 1;
			}
			boolean berserkUsage = player.getManager().getPrayers().prayerOn(Prayer.BERSERKER);
			for (int skill = 0; skill < 25; skill++) {
				if (skill == SkillConstants.SUMMONING) {
					continue;
				}
				for (int time = 0; time < restoreCount; time++) {
					int currentLevel = player.getSkills().getLevel(skill);
					int normalLevel = player.getSkills().getLevelForXp(skill);
					if (currentLevel > normalLevel) {
						if ((skill == SkillConstants.ATTACK || skill == SkillConstants.STRENGTH || skill == SkillConstants.DEFENCE || skill == SkillConstants.RANGE || skill == SkillConstants.MAGIC) && berserkUsage && RandomFunction.getRandom(100) <= 15) {
							continue;
						}
						player.getSkills().setLevel(skill, currentLevel - 1);
					} else if (currentLevel < normalLevel) {
						player.getSkills().setLevel(skill, currentLevel + 1);
					} else {
						break;
					}
				}
			}
		}
	}
}
