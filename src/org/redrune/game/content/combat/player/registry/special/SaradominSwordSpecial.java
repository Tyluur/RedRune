package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.utility.tool.RandomFunction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class SaradominSwordSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("saradomin sword");
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
		final int meleeDamage = swing.randomizeHit(maxHit, attackBonus, defenceBonus);
		final int magicDamage = RandomFunction.random(160);
		final Hit meleeHit = new Hit(player, meleeDamage, HitSplat.MELEE_DAMAGE).setMaxHit(maxHit);
		final Hit magicHit = new Hit(player, magicDamage, HitSplat.MAGIC_DAMAGE).setMaxHit(160);
		
		player.sendAnimation(11993);
		target.sendGraphics(1194);
		swing.applyHit(player, target, meleeHit, player.getEquipment().getWeaponId(), combatStyle, 1);
		if (meleeDamage > 0) {
			swing.applyHit(player, target, magicHit, player.getEquipment().getWeaponId(), combatStyle, 1);
		}
	}
}
