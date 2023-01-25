package org.redrune.game.node.item;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Container class.
 *
 * @author Graham / edited by Dragonkk(Alex)
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/17
 */
public final class ItemsContainer<T extends Item> {
	
	/**
	 * The items in the container
	 */
	@Getter
	private final Item[] items;
	
	/**
	 * If the items in the container should stack
	 */
	private boolean alwaysStackable = false;
	
	/**
	 * Constructs a new {@code ItemsContainer}
	 *
	 * @param size
	 * 		The size of the container
	 * @param alwaysStackable
	 * 		If this container is stackable.
	 */
	public ItemsContainer(int size, boolean alwaysStackable) {
		this.items = new Item[size];
		this.alwaysStackable = alwaysStackable;
	}
	
	@Override
	public String toString() {
		StringBuilder items = new StringBuilder();
		for (Item item : this.items) {
			if (item == null) {
				continue;
			}
			items.append(item.toString());
		}
		return "ItemsContainer[" + items + "]";
	}
	
	/**
	 * Shifts items over one to the right
	 */
	public void shift() {
		Item[] oldData = items;
		reset();
		int ptr = 0;
		for (int i = 0; i < items.length; i++) {
			if (oldData[i] != null) {
				items[ptr++] = oldData[i];
			}
		}
	}
	
