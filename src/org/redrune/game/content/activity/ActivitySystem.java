package org.redrune.game.content.activity;

import org.redrune.game.GameConstants;
import org.redrune.game.content.activity.impl.pvp.PvPAreaActivity;
import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.content.activity.impl.pvp.WildernessActivity;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class ActivitySystem {
	
	/**
	 * Fires a location update to the player's activity
	 *
	 * @param player
	 * 		The player
	 * @param location
	 * 		The location of the player, parameterized because we might have a new location before the actual location is
	 * 		updated [teleporting instance]
	 */
	public static void fireLocationUpdate(Player player, Location location) {
		fireAreaActivity(player, location);
		player.getManager().getActivities().getActivity().ifPresent(Activity::updateLocation);
	}
	
	/**
	 * Fires the area activity
	 *
	 * @param player
	 * 		The player
	 * @param location
	 * 		The location of the player, parameterized because we might have a new location before the actual location is
	 * 		updated [teleporting instance]
	 */
	public static void fireAreaActivity(Player player, Location location) {
		// we have an activity so we dont force another to start
		if (player.getManager().getActivities().getActivity().isPresent()) {
			return;
		}
		// starting the pvp activity if we're in a pvp world and a pvp location
		if (World.get().getId() == GameConstants.PVP_WORLD_ID) {
			if (PvPLocation.isAtPvpLocation(location)) {
				startActivity(player, new PvPAreaActivity());
			}
		} else {
			// first check is the wilderness activity
			if (PvPLocation.isAtWild(location)) {
				startActivity(player, new WildernessActivity());
			}
		}
	}
	
	/**
	 * Starts an activity and ends the previous one
	 *
	 * @param player
	 * 		The player
	 * @param activity
	 * 		The activity
	 */
	public static void startActivity(Player player, Activity activity) {
		player.getManager().getActivities().getActivity().ifPresent(Activity::end);
		player.getManager().getActivities().startActivity(activity);
	}
	
}
