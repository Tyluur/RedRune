package org.redrune.game.module.command.owner;

import org.redrune.cache.CacheFileStore;
import org.redrune.cache.parse.ObjectDefinitionParser;
import org.redrune.cache.parse.definition.ObjectDefinition;
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
@CommandManifest(description = "Finds an object by the entered name", types = { String.class })
public class FindObjectByNameCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("findobject", "objn");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		// the identifier to search by
		final String identifier = args[1].replaceAll("_", " ");
		// the option flag
		final String optionFlag = stringParamOrDefault(args, 2, null);
		// the list of items that were found
		List<String> found = new ArrayList<>();
		for (int objectId = 0; objectId < CacheFileStore.getObjectDefinitionsSize(); objectId++) {
			// the definition instance
			ObjectDefinition definition = ObjectDefinitionParser.forId(objectId);
			if (definition == null) {
				System.out.println("NPC #" + objectId + " had no definitions.");
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
			found.add("[<col=FF0000>" + objectId + "</col>] <col=" + ColorConstants.LIGHT_BLUE + ">" + definition.getName() + "</col> options=" + Arrays.toString(definition.getOptions()) + " sizeX=[" + definition.getSizeX() + "], sizeY=[" + definition.getSizeY() + "]");
		}
		// shows the entries found
		found.forEach(entry -> player.getTransmitter().sendConsoleMessage(entry));
		// sends a response with the amount of entries found
		sendResponse(player, "Found " + found.size() + " objects by name '" + identifier + "'.", console);
	}
}
