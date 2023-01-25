package org.redrune.cache.parse;

import org.redrune.cache.parse.definition.CS2ScriptDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author `Discardedx2
 */
public final class CS2ScriptParser {
	
	/**
	 * The definitions mapping.
	 */
	private static final Map<Integer, CS2ScriptDefinition> DEFINITIONS = new HashMap<>();
	
	/**
	 * The client script setting definitions.
	 *
	 * @param id
	 * 		The client script id.
	 * @return The definitions.
	 */
	public static CS2ScriptDefinition forId(int id) {
		CS2ScriptDefinition def = DEFINITIONS.get(id);
		if (def != null) {
			return def;
		}
		try {
			def = def.load(id);
		} catch (IOException e) {
			System.out.println("Unable to parse CS2 definitions for id " + id);
			e.printStackTrace();
			return null;
		}
		DEFINITIONS.put(id, def);
		return def;
	}
	
}