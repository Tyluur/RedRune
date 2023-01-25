package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
@CommandManifest(description = "Lists the objects closeby", types = { Boolean.class, Integer.class })
public class ListObjectsCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("listobjs");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		boolean spawned = boolParam(args, 1);
		int radius = intParam(args, 2);
		List<GameObject> objectList = new ArrayList<>(spawned ? player.getRegion().getDefaultObjects() : player.getRegion().getSpawnedObjects());
		// removing those that aren't in our radius
		objectList = objectList.stream().filter(object -> object.getLocation().withinDistance(player.getLocation(), radius)).collect(Collectors.toList());
		// sort by distance from the player
		objectList.sort(Comparator.comparingInt(o -> o.getLocation().getDistance(player.getLocation())));
		// show them
		objectList.forEach(System.out::println);
		System.out.println(objectList.size() + " objects were found within a radius of " + radius + ".");
	}
}
