package org.redrune.game.module.command.player;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
@CommandManifest(description = "Sets the level of a skill", types = { Integer.class, Integer.class })
public class SetLevelCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("setlevel");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int skillId = intParam(args, 1);
		int level = intParam(args, 2);
		if (level <= 0) {
			level = 1;
		} else if (level > 99) {
			level = 99;
		}
		int exp = PlayerSkills.getXPForLevel(level);
		player.getSkills().setLevel(skillId, level);
		player.getSkills().setXp(skillId, exp);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
}
