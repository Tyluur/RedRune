package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class FaladorTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 37;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(WATER_RUNE, 1, AIR_RUNE, 3, LAW_RUNE, 1);
	}
	
	@Override
	public Location destination() {
		return new Location(2964, 3379, 0);
	}
	
	@Override
	public int spellId() {
		return 46;
	}
	
	@Override
	public double exp() {
		return 48;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
