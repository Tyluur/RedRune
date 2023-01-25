package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class LassarTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 72;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, WATER_RUNE, 4);
	}
	
	@Override
	public Location destination() {
		return Location.create(3006, 3471, 0);
	}
	
	@Override
	public int spellId() {
		return 43;
	}
	
	@Override
	public double exp() {
		return 82;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
