package org.redrune.game.node.entity.player.render.update;

import org.redrune.game.node.entity.player.Player;

/**
 * @author Sean
 * @author Tyluur <itstyluur@gmail.com>
 */
public enum LocalUpdateStage {
	
	REMOVE_PLAYER,
	WALKING,
	RUNNING,
	TELEPORTED,
	NO_UPDATE;
	
	/**
	 * Gets the local player updating stages.
	 *
	 * @param player
	 * 		The player for the update.
	 * @param otherPlayer
	 * 		The player to update for.
	 * @return The stage of a certain update.
	 */
	public static LocalUpdateStage getStage(Player player, Player otherPlayer) {
		if (otherPlayer == null || !player.getLocation().isWithinDistance(otherPlayer.getLocation())) {
			return REMOVE_PLAYER;
		} else if (otherPlayer.teleporting()) {
			return TELEPORTED;
		} else if (otherPlayer.getMovement().getNextRunDirection() != -1) {
			return RUNNING;
		} else if (otherPlayer.getMovement().getNextWalkDirection() != -1) {
			return WALKING;
		}
		return otherPlayer.getUpdateMasks().isUpdateRequired() ? NO_UPDATE : null;
	}
}
