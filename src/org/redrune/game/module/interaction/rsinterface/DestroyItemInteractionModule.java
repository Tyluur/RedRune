package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.packet.outgoing.impl.InterfaceItemBuilder;
import org.redrune.utility.AttributeKey;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class DestroyItemInteractionModule implements InterfaceInteractionModule {
	
	/**
	 * The id of the destroy interface
	 */
	public static final int INTERFACE_ID = 94;
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		// the yes option
		if (componentId == 3) {
			String destroyType = player.removeAttribute(AttributeKey.DESTROY_INTERFACE_TYPE, "");
			switch (destroyType) {
				case "delete":
					int destroySotId = player.removeAttribute("destroy_slot_id", -1);
					if (destroySotId == -1) {
						break;
					}
					Item item = player.getInventory().getItems().get(destroySotId);
					if (item == null) {
						break;
					}
					player.getInventory().deleteItem(destroySotId, item);
					break;
			}
		}
		// regardless of the result, interfaces should be closed.
		player.getManager().getInterfaces().closeAll();
		return true;
	}
	
	/**
	 * Shows the destroy item interface to the player
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item to destroy
	 * @param sureLine
	 * 		The line to show on the 'are you sure' component
	 * @param earnLine
	 * 		The line to show on the 'you have to earn this' component
	 */
	public static void show(Player player, Item item, String sureLine, String earnLine) {
		player.getManager().getInterfaces().sendChatboxInterface(INTERFACE_ID);
		player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, 8, item.getDefinitions().getName()).sendInterfaceText(INTERFACE_ID, 2, sureLine).sendInterfaceText(INTERFACE_ID, 7, earnLine);
		player.getTransmitter().send(new InterfaceItemBuilder(INTERFACE_ID, 9, item.getId(), 1).build(player));
	}
}
