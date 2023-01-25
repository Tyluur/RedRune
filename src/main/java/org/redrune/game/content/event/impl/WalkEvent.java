package org.redrune.game.content.event.impl;

import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.context.WalkEventContext;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class WalkEvent extends Event<WalkEventContext> {
	
	/**
	 * Constructs a new event
	 */
	public WalkEvent() {
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
	}
	
	@Override
	public void run(Player player, WalkEventContext context) {
		int[] bufferX = context.getBufferX();
		int[] bufferY = context.getBufferY();
		int steps = context.getSteps();
		player.getMovement().reset(context.isRunning());
		int last = -1;
		for (int i = steps - 1; i >= 0; i--) {
			if (!player.getMovement().addWalkSteps(bufferX[i], bufferY[i], 25, true)) {
				break;
			}
			last = i;
		}
		
		if (last != -1) {
			Location tile = new Location(bufferX[last], bufferY[last], player.getLocation().getPlane());
			player.getTransmitter().sendMinimapFlag(tile.getLocalX(player.getLastLoadedLocation()), tile.getLocalY(player.getLastLoadedLocation()));
		} else {
			player.getTransmitter().sendMinimapFlagReset();
		}
	}
	
	@Override
	public boolean canStart(Player player, WalkEventContext context) {
		return !player.getManager().getLocks().isLocked(LockType.MOVEMENT);
	}
}