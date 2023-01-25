package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.packet.outgoing.impl.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public final class PlayerBank {
	
	/**
	 * The max size of items in the bank
	 */
	private static final int MAX_BANK_SIZE = 438;
	
	/**
	 * The player bank details instance
	 */
	@Getter
	private final PlayerBankDetails details;
	
	/**
	 * The items in the bank.
	 */
	//tab, items
	private Item[][] bankTabs;
	
	/**
	 * The player who owns this bank
	 */
	@Setter
	private transient Player player;
	
	/**
	 * The tab the player is on
	 */
	@Getter
	@Setter
	private transient int currentTab;
	
	/**
	 * The last copy of the container
	 */
	private transient Item[] lastContainerCopy;
	
	public PlayerBank() {
		this.bankTabs = new Item[1][0];
		this.details = new PlayerBankDetails();
	}
	
	/**
	 * Gets the start slot
	 *
	 * @param tabId
	 * 		The tab id
	 */
	private int getStartSlot(int tabId) {
		int slotId = 0;
		for (int tab = 1; tab < (tabId == 0 ? bankTabs.length : tabId); tab++) {
			slotId += bankTabs[tab].length;
		}
		return slotId;
	}
	
	/**
	 * Gets an item by the id
	 *
	 * @param id
	 * 		The id of the item
	 */
	private Item getItem(int id) {
		for (Item[] bankTab : bankTabs) {
			for (Item item : bankTab) {
				if (item == null) {
					continue;
				}
				if (item.getId() == id) {
					return item;
				}
			}
		}
		return null;
	}
	
	/**
	 * Deposits all the items in our inventory
	 */
	public boolean depositAllInventory() {
		for (int i = 0; i < 28; i++) {
			final Item item = player.getInventory().getItems().get(i);
			if (item == null) {
				continue;
			}
			if (!hasSpaceForItem(item.getId())) {
				player.getTransmitter().sendMessage("Not enough space in your bank.");
				return false;
			}
			if (!depositItem(i, Integer.MAX_VALUE, true)) {
				refreshTab(currentTab);
				refreshItems();
				return false;
			}
		}
		refreshTab(currentTab);
		refreshItems();
		return true;
	}
	
	/**
	 * Deposits all the items in our equipment
	 */
	public boolean depositAllEquipment() {
		Item[] array = player.getEquipment().getItems().toArray();
		for (int slot = 0; slot < array.length; slot++) {
			Item item = array[slot];
			if (item == null) {
				continue;
			}
			if (!hasSpaceForItem(item.getId()) || !addItem(item.getId(), item.getAmount(), true)) {
				return false;
			} else {
				player.getEquipment().getItems().set(slot, null);
				player.getEquipment().sendContainer();
				player.getUpdateMasks().register(new AppearanceUpdate(player));
			}
		}
		return true;
	}
	
	/**
	 * Deposits an item to the bank
	 *
	 * @param invSlot
	 * 		The slot of the item in the inv
	 * @param quantity
	 * 		The amount
	 * @param refresh
	 * 		If we should refresh
	 */
	public boolean depositItem(int invSlot, int quantity, boolean refresh) {
		if (quantity < 1 || invSlot < 0 || invSlot > 27) {
			return false;
		}
		Item item = player.getInventory().getItems().get(invSlot);
		if (item == null) {
			return false;
		}
		int amt = player.getInventory().getItems().getNumberOf(item);
		if (amt < quantity) {
			item = new Item(item.getId(), amt);
		} else {
			item = new Item(item.getId(), quantity);
		}
		ItemDefinition defs = item.getDefinitions();
		
		int originalId = item.getId();
		if (defs.isNoted() && defs.getNoteId() != -1) {
			item.setId((short) defs.getNoteId());
		}
		Item bankedItem = getItem(item.getId());
		if (bankedItem != null) {
			if (bankedItem.getAmount() + item.getAmount() <= 0) {
				item.setAmount(Integer.MAX_VALUE - bankedItem.getAmount());
				player.getTransmitter().sendMessage("Not enough space in your bank.");
				player.getInventory().deleteItem(invSlot, new Item(originalId, item.getAmount()));
				addItem(item.getId(), item.getAmount(), refresh);
				return false;
			}
		} else if (!hasBankSpace()) {
			player.getTransmitter().sendMessage("Not enough space in your bank.");
			return false;
		}
		player.getInventory().deleteItem(invSlot, new Item(originalId, item.getAmount()));
		addItem(item.getId(), item.getAmount(), refresh);
		return true;
	}
	
	/**
	 * Checks if we have space for an item
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	private boolean hasSpaceForItem(int itemId) {
		int[] slotInfo = getItemSlot(itemId);
		// if the item doesnt exist in our bank already and we have no more slots left
		return !(slotInfo == null && !hasBankSpace());
	}
	
	/**
	 * Switches an item from slots
	 *
	 * @param fromSlot
	 * 		The from slot
	 * @param toSlot
	 * 		The to slot
	 * @param toComponentId
	 * 		The to component id
	 */
	public void switchItem(int fromSlot, int toSlot, int toComponentId) {
		if (toSlot == -1) {
			int toTab = toComponentId >= 74 ? 8 - (82 - toComponentId) : 9 - ((toComponentId - 44) / 2);
			if (toTab < 0 || toTab > 9) {
				return;
			}
			if (bankTabs.length == toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null) {
					return;
				}
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null) {
					return;
				}
				removeItem(fromSlot, item.getAmount(), false, true);
				createTab();
				bankTabs[bankTabs.length - 1] = new Item[] { item };
				refreshTab(fromRealSlot[0]);
				refreshTab(toTab);
				refreshItems();
			} else if (bankTabs.length > toTab) {
				int[] fromRealSlot = getRealSlot(fromSlot);
				if (fromRealSlot == null) {
					return;
				}
				if (toTab == fromRealSlot[0]) {
					switchItem(fromSlot, getStartSlot(toTab));
					return;
				}
				Item item = getItem(fromRealSlot);
				if (item == null) {
					return;
				}
				boolean removed = removeItem(fromSlot, item.getAmount(), false, true);
				if (!removed) {
					refreshTab(fromRealSlot[0]);
				} else if (fromRealSlot[0] != 0 && toTab >= fromRealSlot[0]) {
					toTab -= 1;
				}
				refreshTab(fromRealSlot[0]);
				addItem(item.getId(), item.getAmount(), toTab, true);
			}
		} else {
			if (toComponentId == 93) {
				if (details.isInsertItems()) {
					insertItem(fromSlot, toSlot);
				} else {
					switchItem(fromSlot, toSlot);
				}
			}
		}
	}
	
	/**
	 * Switches an item from a slot to a slot
	 *
	 * @param fromSlot
	 * 		The slot its coming from
	 * @param toSlot
	 * 		The slot its going to
	 */
	private void switchItem(int fromSlot, int toSlot) {
		int[] fromRealSlot = getRealSlot(fromSlot);
		Item fromItem = getItem(fromRealSlot);
		if (fromItem == null) {
			return;
		}
		int[] toRealSlot = getRealSlot(toSlot);
		Item toItem = getItem(toRealSlot);
		if (toItem == null) {
			return;
		}
		bankTabs[fromRealSlot[0]][fromRealSlot[1]] = toItem;
		bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
		refreshTab(fromRealSlot[0]);
		if (fromRealSlot[0] != toRealSlot[0]) {
			refreshTab(toRealSlot[0]);
		}
		refreshItems();
	}
	
	/**
	 * Inserts an item to a slot
	 *
	 * @param fromSlot
	 * 		The slot its coming from
	 * @param toSlot
	 * 		The slot its going to
	 */
	private void insertItem(int fromSlot, int toSlot) {
		int[] fromRealSlot = getRealSlot(fromSlot);
		Item fromItem = getItem(fromRealSlot);
		if (fromItem == null) {
			return;
		}
		int[] toRealSlot = getRealSlot(toSlot);
		Item toItem = getItem(toRealSlot);
		if (toItem == null) {
			return;
		}
		if (toRealSlot[0] != fromRealSlot[0]) {
			bankTabs[fromRealSlot[0]][fromRealSlot[1]] = toItem;
			bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
		} else {
			if (toRealSlot[1] > fromRealSlot[1]) {
				for (int i = fromRealSlot[1]; i < toRealSlot[1]; i++) {
					Item toShift = bankTabs[toRealSlot[0]][fromRealSlot[1] += 1];
					bankTabs[fromRealSlot[0]][i] = toShift;
				}
			} else if (fromRealSlot[1] > toRealSlot[1]) {
				for (int i = fromRealSlot[1]; i > toRealSlot[1]; i--) {
					Item toShift = bankTabs[toRealSlot[0]][fromRealSlot[1] -= 1];
					bankTabs[fromRealSlot[0]][i] = toShift;
				}
			}
			bankTabs[toRealSlot[0]][toRealSlot[1]] = fromItem;
		}
		refreshTab(fromRealSlot[0]);
		if (fromRealSlot[0] != toRealSlot[0]) {
			refreshTab(toRealSlot[0]);
		}
		refreshItems();
	}
	
	/**
	 * Creates a new tab
	 */
	private void createTab() {
		int slot = bankTabs.length;
		Item[][] tabs = new Item[slot + 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		tabs[slot] = new Item[0];
		bankTabs = tabs;
	}
	
	/**
	 * Gets the item in the slot
	 *
	 * @param bankSlot
	 * 		The slot
	 */
	public Item getItemInSlot(int bankSlot) {
		return getItem(getRealSlot(bankSlot));
	}
	
	/**
	 * Gets the item
	 *
	 * @param slot
	 * 		The slot details
	 */
	private Item getItem(int[] slot) {
		if (slot == null) {
			return null;
		}
		return bankTabs[slot[0]][slot[1]];
	}
	
	/**
	 * Gets the real slot
	 *
	 * @param slot
	 * 		The slot
	 */
	private int[] getRealSlot(int slot) {
		for (int tab = 1; tab < bankTabs.length; tab++) {
			if (slot >= bankTabs[tab].length) {
				slot -= bankTabs[tab].length;
			} else {
				return new int[] { tab, slot };
			}
		}
		if (slot >= bankTabs[0].length) {
			return null;
		}
		return new int[] { 0, slot };
	}
	
	/**
	 * Handles withdrawing an item from the bank
	 *
	 * @param bankSlot
	 * 		The slot
	 * @param quantity
	 * 		The amount
	 */
	public void withdrawItem(int bankSlot, int quantity) {
		if (quantity < 1) {
			return;
		}
		Item item = getItem(getRealSlot(bankSlot));
		if (item == null) {
			return;
		}
		if (item.getAmount() < quantity) {
			item = new Item(item.getId(), item.getAmount());
		} else {
			item = new Item(item.getId(), quantity);
		}
		boolean noted = false;
		ItemDefinition defs = item.getDefinitions();
		if (details.isWithdrawingNotes()) {
			if (!defs.isNoted() && defs.getNoteId() != -1) {
				item.setId((short) defs.getNoteId());
				noted = true;
			} else {
				player.getTransmitter().sendMessage("You cannot withdraw this item as a note.");
			}
		}
		if (noted || defs.isStackable()) {
			if (player.getInventory().getItems().containsOne(item)) {
				int slot = player.getInventory().getItems().getThisItemSlot(item);
				Item invItem = player.getInventory().getItems().get(slot);
				if (invItem == null) {
					return;
				}
				if (invItem.getAmount() + item.getAmount() <= 0) {
					item.setAmount(Integer.MAX_VALUE - invItem.getAmount());
					player.getTransmitter().sendMessage("Not enough space in your inventory.");
				}
			} else if (!player.getInventory().hasFreeSlots()) {
				player.getTransmitter().sendMessage("Not enough space in your inventory.");
				return;
			}
		} else {
			int freeSlots = player.getInventory().getItems().freeSlots();
			if (freeSlots == 0) {
				player.getTransmitter().sendMessage("Not enough space in your inventory.");
				return;
			}
			if (freeSlots < item.getAmount()) {
				item.setAmount(freeSlots);
				player.getTransmitter().sendMessage("Not enough space in your inventory.");
			}
		}
		removeItem(bankSlot, item.getAmount(), true, false);
		player.getInventory().addItem(item.getId(), item.getAmount());
	}
	
	/**
	 * If we have enough bank space to continue adding items.
	 */
	private boolean hasBankSpace() {
		return getBankSize() < MAX_BANK_SIZE;
	}
	
	/**
	 * Removes an item from the bank
	 *
	 * @param fakeSlot
	 * 		The fake slot, used to generate the real slot
	 * @param quantity
	 * 		The amount of the item to remove
	 * @param refresh
	 * 		If we should refresh the bank container
	 * @param forceDestroy
	 * 		If we should force the tab to be destroyed
	 * @return True if successfully removed
	 */
	private boolean removeItem(int fakeSlot, int quantity, boolean refresh, boolean forceDestroy) {
		return removeItem(getRealSlot(fakeSlot), quantity, refresh, forceDestroy);
	}
	
	/**
	 * Removes an item from the bank
	 *
	 * @param slot
	 * 		The slot details
	 * @param quantity
	 * 		The amount of the item to remove
	 * @param refresh
	 * 		If we should refresh the bank container
	 * @param forceDestroy
	 * 		If we should force the tab to be destroyed
	 * @return True if successfully removed
	 */
	private boolean removeItem(int[] slot, int quantity, boolean refresh, boolean forceDestroy) {
		if (slot == null) {
			return false;
		}
		Item item = bankTabs[slot[0]][slot[1]];
		boolean destroyed = false;
		if (quantity >= item.getAmount()) {
			if (bankTabs[slot[0]].length == 1 && (forceDestroy || bankTabs.length != 1)) {
				destroyTab(slot[0]);
				if (refresh) {
					refreshTabs();
				}
				destroyed = true;
			} else {
				Item[] tab = new Item[bankTabs[slot[0]].length - 1];
				System.arraycopy(bankTabs[slot[0]], 0, tab, 0, slot[1]);
				System.arraycopy(bankTabs[slot[0]], slot[1] + 1, tab, slot[1], bankTabs[slot[0]].length - slot[1] - 1);
				bankTabs[slot[0]] = tab;
				if (refresh) {
					refreshTab(slot[0]);
				}
			}
		} else {
			bankTabs[slot[0]][slot[1]] = new Item(item.getId(), item.getAmount() - quantity);
		}
		if (refresh) {
			refreshItems();
		}
		return destroyed;
	}
	
	/**
	 * Adds an item to the bank
	 *
	 * @param id
	 * 		The id of the item
	 * @param quantity
	 * 		The amount of the item
	 * @param refresh
	 * 		If we should refresh the bank
	 */
	private boolean addItem(int id, int quantity, boolean refresh) {
		return addItem(id, quantity, currentTab, refresh);
	}
	
	/**
	 * Adds an item to the bank
	 *
	 * @param id
	 * 		The id of the item
	 * @param quantity
	 * 		The amount of the item
	 * @param creationTab
	 * 		The tab
	 * @param refresh
	 * 		If we should refresh the bank
	 */
	private boolean addItem(int id, int quantity, int creationTab, boolean refresh) {
		int[] slotInfo = getItemSlot(id);
		
		// if we cant find an item in the bank with that id, we must add it to the bank
		if (slotInfo == null) {
			if (creationTab >= bankTabs.length) {
				creationTab = bankTabs.length - 1;
			}
			if (creationTab < 0) {
				creationTab = 0;
			}
			int slot = bankTabs[creationTab].length;
			Item[] tab = new Item[slot + 1];
			System.arraycopy(bankTabs[creationTab], 0, tab, 0, slot);
			tab[slot] = new Item(id, quantity);
			bankTabs[creationTab] = tab;
			if (refresh) {
				refreshTab(creationTab);
			}
		} else {
			Item item = bankTabs[slotInfo[0]][slotInfo[1]];
			if (item.getAmount() + quantity <= 0) {
				player.getTransmitter().sendMessage("Not enough space in your bank.");
				return false;
			}
			bankTabs[slotInfo[0]][slotInfo[1]] = new Item(item.getId(), item.getAmount() + quantity);
		}
		if (refresh) {
			refreshItems();
		}
		return true;
	}
	
	/**
	 * Destroys the tab
	 *
	 * @param slot
	 * 		The tab id
	 */
	private void destroyTab(int slot) {
		Item[][] tabs = new Item[bankTabs.length - 1][];
		System.arraycopy(bankTabs, 0, tabs, 0, slot);
		System.arraycopy(bankTabs, slot + 1, tabs, slot, bankTabs.length - slot - 1);
		bankTabs = tabs;
		if (currentTab != 0 && currentTab >= slot) {
			currentTab--;
		}
	}
	
	/**
	 * Gets the slot of an item
	 *
	 * @param id
	 * 		The id of the item
	 */
	private int[] getItemSlot(int id) {
		for (int tab = 0; tab < bankTabs.length; tab++) {
			for (int slot = 0; slot < bankTabs[tab].length; slot++) {
				if (bankTabs[tab][slot].getId() == id) {
					return new int[] { tab, slot };
				}
			}
		}
		return null;
	}
	
	/**
	 * Refreshes all the items
	 */
	private void refreshItems() {
		refreshTotalSize();
		refreshItems(generateContainer(), getContainerCopy());
	}
	
	/**
	 * Refreshes the items
	 *
	 * @param itemsAfter
	 * 		The items that were refreshed after
	 * @param itemsBefore
	 * 		The items that were refreshed last
	 */
	private void refreshItems(Item[] itemsAfter, Item[] itemsBefore) {
		if (itemsBefore.length != itemsAfter.length) {
			lastContainerCopy = itemsAfter;
			sendItems();
			return;
		}
		int[] changedSlots = new int[itemsAfter.length];
		int count = 0;
		for (int index = 0; index < itemsAfter.length; index++) {
			if (itemsBefore[index] != itemsAfter[index]) {
				changedSlots[count++] = index;
			}
		}
		int[] finalChangedSlots = new int[count];
		System.arraycopy(changedSlots, 0, finalChangedSlots, 0, count);
		lastContainerCopy = itemsAfter;
		refreshItems(finalChangedSlots);
	}
	
	/**
	 * Refreshes the items in slots
	 *
	 * @param slots
	 * 		The slots
	 */
	private void refreshItems(int[] slots) {
		//		player.getTransmitter().send(new ContainerPacketBuilder(95, getContainerCopy()).build(player));
		player.getTransmitter().send(new ContainerUpdateBuilder(95, getContainerCopy(), slots).build(player));
	}
	
	/**
	 * Opens the bank interfaces
	 */
	public void open() {
		// removing the equipment stats button
		player.getTransmitter().send(new InterfaceChangeBuilder(762, 117, true).build(player)).send(new InterfaceChangeBuilder(762, 118, true).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(563, 4194304).build(player));
		player.getTransmitter().send(new BlackCS2ScriptBuilder(1451).build(player));
		
		refreshTotalSize();
		refreshViewingTab();
		refreshTabs();
		unlockButtons();
		sendItems();
		refreshLastX();
		
		player.getManager().getInterfaces().sendInterface(762, true).sendInventoryInterface(763);
	}
	
	/**
	 * Refreshes the total size of the bank.
	 */
	private void refreshTotalSize() {
		player.getTransmitter().send(new CS2ConfigBuilder(1038, 0).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(192, getBankSize()).build(player));
	}
	
	/**
	 * Refreshes the viewing tab
	 */
	private void refreshViewingTab() {
		player.getTransmitter().send(new ConfigFilePacketBuilder(4893, currentTab + 1).build(player));
	}
	
	/**
	 * Refreshes all the tabs
	 */
	private void refreshTabs() {
		for (int slot = 1; slot < 9; slot++) {
			refreshTab(slot);
		}
	}
	
	/**
	 * Unlocks the interfaces
	 */
	private void unlockButtons() {
		player.getTransmitter().send(new AccessMaskBuilder(762, 93, 40, 1278, 0, 438).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(763, 0, 37, 1150, 0, 27).build(player));
	}
	
	/**
	 * Sends the whole bank container
	 */
	private void sendItems() {
		player.getTransmitter().send(new ContainerPacketBuilder(95, getContainerCopy()).build(player));
	}
	
	/**
	 * Sends the last x amount
	 */
	public void refreshLastX() {
		player.getTransmitter().send(new ConfigPacketBuilder(1249, details.lastX).build(player));
	}
	
	/**
	 * Gets the size of the bank
	 */
	private int getBankSize() {
		int size = 0;
		for (Item[] bankTab : bankTabs) {
			size += bankTab.length;
		}
		return size;
	}
	
	/**
	 * Refreshes a tab
	 *
	 * @param tabId
	 * 		The id of the tab
	 */
	private void refreshTab(int tabId) {
		if (tabId == 0) {
			return;
		}
		player.getTransmitter().send(new ConfigFilePacketBuilder(4885 + (tabId - 1), getTabSize(tabId)).build(player));
	}
	
	/**
	 * Gets the whole bank container
	 */
	private Item[] getContainerCopy() {
		if (lastContainerCopy == null) {
			lastContainerCopy = generateContainer();
		}
		return lastContainerCopy;
	}
	
	/**
	 * Gets the size of the items in the tab
	 *
	 * @param tabId
	 * 		The id of the tab
	 */
	private int getTabSize(int tabId) {
		if (tabId >= bankTabs.length) {
			return 0;
		}
		return bankTabs[tabId].length;
	}
	
	/**
	 * Generates a new copy of the bank container
	 */
	private Item[] generateContainer() {
		Item[] container = new Item[getBankSize()];
		int count = 0;
		for (int slot = 1; slot < bankTabs.length; slot++) {
			System.arraycopy(bankTabs[slot], 0, container, count, bankTabs[slot].length);
			count += bankTabs[slot].length;
		}
		System.arraycopy(bankTabs[0], 0, container, count, bankTabs[0].length);
		return container;
	}
	
	/**
	 * Collapses a tab
	 *
	 * @param tabId
	 * 		The id of the tab
	 */
	public void collapse(int tabId) {
		if (tabId == 0 || tabId >= bankTabs.length) {
			return;
		}
		Item[] items = bankTabs[tabId];
		for (Item item : items) {
			removeItem(getItemSlot(item.getId()), item.getAmount(), false, true);
		}
		for (Item item : items) {
			addItem(item.getId(), item.getAmount(), 0, false);
		}
		refreshTabs();
		refreshItems();
	}
	
	/**
	 * Switches the withdraw notes mode
	 */
	public void switchWithdrawNotes() {
		details.setWithdrawingNotes(!details.isWithdrawingNotes());
	}
	
	/**
	 * Switches the insert items mode
	 */
	public void switchInsertItems() {
		details.setInsertItems(!details.isInsertItems());
	}
	
	/**
	 * Withdraws item.getAmount() - 1 of the item in the slot
	 *
	 * @param fakeSlot
	 * 		The slotId received, not the actual item slot
	 */
	public void withdrawItemButOne(int fakeSlot) {
		int[] fromRealSlot = getRealSlot(fakeSlot);
		Item item = getItem(fromRealSlot);
		if (item == null) {
			return;
		}
		if (item.getAmount() <= 1) {
			player.getTransmitter().sendMessage("You only have one of this item in your bank");
			return;
		}
		withdrawItem(fakeSlot, item.getAmount() - 1);
	}
	
	/**
	 * Sets the starter bank up
	 */
	public void setDefaultBank() {
		{
			bankTabs = new Item[3][0];
			bankTabs[0] = new Item[44];
			bankTabs[0][0] = new Item(6685, 100_000);
			bankTabs[0][1] = new Item(3024, 100_000);
			bankTabs[0][2] = new Item(10925, 100_000);
			bankTabs[0][3] = new Item(2434, 100_000);
			bankTabs[0][4] = new Item(3040, 100_000);
			bankTabs[0][5] = new Item(2444, 100_000);
			bankTabs[0][6] = new Item(2448, 100_000);
			bankTabs[0][7] = new Item(2440, 100_000);
			bankTabs[0][8] = new Item(2436, 100_000);
			bankTabs[0][9] = new Item(2442, 100_000);
			bankTabs[0][10] = new Item(6687, 100_000);
			bankTabs[0][11] = new Item(3026, 100_000);
			bankTabs[0][12] = new Item(10927, 100_000);
			bankTabs[0][13] = new Item(139, 100_000);
			bankTabs[0][14] = new Item(3042, 100_000);
			bankTabs[0][15] = new Item(169, 100_000);
			bankTabs[0][16] = new Item(181, 100_000);
			bankTabs[0][17] = new Item(157, 100_000);
			bankTabs[0][18] = new Item(145, 100_000);
			bankTabs[0][19] = new Item(163, 100_000);
			bankTabs[0][20] = new Item(6689, 100_000);
			bankTabs[0][21] = new Item(3028, 100_000);
			bankTabs[0][22] = new Item(10929, 100_000);
			bankTabs[0][23] = new Item(141, 100_000);
			bankTabs[0][24] = new Item(3044, 100_000);
			bankTabs[0][25] = new Item(171, 100_000);
			bankTabs[0][26] = new Item(183, 100_000);
			bankTabs[0][27] = new Item(159, 100_000);
			bankTabs[0][28] = new Item(147, 100_000);
			bankTabs[0][29] = new Item(165, 100_000);
			bankTabs[0][30] = new Item(6691, 100_000);
			bankTabs[0][31] = new Item(3030, 100_000);
			bankTabs[0][32] = new Item(10931, 100_000);
			bankTabs[0][33] = new Item(143, 100_000);
			bankTabs[0][34] = new Item(3046, 100_000);
			bankTabs[0][35] = new Item(173, 100_000);
			bankTabs[0][36] = new Item(185, 100_000);
			bankTabs[0][37] = new Item(161, 100_000);
			bankTabs[0][38] = new Item(149, 100_000);
			bankTabs[0][39] = new Item(167, 100_000);
			bankTabs[0][40] = new Item(385, 100_000);
			bankTabs[0][41] = new Item(3144, 100_000);
			bankTabs[0][42] = new Item(8013, 100_000);
			bankTabs[0][43] = new Item(5, 100_000);
			
			bankTabs[1] = new Item[14];
			bankTabs[1][0] = new Item(554, 100_000);
			bankTabs[1][1] = new Item(555, 100_000);
			bankTabs[1][2] = new Item(556, 100_000);
			bankTabs[1][3] = new Item(557, 100_000);
			bankTabs[1][4] = new Item(558, 100_000);
			bankTabs[1][5] = new Item(559, 100_000);
			bankTabs[1][6] = new Item(560, 100_000);
			bankTabs[1][7] = new Item(561, 100_000);
			bankTabs[1][8] = new Item(562, 100_000);
			bankTabs[1][9] = new Item(563, 100_000);
			bankTabs[1][10] = new Item(564, 100_000);
			bankTabs[1][11] = new Item(565, 100_000);
			bankTabs[1][12] = new Item(566, 100_000);
			bankTabs[1][13] = new Item(9075, 100_000);
			
			bankTabs[2] = new Item[83];
			bankTabs[2][0] = new Item(7459, 100_000);
			bankTabs[2][1] = new Item(7462, 100_000);
			bankTabs[2][2] = new Item(4587, 100_000);
			bankTabs[2][3] = new Item(1215, 100_000);
			bankTabs[2][4] = new Item(5698, 100_000);
			bankTabs[2][5] = new Item(1434, 100_000);
			bankTabs[2][6] = new Item(1305, 100_000);
			bankTabs[2][7] = new Item(4675, 100_000);
			bankTabs[2][8] = new Item(1383, 100_000);
			bankTabs[2][9] = new Item(9185, 100_000);
			bankTabs[2][10] = new Item(10499, 100_000);
			bankTabs[2][11] = new Item(2503, 100_000);
			bankTabs[2][12] = new Item(2497, 100_000);
			bankTabs[2][13] = new Item(1129, 100_000);
			bankTabs[2][14] = new Item(3105, 100_000);
			bankTabs[2][15] = new Item(3842, 100_000);
			bankTabs[2][16] = new Item(6108, 100_000);
			bankTabs[2][17] = new Item(6107, 100_000);
			bankTabs[2][18] = new Item(6109, 100_000);
			bankTabs[2][19] = new Item(6106, 100_000);
			bankTabs[2][20] = new Item(4089, 100_000);
			bankTabs[2][21] = new Item(4091, 100_000);
			bankTabs[2][22] = new Item(4093, 100_000);
			bankTabs[2][23] = new Item(4095, 100_000);
			bankTabs[2][24] = new Item(4097, 100_000);
			bankTabs[2][25] = new Item(4099, 100_000);
			bankTabs[2][26] = new Item(4101, 100_000);
			bankTabs[2][27] = new Item(4103, 100_000);
			bankTabs[2][28] = new Item(4105, 100_000);
			bankTabs[2][29] = new Item(4107, 100_000);
			bankTabs[2][30] = new Item(4109, 100_000);
			bankTabs[2][31] = new Item(4111, 100_000);
			bankTabs[2][32] = new Item(4113, 100_000);
			bankTabs[2][33] = new Item(4115, 100_000);
			bankTabs[2][34] = new Item(4117, 100_000);
			bankTabs[2][35] = new Item(7400, 100_000);
			bankTabs[2][36] = new Item(7399, 100_000);
			bankTabs[2][37] = new Item(7398, 100_000);
			bankTabs[2][38] = new Item(2890, 100_000);
			bankTabs[2][39] = new Item(861, 100_000);
			bankTabs[2][40] = new Item(1153, 100_000);
			bankTabs[2][41] = new Item(1115, 100_000);
			bankTabs[2][42] = new Item(1067, 100_000);
			bankTabs[2][43] = new Item(1081, 100_000);
			bankTabs[2][44] = new Item(1191, 100_000);
			bankTabs[2][45] = new Item(5574, 100_000);
			bankTabs[2][46] = new Item(5575, 100_000);
			bankTabs[2][47] = new Item(5576, 100_000);
			bankTabs[2][48] = new Item(9672, 100_000);
			bankTabs[2][49] = new Item(9674, 100_000);
			bankTabs[2][50] = new Item(9676, 100_000);
			bankTabs[2][51] = new Item(1163, 100_000);
			bankTabs[2][52] = new Item(1127, 100_000);
			bankTabs[2][53] = new Item(1079, 100_000);
			bankTabs[2][54] = new Item(1093, 100_000);
			bankTabs[2][55] = new Item(8850, 100_000);
			bankTabs[2][56] = new Item(1201, 100_000);
			bankTabs[2][57] = new Item(1052, 100_000);
			bankTabs[2][58] = new Item(6568, 100_000);
			bankTabs[2][59] = new Item(2412, 100_000);
			bankTabs[2][60] = new Item(2414, 100_000);
			bankTabs[2][61] = new Item(2413, 100_000);
			bankTabs[2][62] = new Item(868, 100_000);
			bankTabs[2][63] = new Item(4131, 100_000);
			bankTabs[2][64] = new Item(6328, 100_000);
			bankTabs[2][65] = new Item(9144, 100_000);
			bankTabs[2][66] = new Item(9143, 100_000);
			bankTabs[2][67] = new Item(9142, 100_000);
			bankTabs[2][68] = new Item(9141, 100_000);
			bankTabs[2][69] = new Item(892, 100_000);
			bankTabs[2][70] = new Item(9241, 100_000);
			bankTabs[2][71] = new Item(9242, 100_000);
			bankTabs[2][72] = new Item(9243, 100_000);
			bankTabs[2][73] = new Item(9244, 100_000);
			bankTabs[2][74] = new Item(9245, 100_000);
			bankTabs[2][75] = new Item(10828, 100_000);
			bankTabs[2][76] = new Item(3751, 100_000);
			bankTabs[2][77] = new Item(3753, 100_000);
			bankTabs[2][78] = new Item(3755, 100_000);
			bankTabs[2][79] = new Item(3749, 100_000);
			bankTabs[2][80] = new Item(1712, 100_000);
			bankTabs[2][81] = new Item(1725, 100_000);
			bankTabs[2][82] = new Item(1727, 100_000);
		}
	}
	
	/**
	 * The details of the bank
	 */
	public static class PlayerBankDetails {
		
		/**
		 * The last x amount to send
		 */
		@Getter
		@Setter
		private int lastX;
		
		/**
		 * If items should be inserted (and the bank shifted) when moving them to a new location
		 */
		@Getter
		@Setter
		private transient boolean insertItems;
		
		/**
		 * If the player should withdraw the noted form of items
		 */
		@Getter
		@Setter
		private transient boolean withdrawingNotes;
		
		/**
		 * Constructs a regular bank detail
		 */
		private PlayerBankDetails() {
			this(1000, false, false);
		}
		
		/**
		 * Constructs a {@code PlayerBankDetails} instance
		 *
		 * @param lastX
		 * 		The last x amount
		 * @param withdrawingNotes
		 * 		The withdraw notes flag
		 * @param insertItems
		 * 		The insert items flag
		 */
		private PlayerBankDetails(int lastX, boolean withdrawingNotes, boolean insertItems) {
			this.lastX = lastX;
			this.withdrawingNotes = withdrawingNotes;
			this.insertItems = insertItems;
		}
	}
	
}
