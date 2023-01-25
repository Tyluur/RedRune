package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandModule;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.world.World;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
@CommandManifest(description = "Adds a hit to yourself", types = { Integer.class })
public class HitCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("hit", "dmg");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		String target = Misc.getArrayEntry(args, 2);
		if (target == null) {
			player.getHitMap().applyHit(new Hit(player, intParam(args, 1), HitSplat.MELEE_DAMAGE));
		} else {
			Optional<Player> o = World.get().getPlayerByUsername(target);
			if (!o.isPresent()) {
				player.getTransmitter().sendMessage("Unable to find player by name '" + target + "'");
				return;
			}
			o.get().getHitMap().applyHit(new Hit(player, intParam(args, 1)));
		}
	}
}
