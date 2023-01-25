package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.Directions.Direction;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Spawns an npc by its id", types = { Integer.class })
public class SpawnNPCCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("npc");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		NPC npc = World.get().addNPC(intParam(args, 1), player.getLocation(), Direction.NORTH);
		npc.setRespawnable(false);
	}
}
