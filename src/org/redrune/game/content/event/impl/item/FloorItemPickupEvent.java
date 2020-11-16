package org.redrune.game.content.event.impl.item;

import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.context.item.FloorItemPickupContext;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.item.FloorItem;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class FloorItemPickupEvent extends Event<FloorItemPickupContext> {
	
	/**
	 * Constructs a new event
	 */
	public FloorItemPickupEvent() {
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, FloorItemPickupContext context) {
		final FloorItem contextFloorItem = context.getFloorItem();
		final Optional<FloorItem> optional = player.getRegion().getFloorItem(contextFloorItem.getId(), contextFloorItem.getLocation().getX(), contextFloorItem.getLocation().getY(), contextFloorItem.getLocation().getPlane(), null);
		if (!optional.isPresent()) {
			return;
		}
		FloorItem floorItem = optional.get();
		if (!player.getInventory().getItems().hasSpaceFor(floorItem)) {
			player.getTransmitter().sendMessage("You don't have enough inventory space for that item.");
			return;
		}
		floorItem.setRenderable(false);
		floorItem.getRegion().removeFloorItem(floorItem);
		player.getInventory().addItem(floorItem.getId(), floorItem.getAmount());
		player.sendAnimation(-1);
	}
	
	@Override
	public boolean canStart(Player player, FloorItemPickupContext context) {
		return !player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION);
	}
}
