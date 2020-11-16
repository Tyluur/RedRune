package org.redrune.utility.backend;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Holds the mapdata XTeas.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class MapKeyRepository {
	
	/**
	 * The location of the folder with mapdata
	 */
	private static final String MAPDATA_FOLDER_LOCATION = "./data/repository/mapdata";
	
	/**
	 * The mapdata xteas.
	 */
	private static final Map<Integer, int[]> MAP_DATA_XTEAS = new HashMap<>();
	
	/**
	 * Initializes the mapdata.
	 */
	public static void readAll() {
		final File packedFile = new File(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin");
		boolean packed = false;
		if (!packedFile.exists()) {
			pack();
			packed = true;
		} else {
			load();
		}
		System.out.println("Loaded " + MAP_DATA_XTEAS.size() + " " + (packed ? "packed" : "unpacked") + " map keys.");
	}
	
	/**
	 * Packs the map data
	 */
	private static void pack() {
		try {
			final DataOutputStream out = new DataOutputStream(new FileOutputStream(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin"));
			final File unpacked = new File(MAPDATA_FOLDER_LOCATION + "/unpacked/");
			final File[] files = unpacked.listFiles();
			for (File region : files != null ? files : new File[0]) {
				final String name = region.getName();
				if (!name.contains(".txt")) {
					continue;
				}
				final int regionId = Integer.parseInt(name.replace(".txt", ""));
				BufferedReader in = new BufferedReader(new FileReader(region));
				out.writeShort(regionId);
				final int[] Key = new int[4];
				for (int j = 0; j < 4; j++) {
					Key[j] = Integer.parseInt(in.readLine());
					out.writeInt(Key[j]);
				}
				MAP_DATA_XTEAS.put(regionId, Key);
				in.close();
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Loads the mapdata.
	 */
	private static void load() {
		try {
			final DataInputStream in = new DataInputStream(new FileInputStream(MAPDATA_FOLDER_LOCATION + "/packedKeys.bin"));
			while (in.available() != 0) {
				final int area = in.readShort();
				final int[] parts = new int[4];
				for (int i = 0; i < 4; i++) {
					parts[i] = in.readInt();
				}
				MAP_DATA_XTEAS.put(area, parts);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Gets the mapdata xteas for the given key.
	 *
	 * @param regionId
	 * 		The region id.
	 * @return The mapdata xteas.
	 */
	public static int[] getKeys(int regionId) {
		return MAP_DATA_XTEAS.get(regionId);
	}
}
