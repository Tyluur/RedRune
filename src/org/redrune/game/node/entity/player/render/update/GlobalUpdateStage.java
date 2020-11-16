package org.redrune.game.node.entity.player.render.update;

import org.redrune.game.node.entity.player.Player;

/**
 * @author Sean
 */
public enum GlobalUpdateStage {
	
	ADD_PLAYER,
	HEIGHT_UPDATED,
	MAP_REGION_DIRECTION,
	TELEPORTED;
	
	/**
	 * Gets the global update stages.
	 *
	 * @param player
	 * 		The player for the update,
	 * @param otherPlayer
	 * 		The players to update for.
	 * @return The state.
	 */
	public static GlobalUpdateStage getStage(Player player, Player otherPlayer) {
		if (otherPlayer == null || !otherPlayer.isRenderable()) {
			return null;
		} else if (player != otherPlayer && player.getLocation().isWithinDistance(otherPlayer.getLocation())) {
			return ADD_PLAYER;
		} else if (otherPlayer.getRenderInformation().getLastLocation() != null && otherPlayer.getLocation().getPlane() != otherPlayer.getRenderInformation().getLastLocation().getPlane()) {
			return HEIGHT_UPDATED;
		} else if (otherPlayer.teleporting() || otherPlayer.getRenderInformation().isOnFirstCycle()) {
			return TELEPORTED;
		}
		return null;
	}
}