	/**
	 * Clears all items in the container
	 */
	public void reset() {
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
		}
	}
	
	/**
	 * Forces an item to be added to the first empty slot
	 *
	 * @param item
	 * 		The item
	 */
	public boolean forceAdd(T item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes an item from the container
	 *
	 * @param item
	 * 		The item to remove
	 */
	public int remove(T item) {
		int removed = 0, toRemove = item.getAmount();
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					int amt = items[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						items[i] = new Item(items[i].getId(), amt);
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						items[i] = null;
					}
				}
			}
		}
		return removed;
	}
	
	/**
	 * Removes all items that match this item id
	 *
	 * @param item
	 * 		The item
	 */
	public void removeAll(T item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					items[i] = null;
				}
			}
		}
	}
	
	/**
	 * If we have one of the item
	 *
	 * @param item
	 * 		The item
	 */
	public boolean containsOne(T item) {
		for (Item aData : items) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the container has any item by this id
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	public boolean contains(int itemId) {
		for (Item aData : items) {
			if (aData != null) {
				if (aData.getId() == itemId) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if we contain an item
	 *
	 * @param item
	 * 		The item
	 */
	public boolean contains(T item) {
		int amtOf = 0;
		for (Item aData : items) {
			if (aData != null) {
				if (aData.getId() == item.getId()) {
					amtOf += aData.getAmount();
				}
			}
		}
		return amtOf >= item.getAmount();
	}
	
	/**
	 * The amount of free slots in the container
	 */
	public int getFreeSlots() {
		int amount = 0;
		for (Item aData : items) {
			if (aData == null) {
				amount++;
			}
		}
		return amount;
	}
	
	/**
	 * Gets the number of an item we have
	 *
	 * @param item
	 * 		The item
	 */
	public int getNumberOf(Item item) {
		return getNumberOf(item.getId());
	}
	
	/**
	 * Gets the number of an item we have
	 *
	 * @param itemId
	 * 		The item id
	 */
	public int getNumberOf(int itemId) {
		int count = 0;
		for (Item item1 : items) {
			if (item1 != null) {
				if (item1.getId() == itemId) {
					count += item1.getAmount();
				}
			}
		}
		return count;
	}
	
	/**
	 * Gets a copy of the items
	 */
	public Item[] getItemsCopy() {
		return Arrays.copyOf(items, items.length);
	}
	
	/**
	 * Gets the slot of an item
	 *
	 * @param item
	 * 		The item
	 */
	public int getThisItemSlot(T item) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					return i;
				}
			}
		}
		return getFreeSlot();
	}
	
	/**
	 * Gets the first free slot in the container
	 */
	public int getFreeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Finds the first item by the id
	 *
	 * @param id
	 * 		The id
	 */
	public Item lookup(int id) {
		for (Item aData : items) {
			if (aData == null) {
				continue;
			}
			if (aData.getId() == id) {
				return aData;
			}
		}
		return null;
	}
	
	/**
	 * Finds the first slot the item is in
	 *
	 * @param item
	 * 		The {@code Item} instance  of the item
	 */
	public int lookupSlot(Item item) {
		return lookupSlot(item.getId());
	}
	
	/**
	 * Finds the first slot the item is in
	 *
	 * @param id
	 * 		The id of the item
	 */
	public int lookupSlot(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getId() == id) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Replaces an item in the slot
	 *
	 * @param slotId
	 * 		The slot id of  the item
	 * @param itemId
	 * 		The id of the new item
	 * @param itemAmount
	 * 		The amount of the new item
	 * @return {@code True} if successfully replaced
	 */
	public boolean replace(int slotId, int itemId, int itemAmount) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (i == slotId) {
				items[i] = new Item(itemId, itemAmount);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Removes an item in a slot
	 *
	 * @param preferredSlot
	 * 		The item in the slot
	 * @param item
	 * 		The item
	 */
	public int remove(int preferredSlot, Item item) {
		int removed = 0, toRemove = item.getAmount();
		if (items[preferredSlot] != null) {
			if (items[preferredSlot].getId() == item.getId()) {
				int amt = items[preferredSlot].getAmount();
				if (amt > toRemove) {
					removed += toRemove;
					amt -= toRemove;
					toRemove = 0;
					// items[preferredSlot] = new
					// Item(items[preferredSlot].getDefinition().getApplicableIds(), amt);
					set(preferredSlot, new Item(items[preferredSlot].getId(), amt));
					return removed;
				} else {
					removed += amt;
					toRemove -= amt;
					// items[preferredSlot] = null;
					set(preferredSlot, null);
				}
			}
		}
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) {
				if (items[i].getId() == item.getId()) {
					int amt = items[i].getAmount();
					if (amt > toRemove) {
						removed += toRemove;
						amt -= toRemove;
						toRemove = 0;
						// items[i] = new Item(items[i].getDefinition().getApplicableIds(),
						// amt);
						set(i, new Item(items[i].getId(), amt));
						return removed;
					} else {
						removed += amt;
						toRemove -= amt;
						// items[i] = null;
						set(i, null);
					}
				}
			}
		}
		return removed;
	}
	
	/**
	 * Sets an item to the slot
	 *
	 * @param slot
	 * 		The slot
	 * @param item
	 * 		The item
	 */
	public void set(int slot, Item item) {
		if (slot < 0 || slot >= items.length) {
			return;
		}
		items[slot] = item;
	}
	
	/**
	 * Adds all the items from a container to our container
	 *
	 * @param container
	 * 		The container we're adding from
	 */
	public void addAll(ItemsContainer<T> container) {
		for (int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if (item != null) {
				this.add(item);
			}
		}
	}
	
	/**
	 * Gets the size of the container
	 */
	public int getSize() {
		return items.length;
	}
	
	/**
	 * Gets the item in a slot
	 *
	 * @param slot
	 * 		The slot
	 */
	@SuppressWarnings("unchecked")
	public T get(int slot) {
		if (slot < 0 || slot >= items.length) {
			return null;
		}
		return (T) items[slot];
	}
	
	/**
	 * Adds the item to the  container
	 *
	 * @param item
	 * 		The item
	 * @return {@code True} if we successfully added the item.
	 */
	public boolean add(T item) {
		if (alwaysStackable || item.getDefinitions().isStackable() || item.getDefinitions().isNoted()) {
			for (int i = 0; i < items.length; i++) {
				if (items[i] != null) {
					if (items[i].getId() == item.getId()) {
						long newTotal = (long) (items[i].getAmount() + item.getAmount());
						if (newTotal < 0 || newTotal > Integer.MAX_VALUE) {
							return false;
						}
						items[i] = new Item(items[i].getId(), (int) newTotal);
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				if (freeSlots() >= item.getAmount()) {
					for (int i = 0; i < item.getAmount(); i++) {
						int index = freeSlot();
						items[index] = new Item(item.getId(), 1);
					}
					return true;
				} else {
					return false;
				}
			}
		}
		int index = freeSlot();
		if (index == -1) {
			return false;
		}
		items[index] = item;
		return true;
	}
	
	/**
	 * Gets the amount of free slots in the container
	 */
	public int freeSlots() {
		int amount = 0;
		for (Item aData : items) {
			if (aData == null) {
				amount++;
			}
		}
		return amount;
	}
	
	/**
	 * Finds the first free slot in the container
	 */
	public int freeSlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Checks if we have space to add a container to ours
	 *
	 * @param container
	 * 		The container
	 * @return {@code True} if we can add
	 */
	public boolean hasSpaceFor(ItemsContainer<T> container) {
		for (int i = 0; i < container.getSize(); i++) {
			T item = container.get(i);
			if (item != null) {
				if (!this.hasSpaceForItem(item)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if we have space for an item
	 *
	 * @param item
	 * 		The item
	 * @return {@code True} if we can add the item
	 */
	private boolean hasSpaceForItem(T item) {
		if (alwaysStackable || item.getDefinitions().isStackable() || item.getDefinitions().isNoted()) {
			for (Item aData : items) {
				if (aData != null) {
					if (aData.getId() == item.getId()) {
						return true;
					}
				}
			}
		} else {
			if (item.getAmount() > 1) {
				return freeSlots() >= item.getAmount();
			}
		}
		int index = freeSlot();
		return index != -1;
	}
	
	/**
	 * Gets the
	 */
	public Item[] toArray() {
		return items;
	}
	
	/**
	 * Creates a stream of all the items in the players inventory that are not nulled
	 */
	public Stream<Item> stream() {
		return Arrays.stream(items).filter(Objects::nonNull);
	}
	
	@SuppressWarnings("unchecked")
	public boolean hasSpaceFor(Item item) {
		return hasSpaceForItem((T) item);
	}
	
	/**
	 * Calculates the size of the container by finding the amount of items that exist, not the length.
	 */
	public int size() {
		int size = 0;
		for (Item item : items) {
			if (item == null) {
				continue;
			}
			size++;
		}
		return size;
	}
	
	/**
	 * Clears all the items from the container
	 */
	public void clear() {
		for (int i = 0; i < items.length; i++) {
			items[i] = null;
		}
	}
}
