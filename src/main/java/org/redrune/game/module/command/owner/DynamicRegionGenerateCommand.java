package org.redrune.game.module.command.owner;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.RegionBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/4/2017
 */
@CommandManifest(description = "Generates a dynamic region")
public class DynamicRegionGenerateCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("gendyn");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		// 2398 5086
		int[] boundChunks = RegionBuilder.findEmptyChunkBound(8, 8);
		RegionBuilder.copyAllPlanesMap(302, 639, boundChunks[0], boundChunks[1], 64);
		RegionBuilder.copyAllPlanesMap(296, 632, boundChunks[0], boundChunks[1], 64);
//		RegionBuilder.copyAllPlanesMap(296, 632, boundChunks[0], boundChunks[1], 64);
		player.teleport(getWorldTile(boundChunks, intParamOrDefault(args, 1, 0), intParamOrDefault(args, 2, 0)));
	}
	
	/**
	 * Retrieves a new {@code WorldTile} using the boundChunks of the dynamic
	 * region.
	 *
	 * @param mapX
	 * 		The 'x' coordinate value.
	 * @param mapY
	 * 		The 'y' coordinate value.
	 * @return a new {@code WorldTile}
	 */
	private Location getWorldTile(int[] boundChunks, int mapX, int mapY) {
		return new Location(boundChunks[0] * 8 + mapX, boundChunks[1] * 8 + mapY, 0);
	}
	
}
