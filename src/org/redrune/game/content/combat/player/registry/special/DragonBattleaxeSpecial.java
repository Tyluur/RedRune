package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class DragonBattleaxeSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("dragon battleaxe");
	}
	
	@Override
	public double multiplier() {
		return 1;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		// the player's skills
		final PlayerSkills skills = player.getSkills();
		
		// visuals
		player.sendAnimation(1056);
		player.sendGraphics(246);
		player.sendForcedChat("Raarrrrrgggggghhhhhhh!");
		
		// modifiers
		skills.setLevel(SkillConstants.ATTACK, (int) (skills.getLevelForXp(SkillConstants.ATTACK) * 0.90D));
		skills.setLevel(SkillConstants.DEFENCE, (int) (skills.getLevelForXp(SkillConstants.DEFENCE) * 0.90D));
		skills.setLevel(SkillConstants.RANGE, (int) (skills.getLevelForXp(SkillConstants.RANGE) * 0.90D));
		skills.setLevel(SkillConstants.MAGIC, (int) (skills.getLevelForXp(SkillConstants.MAGIC) * 0.90D));
		skills.setLevel(SkillConstants.STRENGTH, (int) (skills.getLevelForXp(SkillConstants.STRENGTH) * 1.2D));
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
	
	@Override
	public boolean requiresFight() {
		return false;
	}
}
