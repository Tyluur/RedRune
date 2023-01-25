package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/22/2017
 */
public class GraniteMaulSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("granite maul");
	}
	
	@Override
	public double multiplier() {
		return 1.1;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		double attackBonus = swing.getAttackBonus(player, player.getEquipment().getWeaponId(), combatStyle, true);
		double defenceBonus = swing.getDefenceBonus(target, player.getEquipment().getWeaponId(), combatStyle);
		double maxHit = swing.getMaxHit(player, player.getEquipment().getWeaponId(), combatStyle, multiplier());
		
		player.sendAnimation((1667));
		player.sendGraphics(340, 96 << 16, 0);
		swing.applyHit(player, target, new Hit(player, swing.randomizeHit(maxHit, attackBonus, defenceBonus), HitSplat.MELEE_DAMAGE).setMaxHit(maxHit), player.getEquipment().getWeaponId(), combatStyle, 0);
	}
	
	@Override
	public boolean isInstant() {
		return true;
	}
}
