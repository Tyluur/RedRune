package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.RegionDeletion;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class StopObjectSpawnCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("stos");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		CopyOnWriteArrayList<GameObject> objectList = player.getRegion().getDefaultObjects();
		CopyOnWriteArraySet<GameObject> deletedList = player.getRegion().getDeletedObjects();
		
		Optional<GameObject> optional = objectList.stream().filter(object -> !deletedList.contains(object) && object.getLocation().equals(player.getLocation())).findFirst();
		if (!optional.isPresent()) {
			player.getTransmitter().sendMessage("There is no object left to remove.");
			return;
		}
		GameObject object = optional.get();
		
		player.getRegion().removeObject(object);
		RegionDeletion.dumpObject(object);
		deletedList.add(object);
		player.getTransmitter().sendMessage("Removed " + object.toString());
	}
	
}
