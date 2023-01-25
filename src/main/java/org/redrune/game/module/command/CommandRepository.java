package org.redrune.game.module.command;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;
import org.redrune.utility.tool.Misc;

import java.util.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/30/2017
 */
public class CommandRepository {
	
	/**
	 * The map of commands, the key being the flag used to find the command
	 */
	private static final Map<String, CommandModule> COMMAND_MODULES = new HashMap<>();
	
	/**
	 * Populates the {@link #COMMAND_MODULES}
	 */
	public static void populate(boolean reload) {
		if (reload) {
			COMMAND_MODULES.clear();
		}
		for (String directory : Misc.getSubDirectories(CommandRepository.class)) {
			Optional<PlayerRight> optional = PlayerRight.getRightByName(directory);
			if (!optional.isPresent()) {
				System.out.println("Unable to find right by directory '" + directory + "'.");
				continue;
			}
			Misc.getClassesInDirectory(CommandRepository.class.getPackage().getName() + "." + directory).stream().filter(CommandModule.class::isInstance).forEach(clazz -> {
				CommandModule command = (CommandModule) clazz;
				command.setRightRequired(optional.get());
				
				CommandManifest manifest = clazz.getClass().getAnnotation(CommandManifest.class);
				if (manifest != null) {
					command.setManifest(manifest);
				}
				for (String key : command.identifiers()) {
					COMMAND_MODULES.put(key, command);
				}
			});
		}
		System.out.println("Loaded " + COMMAND_MODULES.size() + " command modules.");
	}
	
	/**
	 * Processes the command entry
	 *
	 * @param player
	 * 		The player
	 * @param args
	 * 		The arguments they've supplied.
	 */
	public static void processEntry(Player player, String[] args, boolean console) {
		if (args.length == 0) {
			return;
		}
		String name = args[0];
		CommandModule command = COMMAND_MODULES.get(name);
		if (command == null) {
			CommandModule.sendResponse(player, "Could not find command by name '" + name + "' - try again...", console);
			return;
		}
		// verifying parameters
		final CommandManifest manifest = command.getManifest();
		
		if (manifest != null && manifest.types().length > 0) {
			final Class[] types = manifest.types();
			
			for (int i = 0; i < types.length; i++) {
				String argumentEntry = Misc.getArrayEntry(args, i + 1);
				Class typeExpected = types[i];
				Class typeEntered = argumentEntry == null ? null : Misc.getClassType(argumentEntry);
				
				if (!Objects.equals(typeExpected, typeEntered)) {
					//System.out.println("Expected " + typeExpected + ", entered " + typeEntered);
					sendUnexpectedType(player, name, manifest, console);
					return;
				}
				//System.out.println("typeExpected=" + typeExpected + ",typeEntered=" + typeEntered + ",argumentEntry=" + argumentEntry);
			}
		}
		if (command.consoleUsageOnly() && !console) {
			CommandModule.sendResponse(player, "Unexpected command entry type, please report this on forums.", false);
			return;
		}
		if (!command.getRightRequired().playerHasRights(player)) {
			CommandModule.sendResponse(player, "You do not have the rights to use this command.", console);
			return;
		}
		try {
			command.handle(player, args, console);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the player a message that they entered an unexpected parameter type
	 *
	 * @param player
	 * 		The player
	 * @param name
	 * 		The name of the command
	 * @param manifest
	 * 		The {@code CommandManifest} object
	 * @param console
	 * 		If the command was entered via console
	 */
	private static void sendUnexpectedType(Player player, String name, CommandManifest manifest, boolean console) {
		if (!console) {
			StringBuilder message = new StringBuilder("Command '" + name + "' usage -> " + "::" + "" + name + " ");
			for (Class clazz : manifest.types()) {
				message.append(Misc.getSimplifiedType(clazz.getSimpleName())).append(" ");
			}
			CommandModule.sendResponse(player, message.toString(), false);
		} else {
			List<String> messages = new ArrayList<>();
			messages.add("Invalid command parameters...");
			StringBuilder usageLine = new StringBuilder("---->Expected Usage: " + name + " ");
			for (Class clazz : manifest.types()) {
				usageLine.append(Misc.getSimplifiedType(clazz.getSimpleName())).append(" ");
			}
			messages.add(usageLine.toString());
			messages.forEach(message -> CommandModule.sendResponse(player, message, true));
		}
	}
	
	/**
	 * Gets the commands
	 */
	public static Collection<CommandModule> getCommands() {
		return COMMAND_MODULES.values();
	}
	
}