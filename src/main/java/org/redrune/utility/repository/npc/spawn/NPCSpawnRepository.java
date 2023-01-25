package org.redrune.utility.repository.npc.spawn;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.redrune.core.EngineWorkingSet;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.world.World;
import org.redrune.utility.rs.constant.Directions.Direction;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/30/2017
 */
public class NPCSpawnRepository {
	
	/**
	 * The location that data will be stored
	 */
	private static final String DATA_LOCATION = "./data/repository/npc/regions/";
	
	/**
	 * Loads all of the {@link NPCSpawn}s of the region into the world
	 *
	 * @param regionId
	 * 		The region to find the spawns of
	 */
	public static void loadSpawns(int regionId) {
		if (!regionSpawnsExist(regionId)) {
			return;
		}
		List<NPCSpawn> spawns = loadFromFile(regionId);
		if (spawns == null) {
			return;
		}
		EngineWorkingSet.submitLogic(() -> spawns.forEach(World.get()::addSpawn));
	}
	
	/**
	 * Checks if there are spawns for the region
	 *
	 * @param regionId
	 * 		The region id to check for
	 */
	private static boolean regionSpawnsExist(int regionId) {
		return new File(getFileLocation(regionId)).exists();
	}
	
	/**
	 * Loads and constructs a new {@code NPCSpawn} {@code List} from the {@link #getFileLocation(int)} for the region
	 * id
	 *
	 * @param regionId
	 * 		The id of the reigon
	 */
	private static List<NPCSpawn> loadFromFile(int regionId) {
		File file = new File(getFileLocation(regionId));
		if (!file.exists()) {
			return null;
		}
		return Misc.getGSON().fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<List<NPCSpawn>>() {
		}.getType());
	}
	
	/**
	 * Removes an npc from the spawn
	 *
	 * @param npc
	 * 		The npc to remove
	 */
	public static boolean removeNPC(NPC npc) {
		List<NPCSpawn> spawns = loadFromFile(npc.getRegion().getRegionId());
		if (spawns == null) {
			return false;
		}
		boolean removed = false;
		Iterator<NPCSpawn> it$ = spawns.iterator();
		while (it$.hasNext()) {
			NPCSpawn spawn = it$.next();
			if (spawn.getNpcId() == npc.getId() && spawn.getTile().equals(npc.getSpawnLocation())) {
				it$.remove();
				removed = true;
			}
		}
		if (removed) {
			saveData(npc.getRegion().getRegionId(), spawns);
			System.out.println("Removed npc and saved file!\t" + npc);
		}
		return removed;
	}
	
	/**
	 * Adds a spawn to the list of spawns and saves it
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param location
	 * 		The tile of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public static void addSpawn(int npcId, Location location, Direction direction) {
		List<NPCSpawn> spawns = loadFromFile(location.getRegionId());
		if (spawns == null) {
			spawns = new ArrayList<>();
		}
		spawns.add(new NPCSpawn(npcId, location, direction));
		saveData(location.getRegionId(), spawns);
		World.get().addNPC(npcId, location, direction);
		System.out.println("Spawned " + npcId + " on " + location + " facing " + direction + " at region " + location.getRegionId());
	}
	
	/**
	 * @param regionId
	 * 		The id of the region
	 */
	private static String getFileLocation(int regionId) {
		return DATA_LOCATION + regionId + ".json";
	}
	
	/**
	 * Saves the data to a file
	 *
	 * @param regionId
	 * 		The region of the spawns
	 * @param spawns
	 * 		The spawn data to write
	 */
	public static void saveData(int regionId, List<NPCSpawn> spawns) {
		try (Writer writer = new FileWriter(getFileLocation(regionId))) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
			Gson gson = builder.create();
			gson.toJson(spawns, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
