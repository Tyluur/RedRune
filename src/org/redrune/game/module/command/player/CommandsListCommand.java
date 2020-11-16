package org.redrune.game.module.command.player;

import org.redrune.utility.tool.Misc;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerRight;
import org.redrune.utility.rs.constant.InterfaceConstants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Lists the commands available.")
public class CommandsListCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("commands", "cmds");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		List<String> messages = new ArrayList<>();
		
		Set<CommandModule> commandModulesSet = new LinkedHashSet<>(CommandRepository.getCommands().stream().filter(commandModule -> commandModule.getRightRequired().playerHasRights(player)).collect(Collectors.toList()));
		
		List<CommandModule> commandModules = new ArrayList<>(commandModulesSet);
		
		commandModules.sort(Comparator.comparingInt(o -> o.getRightRequired().ordinal()));
		
		PlayerRight lastRight = null;
		
		for (CommandModule command : commandModules) {
			// skips commands that are only for console
			// or commands with no manifest [only i should know about these]
			if (command.consoleUsageOnly() || command.getManifest() == null) {
				continue;
			}
			CommandManifest manifest = command.getManifest();
			StringBuilder bldr = new StringBuilder();
			String[] identifiers = command.identifiers();
			
			// adds the identifiers of the command to the string
			for (int i = 0; i < identifiers.length; i++) {
				String identifier = identifiers[i];
				bldr.append(identifier).append(i == identifiers.length - 1 ? " -> " : ", ");
			}
			
			Class[] types = manifest == null ? null : manifest.types();
			
			if (types != null) {
				for (int i = 0; i < types.length; i++) {
					final String simpleName = Misc.getSimplifiedType(types[i].getSimpleName());
					boolean last = i == types.length - 1;
					if (i == 0) {
						bldr.append("[");
						bldr.append(simpleName).append("").append(last ? "" : ", ");
						if (i == types.length - 1) {
							bldr.append("]");
						}
					} else if (i == (types.length - 1)) {
						bldr.append(simpleName).append("").append(last ? "" : ", ");
						bldr.append("]");
					} else {
						bldr.append(types[i].getClass().getSimpleName());
					}
				}
				bldr.append(" ");
			}
			
			// how the command should be executed, with info from the manifest if it exists
			String message = "::" + bldr.toString() + (manifest == null ? "" : manifest.description().equals("") ? "" : manifest.description());
			
			// adds the message to the list, and adds right required formatting
			if (lastRight == null || lastRight != command.getRightRequired()) {
				if (lastRight != null) {
					messages.add("");
				}
				messages.add("[" + command.getRightRequired().name() + "]");
				
				lastRight = command.getRightRequired();
			}
			messages.add(message);
		}
		
		InterfaceConstants.sendQuestScroll(player, "Commands", messages.toArray(new String[messages.size()]));
	}
}
