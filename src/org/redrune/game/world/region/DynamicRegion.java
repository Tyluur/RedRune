package org.redrune.game.world.region;

import com.alex.io.InputStream;
import org.redrune.cache.parse.ObjectDefinitionParser;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.game.node.Location;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.utility.rs.constant.RegionConstants;
import org.redrune.game.GameFlags;
import org.redrune.game.node.object.GameObject;
import org.redrune.cache.CacheFileStore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The class that denotes a dynamic region.
 *
 * @author Matrix Team
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/4/2017
 */
public class DynamicRegion extends Region {
	
	/**
	 * Contains render coordinates.
	 */
	private int[][][][] regionCoords;
	
	private boolean[][][] needsReload;
	
	private boolean recheckReload;
	
	DynamicRegion(int regionId) {
		super(regionId);
		// plane,x,y,(real x, real y,or real plane coord, or rotation)
		regionCoords = new int[4][8][8][4];
		needsReload = new boolean[4][8][8];
		for (int z = 0; z < 4; z++) {
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					needsReload[z][x][y] = true;
				}
			}
		}
		recheckReload = false;
	}
	
	@Override
	public void checkLoadMap() {
		if (recheckReload) {
			setLoadMapStage(0);
			recheckReload = false;
		}
		super.checkLoadMap();
	}
	
	@Override
	public void loadRegionMap() {
		for (int dynZ = 0; dynZ < 4; dynZ++) {
			for (int dynX = 0; dynX < 8; dynX++) {
				for (int dynY = 0; dynY < 8; dynY++) {
					if (!needsReload[dynZ][dynX][dynY]) {
						continue;
					}
					unloadChunk(dynX, dynY, dynZ);
				}
			}
		}
		for (int dynZ = 0; dynZ < 4; dynZ++) {
			for (int dynX = 0; dynX < 8; dynX++) {
				for (int dynY = 0; dynY < 8; dynY++) {
					if (!needsReload[dynZ][dynX][dynY]) {
						continue;
					}
					needsReload[dynZ][dynX][dynY] = false;
					translateChunks(dynZ, dynX, dynY);
				}
			}
		}
	}
	
	/**
	 * Translates the chunks into region data
	 */
	private void translateChunks(int dynZ, int dynX, int dynY) {
		int renderChunkX = regionCoords[dynZ][dynX][dynY][0];
		int renderChunkY = regionCoords[dynZ][dynX][dynY][1];
		int renderChunkZ = regionCoords[dynZ][dynX][dynY][2];
		int rotation = regionCoords[dynZ][dynX][dynY][3];
		int renderLocalChunkX = renderChunkX - ((renderChunkX >> 3) << 3);
		int renderLocalChunkY = renderChunkY - ((renderChunkY >> 3) << 3);
		
		if (renderChunkX == 0 && renderChunkY == 0 && renderChunkZ == 0 && rotation == 0) {
			System.err.println("0's");
			return;
		}
		
		int mapID = (renderChunkX >> 3) << 8 | (renderChunkY >> 3);
		final int[] keys = MapKeyRepository.getKeys(mapID);
		int landArchiveId = CacheFileStore.STORE.getIndexes()[5].getArchiveId("l" + (mapID >> 8) + "_" + (mapID & 0xFF));
		int mapArchiveId = CacheFileStore.STORE.getIndexes()[5].getArchiveId("m" + (mapID >> 8) + "_" + (mapID & 0xFF));
		byte[] mapContainerData = mapArchiveId == -1 ? null : CacheFileStore.STORE.getIndexes()[5].getFile(mapArchiveId, 0);
		byte[] landContainerData = landArchiveId == -1 ? null : CacheFileStore.STORE.getIndexes()[5].getFile(landArchiveId, 0, keys);
		byte[][][] mapSettings = mapContainerData == null ? null : new byte[4][64][64];
		
		
		if (regionId == 9551) {
			System.out.println("regionId=" + regionId);
			System.out.println("\tlandArchiveId=" + landArchiveId + ", mapArchiveId=" + mapArchiveId);
			System.out.println("\tmapContainerData=" + Arrays.toString(mapContainerData));
			System.out.println("\tlandContainerData=" + Arrays.toString(landContainerData));
		}
		
		// writing masks
		translateMasks(dynZ, dynX, dynY, renderChunkZ, rotation, renderLocalChunkX, renderLocalChunkY, mapContainerData, mapSettings);
		// adding objects
		if (landContainerData != null) {
			translateObjects(dynZ, dynX, dynY, renderChunkZ, rotation, renderLocalChunkX, renderLocalChunkY, landContainerData, mapSettings);
		}
		
		loadedFlags[RegionConstants.LOADED_NPCS_FLAG] = true;
		
		// debug
		if (mapID == 9511 || GameFlags.debugMode && landContainerData == null && landArchiveId != -1 && keys != null) {
			System.out.println("Unable to generate dynamic region # " + regionId + " [" + Arrays.toString(keys) + "]");
			System.out.println("\tmapContainerData=" + Arrays.toString(mapContainerData));
			System.out.println("\tlandContainerData=" + Arrays.toString(landContainerData));
		}
	}
	
	/**
	 * Translates the packed object data into game-usable data.
	 */
	private void translateObjects(int dynZ, int dynX, int dynY, int renderChunkZ, int rotation, int renderLocalChunkX, int renderLocalChunkY, byte[] landContainerData, byte[][][] mapSettings) {
		InputStream landStream = new InputStream(landContainerData);
		int objectId = -1;
		int addition;
		while ((addition = landStream.readSmart2()) != 0) {
			objectId += addition;
			int location = 0;
			int secondaryAddition;
			while ((secondaryAddition = landStream.readUnsignedSmart()) != 0) {
				location += secondaryAddition - 1;
				int x = (location >> 6 & 0x3f);
				int y = (location & 0x3f);
				int z = location >> 12;
				int objectData = landStream.readUnsignedByte();
				int type = objectData >> 2;
				int rot = objectData & 0x3;
				int realZ = z;
				if (mapSettings != null && (mapSettings[1][x][y] & 2) == 2) {
					realZ--;
				}
				if (realZ == renderChunkZ && (x >> 3) == renderLocalChunkX && (y >> 3) == renderLocalChunkY) {
					ObjectDefinition definition = ObjectDefinitionParser.forId(objectId);
					int[] translate = translate(x & 0x7, y & 0x7, rotation, definition.getSizeX(), definition.getSizeY(), rot);
					spawnObject(new GameObject(objectId, type, (rotation + rot) & 0x3, Location.create((dynX << 3) + translate[0] + ((getRegionId() >> 8) << 6), (dynY << 3) + translate[1] + ((getRegionId() & 0xFF) << 6), dynZ)), (dynX << 3) + translate[0], (dynY << 3) + translate[1], true);
				}
			}
		}
		loadedFlags[RegionConstants.LOADED_OBJECTS_FLAG] = true;
	}
	
	/**
	 * Translates packed mask data into game usable mask data.
	 */
	private void translateMasks(int dynZ, int dynX, int dynY, int renderChunkZ, int rotation, int renderLocalChunkX, int renderLocalChunkY, byte[] mapContainerData, byte[][][] mapSettings) {
		if (mapContainerData != null) {
			InputStream mapStream = new InputStream(mapContainerData);
			for (int plane = 0; plane < 4; plane++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						while (true) {
							int value = mapStream.readUnsignedByte();
							if (value == 0) {
								break;
							} else if (value == 1) {
								mapStream.readByte();
								break;
							} else if (value <= 49) {
								mapStream.readByte();
							} else if (value <= 81) {
								mapSettings[plane][x][y] = (byte) (value - 49);
							}
						}
					}
				}
			}
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						if ((mapSettings[z][x][y] & 0x1) == 1) {
							int realZ = z;
							if ((mapSettings[1][x][y] & 0x2) == 2) {
								realZ--;
							}
							if (realZ == renderChunkZ && (x >> 3) == renderLocalChunkX && (y >> 3) == renderLocalChunkY) {
								int[] translate = translate(x & 0x7, y & 0x7, rotation);
								forceGetRegionMap().addUnwalkable(dynZ, (dynX << 3) | translate[0], (dynY << 3) | translate[1]);
							}
						}
					}
				}
			}
		} else {
			for (int z = 0; z < 4; z++) {
				for (int x = 0; x < 64; x++) {
					for (int y = 0; y < 64; y++) {
						if (z == renderChunkZ && (x >> 3) == renderLocalChunkX && (y >> 3) == renderLocalChunkY) {
							int[] translate = translate(x & 0x7, y & 0x7, rotation);
							forceGetRegionMap().addUnwalkable(dynZ, (dynX << 3) | translate[0], (dynY << 3) | translate[1]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Unloads data from a chunk
	 *
	 * @param chunkX
	 * 		The chunk x
	 * @param chunkY
	 * 		The chunk y
	 * @param chunkZ
	 * 		The chunk z
	 */
	private void unloadChunk(int chunkX, int chunkY, int chunkZ) {
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				int fullX = (chunkX << 3) | x;
				int fullY = (chunkY << 3) | y;
				if (map != null) {
					map.setMask(chunkZ, fullX, fullY, 0);
				}
				if (clippedOnlyMap != null) {
					clippedOnlyMap.setMask(chunkZ, fullX, fullY, 0);
				}
				List<GameObject> removedList = new ArrayList<>(removedObjects);
				for (GameObject removed : removedList) {
					if (removed.getLocation().getPlane() == chunkZ && removed.getLocation().getRegionX() == chunkX && removed.getLocation().getRegionY() == chunkY) {
						removedObjects.remove(removed);
					}
				}
			}
		}
	}
	
	public static int[] translate(int x, int y, int rotation) {
		int[] coords = new int[2];
		if (rotation == 0) {
			coords[0] = x;
			coords[1] = y;
		} else if (rotation == 1) {
			coords[0] = y;
			coords[1] = 7 - x;
		} else if (rotation == 2) {
			coords[0] = 7 - x;
			coords[1] = 7 - y;
		} else {
			coords[0] = 7 - y;
			coords[1] = x;
		}
		return coords;
	}
	
	public static int[] translate(int x, int y, int mapRotation, int sizeX, int sizeY, int objectRotation) {
		int[] coords = new int[2];
		if ((objectRotation & 0x1) == 1) {
			int prevSizeX = sizeX;
			sizeX = sizeY;
			sizeY = prevSizeX;
		}
		if (mapRotation == 0) {
			coords[0] = x;
			coords[1] = y;
		} else if (mapRotation == 1) {
			coords[0] = y;
			coords[1] = 7 - x - (sizeX - 1);
		} else if (mapRotation == 2) {
			coords[0] = 7 - x - (sizeX - 1);
			coords[1] = 7 - y - (sizeY - 1);
		} else if (mapRotation == 3) {
			coords[0] = 7 - y - (sizeY - 1);
			coords[1] = x;
		}
		return coords;
	}
	
	public void setReloadObjects(int plane, int x, int y) {
		needsReload[plane][x][y] = true;
		recheckReload = true;
	}
	
	public int[][][][] getRegionCoords() {
		return regionCoords;
	}
	
	@Override
	public boolean isDynamic() {
		return true;
	}
}
