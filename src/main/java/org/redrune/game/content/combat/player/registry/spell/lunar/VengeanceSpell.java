package org.redrune.game.content.combat.player.registry.spell.lunar;

import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.combat.player.registry.wrapper.context.MagicSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.RegularSpellEvent;
import org.redrune.game.node.entity.player.Player;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class VengeanceSpell implements RegularSpellEvent {
	
	@Override
	public int spellId() {
		return 37;
	}
	
	@Override
	public double exp() {
		return 112;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.LUNARS;
	}
	
	@Override
	public void cast(Player player, MagicSpellContext context) {
		Long lastTimeCast = player.getAttribute("LAST_VENG", -1L);
		if (player.getSkills().getLevel(MAGIC) < 94) {
			player.getTransmitter().sendMessage("Your Magic level is not high enough for this spell.");
			return;
		} else if (player.getSkills().getLevel(DEFENCE) < 40) {
			player.getTransmitter().sendMessage("You need a Defence level of 40 for this spell");
			return;
		} else if (lastTimeCast != null && lastTimeCast + 30_000 > System.currentTimeMillis()) {
			player.getTransmitter().sendMessage("You must wait " + (TimeUnit.MILLISECONDS.toSeconds((lastTimeCast + 30_000) - System.currentTimeMillis())) + " more seconds to cast vengeance.");
			return;
		} else if (player.getAttribute("cast_veng", false)) {
			player.getTransmitter().sendMessage("You already have vengeance cast.");
			return;
		} else if (!CombatRegistry.checkRunes(player, true, ASTRAL_RUNE, 4, DEATH_RUNE, 2, EARTH_RUNE, 10)) {
			return;
		}
		
		// visual effects
		player.sendGraphics(726, 100, 0);
		player.sendAnimation(4410);
		player.getSkills().addExperienceWithMultiplier(MAGIC, exp());
		
		// storing attributes
		player.putAttribute("cast_veng", true);
		player.putAttribute("LAST_VENG", System.currentTimeMillis());
	}
}
