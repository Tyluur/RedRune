package org.redrune.game.node.entity.link.interaction;

import org.redrune.game.content.event.EventListener;
import org.redrune.game.content.event.EventListener.EventType;
import org.redrune.game.content.event.impl.item.ItemEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.world.packet.outgoing.impl.*;
import org.redrune.utility.rs.input.InputResponse;
import org.redrune.utility.rs.input.InputType;
import org.redrune.utility.tool.Misc;

import java.util.Arrays;
import java.util.Objects;

import static org.redrune.network.NetworkConstants.*;
import static org.redrune.utility.rs.constant.InterfaceConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class TradeInteraction extends Interaction {
	
	/**
	 * The parameters used in the cs2script.
	 */
	private static final Object[] PARAMS = new Object[] { "", "", "", "Value<col=FF9040>", "Remove-X", "Remove-All", "Remove-10", "Remove-5", "Remove", -1, 0, 7, 4, 90, 335 << 16 | 31 };
	
	/**
	 * The parameters used in the second cs2script.
	 */
	private static final Object[] PARAMS_1 = new Object[] { "", "", "Lend", "Value<col=FF9040>", "Offer-X", "Offer-All", "Offer-10", "Offer-5", "Offer", -1, 0, 7, 4, 93, 336 << 16 };
	
	/**
	 * The parameters used in the third cs2script.
	 */
	private static final Object[] PARAMS_2 = new Object[] { "", "", "", "", "", "", "", "", "Value<col=FF9040>", -1, 0, 7, 4, 90, 335 << 16 | 34 };
	
	/**
	 * The items the source player is offering
	 */
	private final ItemsContainer<Item> sourceItems;
	
	/**
	 * The items the source player is offering
	 */
	private final ItemsContainer<Item> targetItems;
	
	/**
	 * The state of the trade
	 */
	private TradeState state;
	
	/**
	 * If the source accepted
	 */
	private boolean sourceAccepted;
	
	/**
	 * If the target accepted
	 */
	private boolean targetAccepted;
	
	/**
	 * If the trade is complete
	 */
	private boolean complete;
	
	public TradeInteraction(Entity source, Entity target) {
		super(source, target);
		this.sourceItems = new ItemsContainer<>(28, false);
		this.targetItems = new ItemsContainer<>(28, false);
		source.putAttribute("trade-container", sourceItems);
		target.putAttribute("trade-container", targetItems);
		this.state = TradeState.ADDING_ITEMS;
	}
	
	@Override
	public void start() {
		open(source.toPlayer(), target.toPlayer());
		open(target.toPlayer(), source.toPlayer());
	}
	
	@Override
	public void request() {
		source.toPlayer().getTransmitter().sendMessage("Sending trade request...");
		target.toPlayer().getTransmitter().requestInteraction(100, "wishes to trade with you.", getSource().toPlayer().getDetails().getDisplayName());
	}
	
	@Override
	public void end() {
		close(source.toPlayer());
		close(target.toPlayer());
	}
	
	@Override
	public boolean canRequest() {
		return target.toPlayer().getManager().getInterfaces().getScreenInterface() == -1;
	}
	
	@Override
	public void handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interfaceId == FIRST_TRADE_INTERFACE_ID) {
			if (componentId == 31) {
				switch (packetId) {
					case FIRST_PACKET_ID:
						removeItem(player, slotId, 1);
						return;
					case SECOND_PACKET_ID:
						removeItem(player, slotId, 5);
						return;
					case THIRD_PACKET_ID:
						removeItem(player, slotId, 10);
						return;
					case LAST_PACKET_ID:
						removeItem(player, slotId, Integer.MAX_VALUE);
						return;
					case FIFTH_PACKET_ID:
						player.getTransmitter().requestInput(input -> removeItem(player, slotId, InputResponse.getInput(input)), InputType.INTEGER, "Enter amount:");
						return;
					case SIXTH_PACKET_ID: // value
						return;
					case EXAMINE_PACKET_ID:
						ItemsContainer<Item> container = player.getAttribute("trade-container");
						if (container == null) {
							return;
						}
						ItemEvent.handleItemExamining(player, container.get(slotId));
				}
			} else if (componentId == 18) {
				decline(player);
			} else if (componentId == 16) {
				accept(player);
			}
		} else if (interfaceId == SECOND_TRADE_INTERFACE_ID) {
			switch (componentId) {
				case 21:
					accept(player);
					break;
				case 22:
					decline(player);
					break;
			}
		} else if (interfaceId == TRADE_INVENTORY_INTERFACE_ID) {
			switch (componentId) {
				case 0:
					switch (packetId) {
						case FIRST_PACKET_ID:
							offerItem(player, slotId, 1);
							break;
						case SECOND_PACKET_ID:
							offerItem(player, slotId, 5);
							break;
						case THIRD_PACKET_ID:
							offerItem(player, slotId, 10);
							break;
						case LAST_PACKET_ID:
							offerItem(player, slotId, Integer.MAX_VALUE);
							break;
						case FIFTH_PACKET_ID:
							player.getTransmitter().requestInput(input -> offerItem(player, slotId, InputResponse.getInput(input)), InputType.INTEGER, "Enter amount:");
							break;
						case SIXTH_PACKET_ID: // value
							break;
						case FOURTH_PACKET_ID: // lend
							break;
						case EXAMINE_PACKET_ID:
							ItemEvent.handleItemExamining(player, player.getInventory().getItems().get(slotId));
							break;
					}
					break;
			}
		}
	}
	
	/**
	 * Closes the trade interaction
	 *
	 * @param player
	 * 		The player
	 */
	private void close(Player player) {
		ItemsContainer<Item> container = player.getAttribute("trade-container");
		
		player.turnTo(null);
		player.getInteractionManager().end();
		player.getManager().getInterfaces().closeAll();
		player.removeAttribute("trade-container");
		
		if (container == null || container.size() <= 0) {
			return;
		}
		player.getInventory().getItems().addAll(container);
		player.getInventory().refreshAll();
		container.clear();
	}
	
	/**
	 * Refreshes the amount of free slots we have to our partner
	 *
	 * @param source
	 * 		The player who added/removed items
	 */
	private void refreshFreeSlots(Player source) {
		Player player = (source.equals(this.source) ? target : this.source).toPlayer();
		int freeSlots = source.getInventory().getItems().getFreeSlots();
		player.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 21, "has " + (freeSlots == 0 ? "no" : freeSlots) + " free" + "<br>inventory slots");
	}
	
	/**
	 * Opens the first trade state for the player
	 *
	 * @param player
	 * 		The player
	 * @param partner
	 * 		The partner
	 */
	private void open(Player player, Player partner) {
		player.turnTo(partner);
		String name = partner.getDetails().getDisplayName();
		
		player.getTransmitter().send(new CS2StringBuilder(203, name).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(FIRST_TRADE_INTERFACE_ID, 31, 0, 1150, 0, 27).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(FIRST_TRADE_INTERFACE_ID, 34, 0, 1026, 0, 27).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(TRADE_INVENTORY_INTERFACE_ID, 0, 0, 1278, 0, 27).build(player));
		
		player.getTransmitter().send(new CS2ScriptBuilder(695, "IviiiIsssssssss", PARAMS_2).build(player));
		player.getTransmitter().send(new CS2ScriptBuilder(150, "IviiiIsssssssss", PARAMS_1).build(player));
		player.getTransmitter().send(new CS2ScriptBuilder(150, "IviiiIsssssssss", PARAMS).build(player));
		
		player.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 15, "Trading with: " + name);
		player.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 37, "");
		player.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 22, name);
		
		player.getTransmitter().send(new ContainerPacketBuilder(90, new Item[0]).build(player));
		player.getTransmitter().send(new ContainerPacketBuilder(90, new Item[0], true).build(player));
		
		player.getManager().getInterfaces().sendInterface(FIRST_TRADE_INTERFACE_ID, true).sendInventoryInterface(TRADE_INVENTORY_INTERFACE_ID);
		
		refreshFreeSlots(player);
		refreshTradeWealth();
		
		EventListener.setListener(player, this::end, EventType.MOVE, EventType.DAMAGE, EventType.SCREEN_INTERFACE_CLOSE);
	}
	
	/**
	 * Handles the requesting of a trade
	 *
	 * @param player
	 * 		The player
	 * @param target
	 * 		The player that was requested to trade
	 */
	public static void handleTradeRequesting(Player player, Player target) {
		player.turnTo(target);
		Entity lastRequested = target.getInteractionManager().getLastRequested(TradeInteraction.class);
		if (Objects.equals(lastRequested, player)) {
			player.getInteractionManager().startInteraction(new TradeInteraction(player, target));
		} else {
			player.getInteractionManager().requestInteraction(new TradeInteraction(player, target));
		}
	}
	
	private void decline(Player player) {
		close(player);
		Entity partner = player.equals(source) ? target : source;
		Player p2 = partner.toPlayer();
		close(p2);
	}
	
	private void accept(Player player) {
		boolean source = player.equals(this.source);
		if (source) {
			this.sourceAccepted = true;
		} else {
			this.targetAccepted = true;
		}
		if (sourceAccepted && targetAccepted) {
			nextStage();
		} else {
			refreshAcceptMessages();
		}
	}
	
	private void refreshAcceptMessages() {
		String sourceMessage = getTradeMessage(true);
		String targetMessage = getTradeMessage(false);
		
		Player source = this.source.toPlayer();
		Player target = this.target.toPlayer();
		
		switch (state) {
			case ADDING_ITEMS:
				source.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 37, sourceMessage);
				target.getManager().getInterfaces().sendInterfaceText(FIRST_TRADE_INTERFACE_ID, 37, targetMessage);
				break;
			case CONFIRMING:
				source.getManager().getInterfaces().sendInterfaceText(SECOND_TRADE_INTERFACE_ID, 34, sourceMessage);
				target.getManager().getInterfaces().sendInterfaceText(SECOND_TRADE_INTERFACE_ID, 34, targetMessage);
				break;
		}
	}
	
	private void sendFlash(Player changer, int slot) {
		Player target = (this.source.equals(changer) ? this.target : this.source).toPlayer();
		target.getTransmitter().sendSlotFlash(FIRST_TRADE_INTERFACE_ID, 33, 4, 7, slot);
	}
	
	/**
	 * Gets the trade message
	 *
	 * @param source
	 * 		If the message is for the source player
	 */
	private String getTradeMessage(boolean source) {
		if (source) {
			if (sourceAccepted) {
				return "Waiting for other player...";
			} else if (targetAccepted) {
				return "Other player has accepted.";
			}
		} else {
			if (targetAccepted) {
				return "Waiting for other player...";
			} else if (sourceAccepted) {
				return "Other player has accepted.";
			}
		}
		return state == TradeState.ADDING_ITEMS ? "" : "Are you sure you want to make this trade?";
	}
	
	/**
	 * Cancels the accept flags
	 */
	private void cancelAccept() {
		this.sourceAccepted = this.targetAccepted = false;
		refreshAcceptMessages();
	}
	
	/**
	 * Offers the item in the slot to the container
	 *
	 * @param player
	 * 		The player offering the item
	 * @param inventorySlotId
	 * 		The slot
	 * @param amount
	 * 		The amount
	 */
	private void offerItem(Player player, int inventorySlotId, int amount) {
		ItemsContainer<Item> container = player.getAttribute("trade-container");
		if (container == null) {
			return;
		}
		Item item = player.getInventory().getItems().get(inventorySlotId);
		if (item == null) {
			return;
		}
		if (amount <= 0) {
			amount = 1;
		}
		int realAmount = amount > item.getAmount() ? item.getAmount() : amount;
		Item offer = new Item(item.getId(), realAmount);
		container.add(offer);
		player.getInventory().deleteItem(offer.getId(), offer.getAmount());
		refreshItems(player);
		refreshFreeSlots(player);
		cancelAccept();
	}
	
	/**
	 * Removes the item from the player's container
	 *
	 * @param player
	 * 		The player removing the item
	 * @param tradeSlotId
	 * 		The slot of the item in the trade container
	 * @param amount
	 * 		The amount of the item to remove
	 */
	private void removeItem(Player player, int tradeSlotId, int amount) {
		ItemsContainer<Item> container = player.getAttribute("trade-container");
		if (container == null) {
			return;
		}
		Item item = container.get(tradeSlotId);
		if (item == null) {
			return;
		}
		if (amount <= 0) {
			amount = 1;
		}
		int realAmount = amount > item.getAmount() ? item.getAmount() : amount;
		boolean delete = item.getAmount() - realAmount <= 0;
		if (delete) {
			container.remove(item);
			player.getInventory().addItem(item);
		} else {
			container.set(tradeSlotId, new Item(item.getId(), item.getAmount() - realAmount));
			player.getInventory().addItem(item.getId(), realAmount);
		}
		sendFlash(player, tradeSlotId);
		refreshItems(player);
		refreshFreeSlots(player);
		cancelAccept();
	}
	
	/**
	 * Refreshes the amount of wealth in the trade session
	 */
	public void refreshTradeWealth() {
		Player source = this.source.toPlayer();
		Player target = this.target.toPlayer();
		
		source.getTransmitter().send(new CS2ConfigBuilder(729, 0).build(source));
		source.getTransmitter().send(new CS2ConfigBuilder(697, 0).build(source));
		target.getTransmitter().send(new CS2ConfigBuilder(729, 0).build(source));
		target.getTransmitter().send(new CS2ConfigBuilder(697, 0).build(source));
	}
	
	/**
	 * Refreshes the items to the source and target
	 *
	 * @param adder
	 * 		The player who added the items
	 */
	private void refreshItems(Player adder) {
		ItemsContainer<Item> container = adder.getAttribute("trade-container");
		if (container == null) {
			return;
		}
		adder.getTransmitter().send(new ContainerPacketBuilder(90, container.toArray()).build(adder));
		
		Entity target = adder.equals(source) ? this.target : this.source;
		Player p2 = target.toPlayer();
		p2.getTransmitter().send(new ContainerPacketBuilder(90, container.toArray(), true).build(p2));
	}
	
	/**
	 * Opens the confirmation screen
	 *
	 * @param player
	 * 		The player
	 * @param partner
	 * 		The partner of the player
	 */
	private void openConfirm(Player player, Player partner) {
		player.getManager().getInterfaces().sendInterface(SECOND_TRADE_INTERFACE_ID, true);
		player.getManager().getInterfaces().sendInterfaceText(SECOND_TRADE_INTERFACE_ID, 54, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(partner.getDetails().getDisplayName()));
		player.getManager().getInterfaces().sendInterfaceText(SECOND_TRADE_INTERFACE_ID, 34, "Are you sure you want to make this trade?");
		player.getManager().getInterfaces().closeInventoryInterface();
	}
	
	/**
	 * Pushes this interaction to the next stage
	 */
	private void nextStage() {
		switch (state) {
			case ADDING_ITEMS:
				this.state = TradeState.CONFIRMING;
				
				this.sourceAccepted = false;
				this.targetAccepted = false;
				
				Player source = this.source.toPlayer();
				Player target = this.target.toPlayer();
				
				openConfirm(source, target);
				openConfirm(target, source);
				
				refreshAcceptMessages();
				break;
			case CONFIRMING:
				complete();
				break;
		}
	}
	
	/**
	 * Handles the completion of a trade
	 */
	private void complete() {
		if (complete) {
			return;
		}
		Player source = this.source.toPlayer();
		Player target = this.target.toPlayer();
		
		Arrays.stream(sourceItems.toArray()).filter(Objects::nonNull).forEach(item -> target.getInventory().addItem(item));
		Arrays.stream(targetItems.toArray()).filter(Objects::nonNull).forEach(item -> source.getInventory().addItem(item));
		
		sourceItems.clear();
		targetItems.clear();
		
		close(source);
		close(target);
		
		source.getTransmitter().sendMessage("Accepted trade.");
		target.getTransmitter().sendMessage("Accepted trade.");
		
		complete = true;
	}
	
	public enum TradeState {
		ADDING_ITEMS,
		CONFIRMING
	}
	
}
