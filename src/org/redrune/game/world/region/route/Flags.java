package org.redrune.game.world.region.route;

/**
 * Created at: Jan 21, 2017 10:43:57 AM
 *
 * @author Walied-Yassen A.k.A Cody
 */
public final class Flags {
	
	/**
	 * The object is facing the west direction.
	 */
	public static final int FACE_WEST = 0;
	
	/**
	 * The object is facing the north direction.
	 */
	public static final int FACE_NORTH = 1;
	
	/**
	 * The object is facing the east direction.
	 */
	public static final int FACE_EAST = 2;
	
	/**
	 * The object is facing the south direction.
	 */
	public static final int FACE_SOUTH = 3;
	
	/**
	 * The regular point object flag.
	 */
	public static final int FLAG_OBJECT = 0x100;
	
	/**
	 * The blocked flag.
	 */
	public static final int FLAG_BLOCKED = 0x200000;
	
	/**
	 * The blocked object flag.
	 */
	public static final int FLAG_OBJECT_BLOCK = 0x40000000;
	
	/**
	 * The decoration object flag.
	 */
	public static final int FLAG_DECORATION = 0x40000;
	
	/**
	 * The object is a collision object.
	 */
	public static final int FLAG_OBJECT_COLLISION = FLAG_OBJECT_BLOCK | FLAG_BLOCKED | FLAG_DECORATION;
	
	/**
	 * The projectile block flag.
	 */
	public static final int FLAG_PROJECTILE_BLOCK = 0x20000;
	
	/**
	 * The west wall object flag.
	 */
	public static final int FLAG_WALL_WEST = 0x80;
	
	/**
	 * The north wall object flag.
	 */
	public static final int FLAG_WALL_NORTH = 0x2;
	
	/**
	 * The east wall object flag.
	 */
	public static final int FLAG_WALL_EAST = 0x8;
	
	/**
	 * The south wall object flag.
	 */
	public static final int FLAG_WALL_SOUTH = 0x20;
	
	/**
	 * The west diagonal wall flag.
	 */
	public static final int FLAG_DIAGONAL_WALL_WEST = 0x1;
	
	/**
	 * The north diagonal wall flag.
	 */
	public static final int FLAG_DIAGONAL_WALL_NORTH = 0x4;
	
	/**
	 * The east diagonal wall flag.
	 */
	public static final int FLAG_DIAGONAL_WALL_EAST = 0x10;
	
	/**
	 * The south diagonal wall flag.
	 */
	public static final int FLAG_DIAGONAL_WALL_SOUTH = 0x40;
	
	/**
	 * The solid wall north flag.
	 */
	public static final int FLAG_SOLID_WALL_NORTH = 0x400;
	
	/**
	 * The solid wall east flag.
	 */
	public static final int FLAG_SOLID_WALL_EAST = 0x1000;
	
	/**
	 * The solid wall south flag.
	 */
	public static final int FLAG_SOLID_WALL_SOUTH = 0x4000;
	
	/**
	 * The solid wall west flag.
	 */
	public static final int FLAG_SOLID_WALL_WEST = 0x10000;
	
	/**
	 * The solid diagonal wall west flag.
	 */
	public static final int FLAG_SOLID_DIAGONAL_WALL_WEST = 0x200;
	
	/**
	 * The solid diagonal wall north flag.
	 */
	public static final int FLAG_SOLID_DIAGONAL_WALL_NORTH = 0x800;
	
	/**
	 * The solid diagonal wall east flag.
	 */
	public static final int FLAG_SOLID_DIAGONAL_WALL_EAST = 0x2000;
	
	/**
	 * The solid wall south flag.
	 */
	public static final int FLAG_SOLID_DIAGONAL_WALL_SOUTH = 0x8000;
	
	/**
	 * The wall north block flag.
	 */
	public static final int FLAG_BLOCK_WALL_NORTH = 0x800000;
	
	/**
	 * The wall east block flag.
	 */
	public static final int FLAG_BLOCK_WALL_EAST = 0x2000000;
	
	/**
	 * The wall south block flag.
	 */
	public static final int FLAG_BLOCK_WALL_SOUTH = 0x8000000;
	
	/**
	 * The wall west block flag.
	 */
	public static final int FLAG_BLOCK_WALL_WEST = 0x20000000;
	
