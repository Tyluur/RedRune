package org.redrune.game.module.interaction.npc;

import org.redrune.game.module.interaction.rsinterface.TeleportationInterfaceModule;
import org.redrune.game.module.interaction.rsinterface.TeleportationInterfaceModule.TransportationLocation;
import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class TeleportationWizardInteractionModule implements NPCInteractionModule {
	
	@Override
	public int[] npcSubscriptionIds() {
		return arguments(14332);
	}
	
	@Override
	public boolean handle(Player player, NPC npc, InteractionOption option) {
		if (option == InteractionOption.FIRST_OPTION) {
			TeleportationInterfaceModule.displaySelectionInterface(player, true);
		} else if (option == InteractionOption.SECOND_OPTION) {
			TransportationLocation last = player.getVariables().getAttribute(AttributeKey.LAST_SELECTED_TELEPORT);
			if (last == null) {
				return true;
			}
			TeleportationInterfaceModule.teleportPlayer(player, last.getDestination(), () -> last.getLocations().handlePostTeleportation(player, last.getOptionIndex()));
		}
		return true;
	}
}
