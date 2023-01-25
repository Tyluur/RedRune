package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class ApeAtollTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 64;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(FIRE_RUNE, 2, WATER_RUNE, 2, LAW_RUNE, 2, 1963, 1);
	}
	
	@Override
	public Location destination() {
		return Location.create(2797, 2793, 0);
	}
	
	@Override
	public int spellId() {
		return 72;
	}
	
	@Override
	public double exp() {
		return 76;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
