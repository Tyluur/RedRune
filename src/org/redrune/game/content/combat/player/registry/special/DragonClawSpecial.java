package org.redrune.game.content.combat.player.registry.special;

import org.redrune.utility.tool.Misc;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class DragonClawSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("dragon claws");
	}
	
	@Override
	public double multiplier() {
		return 1.15;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle, true);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, multiplier());
		
		player.sendAnimation(10961);
		player.sendGraphics(1950);
		
		int[] hits;
		int damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
		if (damage > 0) {
			hits = new int[] { damage, damage / 2, (damage / 2) / 2, (damage / 2) - ((damage / 2) / 2) };
		} else {
			damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
			if (damage > 0) {
				hits = new int[] { 0, damage, damage / 2, damage - (damage / 2) };
			} else {
				damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
				if (damage > 0) {
					hits = new int[] { 0, 0, damage / 2, (damage / 2) + 10 };
				} else {
					damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
					if (damage > 0) {
						hits = new int[] { 0, 0, 0, (int) (damage * 1.5) };
					} else {
						hits = new int[] { 0, 0, 0, Misc.getRandom(7) };
					}
				}
			}
		}
		for (int i = 0; i < hits.length; i++) {
			swing.applyHit(player, target, new Hit(player, hits[i], HitSplat.MELEE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, i > 1 ? 2 : 1);
		}
	}
}
