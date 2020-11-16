package org.redrune.network.world.packet.incoming.impl;

import org.redrune.cache.CacheFileStore;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.item.ItemOnItemContext;
import org.redrune.game.content.event.impl.item.ItemOnItemEvent;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class InterfaceClickPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(85, 7, 66, 11, 48, 17, 84, 40, 25, 8, 54, 26);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		try {
			switch (packet.getOpcode()) {
				case 8:
					decodeDialoguePacket(player, packet);
					break;
				case 26:
					decodeItemOnItemPacket(player, packet);
					break;
				default:
					int clickData = packet.readLEInt();
					int interfaceId = clickData & 0xFFF;
					int componentId = clickData >> 16;
					int itemId = packet.readShortA();
					int slotId = packet.readShortA();
					if (itemId == 65535) {
						itemId = -1;
					}
					if (slotId == 65535) {
						slotId = -1;
					}
					if (interfaceId > CacheFileStore.getInterfaceDefinitionsSize()) {
						System.out.println("Unable to handle interface post-decoding! (" + interfaceId + ", " + componentId + ") [packetId=" + packet.getOpcode() + "]");
						return;
					}
					if (!player.getManager().getInterfaces().hasInterfaceOpen(interfaceId)) {
						System.out.println( "Interface " + interfaceId + ", [" + componentId + "] was not existent in the player's mapping of opened interface.");
						return;
					}
					if (ModuleRepository.handle(player, interfaceId, componentId, itemId, slotId, packet.getOpcode())) {
						return;
					}
					StringBuilder bldr = new StringBuilder("[interfaceId=" + interfaceId + ", componentId=" + componentId + "");
					bldr.append(itemId == -1 ? "" : ", itemId=" + itemId + "");
					bldr.append(slotId == -1 ? "" : ", slotId=" + slotId + "");
					bldr.append(", packetId=").append(packet.getOpcode()).append("]");
					System.out.println(bldr.toString());
					break;
			}
		} catch (Exception e) {
			System.out.println("Error reading packet: " + packet.getOpcode());
			e.printStackTrace();
		}
	}
	
	/**
	 * Decodes an incoming dialogue
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void decodeDialoguePacket(Player player, Packet packet) {
		int interfaceHash = packet.readInt2();
		packet.readLEShortA();
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash & 0xFF;
		if (interfaceId > CacheFileStore.getInterfaceDefinitionsSize()) {
			System.out.println("Unable to handle interface post-decoding! (" + interfaceId + ", " + componentId + ")");
			return;
		}
		if (!player.getManager().getInterfaces().hasInterfaceOpen(interfaceId)) {
			System.out.println("Interface " + interfaceId + ", [" + componentId + "] was not existent in the player's mapping of opened interface.");
			return;
		}
		if (ModuleRepository.handle(player, interfaceId, componentId, -1, -1, packet.getOpcode())) {
			return;
		}
		player.getManager().getDialogues().handleOption(interfaceId, componentId);
		System.out.println("[interfaceId=" + interfaceId + ", componentId=" + componentId + "" + ", packetId=" + packet.getOpcode() + "]");
	}
	
	/**
	 * Decodes the packet that is sent when two items are used together
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void decodeItemOnItemPacket(Player player, Packet packet) {
		int usedWithId = packet.readLEShortA();
		int usedWithSlot = packet.readLEShortA();
		int usedSlot = packet.readLEShortA();
		int hash1 = packet.readLEInt();
		int hash2 = packet.readLEInt();
		int usedId = packet.readShortA();

		Item itemUsed = player.getInventory().getItems().get(usedSlot);
		if (itemUsed == null) {
			return;
		}
		Item usedWith = player.getInventory().getItems().get(usedWithSlot);
		if (usedWith == null) {
			return;
		}
		if (usedId != itemUsed.getId() || usedWithId != usedWith.getId()) {
			System.out.println("Error in parsing item on item...");
			return;
		}

		EventRepository.executeEvent(player, ItemOnItemEvent.class, new ItemOnItemContext(usedSlot, usedWithSlot));
	}
}
