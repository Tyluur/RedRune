package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerEquipment;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class MagicBowSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("magic *bow");
	}
	
	@Override
	public double multiplier() {
		return 1.15;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		// we should always be on this mode
		if (!(swing instanceof RangeCombatSwing)) {
			return;
		}
		// the range swing type
		RangeCombatSwing range = (RangeCombatSwing) swing;
		// animates
		player.sendAnimation(1074);
		
		// sends the damages
		RangeCombatSwing.sendDamage(player, target, range, player.getEquipment().getWeaponId(), multiplier(), true);
		RangeCombatSwing.sendDamage(player, target, range, player.getEquipment().getWeaponId(), multiplier(), true);
		
		// sends the projectiles
		visualize(player, target);
		
		// drops the ammo
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS), false);
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS), false);
	}
	
	/**
	 * Visualizes the projectiles
	 *
	 * @param source
	 * 		The projectile from
	 * @param target
	 * 		The projectile to
	 */
	private void visualize(Player source, Entity target) {
		int speed = (int) (27.0D + source.getLocation().getDistance(target.getLocation()) * 5.0D);
		ProjectileManager.sendProjectile(new Projectile(source, target, 249, 41, 36, 20, speed, 15, 0));
		speed = (int) (20.0D + source.getLocation().getDistance(target.getLocation()) * 10.0D);
		ProjectileManager.sendProjectile(new Projectile(source, target, 249, 41, 36, 40, speed, 10, 0));
	}
}
