package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.rs.constant.Directions.Direction;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
@CommandManifest(description = "Stores an npc spawn into the region file", types = { Integer.class })
public class StoreNPCSpawnCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("n", "storespawn");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		// the id of the npc
		int npcId = intParam(args, 1);
		// the name of the direction
		String directionName = stringParamOrDefault(args, 2, "north").toUpperCase();
		// the direction we want to go to
		Optional<Direction> optional = Optional.of(Direction.valueOf(directionName));
		// store the entered detail
		NPCSpawnRepository.addSpawn(npcId, player.getLocation(), optional.get());
	}
}
