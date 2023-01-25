package org.redrune.game.content.action.interaction;

import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.route.RouteFinder;
import org.redrune.game.node.Location;
import org.redrune.game.world.World;
import org.redrune.game.world.region.route.strategy.EntityStrategy;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public class PlayerFollowAction implements Action {
	
	/**
	 * The partner we're following
	 */
	private final Player partner;
	
	public PlayerFollowAction(Player partner) {
		this.partner = partner;
	}
	
	@Override
	public boolean start(Player player) {
		if (!canContinue(player)) {
			return false;
		}
		player.turnTo(partner);
		traverseCalculatePath(player);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		if (!canContinue(player)) {
			return false;
		}
		int direction = partner.getAttribute("direction", 0);
		switch (direction) {
			case 0: //north
				player.getMovement().addWalkSteps(partner.getLocation().getX(), partner.getLocation().getY() + 1, 25, true);
				break;
			case 2048: //north-east
				player.getMovement().addWalkSteps(partner.getLocation().getX() + 1, partner.getLocation().getY() + 1, 25, true);
				break;
			case 4096: //east
				player.getMovement().addWalkSteps(partner.getLocation().getX() + 1, partner.getLocation().getY(), 25, true);
				break;
			case 6144: //south-east
				player.getMovement().addWalkSteps(partner.getLocation().getX() + 1, partner.getLocation().getY() - 1, 25, true);
				break;
			case 8192: //south
				player.getMovement().addWalkSteps(partner.getLocation().getX(), partner.getLocation().getY() - 1, 25, true);
				break;
			case 10240: //south-west
				player.getMovement().addWalkSteps(partner.getLocation().getX() - 1, partner.getLocation().getY() - 1, 25, true);
				break;
			case 12288: //west
				player.getMovement().addWalkSteps(partner.getLocation().getX() - 1, partner.getLocation().getY(), 25, true);
				break;
			case 14336: //north-west
				player.getMovement().addWalkSteps(partner.getLocation().getX() - 1, partner.getLocation().getY() + 1, 25, true);
				break;
		}
		return true;
	}
	
	@Override
	public int processOnTicks(Player player) {
		return canContinue(player) ? 0 : -1;
	}
	
	@Override
	public void stop(Player player) {
		player.turnTo(null);
	}
	
	/**
	 * Verify that we can continue following the partner
	 *
	 * @param player
	 * 		The player
	 */
	private boolean canContinue(Player player) {
		return !(partner == null || !partner.isRenderable() || !World.get().getPlayers().contains(partner) || !partner.getLocation().isWithinDistance(player.getLocation()) || player.isFrozen());
	}
	
	/**
	 * Uses entity route strategizing to path to the partner
	 */
	private void traverseCalculatePath(Player player) {
		int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), new EntityStrategy(partner), false);
		if (steps == -1) {
			return;
		}
		int[] bufferX = RouteFinder.getLastPathBufferX();
		int[] bufferY = RouteFinder.getLastPathBufferY();
		
		Location last = new Location(bufferX[0], bufferY[0], player.getLocation().getPlane());
		player.getMovement().resetWalkSteps();
		player.getTransmitter().sendMinimapFlag(last.getLocalX(player.getLastLoadedLocation()), last.getLocalY(player.getLastLoadedLocation()));
		for (int step = steps - 1; step >= 0; step--) {
			if (!player.getMovement().addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
				break;
			}
		}
	}
	
}
