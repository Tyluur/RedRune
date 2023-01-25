package org.redrune.game.module.command.server_moderator;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.ColorConstants;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
@CommandManifest(description = "Shows your location coordinates")
public class MyPositionCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("pos");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		boolean toClipboard = boolParamOrDefault(args, 1, false);
		StringBuilder builder = new StringBuilder();
		CopyOnWriteArrayList<Integer> mapRegionsIds = player.getMapRegionsIds();
		// writes the regions the player is in, coloring the current region red and other regions black
		for (int i = 0; i < mapRegionsIds.size(); i++) {
			int regionId = mapRegionsIds.get(i);
			boolean dominant = regionId == player.getRegion().getRegionId();
			builder.append(dominant ? "<col=" + ColorConstants.RED + ">" : "");
			builder.append(regionId);
			if (dominant) {
				builder.append("</col>");
			}
			builder.append(i == mapRegionsIds.size() - 1 ? "" : ", ");
		}
		player.getTransmitter().sendMessage("My Location=" + player.getLocation().toString() + ". Regions[" + builder.toString() + "]");
		
		if (toClipboard) {
			StringSelection stringSelection = new StringSelection("new Location(" + player.getLocation().getX() + ", " + player.getLocation().getY() + ", " + player.getLocation().getPlane() + ")");
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
		}
	}
}
