package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.link.EmoteManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class EmoteInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(590);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 8) {
			EmoteManager.handleEmote(player, slotId);
			return true;
		}
		//		System.out.println("player = [" + player + "], interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return false;
	}
}
