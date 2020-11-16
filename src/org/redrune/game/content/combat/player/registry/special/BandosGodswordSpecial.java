package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class BandosGodswordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("bandos godsword");
	}
	
	@Override
	public double multiplier() {
		return 1.21;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle, true);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, multiplier());
		final int damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
		final Hit hit = new Hit(player, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit);
		
		player.sendAnimation(11991);
		player.sendGraphics(2114);
		swing.applyHit(player, target, hit, player.getEquipment().getWeaponId(), combatStyle, 1);
		reduceStats(target, damage);
	}
	
	/**
	 * Reduces the stats
	 *
	 * @param target
	 * 		The target
	 * @param damage
	 * 		The damage
	 */
	private void reduceStats(Entity target, int damage) {
		if (target.isPlayer()) {
			Player targetPlayer = target.toPlayer();
			int amountLeft;
			if ((amountLeft = targetPlayer.getSkills().drainLevel(SkillConstants.DEFENCE, damage / 10)) <= 0) {
				return;
			}
			if ((amountLeft = targetPlayer.getSkills().drainLevel(SkillConstants.STRENGTH, amountLeft)) <= 0) {
				return;
			}
			if ((amountLeft = targetPlayer.getSkills().drainLevel(SkillConstants.PRAYER, amountLeft)) <= 0) {
				return;
			}
			if ((amountLeft = targetPlayer.getSkills().drainLevel(SkillConstants.ATTACK, amountLeft)) <= 0) {
				return;
			}
			if ((amountLeft = targetPlayer.getSkills().drainLevel(SkillConstants.MAGIC, amountLeft)) <= 0) {
				return;
			}
			targetPlayer.getSkills().drainLevel(SkillConstants.RANGE, amountLeft);
		}
	}
}
