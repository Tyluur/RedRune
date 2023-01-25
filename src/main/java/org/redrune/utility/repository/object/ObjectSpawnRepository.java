package org.redrune.utility.repository.object;

import com.google.gson.reflect.TypeToken;
import org.redrune.core.EngineWorkingSet;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.tool.GsonReadable;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class ObjectSpawnRepository implements GsonReadable<List<GameObject>> {
	
	/**
	 * The instance of this class, singleton loading.
	 */
	private static ObjectSpawnRepository singleton;
	
	/**
	 * The file with object spawn information
	 */
	private final String OBJECT_SPAWN_FILE_LOCATION = "./data/repository/object/object_spawns.json";
	
	/**
	 * The map of objects to spawn
	 */
	private final Map<Integer, List<GameObject>> OBJECT_SPAWNS = new HashMap<>();
	
	/**
	 * Gets the instance of the class
	 */
	public static ObjectSpawnRepository get() {
		if (singleton == null) {
			singleton = new ObjectSpawnRepository();
		}
		return singleton;
	}
	
	/**
	 * Loads all game objects to spawn from the file.
	 */
	public void loadAll() {
		List<GameObject> objectList = load(new File(OBJECT_SPAWN_FILE_LOCATION));
		if (objectList == null) {
			System.out.println("Unable to parse game objects to spawn.");
			return;
		}
		// looping through the object list to add objects by region id
		for (GameObject object : objectList) {
			int regionId = object.getLocation().getRegionId();
			
			List<GameObject> spawns = OBJECT_SPAWNS.get(regionId);
			if (spawns == null) {
				spawns = new ArrayList<>();
			}
			spawns.add(object);
			// adding the object list by region id
			OBJECT_SPAWNS.put(regionId, spawns);
		}
		System.out.println("Loaded " + OBJECT_SPAWNS.size() + " custom object spawns.");
	}
	
	@Override
	public List<GameObject> load(File file) {
		if (!file.exists()) {
			return null;
		}
		return Misc.getGSON().fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<List<GameObject>>() {
		}.getType());
	}
	
	/**
	 * Stores an object spawn
	 *
	 * @param object
	 * 		The object to spawn
	 */
	public void storeSpawn(GameObject object) {
		// we load from the file so we dont have to call the populate method again
		List<GameObject> objects = load(new File(OBJECT_SPAWN_FILE_LOCATION));
		if (objects == null) {
			objects = new ArrayList<>();
		}
		objects.add(object);
		// saves the newly populated list to file.
		save(new File(OBJECT_SPAWN_FILE_LOCATION), objects);
	}
	
	/**
	 * Loads the object spawns for a region
	 */
	public void loadObjectSpawns(int regionId) {
		List<GameObject> objects = OBJECT_SPAWNS.get(regionId);
		if (objects == null) {
			return;
		}
		// the region of the object
		Region region = RegionManager.getRegion(regionId);
		// spawning them on a separate worker
		EngineWorkingSet.submitLogic(() -> objects.forEach(region::spawnObject));
	}
}
