package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.rs.input.InputResponse;
import org.redrune.utility.rs.input.InputType;
import org.redrune.game.module.type.InterfaceInteractionModule;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class BankInterfaceInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(762, 763);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == 762) { // bank
			if (componentId == 93) {
				switch (packetId) {
					case NetworkConstants.FIRST_PACKET_ID:
						player.getBank().withdrawItem(slotId, 1);
						return true;
					case NetworkConstants.SECOND_PACKET_ID:
						player.getBank().withdrawItem(slotId, 5);
						return true;
					case NetworkConstants.THIRD_PACKET_ID:
						player.getBank().withdrawItem(slotId, 10);
						return true;
					case NetworkConstants.LAST_PACKET_ID:
						player.getBank().withdrawItem(slotId, player.getBank().getDetails().getLastX());
						return true;
					case NetworkConstants.FIFTH_PACKET_ID:
						player.getTransmitter().requestInput(input -> {
							player.getBank().withdrawItem(slotId, InputResponse.getInput(input));
							player.getBank().getDetails().setLastX(InputResponse.getInput(input));
							player.getBank().refreshLastX();
						}, InputType.INTEGER, "Enter amount:");
						return true;
					case NetworkConstants.SIXTH_PACKET_ID:
						player.getBank().withdrawItem(slotId, Integer.MAX_VALUE);
						return true;
					case NetworkConstants.FOURTH_PACKET_ID:
						player.getBank().withdrawItemButOne(slotId);
						return true;
					case NetworkConstants.EXAMINE_PACKET_ID:
						Item item = player.getBank().getItemInSlot(slotId);
						if (item == null) {
							return true;
						}
						ItemEvent.handleItemExamining(player, item);
						return true;
				}
			} else if (componentId == 15) {
				player.getBank().switchInsertItems();
				return true;
			} else if (componentId == 19) {
				player.getBank().switchWithdrawNotes();
				return true;
			} else if (componentId == 33) {
				player.getBank().depositAllInventory();
				return true;
			} else if (componentId == 35) {
				player.getBank().depositAllEquipment();
				return true;
			} else if (componentId == 37) {
				//player.getBank().depositAllBob(true);
				return true;
			} else if (componentId == 44) { // '?'
				
				return true;
			} else if (componentId >= 46 && componentId <= 62) {
				int tabId = 9 - ((componentId - 44) / 2);
				if (packetId == NetworkConstants.FIRST_PACKET_ID) {
					player.getBank().setCurrentTab(tabId);
				} else if (packetId == NetworkConstants.SECOND_PACKET_ID) {
					player.getBank().collapse(tabId);
				}
				return true;
			} else if (componentId == 43) {
				player.getManager().getInterfaces().closeAll();
			}
		} else if (interfaceId == 763) { // bank inventory
			switch (packetId) {
				case NetworkConstants.FIRST_PACKET_ID:
					player.getBank().depositItem(slotId, 1, true);
					return true;
				case NetworkConstants.SECOND_PACKET_ID:
					player.getBank().depositItem(slotId, 5, true);
					return true;
				case NetworkConstants.THIRD_PACKET_ID:
					player.getBank().depositItem(slotId, 10, true);
					return true;
				case NetworkConstants.LAST_PACKET_ID:
					player.getBank().depositItem(slotId, player.getBank().getDetails().getLastX(), true);
					return true;
				case NetworkConstants.FIFTH_PACKET_ID:
					player.getTransmitter().requestInput(input -> {
						player.getBank().depositItem(slotId, InputResponse.getInput(input), true);
						player.getBank().getDetails().setLastX(InputResponse.getInput(input));
						player.getBank().refreshLastX();
					}, InputType.INTEGER, "Enter amount:");
					return true;
				case NetworkConstants.SIXTH_PACKET_ID:
					player.getBank().depositItem(slotId, Integer.MAX_VALUE, true);
					return true;
				case NetworkConstants.EXAMINE_PACKET_ID:
					Item item = player.getInventory().getItems().get(slotId);
					if (item == null) {
						return true;
					}
					ItemEvent.handleItemExamining(player, item);
					return true;
			}
		}
		System.out.println("interfaceId = [" + interfaceId + "], componentId = [" + componentId + "], itemId = [" + itemId + "], slotId = [" + slotId + "], packetId = [" + packetId + "]");
		return true;
	}
}
