package org.redrune.cache.parse;

import com.google.common.base.Stopwatch;
import org.redrune.cache.CacheFileStore;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.utility.tool.Misc;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class ItemDefinitionParser {
	
	/**
	 * The map of definitions
	 */
	private static final ConcurrentHashMap<Integer, ItemDefinition> ITEM_DEFINITIONS = new ConcurrentHashMap<>();
	
	/**
	 * The text in the equip data file, cached for ease of access
	 */
	private static final Map<Integer, Integer[]> EQUIPMENT_DATA = new ConcurrentHashMap<>();
	
	/**
	 * The location of the file with equip data
	 */
	private static final String EQUIP_DATA_FILE = "./data/repository/item/equipment_data.txt";
	
	/**
	 * Loads all equip ids, slots, and equipment types
	 */
	public static void loadEquipmentConfiguration() {
		Stopwatch watch = Stopwatch.createStarted();
		if (EQUIPMENT_DATA.isEmpty()) {
			cacheData();
		}
		int equipId = 0;
		for (int itemId = 0; itemId < CacheFileStore.getItemDefinitionsSize(); itemId++) {
			ItemDefinition def = forId(itemId);
			if (def.getMaleWornModelId1() >= 0 || def.getMaleWornModelId2() >= 0) {
				def.setEquipId(equipId++);
			}
			Integer[] equipmentData = EQUIPMENT_DATA.get(itemId);
			if (equipmentData == null) {
				continue;
			}
			def.setEquipSlot(equipmentData[0]);
			def.setEquipType(equipmentData[1]);
		}
		System.out.println("Successfully loaded " + EQUIPMENT_DATA.size() + " equipment data and all equipment slots in " + watch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
	}
	
	/**
	 * Caches all the equipment data
	 */
	private static void cacheData() {
		for (String txt : Misc.getFileText(EQUIP_DATA_FILE)) {
			txt = txt.trim().toLowerCase();
			if (txt.startsWith("/")) {
				continue;
			}
			String[] stringSplit = txt.split(":");
			String[] digitSplit = stringSplit[1].split(",");
			Integer slot = Integer.parseInt(digitSplit[0]);
			Integer type = Integer.parseInt(digitSplit[1]);
			EQUIPMENT_DATA.put(Integer.parseInt(stringSplit[0]), new Integer[] { slot, type });
		}
	}
	
	/**
	 * Gets the definitions of an item by the id
	 *
	 * @param itemId
	 * 		The id of the item
	 * @return An {@code ItemDefinition} {@code Object}
	 */
	public static ItemDefinition forId(int itemId) {
		ItemDefinition definitions = ITEM_DEFINITIONS.get(itemId);
		if (definitions != null) {
			return definitions;
		} else {
			ItemDefinition def = new ItemDefinition(itemId);
			try {
				def.loadItemDefinition();
			} catch (IOException e) {
				System.out.println("Unable to parse item definitions, error:");
				e.printStackTrace();
			}
			ITEM_DEFINITIONS.put(itemId, def);
			return def;
		}
	}
	
	public static void loadEquipData() {
	
	}
	
}