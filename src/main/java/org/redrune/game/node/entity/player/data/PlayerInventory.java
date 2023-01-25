package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheFileStore;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.world.packet.outgoing.impl.ContainerPacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.ContainerUpdateBuilder;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.constant.InterfaceConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class PlayerInventory {
	
	/**
	 * The items in the container
	 */
	@Getter
	private ItemsContainer<Item> items;
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * The weight of the items carried in the inventory
	 */
	@Getter
	@Setter
	private transient double weight;
	
	public PlayerInventory() {
		items = new ItemsContainer<>(28, false);
	}
	
	/**
	 * Initializes the inventory container
	 */
	public void initialize() {
		player.getTransmitter().send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 0, 27, 4554126).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(InterfaceConstants.INVENTORY_INTERFACE_ID, 0, 28, 55, 2097152).build(player));
		sendContainer();
	}
	
	/**
	 * Sends the container items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(93, items.toArray(), false).build(player));
		calculateWeight();
	}
	
	/**
	 * Recalculates the total weight of items in the inventory
	 */
	private void calculateWeight() {
		double weight = 0;
		for (Item item : items.toArray()) {
			if (item == null) {
				continue;
			}
			weight += ItemRepository.getWeight(item.getId(), false);
		}
		this.weight = weight;
		player.getTransmitter().sendWeight();
	}
	
	/**
	 * Adds an item to the container
	 *
	 * @param item
	 * 		The  item instance
	 */
	public boolean addItem(Item item) {
		if (item.getId() < 0 || item.getAmount() < 0 || item.getId() > CacheFileStore.getItemDefinitionsSize()) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		if (!items.add(item)) {
			items.add(new Item(item.getId(), items.getFreeSlots()));
			player.getTransmitter().sendMessage("Not enough space in your inventory.", true);
			refreshItems(itemsBefore);
			return false;
		}
		refreshItems(itemsBefore);
		return true;
	}
	
	/**
	 * Adds an item to the container
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item
	 */
	public boolean addItem(int itemId, int amount) {
		return addItem(new Item(itemId, amount));
	}
	
	/**
	 * Refreshes the items
	 *
	 * @param itemsBefore
	 * 		The items array
	 */
	private void refreshItems(Item[] itemsBefore) {
		int[] changedSlots = new int[itemsBefore.length];
		int count = 0;
		for (int index = 0; index < itemsBefore.length; index++) {
			if (itemsBefore[index] != items.getItems()[index]) {
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		refresh(finalChangedSlots);
	}
	
	/**
	 * Refreshes the items in the slots
	 *
	 * @param slots
	 * 		The slots
	 */
	public void refresh(int... slots) {
		player.getTransmitter().send(new ContainerUpdateBuilder(93, items.toArray(), slots).build(player));
		calculateWeight();
	}
	
	/**
	 * Refreshes all the items
	 */
	public void refreshAll() {
		refresh(items.toArray().length);
	}
	
	/**
	 * Deletes an item from the container
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount of the item to delete
	 */
	public boolean deleteItem(int itemId, int amount) {
		if (itemId < 0 || amount < 0 || itemId > CacheFileStore.getItemDefinitionsSize()) {
			return false;
		}
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(new Item(itemId, amount));
		refreshItems(itemsBefore);
		return true;
	}
	
	/**
	 * Delets an item from the slot
	 *
	 * @param slot
	 * 		The slot
	 * @param item
	 * 		The item
	 */
	public void deleteItem(int slot, Item item) {
		Item[] itemsBefore = items.getItemsCopy();
		items.remove(slot, item);
		refreshItems(itemsBefore);
	}
	
	/**
	 * Deletes the item from the slot
	 *
	 * @param slotId
	 * 		The slot
	 */
	public void deleteSlotItem(int slotId) {
		Item[] itemsBefore = items.getItemsCopy();
		items.set(slotId, null);
		refreshItems(itemsBefore);
	}
	
	/**
	 * Switches items in slots
	 *
	 * @param fromSlot
	 * 		The slot the item is comign from
	 * @param toSlot
	 * 		The slot the item is going to
	 */
	public void switchItem(int fromSlot, int toSlot) {
		Item[] itemsBefore = items.getItemsCopy();
		Item fromItem = items.get(fromSlot);
		Item toItem = items.get(toSlot);
		items.set(fromSlot, toItem);
		items.set(toSlot, fromItem);
		refreshItems(itemsBefore);
	}
	
	/**
	 * Checks to make sure there are empty slots
	 */
	public boolean hasFreeSlots() {
		return items.getFreeSlot() != -1;
	}
	
	/**
	 * Gets the item that has the id
	 *
	 * @param itemId
	 * 		The id we want
	 * @param item1
	 * 		The item to check
	 * @param item2
	 * 		The second item to check
	 */
	public Item getItem(int itemId, Item item1, Item item2) {
		if (item1.getId() == itemId) {
			return item1;
		} else if (item2.getId() == itemId) {
			return item2;
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if the container contains an item
	 *
	 * @param itemId
	 * 		The id of the item
	 * @param amount
	 * 		The amount we need to have
	 */
	public boolean containsItem(int itemId, int amount) {
		return items.contains(new Item(itemId, amount));
	}
}
