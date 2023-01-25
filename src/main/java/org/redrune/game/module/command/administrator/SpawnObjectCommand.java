package org.redrune.game.module.command.administrator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
@CommandManifest(description = "Spawns an object", types = { Integer.class })
public class SpawnObjectCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("obj", "spawnobject");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int objectId = intParam(args, 1);
		int objectType = intParamOrDefault(args, 2, 10);
		int objectRotation = intParamOrDefault(args, 3, 0);
		player.getRegion().spawnObject(new GameObject(objectId, objectType, objectRotation, player.getLocation()));
	}
}
