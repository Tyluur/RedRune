package org.redrune.game.content.combat.player.registry.spell.modern.strike;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
public class EarthStrikeEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 14221;
	}
	
	@Override
	public int hitGfx() {
		return 2723;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 60;
	}
	
	@Override
	public int spellId() {
		return 30;
	}
	
	@Override
	public double exp() {
		return 9.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		player.sendGraphics(2713);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 2718, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this);
	}
}
