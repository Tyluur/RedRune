package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class WatchtowerTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 58;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(EARTH_RUNE, 2, LAW_RUNE, 2);
	}
	
	@Override
	public Location destination() {
		return Location.create(2547, 3113, 2);
	}
	
	@Override
	public int spellId() {
		return 62;
	}
	
	@Override
	public double exp() {
		return 68;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
