package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.WalkEventContext;
import org.redrune.game.content.event.impl.WalkEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.route.RouteFinder;
import org.redrune.game.world.region.route.strategy.FixedTileStrategy;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class WalkPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The important opcodes
	 */
	private static final int MINIMAP_CLICK = 53, GAME_CLICK = 56;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(GAME_CLICK, MINIMAP_CLICK);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int steps = (packet.getLength() - 5) >> 1;
		if (steps > 25) {
			return;
		}
		// the destination y coordinate
		int y = packet.readLEShort();
		// if we should force run on
		boolean running = packet.readByteC() == 1;
		// the destination x coordinate
		int x = packet.readLEShortA();
		
		// when the player is frozen, we don't want to calculate pathfinding
		// in the case that they shouldn't move.
		if (player.isFrozen()) {
			player.getTransmitter().sendUnrepeatingMessages("A magical force prevents you from moving.");
			return;
		} else {
			player.unfreeze();
		}
		
		// calculates the amount of steps in the path
		int calculatedSteps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), player.getSize(), new FixedTileStrategy(x, y), true);
		// the buffer with the x steps
		int[] bufferX = RouteFinder.getLastPathBufferX();
		// the buffer with they steps
		int[] bufferY = RouteFinder.getLastPathBufferY();
		
		// if we're walking, we wait for it to stop bc render emote must happen first.
		if (player.getAttribute("walking", false)) {
			return;
		}
		
		// we can travel to the path, so we execute the event
		EventRepository.executeEvent(player, WalkEvent.class, new WalkEventContext(x, y, bufferX, bufferY, running, calculatedSteps));
	}
}
