package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.repository.object.ObjectSpawnRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
@CommandManifest(description = "Stores an object spawn to the file", types = { Integer.class })
public class StoreObjectSpawnCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("storeobj");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int objectId = intParam(args, 1);
		int objectType = intParamOrDefault(args, 2, 10);
		int objectRotation = intParamOrDefault(args, 3, 0);
		
		final GameObject object = new GameObject(objectId, objectType, objectRotation, player.getLocation());
		ObjectSpawnRepository.get().storeSpawn(object);
		player.getRegion().spawnObject(object);
		player.getTransmitter().sendMessage("Stored " + object + " successfully.");
	}
}
