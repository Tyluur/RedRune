package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class ZamorakGodswordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("zamorak godsword");
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
		
		player.sendAnimation(7070);
		player.sendGraphics(1221);
		if (damage > 0 && target.getSize() == 1) {
			target.sendGraphics(2104);
			target.freeze(player, TimeUnit.SECONDS.toMillis(20), "You have been frozen!");
		}
		swing.applyHit(player, target, hit, player.getEquipment().getWeaponId(), combatStyle, 1);
	}
}
