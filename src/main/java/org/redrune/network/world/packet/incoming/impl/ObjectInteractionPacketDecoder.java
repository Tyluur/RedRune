package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;
import org.redrune.game.GameFlags;
import org.redrune.game.node.Location;
import org.redrune.game.content.event.context.ObjectEventContext;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.content.event.impl.ObjectEvent;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.RegionDeletion;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.rs.InteractionOption;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class ObjectInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The opcode for the interface on object packet
	 */
	public static final int INTERFACE_ON_OBJECT_OPCODE = 42;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(1, 39, 86, 58, 38, 75, INTERFACE_ON_OBJECT_OPCODE);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		if (packet.getOpcode() == INTERFACE_ON_OBJECT_OPCODE) {
			handleInterfaceOnObjectPacket(player, packet);
		} else {
			int y = packet.readShortA();
			int x = packet.readLEShortA();
			int id = packet.readLEShort();
			boolean forceRun = packet.readByte() == 1;
			int packetId = packet.getOpcode();
			int regionId = Location.getRegionId(x, y);
			
			Optional<GameObject> optional = RegionManager.getRegion(regionId).findAnyGameObject(id, x, y, player.getLocation().getPlane(), -1);
			if (!optional.isPresent()) {
				System.out.println(id + ", " + x + ", " + y + " [ " + regionId + "]: N/A");
				return;
			}
			GameObject object = optional.get();
			InteractionOption option = getOption(packetId);
			if (option == null) {
				throw new IllegalStateException("Unexpected packet id " + packetId + ", could not find option.");
			}
			switch (option) {
				// TODO object examines
				case EXAMINE:
					if (player.getAttribute("remove_spawns", false)) {
						player.getRegion().removeObject(object);
						RegionDeletion.dumpObject(object);
					} else {
						if (GameFlags.debugMode) {
							player.getTransmitter().sendMessage("Examining: [" + object.getDefinitions().getName() + ": " + id + ", [" + x + ", " + y + ", " + regionId + "], " + object.getType() + ", " + object.getRotation(), true);
							System.out.println(object.toGameObject());
						}
					}
					break;
				default:
					player.getMovement().reset(forceRun);
					EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(object, () -> {
						// executing the object interaction event
						EventRepository.executeEvent(player, ObjectEvent.class, new ObjectEventContext(object, option));
					}));
					break;
			}
		}
	}
	
	/**
	 * Handles the interact on object packet interaction
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void handleInterfaceOnObjectPacket(Player player, Packet packet) {
		final int y = packet.readLEShortA();
		final int x = packet.readShort();
		final int itemSlot = packet.readByte();
		packet.readByte(); // unknown
		final int fromInterface = packet.readLEShort();
		packet.readShort(); // unknown
		packet.readShort(); // item id
		int runningFlag = packet.readByte();
		final int objectId = packet.readLEShortA();
		
		final int regionId = Location.getRegionId(x, y);
		
		Optional<GameObject> optional = RegionManager.getRegion(regionId).findAnyGameObject(objectId, x, y, player.getLocation().getPlane(), -1);
		if (!optional.isPresent()) {
			System.out.println(objectId + ", " + x + ", " + y + " [ " + regionId + "]: N/A");
			return;
		}
		GameObject object = optional.get();
		boolean forceRun = runningFlag > 0;
		
		if (fromInterface == 679) {
			Item item = player.getInventory().getItems().get(itemSlot);
			if (item == null) {
				return;
			}
			player.getMovement().reset(forceRun);
			EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(object, () -> {
				// executing the object interaction event on arrival
				EventRepository.executeEvent(player, ObjectEvent.class, new ObjectEventContext(object, InteractionOption.ITEM_ON_OBJECT, item));
			}));
		}
	}
	
	/**
	 * Gets the option that the player clicked by the packet id
	 *
	 * @param packetId
	 * 		The packet id
	 */
	private InteractionOption getOption(int packetId) {
		switch (packetId) {
			case 1:
				return InteractionOption.FIRST_OPTION;
			case 39:
				return InteractionOption.SECOND_OPTION;
			case 86:
				return InteractionOption.THIRD_OPTION;
			case 58:
				return InteractionOption.FOURTH_OPTION;
			case 38:
				return InteractionOption.FIFTH_OPTION;
			case 75:
				return InteractionOption.EXAMINE;
			default:
				return null;
		}
	}
}
