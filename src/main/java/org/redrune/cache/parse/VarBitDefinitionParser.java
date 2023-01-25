package org.redrune.cache.parse;

import org.redrune.cache.CacheManager;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.definition.VarBitDefinition;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class VarBitDefinitionParser {
	
	/**
	 * The cached map of varbit definitions
	 */
	private static final ConcurrentHashMap<Integer, VarBitDefinition> VARPBIT_DEFS = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		Cache.init();
		VarBitDefinition definition = VarBitDefinitionParser.forId(173);
		System.out.println(definition.getStartBit() + ", " + definition.getEndBit() + ", " + definition.getBaseVar());
	}
	
	/**
	 * Gets the {@code VarBitDefinition} by an {@code Integer} id
	 *
	 * @param id
	 * 		The id of the varbit
	 */
	public static VarBitDefinition forId(int id) {
		VarBitDefinition script = VARPBIT_DEFS.get(id);
		if (script != null) {
			return script;
		}
		byte[] data = new byte[0];
		try {
			data = CacheManager.getData(22, id >>> 10, id & 0x3ff);
		} catch (IOException e) {
			System.out.println("Varbit " + id + " doesn't exist in the cache");
		}
		script = new VarBitDefinition();
		script.setId(id);
		if (data != null) {
			try {
				script.readValueLoop(ByteBuffer.wrap(data));
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Error while reading " + id);
			}
		}
		VARPBIT_DEFS.put(id, script);
		return script;
	}
	
}