package org.redrune.utility.rs.constant;

import org.redrune.game.node.Location;

import java.awt.*;
import java.util.Arrays;
import java.util.Optional;

/**
 * Represents the walking and running directions.
 *
 * @author Dementhium development team (?)
 */
public class Directions {
	
	public static final byte[] DIRECTION_DELTA_X = new byte[] { -1, 0, 1, -1, 1, -1, 0, 1 };
	
	public static final byte[] DIRECTION_DELTA_Y = new byte[] { -1, -1, -1, 0, 0, 1, 1, 1 };
	
	public static WalkingDirection directionFor(Location currentPos, Location nextPos) {
		int dirX = nextPos.getX() - currentPos.getX();
		int dirY = nextPos.getY() - currentPos.getY();
		if (dirX < 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_WEST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_WEST;
			} else {
				return WalkingDirection.WEST;
			}
		} else if (dirX > 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_EAST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_EAST;
			} else {
				return WalkingDirection.EAST;
			}
		} else {
			if (dirY < 0) {
				return WalkingDirection.SOUTH;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH;
			} else {
				return null;
			}
		}
	}
	
	public static RunningDirection runningDirectionFor(Point currentPos, Point nextPos) {
		int dirX = (int) (nextPos.getX() - currentPos.getX());
		int dirY = (int) (nextPos.getY() - currentPos.getY());
		switch (dirX) {
			case -2:
				switch (dirY) {
					case -2:
						return RunningDirection.SS_WW;
					case -1:
						return RunningDirection.S_WW;
					case 0:
						return RunningDirection.WW;
					case 1:
						return RunningDirection.N_WW;
					case 2:
						return RunningDirection.NN_WW;
				}
				return null;
			case -1:
				switch (dirY) {
					case -2:
						return RunningDirection.SS_W;
					case 2:
						return RunningDirection.NN_W;
				}
				return null;
			case 0:
				switch (dirY) {
					case -2:
						return RunningDirection.SS;
					case 2:
						return RunningDirection.NN;
				}
				return null;
			case 1:
				switch (dirY) {
					case -2:
						return RunningDirection.SS_E;
					case 2:
						return RunningDirection.NN_E;
				}
				return null;
			case 2:
				switch (dirY) {
					case -2:
						return RunningDirection.SS_EE;
					case -1:
						return RunningDirection.S_EE;
					case 0:
						return RunningDirection.EE;
					case 1:
						return RunningDirection.N_EE;
					case 2:
						return RunningDirection.NN_EE;
				}
				return null;
		}
		return null;
	}
	
	public static WalkingDirection directionFor(int dirX, int dirY) {
		if (dirX < 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_WEST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_WEST;
			} else {
				return WalkingDirection.WEST;
			}
		} else if (dirX > 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_EAST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_EAST;
			} else {
				return WalkingDirection.EAST;
			}
		} else {
			if (dirY < 0) {
				return WalkingDirection.SOUTH;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH;
			} else {
				return null;
			}
		}
	}
	
	public static WalkingDirection directionFor(int curX, int curY, int dstX, int dstY) {
		int dirX = dstX - curX;
		int dirY = dstY - curX;
		if (dirX < 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_WEST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_WEST;
			} else {
				return WalkingDirection.WEST;
			}
		} else if (dirX > 0) {
			if (dirY < 0) {
				return WalkingDirection.SOUTH_EAST;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH_EAST;
			} else {
				return WalkingDirection.EAST;
			}
		} else {
			if (dirY < 0) {
				return WalkingDirection.SOUTH;
			} else if (dirY > 0) {
				return WalkingDirection.NORTH;
			} else {
				return null;
			}
		}
	}
	
	public enum RunningDirection {
		EE(8),
		N_EE(10),
		N_WW(9),
		NN(13),
		NN_E(14),
		NN_EE(15),
		NN_W(12),
		NN_WW(11),
		S_EE(6),
		S_WW(5),
		SS(2),
		SS_E(3),
		SS_EE(4),
		SS_W(1),
		SS_WW(0),
		WW(7);
		
		private int dir;
		
		RunningDirection(int dir) {
			this.dir = dir;
		}
		
		@Override
		public String toString() {
			return "[run] [dir=" + dir + ", type=" + super.toString() + "]";
		}
		
		public static Optional<RunningDirection> getDirection(int dir) {
			return Arrays.stream(values()).filter(direction -> dir == direction.dir).findAny();
		}
		
		public int intValue() {
			return dir;
		}
		
		public int npcIntValue() {
			throw new UnsupportedOperationException("The GNP protocol does not support 2 step running directions!");
		}
	}
	
	public enum WalkingDirection {
		EAST(4, 2),
		NORTH(6, 0),
		NORTH_EAST(7, 1),
		NORTH_WEST(5, 7),
		SOUTH(1, 4),
		SOUTH_EAST(2, 3),
		SOUTH_WEST(0, 5),
		WEST(3, 6);
		
		private int dir;
		
		private int npcDir;
		
		WalkingDirection(int dir, int npcDir) {
			this.dir = dir;
			this.npcDir = npcDir;
		}
		
		public static WalkingDirection forIntValue(int value) {
			switch (value) {
				case 0:
					return SOUTH_WEST;
				case 1:
					return SOUTH;
				case 2:
					return SOUTH_EAST;
				case 3:
					return WEST;
				case 4:
					return EAST;
				case 5:
					return NORTH_WEST;
				case 6:
					return NORTH;
				case 7:
					return NORTH_EAST;
			}
			return null;
		}
		
		public int intValue() {
			return dir;
		}
		
		public int npcIntValue() {
			return npcDir;
		}
		
		@Override
		public String toString() {
			return "[walk] [dir=" + dir + ", type=" + super.toString() + "]";
		}
	}
	
	public enum NormalDirection {
		EAST(4, 2),
		NORTH(6, 0),
		NORTH_EAST(7, 1),
		NORTH_WEST(5, 7),
		SOUTH(1, 4),
		SOUTH_EAST(2, 3),
		SOUTH_WEST(0, 5),
		WEST(3, 6);
		
		private int dir;
		
		private int npcDir;
		
		NormalDirection(int dir, int npcDir) {
			this.dir = dir;
			this.npcDir = npcDir;
		}
		
		public static NormalDirection forIntValue(int value) {
			switch (value) {
				case 0:
					return SOUTH_WEST;
				case 1:
					return SOUTH;
				case 2:
					return SOUTH_EAST;
				case 3:
					return WEST;
				case 4:
					return EAST;
				case 5:
					return NORTH_WEST;
				case 6:
					return NORTH;
				case 7:
					return NORTH_EAST;
			}
			return null;
		}
		
		public static NormalDirection forNpcDirValue(int value) {
			switch (value) {
				case 0:
					return NORTH;
				case 1:
					return NORTH_EAST;
				case 2:
					return EAST;
				case 3:
					return SOUTH_EAST;
				case 4:
					return SOUTH;
				case 5:
					return SOUTH_WEST;
				case 6:
					return WEST;
				case 7:
					return NORTH_WEST;
			}
			return null;
		}
		
		public int intValue() {
			return dir;
		}
		
		public int npcIntValue() {
			return npcDir;
		}
		
		public String stringValue() {
			return super.toString();
		}
		
		@Override
		public String toString() {
			return "[walk] [dir=" + dir + ", type=" + super.toString() + "]";
		}
		
	}
	
	/**
	 * The direction types, with the values being the ordinals
	 */
	public enum Direction {
		
		NORTH,
		NORTHEAST,
		EAST,
		SOUTHEAST,
		SOUTH,
		SOUTHWEST,
		WEST,
		NORTHWEST
		
	}
	
	/**
	 * Holds direction-related methods.
	 *
	 * @author Jolt Environment v2 development team.
	 * @author Emperor (converting to Java)
	 */
	public static class DirectionUtilities {
		
		/**
		 * The three bits movement type array.
		 */
		public static final int[][] ThreeBitsMovementType = new int[][] { { 0, 3, 5 }, { 1, -1, 6 }, { 2, 4, 7 } };
		
		/**
		 * The four bits movement type array.
		 */
		public static final int[][] FourBitsMovementType = new int[][] { { 0, 5, 7, 9, 11 }, { 1, -1, -1, -1, 12 }, { 2, -1, -1, -1, 13 }, { 3, -1, -1, -1, 14 }, { 4, 6, 8, 10, 15 } };
		
		/**
		 * The region movement types.
		 */
		public static final int[][] RegionMovementType = new int[][] { { 0, 3, 5 }, { 1, -1, 6 }, { 2, 4, 7 } };
	}
}
