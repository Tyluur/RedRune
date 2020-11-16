package org.redrune.game.content.event.impl.item;

import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.context.item.ItemEventContext;
import org.redrune.game.module.interaction.rsinterface.DestroyItemInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.repository.item.ItemRepository;

import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class ItemDropEvent extends ItemEvent {
	
	/**
	 * Constructs a new event
	 */
	public ItemDropEvent() {
		super();
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, ItemEventContext context) {
		final Item item = context.getItem();
		// if for some reason the items in the slot dont match
		if (!Objects.equals(player.getInventory().getItems().get(context.getSlotId()), item)) {
			return;
		}
		// if its a destroyable item
		boolean destroy = item.getDefinitions().isLended() || item.getDefinitions().hasOption("destroy") || ItemRepository.isUntradeable(item.getId());
		// if the item should be destroyed we show the interface
		if (destroy) {
			// sends the type of management for the destroy interface
			// it will be used for different things
			player.putAttribute(AttributeKey.DESTROY_INTERFACE_TYPE, "delete");
			// sends the slot that the item was in
			player.putAttribute("destroy_slot_id", context.getSlotId());
			// shows the interface
			DestroyItemInteractionModule.show(player, item, "Are you sure you want to destroy this item?", "If you destroy this item, you will have to earn it again.");
			return;
		}
		// otherwise we just delete the item
		player.getInventory().deleteItem(context.getSlotId(), item);
		addFloorItem(player, item);
	}
	
	/**
	 * Handles the addition of the item to the floor
	 *
	 * @param player
	 * 		The player dropping the item
	 * @param item
	 * 		The item being dropped
	 */
	private void addFloorItem(Player player, Item item) {
		boolean shouldPublicize = false;
		boolean isEdible = false;
		if (item.getDefinitions().isEdible()) {
			isEdible = true;
		}
		// if we are in a pvp area we will auto publicize all items that aren't edible
		if (World.get().isPvpArea(player.getLocation())) {
			shouldPublicize = true;
		}
		// edible items should not be automatically public
		if (isEdible && shouldPublicize) {
			shouldPublicize = false;
		}
		if (shouldPublicize) {
			RegionManager.addPublicFloorItem(item.getId(), item.getAmount(), 200, player.getLocation());
		} else {
			RegionManager.addFloorItem(item.getId(), item.getAmount(), 200, player.getLocation(), player.getDetails().getUsername());
		}
	}
	
}
