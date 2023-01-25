package org.redrune.game.content.combat.player.registry.spell.modern.god;

import org.redrune.core.system.SystemManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class SaradominStrikeEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return 5;
	}
	
	@Override
	public int animationId() {
		return 811;
	}
	
	@Override
	public int hitGfx() {
		return 76;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		if (player.getVariables().getAttribute(AttributeKey.GOD_CHARGED, -1L) >= SystemManager.getUpdateWorker().getTicks()) {
			return 300;
		} else {
			return 200;
		}
	}
	
	@Override
	public int spellId() {
		return 66;
	}
	
	@Override
	public double exp() {
		return 34.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		context.getSwing().sendSpell(player, context.getTarget(), this);
	}
}
