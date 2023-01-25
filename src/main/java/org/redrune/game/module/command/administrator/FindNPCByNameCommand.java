package org.redrune.game.module.command.administrator;

import org.redrune.cache.CacheFileStore;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.ColorConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/2/2017
 */
@CommandManifest(description = "Finds an npc by the name you enter", types = { String.class })
public class FindNPCByNameCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("findnpc", "nn");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		// the identifier to search by
		final String identifier = args[1].replaceAll("_", " ");
		// the option flag
		final String optionFlag = stringParamOrDefault(args, 2, null);
		// the list of items that were found
		List<String> found = new ArrayList<>();
		for (int npcId = 0; npcId < CacheFileStore.getNPCDefinitionsSize(); npcId++) {
			// the definition instance
			NPCDefinition definition = NPCDefinitionParser.forId(npcId);
			if (definition == null) {
				System.out.println("NPC #" + npcId + " had no definitions.");
				continue;
			}
			// the name of the item
			final String name = definition.getName().toLowerCase();
			boolean added = false;
			// the name has the identifier we want
			if (name.contains(identifier)) {
				// we only want to find items by the identifier
				if (optionFlag == null) {
					added = true;
				} else {
					// we found that the definition has the option we want
					if (definition.hasOption(optionFlag)) {
						added = true;
					}
				}
			}
			// the item was not added because it was not found by filter
			if (!added) {
				continue;
			}
			found.add("[<col=FF0000>" + npcId + "</col>] <col=" + ColorConstants.LIGHT_BLUE + ">" + definition.getName() + "</col> options=" + Arrays.toString(definition.getOptions()));
		}
		// shows the entries found
		found.forEach(entry -> player.getTransmitter().sendConsoleMessage(entry));
		// sends a response with the amount of entries found
		sendResponse(player, "Found " + found.size() + " npcs by name '" + identifier + "'.", console);
	}
}
