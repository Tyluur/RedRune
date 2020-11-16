package org.redrune.game.world.region;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.activity.ActivitySystem;
import org.redrune.game.content.event.EventListener;
import org.redrune.game.content.event.EventListener.EventType;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.route.Flags;
import org.redrune.utility.tool.Misc;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @author Emperor
 * @author Dementhium development team (mainly).
 * @since 5/26/2017
 */
public class RegionManager {
	
	/**
	 * The direction deltas, different from the ones in {@link Location}.
	 */
	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	
	/**
	 * The direction deltas, different from the ones in {@link Location}.
	 */
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { 1, 1, 1, 0, 0, -1, -1, -1 };
	
	
	/**
	 * The set of regions that couldn't be loaded
	 */
	public static final Set<Integer> BROKEN_REGIONS = new HashSet<>();
	
	/**
	 * The set of regions that were loaded
	 */
	public static final Set<Integer> LOADED_REGIONS = new HashSet<>();
	
	/**
	 * The region mapping.
	 */
	private static final Map<Integer, Region> REGION_CACHE = new ConcurrentHashMap<>();
	
	/**
	 * When an entity enters a new region, we must add them to the new region, remove them from the previous one as
	 * well. This method is also fired when the entity moves.
	 *
	 * @param entity
	 * 		The entity.
	 */
	// TODO: region-music support
	public static void updateEntityRegion(Entity entity) {
		EventListener.fireListener(entity, EventType.MOVE);
		if (!entity.isRenderable()) {
			entity.getRegion().removeEntity(entity);
			return;
		}
		int regionId = entity.getRegion().getRegionId();
		int lastRegionId = entity.getLastRegion() == null ? 0 : entity.getLastRegion().getRegionId();
		
		// change of region
		if (lastRegionId != regionId) {
			if (lastRegionId > 0) {
				getRegion(lastRegionId).removeEntity(entity);
			}
			Region region = getRegion(regionId);
			region.addEntity(entity);
			entity.setLastRegion(entity.getRegion());
		}
		entity.checkMultiArea();
		// we update the location to the activities
		if (entity.isPlayer()) {
			ActivitySystem.fireLocationUpdate(entity.toPlayer(), entity.getLocation());
		}
	}
	
	/**
	 * Gets a new region by the id
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	public static Region getRegion(int regionId) {
		Region region = REGION_CACHE.get(regionId);
		if (region == null) {
			region = new Region(regionId);
			REGION_CACHE.put(regionId, region);
			return region;
		} else {
			return region;
		}
	}
	
	/**
	 * Gets a region by its id, if it doesn't exist, and forces it to load
	 *
	 * @param regionId
	 * 		The id of the region
	 */
	public static Region getRegionAndLoad(int regionId) {
		Region region = REGION_CACHE.get(regionId);
		if (region == null) {
			region = new Region(regionId);
		}
		region.checkLoadMap();
		if (!REGION_CACHE.containsKey(regionId)) {
			REGION_CACHE.put(regionId, region);
		}
		return region;
	}
	
