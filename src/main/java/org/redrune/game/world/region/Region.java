package org.redrune.game.world.region;

import com.alex.io.InputStream;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheFileStore;
import org.redrune.cache.parse.definition.ObjectDefinition;
import org.redrune.core.EngineWorkingSet;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.core.task.impl.FloorItemTask;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.node.object.GameObject.ObjectType;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.outgoing.impl.FloorItemAdditionBuilder;
import org.redrune.network.world.packet.outgoing.impl.FloorItemRemovalBuilder;
import org.redrune.network.world.packet.outgoing.impl.ObjectAdditionBuilder;
import org.redrune.network.world.packet.outgoing.impl.ObjectRemovalBuilder;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.repository.object.ObjectSpawnRepository;
import org.redrune.utility.rs.constant.RegionConstants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Emperor
 * @author Dementhium development team (mainly).
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class Region {
	
	/**
	 * A list of players in this region.
	 */
	@Getter
	protected final CopyOnWriteArraySet<Player> players = new CopyOnWriteArraySet<>();
	
	/**
	 * A list of NPCs in this region.
	 */
	@Getter
	protected final CopyOnWriteArraySet<NPC> npcs = new CopyOnWriteArraySet<>();
	
	/**
	 * The map of ticks spent in the region, based on the player names as the key
	 */
	private final Map<String, Integer> playerExistanceTicks = new HashMap<>();
	
	/**
	 * The id of the region
	 */
	@Getter
	protected final int regionId;
	
	/**
	 * If all the spawns have been loaded.
	 */
	@Getter
	protected final boolean[] loadedFlags = new boolean[2];
	
	/**
	 * The list of floor items
	 */
	@Getter
	protected final CopyOnWriteArrayList<FloorItem> floorItems = new CopyOnWriteArrayList<>();
	
	/**
	 * A list of game defaultObjects on this region.
	 */
	@Getter
	protected final CopyOnWriteArrayList<GameObject> defaultObjects = new CopyOnWriteArrayList<>();
	
	/**
	 * The list of objects that have been removed from the region.
	 */
	@Getter
	protected final CopyOnWriteArraySet<GameObject> removedObjects = new CopyOnWriteArraySet<>();
	
	/**
	 * The list of objects that have been spawned in the region
	 */
	@Getter
	protected final CopyOnWriteArraySet<GameObject> spawnedObjects = new CopyOnWriteArraySet<>();
	
	/**
	 * The list of objects that were deleted (these will never be spawned)
	 */
	@Getter
	protected final CopyOnWriteArraySet<GameObject> deletedObjects;
	
	/**
	 * The map of the region
	 */
	@Getter
	protected RegionMap map;
	
	/**
	 * The clipped only map
	 */
	protected RegionMap clippedOnlyMap;
	
	/**
	 * The map stage
	 */
	@Getter
	@Setter
	protected volatile int loadMapStage;
	
	/**
	 * Constructs a new {@code Region} {@code Object}.
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	public Region(int regionId) {
		this.regionId = regionId;
		this.deletedObjects = RegionManager.findDeletedObjects(this);
	}
	
	@Override
	public String toString() {
		return "[id=" + regionId + ", players=" + players + ", npcs=" + npcs + "]";
	}
	
	/**
	 * Checks the region load map
	 */
	void checkLoadMap() {
		if (getLoadMapStage() == 0) {
			setLoadMapStage(1);
			EngineWorkingSet.submitEngineWork(() -> {
				try {
					loadRegionMap();
					setLoadMapStage(2);
					if (!loadedFlags[RegionConstants.LOADED_OBJECTS_FLAG]) {
						checkObjectSpawns();
						loadedFlags[RegionConstants.LOADED_OBJECTS_FLAG] = true;
					}
					if (!loadedFlags[RegionConstants.LOADED_NPCS_FLAG]) {
						loadNPCSpawns();
						loadedFlags[RegionConstants.LOADED_NPCS_FLAG] = true;
					}
				} catch (Throwable e) {
					e.printStackTrace();
				}
			});
		}
	}
	
	/**
	 * Loads the region map data from the cache.
	 */
	public void loadRegionMap() {
		int regionX = regionId >> 8;
		int regionY = regionId & 0xff;
		int baseX = regionX << 6;
		int baseY = regionY << 6;
		int landArchiveId = 0;
		byte[] landContainerData = new byte[0];
		try {
			landArchiveId = CacheFileStore.STORE.getIndexes()[5].getArchiveId("l" + regionX + "_" + regionY);
			landContainerData = landArchiveId == -1 ? null : CacheFileStore.STORE.getIndexes()[5].getFile(landArchiveId, 0, MapKeyRepository.getKeys(regionId));
			int mapArchiveId = CacheFileStore.STORE.getIndexes()[5].getArchiveId("m" + (regionX + "_" + regionY));
			byte[] mapContainerData = mapArchiveId == -1 ? null : CacheFileStore.STORE.getIndexes()[5].getFile(mapArchiveId, 0);
			byte[][][] mapSettings = mapContainerData == null ? null : new byte[4][64][64];
			if (mapContainerData != null) {
				InputStream mapStream = new InputStream(mapContainerData);
				for (int plane = 0; plane < 4; plane++) {
					for (int x = 0; x < 64; x++) {
						for (int y = 0; y < 64; y++) {
							while (true) {
								int value = mapStream.readByte() & 0xff;
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
				// floor textures (water/lava)
				for (int plane = 0; plane < 4; plane++) {
					for (int x = 0; x < 64; x++) {
						for (int y = 0; y < 64; y++) {
							if ((mapSettings[plane][x][y] & 1) == 1) {
								int height = plane;
								if ((mapSettings[1][x][y] & 2) == 2) {
									height--;
								}
								if (height >= 0 && height <= 3) {
									forceGetRegionMap().addUnwalkable(height, x, y);
								}
							}
						}
					}
				}
			}
			if (landContainerData != null) {
				InputStream landStream = new InputStream(landContainerData);
				int objectId = -1;
				int incr;
				while ((incr = landStream.readSmart2()) != 0) {
					objectId += incr;
					int location = 0;
					int incr2;
					while ((incr2 = landStream.readUnsignedSmart()) != 0) {
						location += incr2 - 1;
						int localX = (location >> 6 & 0x3f);
						int localY = (location & 0x3f);
						int plane = location >> 12;
						int objectData = landStream.readUnsignedByte();
						int type = objectData >> 2;
						int rotation = objectData & 0x3;
						if (localX < 0 || localX >= 64 || localY < 0 || localY >= 64) {
							continue;
						}
						int objectPlane = plane;
						if (mapSettings != null && (mapSettings[1][localX][localY] & 2) == 2) {
							objectPlane--;
						}
						if (objectPlane < 0 || objectPlane >= 4 || plane < 0 || plane >= 4) {
							continue;
						}
						spawnObject(new GameObject(objectId, type, rotation, Location.create(localX + baseX, localY + baseY, objectPlane)), localX, localY, true);
					}
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		if (landContainerData == null && landArchiveId != -1 && MapKeyRepository.getKeys(regionId) != null) {
			System.out.println("Missing xteas for region " + regionId + ".");
		}
	}
	
	/**
	 * Checks the object spawns
	 */
	private void checkObjectSpawns() {
		ObjectSpawnRepository.get().loadObjectSpawns(regionId);
	}
	
	/**
	 * Checks for all spawns to be done
	 */
	private void loadNPCSpawns() {
		NPCSpawnRepository.loadSpawns(regionId);
	}
	
	/**
	 * Gets the region map, if it isn't set we create a new one that isn't clipped only
	 */
	public RegionMap forceGetRegionMap() {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		return map;
	}
	
	/**
	 * Spawns an object
	 *
	 * @param object
	 * 		The object
	 * @param localX
	 * 		The local x of the object
	 * @param localY
	 * 		The local y of the object
	 * @param original
	 * 		If its an original cache object
	 */
	void spawnObject(GameObject object, int localX, int localY, boolean original) {
		if (original) {
			if (deleteListContains(object)) {
				unclip(object, localX, localY);
				removedObjects.add(object);
				return;
			}
			addDefaultObject(object);
			clip(object, localX, localY);
			return;
		}
		Optional<GameObject> spawnedOptional = findSpawnedGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
		if (spawnedOptional.isPresent()) {
			final GameObject spawned = spawnedOptional.get();
			spawnedObjects.remove(spawned);
			removedObjects.add(spawned);
			unclip(spawned, spawned.getLocation().getXInRegion(), spawned.getLocation().getY());
		}
		spawnedObjects.add(object);
		object.setSpawnType(ObjectType.SERVER);
		clip(object, object.getLocation().getXInRegion(), object.getLocation().getYInRegion());
		players.forEach(this::refreshAllObjects);
	}
	
	/**
	 * Checks if the deleted list contains the object
	 *
	 * @param object
	 * 		The object
	 */
	private boolean deleteListContains(GameObject object) {
		return deletedObjects.stream().anyMatch(deleted -> deleted.getId() == object.getId() && deleted.getLocation().equals(object.getLocation()) && deleted.getType() == object.getType());
	}
	
	/**
	 * Adds a game object.
	 *
	 * @param object
	 * 		The object to add.
	 */
	private void addDefaultObject(GameObject object) {
		object.setSpawnType(ObjectType.CACHE);
		defaultObjects.add(object);
	}
	
	/**
	 * Clips an object
	 *
	 * @param object
	 * 		The object
	 * @param localX
	 * 		The local x of the object
	 * @param localY
	 * 		The local y of the object
	 */
	private void clip(GameObject object, int localX, int localY) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clippedOnlyMap == null) {
			clippedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getLocation().getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (localX < 0 || localY < 0 || localX >= map.getMasks()[plane].length || localY >= map.getMasks()[plane][localX].length) {
			return;
		}
		ObjectDefinition objectDefinition = object.getDefinitions();
		if (type == 22 ? objectDefinition.getActionCount() != 1 : objectDefinition.getActionCount() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			if (!objectDefinition.isNotClipped()) {
				map.addWall(plane, localX, localY, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			}
			if (objectDefinition.isProjectileClipped()) {
				clippedOnlyMap.addWall(plane, localX, localY, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.addObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			if (objectDefinition.isProjectileClipped()) {
				clippedOnlyMap.addObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			}
		} else if (type == 22) {
			map.addFloor(plane, localX, localY);
		}
	}
	
	/**
	 * Gets a game object from the {@link #spawnedObjects} list.
	 *
	 * @param objectId
	 * 		The id of the object, -1 if we should not check
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findSpawnedGameObject(int objectId, int x, int y, int plane, int type) {
		return spawnedObjects.stream().filter(object -> {
			if (objectId == -1) {
				if (type == -1) {
					return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			} else {
				if (type == -1) {
					return objectId == object.getId() && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return objectId == object.getId() && type == object.getType() && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			}
		}).findFirst();
	}
	
	/**
	 * Unclips a game object
	 *
	 * @param object
	 * 		The object
	 * @param localX
	 * 		The local x of the object
	 * @param localY
	 * 		The local y of the object
	 */
	public void unclip(GameObject object, int localX, int localY) {
		if (map == null) {
			map = new RegionMap(regionId, false);
		}
		if (clippedOnlyMap == null) {
			clippedOnlyMap = new RegionMap(regionId, true);
		}
		int plane = object.getLocation().getPlane();
		int type = object.getType();
		int rotation = object.getRotation();
		if (localX < 0 || localY < 0 || localX >= map.getMasks()[plane].length || localY >= map.getMasks()[plane][localX].length) {
			return;
		}
		ObjectDefinition objectDefinition = object.getDefinitions();
		
		if (type == 22 ? objectDefinition.getActionCount() != 1 : objectDefinition.getActionCount() == 0) {
			return;
		}
		if (type >= 0 && type <= 3) {
			map.removeWall(plane, localX, localY, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			if (objectDefinition.isProjectileClipped()) {
				clippedOnlyMap.removeWall(plane, localX, localY, type, rotation, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			}
		} else if (type >= 9 && type <= 21) {
			int sizeX;
			int sizeY;
			if (rotation != 1 && rotation != 3) {
				sizeX = objectDefinition.getSizeX();
				sizeY = objectDefinition.getSizeY();
			} else {
				sizeX = objectDefinition.getSizeY();
				sizeY = objectDefinition.getSizeX();
			}
			map.removeObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			if (objectDefinition.isProjectileClipped()) {
				clippedOnlyMap.removeObject(plane, localX, localY, sizeX, sizeY, objectDefinition.isProjectileClipped(), !objectDefinition.isNotClipped());
			}
		} else if (type == 22) {
			map.removeFloor(plane, localX, localY);
		}
	}
	
	/**
	 * Gets the clipped only region map, if it isn't set we create a new oen that isn't clipped only
	 */
	public RegionMap forceGetRegionMapClipedOnly() {
		if (clippedOnlyMap == null) {
			clippedOnlyMap = new RegionMap(regionId, true);
		}
		return clippedOnlyMap;
	}
	
	/**
	 * Gets the mask
	 *
	 * @param plane
	 * 		The plane
	 * @param localX
	 * 		The local x
	 * @param localY
	 * 		The local y
	 */
	public int getMask(int plane, int localX, int localY) {
		if (map == null || !allLoaded()) {
			return -1; // cliped tile
		}
		return map.getMasks()[plane][localX][localY];
	}
	
	/**
	 * Checks if all the data has loaded
	 */
	public boolean allLoaded() {
		for (boolean flag : loadedFlags) {
			if (!flag) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the clipped only mask
	 *
	 * @param plane
	 * 		The plane
	 * @param localX
	 * 		The local x
	 * @param localY
	 * 		The local y
	 */
	public int getMaskClippedOnly(int plane, int localX, int localY) {
		if (clippedOnlyMap == null || !allLoaded()) {
			return -1; // cliped tile
		}
		return clippedOnlyMap.getMasks()[plane][localX][localY];
	}
	
	/**
	 * Adds an entity to this region.
	 *
	 * @param entity
	 * 		The entity to add.
	 */
	public void addEntity(Entity entity) {
		if (entity.isPlayer()) {
			players.add(entity.toPlayer());
			handleRegionEntry(entity.toPlayer());
		} else if (entity.isNPC()) {
			if (!npcs.contains(entity.toNPC())) {
				npcs.add(entity.toNPC());
			}
		}
	}
	
	/**
	 * Removes an entity from this region.
	 *
	 * @param entity
	 * 		The entity to remove.
	 */
	public void removeEntity(Entity entity) {
		if (entity.isPlayer()) {
			playerExistanceTicks.remove(entity.toPlayer().getDetails().getUsername());
			players.remove(entity.toPlayer());
		} else if (entity.isNPC()) {
			npcs.remove(entity.toNPC());
		}
	}
	
	/**
	 * Finds a game object, used to verify the existence of objects
	 *
	 * @param object
	 * 		The object
	 */
	public Optional<GameObject> findAnyGameObject(GameObject object) {
		if (object == null) {
			return Optional.empty();
		} else {
			return findAnyGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
		}
	}
	
	/**
	 * Finds any game object (cache/spawned)
	 *
	 * @param objectId
	 * 		The id of the object, -1 to find any
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	public Optional<GameObject> findAnyGameObject(int objectId, int x, int y, int plane, int type) {
		Optional<GameObject> optional = findDefaultGameObject(objectId, x, y, plane, type);
		if (!optional.isPresent()) {
			return spawnedObjects.stream().filter(object -> {
				if (objectId == -1) {
					if (type == -1) {
						return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					} else {
						return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					}
				} else {
					if (type == -1) {
						return object.getId() == objectId && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					} else {
						return object.getId() == objectId && object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
					}
				}
			}).findFirst();
		}
		return optional;
	}
	
	/**
	 * Gets a game object from the {@link #defaultObjects} list.
	 *
	 * @param objectId
	 * 		The object id. If we don't know the id we enter -1 and find the first one that matches the other flags
	 * @param x
	 * 		The x coordinate of the object
	 * @param y
	 * 		The y coordinate of the object
	 * @param plane
	 * 		The plane of the object
	 * @param type
	 * 		The type of the object
	 * @return The game object, or null if the list didn't contain this object.
	 */
	private Optional<GameObject> findDefaultGameObject(int objectId, int x, int y, int plane, int type) {
		return defaultObjects.stream().filter(object -> {
			if (objectId == -1) {
				if (type == -1) {
					return object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			} else {
				if (type == -1) {
					return object.getId() == objectId && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				} else {
					return object.getId() == objectId && object.getType() == type && object.getLocation().getX() == x && object.getLocation().getY() == y && object.getLocation().getPlane() == plane;
				}
			}
		}).findFirst();
	}
	
	/**
	 * Gets an optional {@code FloorItem} for the item id we're looking for
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param x
	 * 		The x coordinate of the item
	 * @param y
	 * 		The y coordinate of the item
	 * @param plane
	 * 		The plane of the item
	 * @param ownerName
	 * 		If we want to find the owner name, this is put and we filter for it
	 */
	public Optional<FloorItem> getFloorItem(int itemId, int x, int y, int plane, String ownerName) {
		if (ownerName != null) {
			return floorItems.stream().filter(item -> item.isRenderable() && item.getId() == itemId && item.getLocation().getX() == x && item.getLocation().getY() == y && item.getLocation().getPlane() == plane && (item.getOwnerUsername() != null && item.getOwnerUsername().equals(ownerName))).findFirst();
		} else {
			return floorItems.stream().filter(item -> item.isRenderable() && item.getId() == itemId && item.getLocation().getX() == x && item.getLocation().getY() == y && item.getLocation().getPlane() == plane).findFirst();
		}
	}
	
	/**
	 * Adds the floor item to the list
	 *
	 * @param item
	 * 		The item
	 */
	boolean addFloorItemToList(FloorItem item) {
		return floorItems.add(item);
	}
	
	/**
	 * Refreshes an item
	 *
	 * @param item
	 * 		The item
	 */
	void refreshItem(FloorItem item) {
		players.forEach(player -> player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player)));
		// only adding the item for those its visible to
		players.stream().filter(item::visibleFor).forEach(player -> player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player)));
	}
	
	/**
	 * Sends the floor item to everyone in the region
	 *
	 * @param item
	 * 		The item
	 * @param skipOwner
	 * 		If we should skip the owner
	 */
	public void sendFloorItemToAll(FloorItem item, boolean skipOwner) {
		players.stream().filter(player -> {
			if (player != null) {
				if (skipOwner) {
					Optional<Player> optional = World.get().getPlayerByUsername(item.getOwnerUsername());
					if (optional.isPresent() && optional.get().equals(player)) {
						return false;
					}
				}
				if (player.getLocation().getPlane() == item.getLocation().getPlane() && player.isRenderable() && player.getMapRegionsIds().contains(regionId)) {
					return true;
				}
				return true;
			}
			return false;
		}).forEach(player -> {
			player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player));
			player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player));
		});
	}
	
	/**
	 * Handles the addition of a new item to the region
	 *
	 * @param item
	 * 		The item added
	 */
	void handleAddition(FloorItem item) {
		if (item.isDefaultPublic()) {
			sendFloorItemToAll(item, false);
		} else {
			Optional<Player> optional = World.get().getPlayerByUsername(item.getOwnerUsername());
			if (!optional.isPresent()) {
				return;
			}
			optional.get().getTransmitter().send(new FloorItemAdditionBuilder(item).build(optional.get()));
		}
		SystemManager.getScheduler().schedule(new FloorItemTask(item));
	}
	
	/**
	 * Handles the player entering the region
	 *
	 * @param player
	 * 		The player
	 */
	public void handleRegionEntry(Player player) {
		for (FloorItem item : floorItems) {
			if (!item.visibleFor(player)) {
				continue;
			}
			player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player));
			player.getTransmitter().send(new FloorItemAdditionBuilder(item).build(player));
		}
		refreshAllObjects(player);
	}
	
	/**
	 * Refreshes all objects in the region
	 *
	 * @param player
	 * 		The player
	 */
	private void refreshAllObjects(Player player) {
		SystemManager.getScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void run() {
				deletedObjects.forEach(object -> player.getTransmitter().send(new ObjectRemovalBuilder(object).build(player)));
				removedObjects.forEach(object -> player.getTransmitter().send(new ObjectRemovalBuilder(object).build(player)));
				spawnedObjects.forEach(object -> player.getTransmitter().send(new ObjectAdditionBuilder(object).build(player)));
			}
		});
	}
	
	/**
	 * Removes a floor item from the region
	 *
	 * @param item
	 * 		The floor item
	 */
	public boolean removeFloorItem(FloorItem item) {
		// if the item was removed
		final boolean remove = floorItems.remove(item);
		// removes the item for all players
		players.stream().filter(Objects::nonNull).forEach(player -> player.getTransmitter().send(new FloorItemRemovalBuilder(item).build(player)));
		return remove;
	}
	
	/**
	 * Spawns an object, with type {@link ObjectType#SERVER}
	 *
	 * @param object
	 * 		The object
	 */
	public void spawnObject(GameObject object) {
		spawnObject(object, object.getLocation().getXInRegion(), object.getLocation().getYInRegion(), false);
	}
	
	/**
	 * Removes an object
	 *
	 * @param object
	 * 		The object
	 */
	public void removeObject(GameObject object) {
		if (object.getSpawnType() == ObjectType.SERVER) {
			spawnedObjects.remove(object);
		}
		removedObjects.add(object);
		unclip(object, object.getLocation().getXInRegion(), object.getLocation().getYInRegion());
		players.forEach(this::refreshAllObjects);
	}
	
	/**
	 * Finds an npc by the id
	 *
	 * @param npcId
	 * 		The id of the npc  we want
	 */
	public Optional<NPC> findNPC(int npcId) {
		return npcs.stream().filter(npc -> npc.getId() == npcId).findAny();
	}
	
	/**
	 * Checks if the region is dynamic
	 */
	public boolean isDynamic() {
		return false;
	}
	
	/**
	 * Increases the amount of time the player spent in the region
	 *
	 * @param player
	 * 		The player
	 */
	public void increaseTimeSpent(Player player) {
		String username = player.getDetails().getUsername();
		Integer time = playerExistanceTicks.get(username);
		playerExistanceTicks.put(username, (time == null ? 1 : time + 1));
	}
	
	/**
	 * Gets the amount of time the player has spent in the region
	 *
	 * @param player
	 * 		The player
	 */
	public int getTimeSpent(Player player) {
		Integer time = playerExistanceTicks.get(player.getDetails().getUsername());
		return time == null ? 0 : time;
	}
}