	/**
	 * The diagonal wall north block flag.
	 */
	public static final int FLAG_BLOCK_DIAGONAL_WALL_NORTH = 0x1000000;
	
	/**
	 * The diagonal wall east block flag.
	 */
	public static final int FLAG_BLOCK_DIAGONAL_WALL_EAST = 0x4000000;
	
	/**
	 * The diagonal wall south block flag.
	 */
	public static final int FLAG_BLOCK_DIAGONAL_WALL_SOUTH = 0x10000000;
	
	/**
	 * The diagonal wall west block flag.
	 */
	public static final int FLAG_BLOCK_DIAGONAL_WALL_WEST = 0x400000;
	
	/**
	 * The route north block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_NORTH = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_NORTH;
	
	/**
	 * The route east block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_EAST = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_EAST;
	
	/**
	 * The route south block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_SOUTH = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_SOUTH;
	
	/**
	 * The route west block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_WEST = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_WEST;
	
	/**
	 * The route diagonal north block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_DIAGONAL_NORTH = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_ROUTE_BLOCK_NORTH | FLAG_ROUTE_BLOCK_EAST | FLAG_BLOCK_WALL_NORTH | FLAG_BLOCK_WALL_EAST | FLAG_BLOCK_DIAGONAL_WALL_NORTH;
	
	/**
	 * The route diagonal east block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_DIAGONAL_EAST = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_ROUTE_BLOCK_SOUTH | FLAG_ROUTE_BLOCK_EAST | FLAG_BLOCK_WALL_SOUTH | FLAG_BLOCK_WALL_EAST | FLAG_BLOCK_DIAGONAL_WALL_EAST;
	
	/**
	 * The route diagonal south block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_DIAGONAL_SOUTH = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_ROUTE_BLOCK_SOUTH | FLAG_ROUTE_BLOCK_WEST | FLAG_BLOCK_WALL_WEST | FLAG_BLOCK_WALL_SOUTH | FLAG_BLOCK_DIAGONAL_WALL_SOUTH;
	
	/**
	 * The route diagonal west block flag.
	 */
	public static final int FLAG_ROUTE_BLOCK_DIAGONAL_WEST = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_ROUTE_BLOCK_NORTH | FLAG_ROUTE_BLOCK_WEST | FLAG_BLOCK_WALL_NORTH | FLAG_BLOCK_WALL_WEST | FLAG_BLOCK_DIAGONAL_WALL_WEST;
	
	public static final int FLAG_UNKNOWN = 0x80000;
	
	public static final int FLAG_UNKNOWN_NORTH = FLAG_OBJECT | FLAG_BLOCKED | FLAG_DECORATION | FLAG_WALL_NORTH | FLAG_UNKNOWN;
	
	public static final int FLAG_UNKNOWN_EAST = FLAG_OBJECT | FLAG_BLOCKED | FLAG_DECORATION | FLAG_WALL_EAST | FLAG_UNKNOWN;
	
	public static final int FLAG_UNKNOWN_SOUTH = FLAG_OBJECT | FLAG_BLOCKED | FLAG_DECORATION | FLAG_WALL_SOUTH | FLAG_UNKNOWN;
	
	public static final int FLAG_UNKNOWN_WEST = FLAG_OBJECT | FLAG_BLOCKED | FLAG_DECORATION | FLAG_WALL_WEST | FLAG_UNKNOWN;
	
	public static final int anInt3226 = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_NORTH | FLAG_BLOCK_WALL_EAST | FLAG_BLOCK_WALL_SOUTH | FLAG_BLOCK_DIAGONAL_WALL_NORTH | FLAG_BLOCK_DIAGONAL_WALL_EAST | FLAG_ROUTE_BLOCK_NORTH | FLAG_ROUTE_BLOCK_EAST | FLAG_ROUTE_BLOCK_SOUTH | FLAG_ROUTE_BLOCK_DIAGONAL_NORTH | FLAG_ROUTE_BLOCK_DIAGONAL_EAST;
	
	public static final int anInt3225 = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_NORTH | FLAG_BLOCK_WALL_SOUTH | FLAG_BLOCK_WALL_WEST | FLAG_ROUTE_BLOCK_NORTH | FLAG_ROUTE_BLOCK_SOUTH | FLAG_ROUTE_BLOCK_WEST | FLAG_BLOCK_DIAGONAL_WALL_SOUTH | FLAG_BLOCK_DIAGONAL_WALL_WEST | FLAG_ROUTE_BLOCK_DIAGONAL_SOUTH | FLAG_ROUTE_BLOCK_DIAGONAL_WEST;
	
