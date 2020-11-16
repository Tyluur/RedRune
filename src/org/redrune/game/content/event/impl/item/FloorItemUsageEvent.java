package org.redrune.game.content.event.impl.item;

import org.redrune.game.content.event.context.item.FloorItemPickupContext;
import org.redrune.game.content.skills.firemaking.Fire;
import org.redrune.game.content.skills.firemaking.FiremakingAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.FloorItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/30/2017
 */
public class FloorItemUsageEvent extends FloorItemPickupEvent {
	
	@Override
	public void run(Player player, FloorItemPickupContext context) {
		FloorItem item = context.getFloorItem();
		
		// the fire instance
		Fire fire = Fire.getFireInstance(item.getId());
		// the only instance this is applicable for is fires & hunter traps
		// http://i.imgur.com/Lz20xAa.gifv [data]
		if (fire == null) {
			return;
		}
		player.getManager().getActions().startAction(new FiremakingAction(fire, true));
	}
}
