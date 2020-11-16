package org.redrune.game.content.activity.impl.pvp;

import org.redrune.game.GameConstants;
import org.redrune.game.content.activity.Activity;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.HeadIcons.SkullIcon;
import org.redrune.utility.tool.Misc;

import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class PvPAreaActivity extends Activity {
	
	/**
	 * The wilderness level in pvp worlds
	 */
	private static final int WILDERNESS_LEVEL = 20;
	
	/**
	 * The time we will be considered safe at
	 */
	private long timeAtSafe = -1;
	
	/**
	 * If we are counting down from leaving a pvp zone while in combat
	 */
	private boolean countingDown = false;
	
	/**
	 * If we landed in a safe area
	 */
	private boolean arrivedSafely = false;
	
	@Override
	public void start() {
		if (player.getWorld() != GameConstants.PVP_WORLD_ID) {
			end();
			return;
		}
		checkLocations();
		updateDangerousLevels(player, WILDERNESS_LEVEL);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	@Override
	public void updateLocation() {
		checkLocations();
	}
	
	@Override
	public void tick() {
		if (timeAtSafe > System.currentTimeMillis()) {
			sendSafeTimeLeft(player, (int) TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis()));
		}
		if (countingDown && timeAtSafe < System.currentTimeMillis()) {
			boolean insidePvpArea = PvPLocation.isAtPvpLocation(player.getLocation());
			boolean insideSafeArea = PvPLocation.isAtSafeLocation(player.getLocation());
			
			if (!insidePvpArea || insideSafeArea) {
				removeIcon(true, true);
				toggleSafeIcon(player, PvPLocation.isAtSafeLocation(player.getLocation()));
				timeAtSafe = -1;
				countingDown = false;
				arrivedSafely = true;
			}
		}
	}
	
	@Override
	public void end() {
		super.end();
		removeIcon(true, false);
		player.setInFightArea(false);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	@Override
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		if (option == InteractionOption.ATTACK_OPTION) {
			if (player.getVariables().isInFightArea() && !target.getVariables().isInFightArea()) {
				player.getTransmitter().sendMessage("You can only attack players in a player-vs-player area.", false);
				return true;
			}
			return !wildernessLevelsVerified(target);
		} else {
			return false;
		}
	}
	
	@Override
	public boolean combatAcceptable(Entity target) {
		if (player.getCombatDefinitions().getSpellId() <= 0 && Misc.inCircle(new Location(3105, 3933, 0), target.getLocation(), 24)) {
			player.getTransmitter().sendMessage("You can only use magic in the arena.");
			return false;
		}
		if (target.isNPC()) {
			return true;
		}
		if (target.getAttackedBy() != player && player.getAttackedBy() != target) {
			player.setSkull(SkullIcon.DEFAULT, TimeUnit.MINUTES.toMillis(10));
		}
		return true;
	}
	
	@Override
	public boolean savesOnLogout() {
		return true;
	}
	
	/**
	 * Checks if the target's wilderness level is capable of fighting us
	 *
	 * @param target
	 * 		The target
	 */
	private boolean wildernessLevelsVerified(Player target) {
		if (!(Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= WILDERNESS_LEVEL && Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= WILDERNESS_LEVEL)) {
			player.getTransmitter().sendMessage("You must travel deeper into the wilderness to attack that player.");
			return false;
		}
		return true;
	}
	
	private void checkLocations() {
		boolean insidePvpArea = PvPLocation.isAtPvpLocation(player.getLocation());
		boolean insideSafeArea = PvPLocation.isAtSafeLocation(player.getLocation());
		boolean waitingForSafe = timeAtSafe > System.currentTimeMillis();
		
		if (insidePvpArea && !insideSafeArea) {
			if (waitingForSafe) {
				timeAtSafe = -1;
				countingDown = false;
				removeIcon(false, true);
			}
			arrivedSafely = false;
			player.setInFightArea(true);
			showSkull();
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		} else if (insideSafeArea) {
			if (!waitingForSafe) {
				if (inCombatRecently() && !arrivedSafely) {
					timeAtSafe = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10);
					countingDown = true;
					sendSafeTimeLeft(player, (int) TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis()));
				} else {
					removeIcon(true, true);
					toggleSafeIcon(player, PvPLocation.isAtSafeLocation(player.getLocation()));
				}
			} else {
				sendSafeTimeLeft(player, (int) TimeUnit.MILLISECONDS.toSeconds(timeAtSafe - System.currentTimeMillis()));
			}
		} else {
			player.setInFightArea(false);
			removeIcon(true, false);
			end();
		}
	}
	
	/**
	 * This method handles the removing of the on screen wilderness interfaces.
	 *
	 * @param force
	 * 		If we should force the removal of the interfaces and the text.
	 * @param skullOnly
	 * 		If we should only remove the skull on the screen, and leave the level text.
	 */
	public void removeIcon(boolean force, boolean skullOnly) {
		if (!force) {
			return;
		}
		player.setInFightArea(false);
		if (!skullOnly) {
			player.getManager().getInterfaces().sendInterfaceText(!player.getManager().getInterfaces().usingFixedMode() ? 746 : 548, !player.getManager().getInterfaces().usingFixedMode() ? 17 : 10, "");
			player.getManager().getInterfaces().sendInterfaceText(!player.getManager().getInterfaces().usingFixedMode() ? 746 : 548, !player.getManager().getInterfaces().usingFixedMode() ? 17 : 11, "");
		}
		// till safe
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 4, true);
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 5, true);
		player.getManager().getInterfaces().sendInterfaceText(745, 5, "");
		
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 6, true);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		player.getEquipment().refresh();
	}
	
	/**
	 * Shows the pvp area skulls
	 */
	public void showSkull() {
		updateDangerousLevels(player, WILDERNESS_LEVEL);
		// till safe
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 4, true);
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 5, true);
		player.getManager().getInterfaces().sendInterfaceText(745, 5, "");
		
		// dangerous skull
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 6, false);
	}
	
	/**
	 * Checks if the player was in combat recently
	 */
	private boolean inCombatRecently() {
		long lastTimeCombated = player.getAttribute("last_time_combatted", -1L);
		if (lastTimeCombated == -1) {
			return player.isUnderCombat();
		} else {
			final long toSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTimeCombated);
			return toSeconds < 10;
		}
	}
	
	/**
	 * Sends the amount of time left till we are safe
	 *
	 * @param player
	 * 		The player to send
	 * @param time
	 * 		The time
	 */
	public static void sendSafeTimeLeft(Player player, int time) {
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 6, true);
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 4, false);
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 5, false);
		player.getManager().getInterfaces().sendInterfaceText(745, 5, "" + time);
	}
	
	/**
	 * Toggles the safe icon
	 *
	 * @param player
	 * 		The player
	 * @param show
	 * 		If we should show it or not
	 */
	private static void toggleSafeIcon(Player player, boolean show) {
		if (show) {
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 4, true);
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 5, true);
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 6, true);
		} else {
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 4, false);
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 5, false);
			player.getManager().getInterfaces().sendInterfaceComponentChange(745, 6, false);
		}
		player.getManager().getInterfaces().sendInterfaceComponentChange(745, 3, !show);
	}
	
	/**
	 * Updates the levels that are dangerous for the player
	 *
	 * @param player
	 * 		The player to update it for
	 * @param wildernessLevel
	 * 		The level to modify by
	 */
	public static void updateDangerousLevels(Player player, int wildernessLevel) {
		int lowest = player.getSkills().getCombatLevel() - wildernessLevel < 3 ? 3 : player.getSkills().getCombatLevel() - wildernessLevel;
		int highest = player.getSkills().getCombatLevel() + wildernessLevel > 138 ? 138 : player.getSkills().getCombatLevel() + wildernessLevel;
		if (player.getManager().getInterfaces().usingFixedMode()) {
			player.getManager().getInterfaces().sendInterfaceText(548, 9, "" + lowest + " - " + highest);
			player.getManager().getInterfaces().sendInterfaceText(548, 10, player.getVariables().getFormattedEarningPotential());
		} else {
			player.getManager().getInterfaces().sendInterfaceText(746, 16, "" + lowest + " - " + highest + "<br>" + player.getVariables().getFormattedEarningPotential());
		}
		toggleSafeIcon(player, PvPLocation.isAtSafeLocation(player.getLocation()));
	}
}
