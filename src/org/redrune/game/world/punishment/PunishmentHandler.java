package org.redrune.game.world.punishment;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.PunishmentAdditionRequestPacketOut;
import org.redrune.network.master.client.packet.out.PunishmentRemovalRequestPacketOut;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.master.utility.rs.LoginConstants;
import org.redrune.utility.tool.Misc;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.redrune.game.world.punishment.PunishmentType.*;
import static org.redrune.utility.tool.ColorConstants.BLUE;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class PunishmentHandler {
	
	/**
	 * Adds a punishment
	 *
	 * @param player
	 * 		The player punishing
	 * @param name
	 * 		The punished users name
	 * @param type
	 * 		The type of punishment
	 * @param hours
	 * 		The duration of the punishment
	 */
	public static void addPunishment(Player player, String name, int hours, PunishmentType type) {
		// the time in horus for the punishment
		long time = hours == 0 ? Long.MAX_VALUE : System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hours);
		// the puishment instance
		Punishment punishment = new Punishment(player.getDetails().getUsername(), name, type, time);
		// writing the punishment to the servers
		MasterCommunication.write(new PunishmentAdditionRequestPacketOut(punishment));
		// sending the message
		String message = String.format("[<col=%s>Attempting to %s %s for%s</col>.]", BLUE, type.name().toLowerCase(), name, hours == 0 ? "ever" : " " + Misc.format(hours) + " hours");
		player.getTransmitter().sendMessage(message);
		System.out.println(message);
	}
	
	/**
	 * Requests the removal of a punishment
	 *
	 * @param player
	 * 		The player removing the punishment
	 * @param name
	 * 		The punished users name
	 * @param type
	 * 		The type of punishment
	 */
	public static void removePunishment(Player player, String name, PunishmentType type) {
		/*
			1. send removal attempt to master server
			2. masster server sends attempt to all worlds
			3. if the worlds can find a player by that name they punish them
			4. on punish, we send back the success to the master server
			5. the success is then delivered back to the world it came from
		 */
		
		// writing the removal request
		MasterCommunication.write(new PunishmentRemovalRequestPacketOut(new Punishment(player.getDetails().getUsername(), name, type, 0)));
		
		// the message for a request
		String message = String.format("[<col=%s>Attempting to remove punishment '%s' from %s</col>.]", BLUE, type.name().toLowerCase(), name);
		player.getTransmitter().sendMessage(message);
		System.out.println(message);
	}
	
	/**
	 * Gets the message for the punishment
	 *
	 * @param punishment
	 * 		The punishment
	 * @param add
	 * 		If the punishment was added or removed
	 * @param success
	 * 		if the punishment was successful or failed
	 */
	public static String getMessage(Punishment punishment, boolean add, boolean success) {
		return "[" + (success ? "Successfully" : "Failed to") + " " + (add ? "add" + (!success ? "" : "ed") + "" : "remove" + (!success ? "" : "d")) + " punishment '" + punishment.getType().name() + "' to '" + punishment.getPunished() + "'.]";
	}
	
	/**
	 * Handles the addition of a punishment
	 *
	 * @param punishment
	 * 		The punishment to ad
	 * @return {@code True} If we could add a punishment, meaning there was a player in the world by that name.
	 */
	public static boolean addPunishment(Punishment punishment) {
		String punished = punishment.getPunished();
		Optional<Player> optional = World.get().getPlayerByUsername(punished);
		if (!optional.isPresent()) {
			return false;
		}
		Player player = optional.get();
		// make sure the player is renderable before we do this incase of saving issues
		if (!player.isRenderable() && !player.getSession().isInLobby()) {
			return false;
		}
		// as long as the punishment as added to the list successfully [ban/mute]
		return punishment.getType().add(World.get(), player, punishment);
	}
	
	/**
	 * Handles the deletion of a punishment
	 *
	 * @param punishment
	 * 		The punishment to delete
	 */
	public static boolean deletePunishment(Punishment punishment) {
		PunishmentType type = punishment.getType();
		String punished = punishment.getPunished();
		if (type == MUTE) {
			Optional<Player> optional = World.get().getPlayerByUsername(punished);
			if (!optional.isPresent()) {
				return false;
			}
			Player player = optional.get();
			// make sure the player is renderable before we do this incase of saving issues
			if (!player.isRenderable() && !player.getSession().isInLobby()) {
				return false;
			}
			// as long as the punishment was removed
			return type.remove(World.get(), player, punishment);
		} else if (type == BAN) {
			Optional<Player> optional = World.get().getPlayerByUsername(punished);
			// that player is online so its not possible they are banned
			if (optional.isPresent()) {
				return false;
			}
			// that player didn't exist
			if (!Utility.playerFileExists(punished)) {
				return false;
			}
			// the player file existed, so we use the text in it
			String fileText = Utility.getCollapsedText(LoginConstants.getLocation(punished));
			// the player from the file
			Player player = Misc.loadPlayer(fileText);
			// unable to load file
			if (player == null) {
				return false;
			}
			// as long as the punishment was removed
			boolean remove = type.remove(World.get(), player, punishment);
			System.out.println("Remove = " + remove);
			return remove;
		} else if (type == ADDRESS_BAN || type == ADDRESS_MUTE) {
			// TODO: address punishments
		}
		return false;
	}
	
}
