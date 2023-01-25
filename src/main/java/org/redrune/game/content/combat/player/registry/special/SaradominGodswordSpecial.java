package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class SaradominGodswordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("saradomin godsword");
	}
	
	@Override
	public double multiplier() {
		return 1.10;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle, true);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, multiplier());
		final int damage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
		final Hit hit = new Hit(player, damage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit);
		
		player.sendAnimation(12019);
		player.sendGraphics(2109);
		if (damage > 0) {
			player.heal(damage / 2);
			player.getManager().getPrayers().restorePrayer((damage / 4) * 10);
		}
		swing.applyHit(player, target, hit, player.getEquipment().getWeaponId(), combatStyle, 1);
	}
}
