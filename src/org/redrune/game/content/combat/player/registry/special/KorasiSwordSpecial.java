package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class KorasiSwordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("korasi's sword");
	}
	
	@Override
	public double multiplier() {
		return 1;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle, true);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, multiplier());
		final int damage = (int) (swing.randomizeHit(maxHit, attackBonus, defenceBonus) + (0.5 + Math.random()));
		final Hit hit = new Hit(player, damage, HitSplat.MAGIC_DAMAGE).setMaxHit(maxHit);
		
		player.sendAnimation(14788);
		player.sendGraphics(1729);
		target.sendGraphics(2795, 100, 0);
		
		swing.applyHit(player, target, hit, player.getEquipment().getWeaponId(), combatStyle, 1);
	}
}
