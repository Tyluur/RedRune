package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.link.interaction.TradeInteraction;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class TradeInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(FIRST_TRADE_INTERFACE_ID, SECOND_TRADE_INTERFACE_ID, TRADE_INVENTORY_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (player.getInteractionManager().interactionIs(TradeInteraction.class)) {
			player.getInteractionManager().handleInterface(player, interfaceId, componentId, itemId, slotId, packetId);
			return true;
		}
		return true;
	}
}