	/**
	 * If we can move around based on the entity size
	 *
	 * @param plane
	 * 		The plane
	 * @param x
	 * 		The x coordinate
	 * @param y
	 * 		The y coordinate
	 * @param size
	 * 		The size
	 */
	public static boolean canMoveNPC(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (getMask(plane, tileX, tileY) != 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the mask at coordinates
	 *
	 * @param plane
	 * 		The plane
	 * @param x
	 * 		The x coordinate
	 * @param y
	 * 		The y coordinate
	 */
	public static int getMask(int plane, int x, int y) {
		Location tile = new Location(x, y, plane);
		Region region = getRegion(tile.getRegionId());
		if (region == null) {
			return -1;
		}
		return region.getMask(tile.getPlane(), tile.getXInRegion(), tile.getYInRegion());
	}
	
	/***
	 * Checks if a tile is free
	 * @param plane The plane of the tile
	 * @param x The x of the tile
	 * @param y The y of the tile
	 * @param size The size of the entity to check
	 */
	public static boolean isTileFree(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (!isFloorFree(plane, tileX, tileY) || !isWallsFree(plane, tileX, tileY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/***
	 * Checks if the floor is free via masks
	 * @param plane The plane of the tile
	 * @param x The x of the tile
	 * @param y The y of the tile
	 * @param size The size of the entity to check
	 */
	public static boolean isFloorFree(int plane, int x, int y, int size) {
		for (int tileX = x; tileX < x + size; tileX++) {
			for (int tileY = y; tileY < y + size; tileY++) {
				if (!isFloorFree(plane, tileX, tileY)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/***
	 * Checks if the floor is free via masks
	 * @param plane The plane of the tile
	 * @param x The x of the tile
	 * @param y The y of the tile
	 */
	public static boolean isFloorFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ)) == 0;
	}
	
	/***
	 * Checks if the walls are free via masks
	 * @param plane The plane of the tile
	 * @param x The x of the tile
	 * @param y The y of the tile
	 */
	public static boolean isWallsFree(int plane, int x, int y) {
		return (getMask(plane, x, y) & (Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST | Flags.WALLOBJ_EAST | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST)) == 0;
	}
	
	/**
	 * Adds a public floor item
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param targetTicks
	 * 		The ticks until the next item phase is hit
	 * @param location
	 * 		The location of the item
	 */
	public static void addPublicFloorItem(int itemId, int itemAmount, int targetTicks, Location location) {
		addFloorItem(itemId, itemAmount, targetTicks, location, null);
	}
	
	/**
	 * Adds a floor item to the region
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param targetTicks
	 * 		The ticks until the next item phase is hit
	 * @param location
	 * 		The location of the item
	 * @param ownerUsername
	 * 		The name of the user who owns the item
	 */
	public static void addFloorItem(int itemId, int itemAmount, int targetTicks, Location location, String ownerUsername) {
		Region region = getRegion(location.getRegionId());
		FloorItem item = new FloorItem(ownerUsername, targetTicks, itemId, itemAmount, location);
		if (!region.addFloorItemToList(item)) {
			throw new IllegalStateException("Unable to add floor item to region list.");
		}
		region.handleAddition(item);
	}
	
	/**
	 * Adds a floor item to the region
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param itemAmount
	 * 		The amount of the item
	 * @param targetTicks
	 * 		The ticks until the next item phase is hit
	 * @param location
	 * 		The location of the item
	 * @param ownerUsername
	 * 		The name of the user who owns the item
	 */
	public static void addStackableFloorItem(int itemId, int itemAmount, int targetTicks, Location location, String ownerUsername) {
		Region region = getRegion(location.getRegionId());
		Optional<FloorItem> optional = region.getFloorItem(itemId, location.getX(), location.getY(), location.getPlane(), ownerUsername);
		if (!optional.isPresent()) {
			addFloorItem(itemId, itemAmount, targetTicks, location, ownerUsername);
		} else {
			FloorItem item = optional.get();
			long newAmount = (long) (item.getAmount() + itemAmount);
			if (newAmount > Integer.MAX_VALUE) {
				// TODO: split into two items
			} else {
				item.setAmount((int) newAmount);
			}
			region.refreshItem(item);
		}
	}
	
	/**
	 * Finds the objects to delete in the region
	 *
	 * @param region
	 * 		The region
	 */
	public static CopyOnWriteArraySet<GameObject> findDeletedObjects(Region region) {
		Optional<List<GameObject>> optional = RegionDeletion.getObjectsToDelete(region.getRegionId());
		if (!optional.isPresent()) {
			return new CopyOnWriteArraySet<>();
		} else {
			// the objects that were found to be deleted
			List<GameObject> foundObjects = optional.get();
			CopyOnWriteArraySet<GameObject> objectList = new CopyOnWriteArraySet<>();
			objectList.addAll(foundObjects);
			return objectList;
		}
	}
	
	/**
	 * Adds a game object that will be removed after delay, and replaced with an item
	 *
	 * @param object
	 * 		The object
	 * @param itemReplaceId
	 * 		The item that wil replace it
	 * @param itemReplaceAmount
	 * 		The amount of the item to replace with
	 * @param ticks
	 * 		The ticks
	 */
	public static void addTimedGamedObject(GameObject object, int itemReplaceId, int itemReplaceAmount, int ticks) {
		object.getRegion().spawnObject(object);
		SystemManager.getScheduler().schedule(new ScheduledTask(ticks) {
			@Override
			public void run() {
				Optional<GameObject> optional = RegionManager.getRegion(object.getLocation().getRegionId()).findSpawnedGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
				if (!optional.isPresent()) {
					return;
				}
				GameObject gameObject = optional.get();
				gameObject.getRegion().removeObject(gameObject);
				RegionManager.addFloorItem(itemReplaceId, itemReplaceAmount, 200, gameObject.getLocation(), null);
			}
		});
	}
	
	/**
	 * Spawns an object that is removed after x ticks
	 *
	 * @param object
	 * 		The object
	 * @param ticks
	 * 		The ticks to wait
	 */
	public static void spawnTimedObject(GameObject object, int ticks) {
		object.getRegion().spawnObject(object);
		SystemManager.getScheduler().schedule(new ScheduledTask(ticks) {
			@Override
			public void run() {
				Optional<GameObject> optional = RegionManager.getRegion(object.getLocation().getRegionId()).findSpawnedGameObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getPlane(), object.getType());
				if (!optional.isPresent()) {
					return;
				}
				object.getRegion().removeObject(object);
			}
		});
	}
	
	/**
	 * Checks if the location is a multi area
	 */
	public static boolean isMultiArea(Location location) {
		int destX = location.getX();
		int destY = location.getY();
		int plane = location.getPlane();
		int regionId = location.getRegionId();
		return (destX >= 3462 && destX <= 3511 && destY >= 9481 && destY <= 9521 && plane == 0) // kalphite
				       // queen
				       // lair
				       || (destX >= 4540 && destX <= 4799 && destY >= 5052 && destY <= 5183 && plane == 0) // thzaar
				       // city
				       || regionId == 11051 || regionId == 16729 // glacors
				       || regionId == 11589 // dags
				       || regionId == 10894 // monkey skeles
				       || regionId == 11573 // sea troll queen
				       || regionId == 10554 || regionId == 10810 // rock crabs
				       || (destX >= 1721 && destX <= 1791 && destY >= 5123 && destY <= 5249) // mole
				       || (destX >= 3029 && destX <= 3374 && destY >= 3759 && destY <= 3903)// wild
				       || (destX >= 2250 && destX <= 2280 && destY >= 4670 && destY <= 4720) || (destX >= 3198 && destX <= 3380 && destY >= 3904 && destY <= 3970) || (destX >= 3191 && destX <= 3326 && destY >= 3510 && destY <= 3759) || (destX >= 2987 && destX <= 3006 && destY >= 3912 && destY <= 3937) || (destX >= 2245 && destX <= 2295 && destY >= 4675 && destY <= 4720) || (destX >= 2450 && destX <= 3520 && destY >= 9450 && destY <= 9550) || (destX >= 3006 && destX <= 3071 && destY >= 3602 && destY <= 3710) || (destX >= 3134 && destX <= 3192 && destY >= 3519 && destY <= 3646) || (destX >= 2815 && destX <= 2966 && destY >= 5240 && destY <= 5375)// wild
				       || (destX >= 2840 && destX <= 2950 && destY >= 5190 && destY <= 5230) // godwars
				       || (destX >= 3547 && destX <= 3555 && destY >= 9690 && destY <= 9699) // zaros
				       || (destX >= 1490 && destX <= 1515 && destY >= 4696 && destY <= 4714) // chaos dwarf battlefield
				       // godwars
				       || (destX >= 2250 && destX <= 2292) && (destY >= 4675 && destY <= 4710) // kbd
				       || (destX >= 2560 && destX <= 2630) && (destY >= 5710 && destY <= 5753) // tormenteds
				       || (destX >= 3083 && destX <= 3120) && (destY >= 5522 && destY <= 5550) // Bork's area
				       || regionId == 12590 || (destX >= 2970 && destX <= 3000 && destY >= 4365 && destY <= 4400)// corp
				       || (destX >= 3195 && destX <= 3327 && destY >= 3520 && destY <= 3970 || (destX >= 2376 && 5127 >= destY && destX <= 2422 && 5168 <= destY)) || (destX >= 2374 && destY >= 5129 && destX <= 2424 && destY <= 5168) // pits
				       || (destX >= 2622 && destY >= 5696 && destX <= 2573 && destY <= 5752) // torms
				       || (destX >= 2368 && destY >= 3072 && destX <= 2431 && destY <= 3135) // castlewars
				       // out
				       || (destX >= 2365 && destY >= 9470 && destX <= 2436 && destY <= 9532) // castlewars
				       || (destX >= 2948 && destY >= 5537 && destX <= 3071 && destY <= 5631) // Risk
				       // ffa.
				       || (destX >= 2756 && destY >= 5537 && destX <= 2879 && destY <= 5631) // Safe
				       // ffa
				       || regionId == 1089 || regionId == 12341 || (destX >= 3011 && destX <= 3132 && destY >= 10052 && destY <= 10175 && (destY >= 10066 || destX >= 3094)); // forinthry dungeon
	}
	
	/**
	 * Gets the first empty tile around a node, excluding the node's tile.
	 *
	 * @param node
	 * 		The node
	 * @param radius
	 * 		The radius
	 * @param size
	 * 		The size of the entity we are looking for (default to 0)
	 */
	public static Location getFirstEmptyTile(Node node, int radius, int size) {
		List<Location> tiles = new ArrayList<>();
		// adds all the tiles around the node
		for (int x = -radius; x <= radius; x++) {
			for (int y = -radius; y <= radius; y++) {
				final Location transform = node.getLocation().transform(x, y, 0);
				if (!tiles.contains(transform)) {
					tiles.add(transform);
				}
			}
		}
		// removes the actual node location
		tiles.remove(node.getLocation());
		// removes the tiles that are unable to be clipped or have objects on them
		for (Iterator<Location> it = tiles.iterator(); it.hasNext(); ) {
			Location tile = it.next();
			int dir = Misc.getMoveDirection(node.getLocation().getX() - tile.getX(), node.getLocation().getY() - tile.getY());
			if (dir == -1) {
				it.remove();
				continue;
			}
			if (!isTileFree(tile.getPlane(), tile.getX(), tile.getY(), dir, size) || !RegionManager.checkProjectileStep(node.getLocation().getPlane(), tile.getX(), tile.getY(), dir, size) || node.getRegion().findAnyGameObject(-1, tile.getX(), tile.getY(), tile.getPlane(), -1).isPresent()) {
				it.remove();
			}
		}
		// if we have no more tiles [all the nearby tiles are clipped]
		if (tiles.isEmpty()) {
			return null;
		}
		// sorts based on closest to the node
		tiles.sort(Comparator.comparingInt(o -> o.getDistance(node.getLocation())));
		return tiles.get(0);
	}
	
	/**
	 * Checks if a tile is free
	 *
	 * @param plane
	 * 		The plane of the tile
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param size
	 * 		The size of the node checking.
	 */
	public static boolean isTileFree(int plane, int x, int y, int dir, int size) {
		return isTileFree(plane, x, y, DIRECTION_DELTA_X[dir], DIRECTION_DELTA_Y[dir], size);
	}
	
	/**
	 * Checks if a projectile can go to this coordinate
	 *
	 * @param plane
	 * 		The plane
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param dir
	 * 		The direction
	 * @param size
	 * 		The size of the projectile
	 */
	public static boolean checkProjectileStep(int plane, int x, int y, int dir, int size) {
		int xOffset = DIRECTION_DELTA_X[dir];
		int yOffset = DIRECTION_DELTA_Y[dir];
		if (size == 1) {
			int mask = getClippedOnlyMask(plane, x + DIRECTION_DELTA_X[dir], y + DIRECTION_DELTA_Y[dir]);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & 0x42240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & 0x60240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & 0x40a40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & 0x48240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & 0x43a40000) == 0 && (getClippedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClippedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & 0x60e40000) == 0 && (getClippedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClippedOnlyMask(plane, x, y - 1) & 0x40a40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & 0x4e240000) == 0 && (getClippedOnlyMask(plane, x - 1, y) & 0x42240000) == 0 && (getClippedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & 0x78240000) == 0 && (getClippedOnlyMask(plane, x + 1, y) & 0x60240000) == 0 && (getClippedOnlyMask(plane, x, y + 1) & 0x48240000) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getClippedOnlyMask(plane, x - 1, y) & 0x43a40000) == 0 && (getClippedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getClippedOnlyMask(plane, x + 2, y) & 0x60e40000) == 0 && (getClippedOnlyMask(plane, x + 2, y + 1) & 0x78240000) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getClippedOnlyMask(plane, x, y - 1) & 0x43a40000) == 0 && (getClippedOnlyMask(plane, x + 1, y - 1) & 0x60e40000) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getClippedOnlyMask(plane, x, y + 2) & 0x4e240000) == 0 && (getClippedOnlyMask(plane, x + 1, y + 2) & 0x78240000) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getClippedOnlyMask(plane, x - 1, y) & 0x4fa40000) == 0 && (getClippedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) == 0 && (getClippedOnlyMask(plane, x, y - 1) & 0x63e40000) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getClippedOnlyMask(plane, x + 1, y - 1) & 0x63e40000) == 0 && (getClippedOnlyMask(plane, x + 2, y - 1) & 0x60e40000) == 0 && (getClippedOnlyMask(plane, x + 2, y) & 0x78e40000) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getClippedOnlyMask(plane, x - 1, y + 1) & 0x4fa40000) == 0 && (getClippedOnlyMask(plane, x - 1, y + 1) & 0x4e240000) == 0 && (getClippedOnlyMask(plane, x, y + 2) & 0x7e240000) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getClippedOnlyMask(plane, x + 1, y + 2) & 0x7e240000) == 0 && (getClippedOnlyMask(plane, x + 2, y + 2) & 0x78240000) == 0 && (getClippedOnlyMask(plane, x + 1, y + 1) & 0x78e40000) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getClippedOnlyMask(plane, x - 1, y) & 0x43a40000) != 0 || (getClippedOnlyMask(plane, x - 1, -1 + (y + size)) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getClippedOnlyMask(plane, x + size, y) & 0x60e40000) != 0 || (getClippedOnlyMask(plane, x + size, y - (-size + 1)) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getClippedOnlyMask(plane, x, y - 1) & 0x43a40000) != 0 || (getClippedOnlyMask(plane, x + size - 1, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getClippedOnlyMask(plane, x, y + size) & 0x4e240000) != 0 || (getClippedOnlyMask(plane, x + (size - 1), y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getClippedOnlyMask(plane, x - 1, y - 1) & 0x43a40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x - 1, y + (-1 + sizeOffset)) & 0x4fa40000) != 0 || (getClippedOnlyMask(plane, sizeOffset - 1 + x, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getClippedOnlyMask(plane, x + size, y - 1) & 0x60e40000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x + size, sizeOffset + (-1 + y)) & 0x78e40000) != 0 || (getClippedOnlyMask(plane, x + sizeOffset, y - 1) & 0x63e40000) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getClippedOnlyMask(plane, x - 1, y + size) & 0x4e240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x - 1, y + sizeOffset) & 0x4fa40000) != 0 || (getClippedOnlyMask(plane, -1 + (x + sizeOffset), y + size) & 0x7e240000) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getClippedOnlyMask(plane, x + size, y + size) & 0x78240000) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getClippedOnlyMask(plane, x + sizeOffset, y + size) & 0x7e240000) != 0 || (getClippedOnlyMask(plane, x + size, y + sizeOffset) & 0x78e40000) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if a tile is free
	 *
	 * @param plane
	 * 		The plane of the tile
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 * @param xOffset
	 * 		The x offset, based on direction
	 * @param yOffset
	 * 		The y offset, based on direction
	 * @param size
	 * 		The size of the node checking.
	 */
	private static boolean isTileFree(int plane, int x, int y, int xOffset, int yOffset, int size) {
		if (size == 1) {
			int mask = getMask(plane, x + xOffset, y + yOffset);
			if (xOffset == -1 && yOffset == 0) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(plane, x - 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0 && (getMask(plane, x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(plane, x + 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0 && (getMask(plane, x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH)) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(plane, x - 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST)) == 0 && (getMask(plane, x, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (mask & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(plane, x + 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_WEST)) == 0 && (getMask(plane, x, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH)) == 0;
			}
		} else if (size == 2) {
			if (xOffset == -1 && yOffset == 0) {
				return (getMask(plane, x - 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(plane, x - 1, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0;
			}
			if (xOffset == 1 && yOffset == 0) {
				return (getMask(plane, x + 2, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(plane, x + 2, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
			}
			if (xOffset == 0 && yOffset == -1) {
				return (getMask(plane, x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(plane, x + 1, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0;
			}
			if (xOffset == 0 && yOffset == 1) {
				return (getMask(plane, x, y + 2) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(plane, x + 1, y + 2) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
			}
			if (xOffset == -1 && yOffset == -1) {
				return (getMask(plane, x - 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(plane, x - 1, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(plane, x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) == 0;
			}
			if (xOffset == 1 && yOffset == -1) {
				return (getMask(plane, x + 1, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) == 0 && (getMask(plane, x + 2, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) == 0 && (getMask(plane, x + 2, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
			}
			if (xOffset == -1 && yOffset == 1) {
				return (getMask(plane, x - 1, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(plane, x - 1, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) == 0 && (getMask(plane, x, y + 2) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
			}
			if (xOffset == 1 && yOffset == 1) {
				return (getMask(plane, x + 1, y + 2) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(plane, x + 2, y + 2) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) == 0 && (getMask(plane, x + 1, y + 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) == 0;
			}
		} else {
			if (xOffset == -1 && yOffset == 0) {
				if ((getMask(plane, x - 1, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0 || (getMask(plane, x - 1, -1 + (y + size)) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 0) {
				if ((getMask(plane, x + size, y) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0 || (getMask(plane, x + size, y - (-size + 1)) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + size, y + sizeOffset) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == -1) {
				if ((getMask(plane, x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0 || (getMask(plane, x + size - 1, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == 0 && yOffset == 1) {
				if ((getMask(plane, x, y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(plane, x + (size - 1), y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size - 1; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == -1) {
				if ((getMask(plane, x - 1, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.CORNEROBJ_NORTHEAST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + (-1 + sizeOffset)) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(plane, sizeOffset - 1 + x, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == -1) {
				if ((getMask(plane, x + size, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + size, sizeOffset + (-1 + y)) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0 || (getMask(plane, x + sizeOffset, y - 1) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_NORTHEAST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == -1 && yOffset == 1) {
				if ((getMask(plane, x - 1, y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_SOUTHEAST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x - 1, y + sizeOffset) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.CORNEROBJ_NORTHEAST | Flags.CORNEROBJ_SOUTHEAST)) != 0 || (getMask(plane, -1 + (x + sizeOffset), y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
						return false;
					}
				}
			} else if (xOffset == 1 && yOffset == 1) {
				if ((getMask(plane, x + size, y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
					return false;
				}
				for (int sizeOffset = 1; sizeOffset < size; sizeOffset++) {
					if ((getMask(plane, x + sizeOffset, y + size) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_EAST | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_SOUTHEAST | Flags.CORNEROBJ_SOUTHWEST)) != 0 || (getMask(plane, x + size, y + sizeOffset) & (Flags.FLOOR_BLOCKSWALK | Flags.FLOORDECO_BLOCKSWALK | Flags.OBJ | Flags.WALLOBJ_NORTH | Flags.WALLOBJ_SOUTH | Flags.WALLOBJ_WEST | Flags.CORNEROBJ_NORTHWEST | Flags.CORNEROBJ_SOUTHWEST)) != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Gets the clipped only mask data
	 *
	 * @param plane
	 * 		The plane
	 * @param x
	 * 		The x
	 * @param y
	 * 		The y
	 */
	private static int getClippedOnlyMask(int plane, int x, int y) {
		Location tile = new Location(x, y, plane);
		int regionId = tile.getRegionId();
		Region region = getRegion(regionId);
		if (region == null) {
			return -1;
		}
		int baseLocalX = x - ((regionId >> 8) * 64);
		int baseLocalY = y - ((regionId & 0xff) * 64);
		return region.getMaskClippedOnly(tile.getPlane(), baseLocalX, baseLocalY);
	}
	
	/**
	 * Gets the map of all regions
	 */
	public static Map<Integer, Region> getRegions() {
		return REGION_CACHE;
	}
	
	/**
	 * Removes a region
	 *
	 * @param regionId
	 * 		The region
	 */
	public static Region removeRegion(int regionId) {
		return REGION_CACHE.remove(regionId);
	}
}