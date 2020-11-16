package org.redrune.utility.repository.npc.spawn;

import lombok.Getter;
import org.redrune.game.node.Location;
import org.redrune.utility.rs.constant.Directions.Direction;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since May 15, 2015
 */
public class NPCSpawn {
	
	/**
	 * The id of the npc of this spawn
	 */
	@Getter
	private final int npcId;
	
	/**
	 * The location of the spawn
	 */
	@Getter
	private final Location tile;
	
	/**
	 * The direction the spawn is facing
	 */
	@Getter
	private final Direction direction;
	
	/**
	 * Constructs a new npc spawn
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param location
	 * 		The location of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public NPCSpawn(int npcId, Location location, Direction direction) {
		this(npcId, location.getX(), location.getY(), location.getPlane(), direction);
	}
	
	/**
	 * Constructs a new npc spawn
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param x
	 * 		The x coordinate of the tile of the spawn
	 * @param y
	 * 		The y coordinate of the tile of the spawn
	 * @param z
	 * 		The plane of the coordinate of the tile of the spawn
	 * @param direction
	 * 		The direction of the spawn
	 */
	public NPCSpawn(int npcId, int x, int y, int z, Direction direction) {
		this.npcId = npcId;
		this.tile = new Location(x, y, z);
		this.direction = direction;
	}
	
	/**
	 * Constructs a new npc spawn facing north
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param location
	 * 		The location of the spawn
	 */
	public NPCSpawn(int npcId, Location location) {
		this(npcId, location.getX(), location.getY(), location.getPlane(), Direction.NORTH);
	}
	
	/**
	 * Constructs a new npc spawn facing north
	 *
	 * @param npcId
	 * 		The id of the spawn
	 * @param x
	 * 		The x coordinate of the tile of the spawn
	 * @param y
	 * 		The y coordinate of the tile of the spawn
	 * @param z
	 * 		The plane of the coordinate of the tile of the spawn
	 */
	public NPCSpawn(int npcId, int x, int y, int z) {
		this.npcId = npcId;
		this.tile = new Location(x, y, z);
		this.direction = Direction.NORTH;
	}
	
	@Override
	public String toString() {
		return "[npcId=" + npcId + ", tile=" + tile + ", direction=" + direction + "]";
	}
}