package org.redrune.utility.repository.item;

import com.google.gson.reflect.TypeToken;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public final class ItemRepository {
	
	/**
	 * The ids of all items with negative weights
	 */
	private static final int[] NEGATIVE_WEIGHT_ITEMS = { 88, 10553, 10069, 10071, 24210, 24208, 24206, 14936, 14938, 24560, 24561, 24562, 24563, 24564 };
	
	/**
	 * The location of the folder with item data
	 */
	private static final String ITEM_REPOSITORY_LOCATION = "./data/repository/item/data/";
	
	/**
	 * The location of the file with untradeable item data.
	 */
	private static final String UNTRADEABLE_ITEMS_LOCATION = "./data/repository/item/untradeables.txt";
	
	/**
	 * The list of items by name that are untradeable
	 */
	private static final List<String> UNTRADEABLES = new ArrayList<>();
	
	/**
	 * The untradeable cache.
	 */
	private static final Map<Integer, Boolean> UNTRADEABLE_CACHE = new HashMap<>();
	
	/**
	 * The map of all the data
	 */
	private static final Map<Integer, ItemData> DATA_MAP = new ConcurrentHashMap<>();
	
	public static void main(String[] args) {
		HashMap<Integer, Double> itemWeights = new HashMap<>();
		try {
			RandomAccessFile in = new RandomAccessFile("weights.bin", "r");
			FileChannel channel = in.getChannel();
			ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
			while (buffer.hasRemaining()) {
				itemWeights.put(buffer.getShort() & 0xffff, buffer.getDouble());
			}
			channel.close();
			in.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		for (Entry<Integer, Double> entry : itemWeights.entrySet()) {
			int itemId = entry.getKey();
			double weight = entry.getValue();
			ItemData data = getItemData(itemId);
			data.setWeight(weight);
			Misc.saveData(getFileById(itemId), data);
			System.out.println("Finished saving " + itemId);
		}
	}
	
	/**
	 * Gets the item data using caching to increase efficiency
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private static ItemData getItemData(int itemId) {
		ItemData data = DATA_MAP.get(itemId);
		boolean add = false;
		if (data == null) {
			data = loadFileData(itemId);
			add = true;
		}
		if (add) {
			DATA_MAP.put(itemId, data);
		}
		return data;
	}
	
	/**
	 * Gets the file of an item by its id
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private static File getFileById(int itemId) {
		return new File(ITEM_REPOSITORY_LOCATION + itemId + ".json");
	}
	
	/**
	 * Loads the file data
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private static ItemData loadFileData(int itemId) {
		File file = getFileById(itemId);
		if (!file.exists()) {
			return null;
		}
		return Misc.getGSON().fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<ItemData>() {
		}.getType());
	}
	
	/**
	 * Loads all item repository data that needs to be stored on startup [and cleared occasionally]
	 *
	 * @param reload
	 * 		If the item should be cleared [in the case of a reload]
	 */
	public static void initialize(boolean reload) {
		if (reload) {
			UNTRADEABLES.clear();
			UNTRADEABLE_CACHE.clear();
		}
		List<String> fileText = Misc.getFileText(UNTRADEABLE_ITEMS_LOCATION);
		UNTRADEABLES.addAll(fileText);
		System.out.println("Loaded " + UNTRADEABLES.size() + " untradeable items.");
	}
	
	/**
	 * Finds the bonuses of an item
	 *
	 * @param itemId
	 * 		The item
	 */
	public static int[] getBonuses(int itemId) {
		ItemData data = getItemData(itemId);
		return data == null ? null : data.getBonuses();
	}
	
	/**
	 * Gets the weight of an item
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param equipped
	 * 		If the item is equipped
	 */
	public static double getWeight(int itemId, boolean equipped) {
		ItemData data = getItemData(itemId);
		if (data == null) {
			return 0;
		}
		double weight = data.getWeight();
		if (equipped) {
			for (int negativeId : NEGATIVE_WEIGHT_ITEMS) {
				if (negativeId == itemId) {
					return -weight;
				}
			}
		}
		return data.getWeight();
	}
	
	/**
	 * Gets the examine of an item
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	public static String getExamine(int itemId) {
		ItemData data = getItemData(itemId);
		return data == null ? null : data.getExamine();
	}
	
	/**
	 * Checks if an item is untradeable by parsing through the list of {@link #UNTRADEABLES} and checking for the item's
	 * name, or id. This uses caching in order to skip looping through a large list every time.
	 *
	 * @param itemId
	 * 		The item
	 */
	public static boolean isUntradeable(int itemId) {
		// it was already cached so we just return the result
		if (UNTRADEABLE_CACHE.containsKey(itemId)) {
			return UNTRADEABLE_CACHE.get(itemId);
		} else {
			// we havent found if its untradeable yet
			// we do it then cache it.
			ItemDefinition definitions = ItemDefinitionParser.forId(itemId);
			boolean flagged = false;
			for (String listName : UNTRADEABLES) {
				// first we check that its not an item id
				if (!Misc.isDigit(listName)) {
					// matches! break loop.
					if (definitions.getName().equalsIgnoreCase(listName)) {
						flagged = true;
						break;
					}
				} else if (Misc.isNumeric(listName)) {
					// the list entry is an item id, lets check if it matches the id we're looking for
					int listId = Integer.parseInt(listName);
					// matches! break loop.
					if (itemId == listId) {
						flagged = true;
						break;
					}
				}
			}
			// adds the result to the cache
			UNTRADEABLE_CACHE.put(itemId, flagged);
			// returns if it was flagged by the untradeable list
			return flagged;
		}
	}
	
}