	public static final int anInt3228 = FLAG_BLOCKED | FLAG_DECORATION | FLAG_OBJECT_BLOCK | FLAG_BLOCK_WALL_SOUTH | FLAG_BLOCK_WALL_EAST | FLAG_BLOCK_WALL_WEST | FLAG_BLOCK_DIAGONAL_WALL_SOUTH | FLAG_BLOCK_DIAGONAL_WALL_EAST | FLAG_ROUTE_BLOCK_SOUTH | FLAG_ROUTE_BLOCK_EAST | FLAG_ROUTE_BLOCK_WEST | FLAG_ROUTE_BLOCK_DIAGONAL_SOUTH | FLAG_ROUTE_BLOCK_DIAGONAL_EAST;
	
	public static final int anInt3231 = FLAG_BLOCKED | FLAG_ROUTE_BLOCK_WEST | FLAG_BLOCK_DIAGONAL_WALL_NORTH | FLAG_DECORATION | FLAG_BLOCK_WALL_NORTH | FLAG_BLOCK_DIAGONAL_WALL_WEST | FLAG_BLOCK_WALL_EAST | FLAG_BLOCK_WALL_WEST | FLAG_OBJECT_BLOCK | FLAG_ROUTE_BLOCK_NORTH | FLAG_ROUTE_BLOCK_DIAGONAL_NORTH | FLAG_ROUTE_BLOCK_EAST | FLAG_ROUTE_BLOCK_DIAGONAL_WEST;
	
	public static final int FLOOR_BLOCKSWALK = 0x200000;
	
	public static final int FLOORDECO_BLOCKSWALK = 0x40000;
	
	public static final int OBJ = 0x100;
	
	public static final int OBJ_BLOCKSFLY = 0x20000;
	
	public static final int OBJ_BLOCKSWALK_ALTERNATIVE = 0x40000000;
	
	public static final int WALLOBJ_NORTH = 0x2;
	
	public static final int WALLOBJ_EAST = 0x8;
	
	public static final int WALLOBJ_SOUTH = 0x20;
	
	public static final int WALLOBJ_WEST = 0x80;
	
	public static final int CORNEROBJ_NORTHWEST = 0x1;
	
	public static final int CORNEROBJ_NORTHEAST = 0x4;
	
	public static final int CORNEROBJ_SOUTHEAST = 0x10;
	
	public static final int CORNEROBJ_SOUTHWEST = 0x40;
	
	public static final int WALLOBJ_NORTH_BLOCKSFLY = 0x400;
	
	public static final int WALLOBJ_EAST_BLOCKSFLY = 0x1000;
	
	public static final int WALLOBJ_SOUTH_BLOCKSFLY = 0x4000;
	
	public static final int WALLOBJ_WEST_BLOCKSFLY = 0x10000;
	
	public static final int CORNEROBJ_NORTHWEST_BLOCKSFLY = 0x200;
	
	public static final int CORNEROBJ_NORTHEAST_BLOCKSFLY = 0x800;
	
	public static final int CORNEROBJ_SOUTHEAST_BLOCKSFLY = 0x2000;
	
	public static final int CORNEROBJ_SOUTHWEST_BLOCKSFLY = 0x8000;
	
	public static final int WALLOBJ_NORTH_BLOCKSWALK_ALTERNATIVE = 0x800000;
	
	public static final int WALLOBJ_EAST_BLOCKSWALK_ALTERNATIVE = 0x2000000;
	
	public static final int WALLOBJ_SOUTH_BLOCKSWALK_ALTERNATIVE = 0x8000000;
	
	public static final int WALLOBJ_WEST_BLOCKSWALK_ALTERNATIVE = 0x20000000;
	
	public static final int CORNEROBJ_NORTHWEST_BLOCKSWALK_ALTERNATIVE = 0x400000;
	
	public static final int CORNEROBJ_NORTHEAST_BLOCKSWALK_ALTERNATIVE = 0x1000000;
	
	public static final int CORNEROBJ_SOUTHEAST_BLOCKSWALK_ALTERNATIVE = 0x4000000;
	
	public static final int CORNEROBJ_SOUTHWEST_BLOCKSWALK_ALTERNATIVE = 0x10000000;
	
}
