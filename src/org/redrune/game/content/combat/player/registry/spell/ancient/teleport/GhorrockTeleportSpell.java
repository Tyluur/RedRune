package org.redrune.game.content.combat.player.registry.spell.ancient.teleport;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class GhorrockTeleportSpell implements TeleportationSpellEvent {
	
	@Override
	public int levelRequired() {
		return 96;
	}
	
	@Override
	public int[] runesRequired() {
		return arguments(LAW_RUNE, 2, WATER_RUNE, 8);
	}
	
	@Override
	public Location destination() {
		return Location.create(2977, 3873, 0);
	}
	
	@Override
	public int spellId() {
		return 47;
	}
	
	@Override
	public double exp() {
		return 106;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.ANCIENTS;
	}
}
