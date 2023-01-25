package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class DareeyakTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 78;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, FIRE_RUNE, 3, AIR_RUNE, 2);
	}
	
	@Override
	public Location destination() {
		return Location.create(2990, 3696, 0);
	}
	
	@Override
	public int spellId() {
		return 44;
	}
	
	@Override
	public double exp() {
		return 88;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
