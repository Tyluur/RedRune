package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.InterfaceConstants;
import org.redrune.game.module.type.InterfaceInteractionModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class WorldMapInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(755);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 44) {
			player.getManager().getInterfaces().sendWindowPane(player.getManager().getInterfaces().usingFixedMode() ? InterfaceConstants.SCREEN_FIXED_WINDOW_ID : InterfaceConstants.SCREEN_RESIZABLE_WINDOW_ID, 2);
		} else {
			System.out.println("componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		}
		return true;
	}
}
