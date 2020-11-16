package org.redrune.game.module.command;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/30/2017
 */
public abstract class CommandModule {
	
	/**
	 * The identifiers of the string
	 */
	public abstract String[] identifiers();
	
	/**
	 * Handles the command
	 *
	 * @param player
	 * 		The player handling the command
	 */
	public abstract void handle(Player player, String[] args, boolean console);
	
	/**
	 * The right the player must have to use this command
	 */
	@Getter
	@Setter
	private PlayerRight rightRequired;
	
	/**
	 * The manifest of the command
	 */
	@Getter
	@Setter
	private CommandManifest manifest;
	
	/**
	 * Sends a message over to the player.
	 *
	 * @param player
	 * 		The player
	 * @param message
	 * 		The message
	 * @param console
	 * 		If the command was sent via the console
	 */
	public static void sendResponse(Player player, String message, boolean console) {
		if (console) {
			player.getTransmitter().sendConsoleMessage(message);
		} else {
			player.getTransmitter().sendMessage(message);
		}
	}
	
	/**
	 * If the command can be used in danger zones.
	 */
	// TODO usage of this when implemented
	public boolean canBeUsedInDangerZone() {
		return false;
	}
	
	/**
	 * If a command is only allowed to be used by the console.
	 */
	public boolean consoleUsageOnly() {
		return false;
	}
	
	/**
	 * Converts a varargs parameter to the String[] array
	 *
	 * @param varArgs
	 * 		The var args
	 */
	protected String[] arguments(String... varArgs) {
		return varArgs;
	}
	
	/**
	 * Gets an integer from the parameter
	 *
	 * @param args
	 * 		The parameters
	 * @param slot
	 * 		The slot
	 */
	protected int intParam(String[] args, int slot) {
		return Integer.parseInt(args[slot]);
	}
	
	/**
	 * Gets an integer parameter if it exists, otherwise we return the default type. This is used for commands with
	 * varags parameters
	 *
	 * @param args
	 * 		The parameters
	 * @param slot
	 * 		The slot
	 * @param defaultType
	 * 		The default return
	 */
	protected int intParamOrDefault(String[] args, int slot, int defaultType) {
		String param = Misc.getArrayEntry(args, slot);
		if (param == null) {
			return defaultType;
		} else {
			return Integer.parseInt(param);
		}
	}
	
	/**
	 * Gets a boolean from the parameter
	 *
	 * @param args
	 * 		The parameter
	 * @param slot
	 * 		The slot
	 */
	protected Boolean boolParam(String[] args, int slot) {
		return Boolean.parseBoolean(args[slot]);
	}
	
	/**
	 * Gets a boolean from the parameter
	 *
	 * @param args
	 * 		The parameter
	 * @param slot
	 * 		The slot
	 */
	protected Boolean boolParamOrDefault(String[] args, int slot, Boolean defaultType) {
		if (slot >= args.length || slot < 0) {
			return defaultType;
		}
		try {
			return Boolean.parseBoolean(args[slot]);
		} catch (Exception e) {
			e.printStackTrace();
			return defaultType;
		}
	}
	
	/**
	 * Gets a string from the parameters
	 *
	 * @param args
	 * 		The parameters
	 * @param slot
	 * 		The slot
	 */
	protected String stringParamOrDefault(String[] args, int slot, String defaultType) {
		String param = Misc.getArrayEntry(args, slot);
		if (param == null) {
			return defaultType;
		} else {
			return param;
		}
	}
	
	/**
	 * Gets a completed version of a string array
	 *
	 * @param array
	 * 		The array
	 * @param index
	 * 		The index to start at
	 */
	protected String getCompleted(String[] array, int index) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < array.length; i++) {
			if (i == array.length - 1 || array[i + 1].startsWith("+")) {
				return sb.append(array[i]).toString();
			}
			sb.append(array[i]).append(" ");
		}
		return "null";
	}
	
	/**
	 * Removes the underscores from the name and replaces them with spaces
	 *
	 * @param unformattedUsername
	 * 		The username
	 */
	protected String formattedUsername(String unformattedUsername) {
		return unformattedUsername.replaceAll("_", " ");
	}
}