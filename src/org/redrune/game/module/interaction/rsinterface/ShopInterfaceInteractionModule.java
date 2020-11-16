package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.market.shop.Shop;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.event.impl.item.ItemEvent;

import static org.redrune.game.content.market.shop.Shop.INTERFACE_ID;
import static org.redrune.game.content.market.shop.Shop.INVENTORY_INTERFACE_ID;
import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public class ShopInterfaceInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(INTERFACE_ID, INVENTORY_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		Shop shop = player.getAttribute("open_shop");
		if (shop == null) {
			return true;
		}
		if (interfaceId == INTERFACE_ID) {
			if (componentId == 25) {
				switch (packetId) {
					case FIRST_PACKET_ID:
						shop.value(player, slotId, false);
						break;
					case SECOND_PACKET_ID:
						shop.buy(player, slotId, 1);
						break;
					case THIRD_PACKET_ID:
						shop.buy(player, slotId, 5);
						break;
					case LAST_PACKET_ID:
						shop.buy(player, slotId, 10);
						break;
					case FIFTH_PACKET_ID:
						shop.buy(player, slotId, 50);
						break;
					case SIXTH_PACKET_ID:
						shop.buy(player, slotId, 500);
						break;
					case EXAMINE_PACKET_ID:
						ItemEvent.handleItemExamining(player, shop.getItem(slotId / 6));
						break;
				}
				return true;
			}
		} else if (interfaceId == INVENTORY_INTERFACE_ID) {
			if (componentId == 0) {
				switch (packetId) {
					case FIRST_PACKET_ID:
						shop.value(player, slotId, true);
						break;
					case SECOND_PACKET_ID:
						shop.sell(player, slotId, 1);
						break;
					case THIRD_PACKET_ID:
						shop.sell(player, slotId, 5);
						break;
					case LAST_PACKET_ID:
						shop.sell(player, slotId, 10);
						break;
					case FIFTH_PACKET_ID:
						shop.sell(player, slotId, 50);
						break;
					case EXAMINE_PACKET_ID:
						ItemEvent.handleItemExamining(player, player.getInventory().getItems().get(slotId));
						break;
				}
				return true;
			}
		}
		System.out.println("interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
}
