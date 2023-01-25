package org.redrune.game.content.combat.player.registry.special;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatTypeSwing;
import org.redrune.game.content.combat.player.swing.RangeCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerEquipment;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.game.content.combat.player.registry.wrapper.SpecialAttackEvent;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/9/2017
 */
public class DarkBowSpecial implements SpecialAttackEvent {
	
	@Override
	public String[] applicableNames() {
		return arguments("dark bow");
	}
	
	@Override
	public double multiplier() {
		return 1.50;
	}
	
	@Override
	public void fire(Player player, Entity target, CombatTypeSwing swing, int combatStyle) {
		// we should always be on this mode
		if (!(swing instanceof RangeCombatSwing)) {
			return;
		}
		final int arrowId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_ARROWS);
		final int weaponId = player.getEquipment().getWeaponId();
		final int style = player.getCombatDefinitions().getAttackStyle();
		
		// the range swing type
		RangeCombatSwing range = (RangeCombatSwing) swing;
		
		// animates
		player.sendAnimation(StaticCombatFormulae.getWeaponAttackEmote(weaponId, combatStyle));
		
		int damage;
		int damage2;
		int maxHit;
		
		boolean dragon;
		
		if (arrowId == 11212) {
			maxHit = (int) swing.getMaxHit(player, weaponId, style, multiplier());
			damage = swing.randomizeHit(maxHit, swing.getAttackBonus(player, weaponId, style, true), swing.getDefenceBonus(target, weaponId, style));
			damage2 = swing.randomizeHit(maxHit, swing.getAttackBonus(player, weaponId, style, true), swing.getDefenceBonus(target, weaponId, style));
			if (damage < 80) {
				damage = 80;
			}
			if (damage2 < 80) {
				damage2 = 80;
			}
			visualize(player, target, dragon = true);
		} else {
			maxHit = (int) swing.getMaxHit(player, weaponId, style, 1.3D);
			damage = swing.randomizeHit(maxHit, swing.getAttackBonus(player, weaponId, style, true), swing.getDefenceBonus(target, weaponId, style));
			damage2 = swing.randomizeHit(maxHit, swing.getAttackBonus(player, weaponId, style, true), swing.getDefenceBonus(target, weaponId, style));
			if (damage < 50) {
				damage = 50;
			}
			if (damage2 < 50) {
				damage2 = 50;
			}
			visualize(player, target, dragon = false);
		}
		final boolean dragonAmmo = dragon;
		int delay = ProjectileManager.getProjectileDelay(player, target);
		
		RangeCombatSwing.sendDamage(player, target, range, weaponId, style, delay - 1, maxHit, damage, () -> target.sendGraphics(dragonAmmo ? 1100 : 1103, 100, 0));
		RangeCombatSwing.sendDamage(player, target, range, weaponId, style, delay, maxHit, damage2, null);
		
		// drops the ammo
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, arrowId, false);
		range.dropAmmo(player, target.getLocation(), PlayerEquipment.SLOT_ARROWS, arrowId, false);
	}
	
	/**
	 * Visualizes the dark bow projectiles
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The target
	 * @param dragon
	 * 		If we should use the dragon projectile
	 */
	public void visualize(Player player, Entity target, boolean dragon) {
		int projectileId = dragon ? 1099 : 1101;
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, target, projectileId, 40, 36, 46, 5, 0));
		int speed = ProjectileManager.getSpeedModifier(player, target);
		ProjectileManager.sendProjectile(new Projectile(player, target, projectileId, 40, 36, 51, speed + 10, 25, 0));
	}
}
