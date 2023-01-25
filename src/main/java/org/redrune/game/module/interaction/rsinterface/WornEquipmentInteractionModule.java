package org.redrune.game.module.interaction.rsinterface;

import lombok.Getter;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.event.context.item.ItemRemovalContext;
import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.content.event.impl.item.ItemRemovalEvent;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.tool.Misc;
import org.redrune.game.content.event.EventRepository;
import org.redrune.utility.rs.constant.EquipConstants;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class WornEquipmentInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(387);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (componentId == 39) { // stats
			BonusesInterfaceInteractionModule.show(player);
		} else if (componentId == 42) { // prices
			player.getManager().getInterfaces().sendInterface(206, false);
		} else if (componentId == 45) { // ikod
			player.getManager().getInterfaces().sendInterface(17, false);
		} else {
			// only thing left is item interaction
			Optional<SlotAction> optional = SlotAction.getSlotAction(componentId);
			if (!optional.isPresent()) {
				return true;
			}
			SlotAction action = optional.get();
			switch (packetId) {
				case NetworkConstants.FIRST_PACKET_ID:
					EventRepository.executeEvent(player, ItemRemovalEvent.class, new ItemRemovalContext(action.getEquipmentSlot()));
					return true;
				case NetworkConstants.EXAMINE_PACKET_ID:
					ItemEvent.handleItemExamining(player, player.getEquipment().getItem(action.getEquipmentSlot()));
					return true;
			}
		}
		System.out.println("interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
	
	private enum SlotAction {
		
		HAT(8, EquipConstants.SLOT_HAT),
		CAPE(11, EquipConstants.SLOT_CAPE),
		AMULET(14, EquipConstants.SLOT_AMULET),
		ARROWS(38, EquipConstants.SLOT_ARROWS),
		WEAPON(17, EquipConstants.SLOT_WEAPON),
		CHEST(20, EquipConstants.SLOT_CHEST),
		SHIELD(23, EquipConstants.SLOT_SHIELD),
		LEGS(26, EquipConstants.SLOT_LEGS),
		HANDS(29, EquipConstants.SLOT_HANDS),
		FEET(32, EquipConstants.SLOT_FEET),
		RING(35, EquipConstants.SLOT_RING),
		AURA(50, EquipConstants.SLOT_AURA);
		
		@Getter
		private final int buttonId;
		
		@Getter
		private final int equipmentSlot;
		
		SlotAction(int buttonId, int equipmentSlot) {
			this.buttonId = buttonId;
			this.equipmentSlot = equipmentSlot;
		}
		
		/**
		 * Gets a slot action for the button clicked
		 *
		 * @param buttonId
		 * 		The button
		 */
		public static Optional<SlotAction> getSlotAction(int buttonId) {
			return Arrays.stream(SlotAction.values()).filter(p -> p.buttonId == buttonId).findFirst();
		}
		
		/**
		 * Handles other packet options
		 *
		 * @param player
		 * 		The player
		 * @param itemId
		 * 		The id of the item clicked
		 * @param packetId
		 * 		The id of the packet
		 */
		public boolean handleOtherOption(Player player, int itemId, int packetId) {
			return false;
		}
	}
}
