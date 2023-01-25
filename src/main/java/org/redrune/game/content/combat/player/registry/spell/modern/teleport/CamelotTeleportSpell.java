package org.redrune.game.content.combat.player.registry.spell.modern.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class CamelotTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 45;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(AIR_RUNE, 5, LAW_RUNE, 1);
	}
	
	@Override
	public Location destination() {
		return Location.create(2757, 3478, 0);
	}
	
	@Override
	public int spellId() {
		return 51;
	}
	
	@Override
	public double exp() {
		return 55.5;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
}
