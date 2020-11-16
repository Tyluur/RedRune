package org.redrune.game.module.interaction.object;

import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportationSpellEvent;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.Coordinates;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class EdgevilleLeverInteractionModule implements ObjectInteractionModule {
	
	@Override
	public int[] objectSubscriptionIds() {
		return arguments(1814, 1815);
	}
	
	@Override
	public boolean handle(Player player, GameObject object, InteractionOption option) {
		switch(object.getId()) {
			case 1814:
				TeleportationSpellEvent.sendLeverTeleport(player, Coordinates.DESERTED_KEEP);
				break;
			case 1815:
				TeleportationSpellEvent.sendLeverTeleport(player, Coordinates.EDGEVILLE_LEVER);
				break;
		}
		return true;
	}
}
