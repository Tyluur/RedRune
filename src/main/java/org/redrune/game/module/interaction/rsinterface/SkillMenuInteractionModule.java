package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class SkillMenuInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(499);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		int skillMenu = player.getAttribute(AttributeKey.SKILL_MENU, -1);
		if (componentId >= 10 && componentId <= 25) {
			player.getTransmitter().send(new ConfigPacketBuilder(965, ((componentId - 10) * 1024) + skillMenu).build(player));
		} else if (componentId == 29) {
			//
		}
		return true;
	}
}
