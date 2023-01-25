package org.redrune.core.task.impl;

import org.redrune.core.SequencialUpdate;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.utility.tool.ColorConstants;
import org.redrune.utility.tool.Misc;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class InformationTabTask extends ScheduledTask {
	
	/**
	 * The id of the information interface
	 */
	private static final int INTERFACE_ID = 930;
	
	/**
	 * The amount of players in the wilderness
	 */
	private int wildernessActivitySize = 0;
	
	public InformationTabTask() {
		super(5, -1);
	}
	
	@Override
	public void run() {
		// we set it with a variable so we don't have to loop through all players twice
		wildernessActivitySize = getWildernessActivitySize();
		// send it to all players
		for (Player player : SequencialUpdate.getRenderablePlayers()) {
			StringBuilder bldr = new StringBuilder();
			
			bldr.append("<col=FF0000>Server<br><br>");
			bldr.append("Online: <col=" + ColorConstants.WHITE + ">").append(World.get().getPlayerCount()).append("<br>");
			bldr.append("Wilderness: <col=" + ColorConstants.WHITE + ">").append(wildernessActivitySize).append("<br>");
			bldr.append("Uptime: <col=" + ColorConstants.WHITE + ">").append(Misc.convertMillisecondsToTime(World.get().getStopwatch().elapsed(TimeUnit.MILLISECONDS))).append("<br>");
			if (player.getDetails().isStaff()) {
				bldr.append("Lag: <col=" + ColorConstants.WHITE + ">").append(Misc.getLagPercentage(SystemManager.getUpdateWorker().getLastCycleTime())).append("%<br>");
			}
			bldr.append("<br><col=FF0000>Wilderness<br><br>");
			bldr.append("Kills: <col=" + ColorConstants.WHITE + ">").append(Misc.format(player.getVariables().getPlayersKilled())).append("<br>");
			bldr.append("Deaths: <col=" + ColorConstants.WHITE + ">").append(Misc.format(player.getVariables().getPlayerDeaths())).append("<br>");
			bldr.append("Streak: <col=" + ColorConstants.WHITE + ">").append(Misc.format(player.getVariables().getKillstreak())).append("<br>");
			bldr.append("KDR: <col=" + ColorConstants.WHITE + ">").append(Misc.round(player.getVariables().getPlayerDeaths() == 0 ? 0 : (double) player.getVariables().getPlayersKilled() / (double) player.getVariables().getPlayerDeaths(), 2)).append("<br>");
			
			player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, 16, bldr.toString());
		}
	}
	
	/**
	 * Gets the amount of people in the wilderness
	 */
	private int getWildernessActivitySize() {
		int size = 0;
		for (Player player : SequencialUpdate.getRenderablePlayers()) {
			if (PvPLocation.isAtWild(player.getLocation()) || PvPLocation.isAtWildSafe(player.getLocation())) {
				size++;
			}
		}
		return size;
	}
}
