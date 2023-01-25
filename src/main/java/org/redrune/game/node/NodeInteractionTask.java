package org.redrune.game.node;

import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.route.RouteStrategy;
import org.redrune.game.world.region.route.strategy.ObjectStrategy;
import org.redrune.utility.rs.constant.Directions.Direction;
import org.redrune.utility.tool.Misc;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.RegionManager;
import org.redrune.game.world.region.route.RouteFinder;
import org.redrune.game.world.region.route.strategy.EntityStrategy;
import org.redrune.game.world.region.route.strategy.FixedTileStrategy;
import org.redrune.game.world.region.route.strategy.FloorItemStrategy;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeInteractionTask {
	
	/**
	 * The node we will be travelling to
	 */
	private final Node node;
	
	/**
	 * The task to execute once we arrive at the node's closest location.
	 */
	private final Runnable task;
	
	/**
	 * Whether we also run on alternative.
	 */
	private final boolean alternative;
	
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;
	
	/**
	 * The destination the player will finally arrive at, this is used for verifying whether the node we're interacting
	 * with can be reached.
	 */
	private Location destination;
	
	/**
	 * Constructs a new path event
	 *
	 * @param node
	 * 		The node
	 * @param task
	 * 		The task to execute
	 * @param alternative
	 * 		If we should run alternative
	 */
	public NodeInteractionTask(Node node, Runnable task, boolean alternative) {
		this.node = node;
		this.task = task;
		this.alternative = alternative;
	}
	
	/**
	 * Finding the tiles between two destinations
	 *
	 * @param start
	 * 		The start location
	 * @param end
	 * 		The end location
	 */
	private static Set<Location> tilesBetweenDestinations(Location start, Location end, Direction direction) {
		final int distance = Misc.getDistance(start.getX(), start.getY(), end.getX(), end.getY());
		final Set<Location> tilesBetween = new LinkedHashSet<>();
		for (int i = 0; i <= distance; i++) {
			switch (direction) {
				case EAST:
					tilesBetween.add(start.transform(i, 0, 0));
					break;
				case WEST:
					tilesBetween.add(start.transform(-i, 0, 0));
					break;
				case NORTH:
					tilesBetween.add(start.transform(0, -i, 0));
					break;
				case SOUTH:
					tilesBetween.add(start.transform(0, i, 0));
					break;
			}
		}
		return tilesBetween;
	}
	
	/**
	 * Processes the event, if it is over, we will return true.
	 *
	 * @param player
	 * 		The player whose path event this is
	 */
	public boolean process(Player player) {
		if (!simpleCheck(player)) {
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		}
		
		if (node.isNPC()) {
			player.turnTo(node.toNPC());
		}
		
		RouteStrategy[] strategies = generateStrategies();
		if (last != null && match(strategies, last) && player.getMovement().isMoving()) {
			return false;
		} else if (last != null && match(strategies, last) && !player.getMovement().isMoving()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					if (alternative) {
						player.getTransmitter().sendMinimapFlagReset();
					}
					if (!player.getMovement().isMoving()) {
						executeTask(player);
					}
					return true;
				}
			}
			
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		} else {
			last = strategies;
			
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative) {
						player.getTransmitter().sendMinimapFlagReset();
					}
					if (!player.getMovement().isMoving()) {
						executeTask(player);
					}
					return true;
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				
				Location last = new Location(bufferX[0], bufferY[0], player.getLocation().getPlane());
				player.getMovement().resetWalkSteps();
				player.getTransmitter().sendMinimapFlag(last.getLocalX(player.getLastLoadedLocation()), last.getLocalY(player.getLastLoadedLocation()));
				
				// we're frozen so we don't move yet.
				if (player.isFrozen()) {
					return false;
				}
				for (int step = steps - 1; step >= 0; step--) {
					destination = new Location(bufferX[step], bufferY[step], node.getLocation().getPlane());
					if (!player.getMovement().addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
						break;
					}
				}
				return false;
			}
			
			player.getTransmitter().sendMessage("You can't reach that.");
			player.getTransmitter().sendMinimapFlagReset();
			return true;
		}
	}
	
	/**
	 * Checks that the node is a node we can travel to with a strategy, and then that we are on the same tile as them
	 *
	 * @param player
	 * 		The player travelling
	 */
	private boolean simpleCheck(Player player) {
		if (!node.isPlayer() && !node.isNPC() && !node.isGameObject() && !node.isItem()) {
			throw new RuntimeException(node + " is not instanceof any reachable entity.");
		}
		return player.getLocation().getPlane() == node.getLocation().getPlane();
	}
	
	/**
	 * Generates strategies to travel to the node
	 */
	private RouteStrategy[] generateStrategies() {
		if (node.isPlayer() || node.isNPC()) {
			return new RouteStrategy[] { new EntityStrategy((Entity) node) };
		} else if (node.isGameObject()) {
			return new RouteStrategy[] { new ObjectStrategy(node.toGameObject()) };
		} else if (node.isItem()) {
			FloorItem item = (FloorItem) node.toItem();
			return new RouteStrategy[] { new FixedTileStrategy(item.getLocation().getX(), item.getLocation().getY()), new FloorItemStrategy(item) };
		} else {
			throw new RuntimeException(node + " is not instanceof any reachable entity.");
		}
	}
	
	/**
	 * Checks that the two strategies match
	 *
	 * @param strategies
	 * 		The strategy
	 * @param routeStrategies
	 * 		The second strategy
	 */
	private boolean match(RouteStrategy[] strategies, RouteStrategy[] routeStrategies) {
		if (strategies.length != routeStrategies.length) {
			return false;
		}
		for (int i = 0; i < strategies.length; i++) {
			if (!strategies[i].equals(routeStrategies[i])) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Executes the task
	 *
	 * @param player
	 * 		The player executing it
	 */
	private void executeTask(Player player) {
	/*	destination = node.getLocation();
		Direction direction = getDirectionToNode(player);
		// we couldn't find a direction [we only support N/S/W/E]
		if (direction == null) {
			task.run();
			return;
		}
		final Set<Location> tiles = tilesBetweenDestinations(player.getLocation(), destination, direction);
		// the set of walls that are between the player and the node.
		Set<GameObject> walls = new LinkedHashSet<>();
		// adding all the walls into the set
		for (Location tile : tiles) {
			dumpPossibleWalls(walls, tile);
		}
		boolean cantReach = false;
		for (GameObject wall : walls) {
			if (wallBetween(player, wall)) {
				cantReach = true;
				break;
			}
		}
		if (cantReach) {
			player.getTransmitter().sendMessage("You can't reach that!");
		} else {
			task.run();
		}
		boolean canPath = EntityMovement.findBasicRoute(player, node.getSize(), node.getLocation(), 25);
		System.out.println(canPath);*/
		task.run();
	}
	
	/**
	 * Checks if the wall is between the player and the node
	 *
	 * @param player
	 * 		The player
	 * @param wall
	 * 		The wall
	 */
	private boolean wallBetween(Player player, GameObject wall) {
		/*
		rotation 2 - ahead east
		rotation 0 - behind east
		
		rotation 1 - north ahead
		rotation 3 - north behind
		*/
		boolean xAhead = wall.getLocation().getX() > player.getLocation().getX();
		boolean yAhead = wall.getLocation().getY() > player.getLocation().getY();
		int rotation = wall.getRotation();
		if (xAhead) {
			if (rotation == 0) {
				return true;
			}
		} else {
			if (rotation == 2) {
				return true;
			}
		}
		// we are on the same tile
		if (!xAhead && !yAhead) {
			Direction direction = getDirectionToNode(player);
			if (direction == null) {
				// safe
				return true;
			}
			// if the wall is ahead or behind us
			if (direction == Direction.NORTH && (rotation == 1 || rotation == 0)) {
				return true;
			}
			// if the wall is
			if (direction == Direction.SOUTH && rotation == 3) {
				return true;
			}
			if (direction == Direction.WEST && rotation == 0) {
				return true;
			}
		}
		return false;
	}
	
	private Direction getDirectionToNode(Player player) {
		int dX = destination.getX(), dY = destination.getY();
		int mX = player.getLocation().getX(), mY = player.getLocation().getY();
		if (mX < dX) {
			return Direction.EAST;
		} else if (mX > dX) {
			return Direction.WEST;
		} else if (mY > dY) {
			return Direction.SOUTH;
		} else if (mY < dY) {
			return Direction.NORTH;
		}
		return null;
	}
	
	/**
	 * Puts all the walls between a location into a set of possible walls
	 *
	 * @param walls
	 * 		The set of walls
	 * @param tile
	 * 		The tile the wall might exist on
	 */
	private void dumpPossibleWalls(Set<GameObject> walls, Location tile) {
		Optional<GameObject> optional = RegionManager.getRegion(tile.getRegionId()).findAnyGameObject(-1, tile.getX(), tile.getY(), tile.getPlane(), 0);
		if (!optional.isPresent()) {
			optional = RegionManager.getRegion(tile.getRegionId()).findAnyGameObject(-1, tile.getX(), tile.getY(), tile.getPlane(), 1);
			if (!optional.isPresent()) {
				optional = RegionManager.getRegion(tile.getRegionId()).findAnyGameObject(-1, tile.getX(), tile.getY(), tile.getPlane(), 2);
				if (!optional.isPresent()) {
					optional = RegionManager.getRegion(tile.getRegionId()).findAnyGameObject(-1, tile.getX(), tile.getY(), tile.getPlane(), 3);
					optional.ifPresent(walls::add);
				} else {
					walls.add(optional.get());
				}
			} else {
				walls.add(optional.get());
			}
		} else {
			walls.add(optional.get());
		}
	}
	
}