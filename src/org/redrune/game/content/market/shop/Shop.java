package org.redrune.game.content.market.shop;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.packet.outgoing.impl.*;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.constant.ItemConstants;
import org.redrune.utility.tool.Misc;

import java.util.List;

/**
 * This class is used to hold the data of all shops
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class Shop {
	
	/**
	 * The id of the inventory interface
	 */
	public static final int INVENTORY_INTERFACE_ID = 621;
	
	/**
	 * The id of the screen interface
	 */
	public static final int INTERFACE_ID = 620;
	
	/**
	 * The identifier of the shop
	 */
	@Getter
	private final int identifier;
	
	/**
	 * The name of the shop, this is not the identifier because we have multiple shops by one name.
	 */
	@Getter
	private final String name;
	
	/**
	 * The list of items in the shop
	 */
	@Getter
	private final List<Item> items;
	
	/**
	 * The name of the currency this shop uses. This is used only to find the {@link #currency} object for this class
	 */
	@Getter
	private final String currencyName;
	
	/**
	 * If this shop is a general store. If this is true, you can sell all items to it
	 */
	@Getter
	@Setter
	private boolean generalStore;
	
	/**
	 * The currency this shop uses
	 */
	@Getter
	@Setter
	private transient ShopCurrency currency;
	
	public Shop(int identifier, String name, List<Item> items, String currencyName) {
		this.identifier = identifier;
		this.name = name;
		this.items = items;
		this.currencyName = currencyName;
	}
	
	/**
	 * Handles the buying of an item
	 *
	 * @param player
	 * 		The player buying
	 * @param slotId
	 * 		The slot the item is in
	 * @param amount
	 * 		The amount of the item we wish to buy
	 */
	public void buy(Player player, int slotId, int amount) {
		slotId = slotId / 6;
		Item item = getItem(slotId);
		if (item == null) {
			return;
		}
		// the amount of the currency the player has
		int currencyAmount = currency.getCurrencyAmount(player);
		// the price to buy one of the item
		int buyPrice = currency.getBuyPrice(item.getId());
		// the default amount of the item the player buys
		int stockAmount = currency.stockAmount(item.getId());
		// the max amount we can get
		int maxQuantity = currencyAmount / buyPrice;
		// the amount we are purchasing
		int buyQuantity = amount;
		
		// if we can afford this
		if (maxQuantity >= buyQuantity) {
			if (item.getDefinitions().isStackable()) {
				if (!player.getInventory().getItems().hasSpaceFor(item)) {
					player.getTransmitter().sendMessage("Not enough space in your inventory.");
					return;
				}
			} else {
				int freeSlots = player.getInventory().getItems().freeSlots();
				if (buyQuantity > freeSlots) {
					buyQuantity = freeSlots;
					player.getTransmitter().sendMessage("Not enough space in your inventory.");
				}
			}
		} else {
			player.getTransmitter().sendMessage("You need " + Misc.format(buyPrice * amount) + " " + currency.name() + " to buy this item.");
			buyQuantity = maxQuantity;
		}
		if (buyQuantity == 0) {
			return;
		}
		int totalPrice = buyPrice * buyQuantity;
		// if we don't have enough of the currency
		if (!(currencyAmount >= totalPrice)) {
			return;
		}
		if (totalPrice > 0) {
			currency.reduceCurrency(player, totalPrice);
		}
		player.getInventory().addItem(item.getId(), buyQuantity * stockAmount);
		// updating onscreen currency amount
		sendCurrency(player);
	}
	
	/**
	 * Gets an item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public Item getItem(int slot) {
		if (slot > items.size()) {
			return null;
		}
		return items.get(slot);
	}
	
	/**
	 * Sends the amount of the currency the player has
	 *
	 * @param player
	 * 		The player
	 */
	private void sendCurrency(Player player) {
		player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, 24, "You currently have " + Misc.format(currency.getCurrencyAmount(player)) + " " + currency.name() + ".");
	}
	
	/**
	 * Handles the selling of an item
	 *
	 * @param player
	 * 		The player
	 * @param slotId
	 * 		The slot the item is in
	 * @param amount
	 * 		The amount we wish to sell
	 */
	public void sell(Player player, int slotId, int amount) {
		Item item = player.getInventory().getItems().get(slotId);
		if (item == null) {
			return;
		}
		if (!sellable(item.getId())) {
			player.getTransmitter().sendMessage("You can't sell this item to the shop.", true);
			return;
		}
		int currencyId = currency.itemId();
		Item currencyItem = new Item(currencyId);
		if (!player.getInventory().getItems().hasSpaceFor(currencyItem)) {
			player.getTransmitter().sendMessage("Not enough space in your inventory.", false);
			return;
		}
		// the amount of the item in the stock
		int stockAmount = currency.stockAmount(item.getId());
		// the amount of the item we have
		int amountOf = player.getInventory().getItems().getNumberOf(item);
		// the value of the item being sold back
		int value = currency.getSellValue(currency.getBuyPrice(item.getId()));
		// the amount we want to sell
		int sellAmount = amount;
		// the item stock is 1, regular selling...
		if (stockAmount == 1) {
			// if the selected amount is too high
			if (sellAmount > amountOf) {
				sellAmount = amountOf;
			}
			// the amount of the currency we get back
			int returnAmount = value * sellAmount;
			player.getInventory().deleteItem(item.getId(), sellAmount);
			if (returnAmount > 0) {
				player.getInventory().addItem(currencyId, returnAmount);
			}
		} else {
			sellAmount = stockAmount * amount;
			// if the selected amount is too high
			if (sellAmount > amountOf) {
				sellAmount = amountOf;
			}
			int totalPossible = sellAmount / stockAmount;
			sellAmount = totalPossible * stockAmount;
			if (sellAmount == 0) {
				player.getTransmitter().sendMessage("This item can only be sold in increments of " + stockAmount + ".", false);
				return;
			}
			// if we have enough to sell
			if (amountOf >= sellAmount) {
				// the amount of the currency we get back
				int returnAmount = value * sellAmount;
				player.getInventory().deleteItem(item.getId(), sellAmount);
				if (returnAmount > 0) {
					player.getInventory().addItem(currencyId, returnAmount);
				}
			}
		}
		sendCurrency(player);
	}
	
	/**
	 * If an item is tradeable, it can be sold to the shop in these cases: <ol> <li>The shop is a general shop, in which
	 * all items can be sold to it.</li> <li>The shop is not a general shop, buy the item being sold is part of the
	 * default stock.</ol>This verifies those cases.
	 *
	 * @param itemId
	 * 		The id of the item being sold.
	 */
	private boolean sellable(int itemId) {
		if (currency.itemId() == -1) {
			return false;
		}
		if (currency.getSellValue(currency.getBuyPrice(itemId)) <= 0) {
			return false;
		}
		if (itemId == ItemConstants.BLOOD_MONEY || ItemRepository.isUntradeable(itemId)) {
			return false;
		}
		if (isGeneralStore()) {
			return true;
		}
		return inStock(itemId);
	}
	
	/**
	 * Checks if an item exists in the stock
	 *
	 * @param itemId
	 * 		The item id
	 */
	private boolean inStock(int itemId) {
		return items.stream().anyMatch(item -> item.getId() == itemId);
	}
	
	/**
	 * Sends the value of an item
	 *
	 * @param player
	 * 		The player
	 * @param slotId
	 * 		The slot of the item
	 * @param inventory
	 * 		If the item is in the inventory or the shop stock
	 */
	public void value(Player player, int slotId, boolean inventory) {
		if (!inventory) {
			slotId = slotId / 6;
		}
		Item item;
		if (inventory) {
			item = player.getInventory().getItems().get(slotId);
		} else {
			item = (slotId > items.size() ? null : items.get(slotId));
		}
		if (item == null) {
			return;
		}
		// if the item isnt sellable
		if (inventory && !sellable(item.getId())) {
			player.getTransmitter().sendMessage("You can't sell this item to the shop.", false);
			return;
		}
		// the amount you get for buying one of the item
		int amount = currency.stockAmount(item.getId());
		// the cost of one item
		int buyPrice = currency.getBuyPrice(item.getId());
		// the price of selling the item
		int sellPrice = currency.getSellValue(buyPrice);
		if (inventory) {
			player.getTransmitter().sendMessage(item.getName() + " will sell for " + (sellPrice == 0 ? "FREE" : Misc.format(sellPrice) + " " + currency.name() + "") + ".", false);
		} else {
			player.getTransmitter().sendMessage((amount == 1 ? "" : Misc.format(amount) + "x ") + item.getName() + ": shop will sell for " + (buyPrice == 0 ? "FREE" : Misc.format(buyPrice)) + " " + currency.name() + ".", false);
		}
	}
	
	/**
	 * Opens a shop to the player
	 *
	 * @param player
	 * 		The player
	 */
	public void open(Player player) {
		sendCurrency(player);
		
		player.getTransmitter().send(new ConfigPacketBuilder(118, 4).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(1496, -1).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(532, 995).build(player));
		
		player.getTransmitter().send(new CS2ConfigBuilder(199, -1).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(1241, 16750848).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(1242, 15439903).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(741, -1).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(743, -1).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(744, 0).build(player));
		
		player.getTransmitter().send(new AccessMaskBuilder(INTERFACE_ID, 26, 0, 1150, 0, 12).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(INTERFACE_ID, 25, 0, 1150, 0, 240).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(621, 0, 36, 1086, 0, 27).build(player));
		
		player.getTransmitter().send(new ContainerPacketBuilder(93, player.getInventory().getItems().toArray()).build(player));
		player.getTransmitter().send(new ContainerPacketBuilder(4, items.toArray(new Item[items.size()])).build(player));
		
		player.getTransmitter().send(new CS2ScriptBuilder(149, "IviiiIsssss", "Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 1, 7, 4, 93, 40697856).build(player));
		
		player.getManager().getInterfaces().sendInterfaceText(INTERFACE_ID, 20, name);
		player.getManager().getInterfaces().sendInterface(INTERFACE_ID, true).sendInventoryInterface(INVENTORY_INTERFACE_ID);
		
		player.putAttribute("open_shop", this);
	}
	
}
