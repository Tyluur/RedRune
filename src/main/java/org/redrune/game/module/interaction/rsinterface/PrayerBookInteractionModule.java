package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public class PrayerBookInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(271);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		player.getManager().getPrayers().handlePrayerSettings(componentId, slotId);
		return true;
	}
}
