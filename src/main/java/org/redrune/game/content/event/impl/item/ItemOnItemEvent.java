package org.redrune.game.content.event.impl.item;

import org.redrune.game.content.skills.firemaking.FiremakingAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.skills.firemaking.Fire;
import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.AnimationPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.context.item.ItemOnItemContext;
import org.redrune.game.node.item.Item;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class ItemOnItemEvent extends Event<ItemOnItemContext> {
	
	/**
	 * Constructs a new event
	 */
	public ItemOnItemEvent() {
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, ItemOnItemContext context) {
		int usedSlot = context.getUsedSlot();
		int withSlot = context.getWithSlot();
		
		Item usedItem = player.getInventory().getItems().get(usedSlot);
		Item withItem = player.getInventory().getItems().get(withSlot);
		if (usedItem == null || withItem == null) {
			return;
		}
		
		Fire fire = FiremakingAction.getFire(player, usedItem, withItem);
		if (fire != null) {
			player.getManager().getActions().startAction(new FiremakingAction(fire, false));
		} else {
			player.getTransmitter().sendMessage("Nothing interesting happens.");
		}
	}
}
