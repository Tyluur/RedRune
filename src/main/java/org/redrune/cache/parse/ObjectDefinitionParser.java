package org.redrune.cache.parse;

import org.redrune.cache.CacheConstants;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.cache.stream.RSByteArrayInputStream;
import org.redrune.cache.stream.RSInputStream;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public final class ObjectDefinitionParser {
	
	/**
	 * The map of all object definitions, with the key being the id of the object
	 */
	private static final ConcurrentHashMap<Integer, ObjectDefinition> OBJECT_DEFINITIONS = new ConcurrentHashMap<>();
	
	/**
	 * Gets an object definition by the id of the object
	 *
	 * @param objectId
	 * 		The id of the object
	 * @return An {@code ObjectDefinition} {@code Object}
	 */
	public static ObjectDefinition forId(int objectId) {
		ObjectDefinition objectDef = OBJECT_DEFINITIONS.get(objectId);
		if (objectDef != null) {
			return objectDef;
		}
		byte[] is = null;
		try {
			is = (CacheManager.getData(CacheConstants.OBJECTDEF_IDX_ID, objectId >>> 8, objectId & 0xff));
		} catch (Exception e) {
			System.out.println("Could not grab object " + objectId);
			e.printStackTrace();
		}
		objectDef = new ObjectDefinition();
		objectDef.setId(objectId);
		if (is != null) {
			try {
				objectDef.readValueLoop(new RSInputStream(new RSByteArrayInputStream(is)));
			} catch (IOException e) {
				System.out.println("Could not load object " + objectId);
				e.printStackTrace();
			}
		}
		objectDef.method3287();
		// bar, bank booth
		final String name = objectDef.getName().toLowerCase();
		
		// energy barrier
		if (objectId == 30141) {
			objectDef.setNotClipped(true);
		} else if (objectId == 11763 || (name.equalsIgnoreCase("bank booth") || name.equalsIgnoreCase("counter"))) {
			// falador bar & bank booths
			objectDef.setNotClipped(false);
			objectDef.setProjectileClipped(true);
			if (objectDef.getActionCount() == 0) {
				objectDef.setActionCount(1);
			}
		}
		// set flag data now
		if (objectDef.isNotClipped()) {
			objectDef.setProjectileClipped(false);
			objectDef.setActionCount(0);
		}
		OBJECT_DEFINITIONS.put(objectId, objectDef);
		return objectDef;
	}
	
}