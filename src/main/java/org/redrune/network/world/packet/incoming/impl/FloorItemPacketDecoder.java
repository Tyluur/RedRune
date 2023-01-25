package org.redrune.network.world.packet.incoming.impl;

import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.context.item.FloorItemPickupContext;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.content.event.impl.item.FloorItemPickupEvent;
import org.redrune.game.content.event.impl.item.FloorItemUsageEvent;
import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet id for picking up floor items
	 */
	private static final byte FLOOR_ITEM_PICKUP_ID = 24;
	
	/**
	 * The packet id for examining floor items
	 */
	private static final byte FLOOR_ITEM_EXAMINE_ID = 28;
	
	/**
	 * The packet id for using items on the floor
	 */
	private static final byte FLOOR_ITEM_USAGE_ID = 49;
	
	@Override
	public int[] bindings() {
		return arguments(FLOOR_ITEM_PICKUP_ID, FLOOR_ITEM_USAGE_ID, FLOOR_ITEM_EXAMINE_ID);
	}
	
	@SuppressWarnings("unused")
	@Override
	public void read(Player player, Packet packet) {
		int y = packet.readShort();
		int itemId = packet.readShort();
		int x = packet.readShort();
		boolean forceRun = packet.readByte() == 1;
		int regionId = Location.getRegionId(x, y);
		Optional<FloorItem> optional = RegionManager.getRegion(regionId).getFloorItem(itemId, x, y, player.getLocation().getPlane(), null);
		if (!optional.isPresent()) {
			System.out.println("No item");
			return;
		}
		FloorItem item = optional.get();
		if (!item.isRenderable()) {
			System.out.println("not renderable");
			return;
		}
		player.getMovement().reset(forceRun);
		// the distance from the two nodes
		final int distance = item.getLocation().getDistance(player.getLocation());
		// pickup
		switch (packet.getOpcode()) {
			case FLOOR_ITEM_PICKUP_ID:
				handleFloorItemPickup(player, item, distance);
				break;
			case FLOOR_ITEM_EXAMINE_ID:
				ItemEvent.handleItemExamining(player, item);
				break;
			case FLOOR_ITEM_USAGE_ID:
				handleFloorItemUsage(player, item, distance);
				break;
		}
	}
	
	/**
	 * Handles the picking up of a floor item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 * @param distance
	 * 		The distance between the two
	 */
	private void handleFloorItemPickup(Player player, FloorItem item, int distance) {
		switch (distance) {
			case 0:
				// we're right on top of the item, we don't need to walk
				EventRepository.executeEvent(player, FloorItemPickupEvent.class, new FloorItemPickupContext(item, distance));
				break;
			case 1:
				player.turnToLocation(item.getLocation());
				// we're one tile away from the item, we can still grab it if we're frozen
				if (player.isFrozen() || !player.getMovement().clippedProjectileToNode(item, true)) {
					player.sendAnimation(832);
					SystemManager.getScheduler().schedule(new ScheduledTask(2) {
						@Override
						public void run() {
							EventRepository.executeEvent(player, FloorItemPickupEvent.class, new FloorItemPickupContext(item, distance));
						}
					});
				} else {
					// otherwise we should just walk to it
					EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(item, () -> {
						EventRepository.executeEvent(player, FloorItemPickupEvent.class, new FloorItemPickupContext(item, distance));
					}));
				}
				break;
			default:
				player.turnToLocation(item.getLocation());
				// we're frozen and far away from the object, won't path
				if (player.isFrozen()) {
					player.getTransmitter().sendUnrepeatingMessages("A magical force prevents you from moving.");
					return;
				}
				// path to the event and when we've arrived, send the floor item pickup event
				EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(item, () -> {
					EventRepository.executeEvent(player, FloorItemPickupEvent.class, new FloorItemPickupContext(item, distance));
				}));
				break;
		}
	}
	
	/**
	 * Handles the usage option on a floor item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 * @param distance
	 * 		The distance between the two
	 */
	private void handleFloorItemUsage(Player player, FloorItem item, int distance) {
		// we're right on top of the item, we don't need to walk
		if (distance == 0) {
			EventRepository.executeEvent(player, FloorItemUsageEvent.class, new FloorItemPickupContext(item, distance));
		} else {
			// path to the event and when we've arrived, send the floor item pickup event
			EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(item, () -> {
				EventRepository.executeEvent(player, FloorItemUsageEvent.class, new FloorItemPickupContext(item, distance));
			}));
		}
	}
}
