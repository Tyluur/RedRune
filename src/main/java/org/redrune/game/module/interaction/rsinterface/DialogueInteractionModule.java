package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.type.InterfaceInteractionModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class DialogueInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(740);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case 740:
				if (componentId == 3) {
					player.getManager().getInterfaces().closeChatboxInterface();
				}
				break;
		}
		return true;
	}
}
