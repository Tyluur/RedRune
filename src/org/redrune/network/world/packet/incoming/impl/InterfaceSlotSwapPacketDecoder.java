package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.rs.constant.InterfaceConstants;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class InterfaceSlotSwapPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(71);
	}
	
	@SuppressWarnings("unused")
	@Override
	
	public void read(Player player, Packet packet) {
		int fromItemId = packet.readShort();
		int toHash = packet.readInt2();
		int fromSlot = packet.readShort();
		int toSlot = packet.readShort();
		int toItemId = packet.readShort();
		int interfaceHash = packet.readInt();
		
		int fromInterface = interfaceHash >> 16;
		int fromChild = interfaceHash & 0xff;
		int toChild = toHash & 0xFFFF;
		int toInterface = toHash >> 16;
		
		//		System.out.println(fromItemId + "," + toHash + "," + fromSlot+ "," + toSlot + "," + toItemId +"," + interfaceHash + "," + fromInterface + "," + fromChild + "," + toChild + "," + toInterface);
		
		if (fromInterface == InterfaceConstants.INVENTORY_INTERFACE_ID && toInterface == InterfaceConstants.INVENTORY_INTERFACE_ID && toChild == 0) {
			toSlot -= 28;
			if (player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION) || toSlot < 0 || toSlot >= player.getInventory().getItems().getSize() || fromSlot >= player.getInventory().getItems().getSize()) {
				return;
			}
			player.getManager().getInterfaces().closeAll();
			player.getInventory().switchItem(fromSlot, toSlot);
		} else if (fromInterface == 762) {
			player.getBank().switchItem(fromSlot, toSlot, toChild);
		}
	}
}
