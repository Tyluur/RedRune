package org.redrune.game.module.command.player;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
@CommandManifest(description = "Masters you out like a pimp")
public class MasterCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("master");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		for (int skillId = 0; skillId < PlayerSkills.SKILL_NAME.length; skillId++) {
			int level = 99;
			int exp = PlayerSkills.getXPForLevel(level);
			player.getSkills().setLevel(skillId, level);
			player.getSkills().setXp(skillId, exp);
		}
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		player.getVariables().setHealthPoints(player.getSkills().getLevelForXp(SkillConstants.HITPOINTS) * 10);
		player.getVariables().setPrayerPoints(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10);
		player.getTransmitter().sendSettings();
	}
}
