package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class DragonDaggerSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("dragon dagger");
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
		
		player.sendAnimation(1062);
		player.sendGraphics(252, 96, 0);
		
		// the first hit
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.MELEE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, 1);
		// the second hit is applied on another tick if its an npc we're speccing
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.MELEE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, target.isPlayer() ? 1 : 2);
	}
}
