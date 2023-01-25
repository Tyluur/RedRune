package org.redrune.cache.parse;

import org.redrune.cache.parse.definition.NPCDefinition;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public final class NPCDefinitionParser {
	
	/**
	 * The cached map of npc definitions, the key being the npc id
	 */
	private static final ConcurrentHashMap<Integer, NPCDefinition> npcDefinitions = new ConcurrentHashMap<>();
	
	/**
	 * Gets an npc definition by the id of the npc
	 *
	 * @param npcId
	 * 		The id of the npc
	 */
	public static NPCDefinition forId(int npcId) {
		NPCDefinition def = npcDefinitions.get(npcId);
		if (def != null) {
			return def;
		}
		try {
			def = NPCDefinition.readDefinitions(npcId);
		} catch (IOException e) {
			System.out.println("Unable to parse npc definitions (" + npcId + ")");
			e.printStackTrace();
			return null;
		}
		npcDefinitions.put(npcId, def);
		return def;
	}
	
	/**
	 * Clears all npc definitions
	 */
	public static void clearNPCDefinitions() {
		npcDefinitions.clear();
	}
	
}