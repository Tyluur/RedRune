package org.redrune.game.content.activity.impl.pvp;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.HeadIcons.SkullIcon;
import org.redrune.utility.rs.constant.ItemConstants;
import org.redrune.utility.tool.Misc;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class WildernessActivity extends Activity {
	
	public static final int INTERFACE_ID = 381;
	
	/**
	 * If we're showing the skull on the wilderness level component
	 */
	private transient boolean showingSkull;
	
	@Override
	public void start() {
		checkLocations();
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	@Override
	public void updateLocation() {
		checkLocations();
	}
	
	@Override
	public void end() {
		super.end();
		player.setInFightArea(false);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		player.getManager().getInterfaces().closePrimaryOverlay();
	}
	
	/**
	 * Checks what to do based on our location
	 */
	private void checkLocations() {
		boolean isAtWild = PvPLocation.isAtWild(player.getLocation());
		boolean isAtWildSafe = PvPLocation.isAtWildSafe(player.getLocation());
		
		// we're inside a danger zone
		if (!showingSkull && isAtWild && !isAtWildSafe) {
			showingSkull = true;
			player.setInFightArea(true);
			player.getManager().getInterfaces().sendPrimaryOverlay(INTERFACE_ID);
		} else if (showingSkull && (isAtWildSafe || !isAtWild)) {
			// we're inside the safe area
			player.getManager().getInterfaces().closePrimaryOverlay();
			player.setInFightArea(false);
			showingSkull = false;
			// force end while removing skull
			if (!isAtWild && !isAtWildSafe) {
				end();
			}
		} else if (isAtWild) {
			// we moved while we're still in the wild
		} else if (!isAtWildSafe) {
			// we're not in the wilderness anymore
			end();
		}
	}
	
	@Override
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		if (option == InteractionOption.ATTACK_OPTION) {
			if (player.getVariables().isInFightArea() && !target.getVariables().isInFightArea()) {
				player.getTransmitter().sendMessage("You can only attack players in a player-vs-player area.", false);
				return true;
			}
			return !wildernessLevelsVerified(target);
		} else {
			return false;
		}
	}
	
	@Override
	public void tick() {
		// if the screen mode is changed for example
		if (showingSkull && player.getManager().getInterfaces().getPrimaryOverlayInterface() != INTERFACE_ID) {
			player.getManager().getInterfaces().sendPrimaryOverlay(INTERFACE_ID);
		}
	}
	
	@Override
	public boolean savesOnLogout() {
		return true;
	}
	
	/**
	 * Checks if the target's wilderness level is capable of fighting us
	 *
	 * @param target
	 * 		The target
	 */
	private boolean wildernessLevelsVerified(Player target) {
		if (!(Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= PvPLocation.getWildLevel(player.getLocation()) && Math.abs(player.getSkills().getCombatLevel() - target.getSkills().getCombatLevel()) <= PvPLocation.getWildLevel(target.getLocation()))) {
			player.getTransmitter().sendMessage("You must travel deeper into the wilderness to attack that player.");
			return false;
		}
		return true;
	}
	
	@Override
	public boolean combatAcceptable(Entity target) {
		if (player.getCombatDefinitions().getSpellId() <= 0 && Misc.inCircle(new Location(3105, 3933, 0), target.getLocation(), 24)) {
			player.getTransmitter().sendMessage("You can only use magic in the arena.");
			return false;
		}
		if (target.isNPC()) {
			return true;
		}
		if (target.getAttackedBy() != player && player.getAttackedBy() != target) {
			player.setSkull(SkullIcon.DEFAULT, TimeUnit.MINUTES.toMillis(10));
		}
		return true;
	}
	
	/**
	 * Sends the death container
	 *
	 * @param dead
	 * 		The player who is dead
	 * @param killer
	 * 		The entity who killed the player
	 * @return An array of the items kept
	 */
	public static Item[] sendDeathContainer(Player dead, Entity killer) {
		CopyOnWriteArrayList<Item> containedItems = dead.findContainedItems();
		if (containedItems.isEmpty()) {
			return null;
		}
		Map<Integer, List<Item>> deathList = getItemsOnDeath(containedItems, dead.getManager().getPrayers().itemKeptCount());
		
		List<Item> itemsDropped = deathList.get(0);
		List<Item> itemsKept = deathList.get(1);
		List<Item> untradeables = deathList.get(2);
		
		// if the killer is an ironman, the drop is handled differently.
		final Location lootTile = dead.getLocation();
		
		for (Item item : itemsDropped) {
			RegionManager.addFloorItem(item.getId(), item.getAmount(), 200, lootTile, killer == null || !killer.isPlayer() ? dead.getDetails().getUsername() : killer.toPlayer().getDetails().getUsername());
		}
		RegionManager.addPublicFloorItem(526, 1, 200, lootTile);
		
		untradeables.stream().filter(item -> item.getDefinitions().isLended()).forEach(dead.getInventory()::addItem);
		untradeables.stream().filter(item -> !item.getDefinitions().isLended()).forEach(item -> {
			// TODO untradeable shop
			/*if (!untradeablesShop.add(item)) {
				sendMessage("Your untradeable shop was too full, so the " + item.getName().toLowerCase() + " was dropped.");
				World.addGroundItem(item, lootTile, this, true, 180, 2, 150);
			}*/
		});
		return itemsKept.toArray(new Item[itemsKept.size()]);
	}
	
	/**
	 * This method finds out the items that we keep on death, items we drop on death, and the untradeables on death. The
	 * first index in this array is the items dropped, second is items kept, third is the untradeables. No operations
	 * are done to the items in this method nor the containedItems list, they are just put into lists.
	 *
	 * @param containedItems
	 * 		The items the player contains in their inventory and equipment
	 * @param amountToKeep
	 * 		The amount of items a player should keep
	 */
	@SuppressWarnings("unchecked")
	public static Map<Integer, List<Item>> getItemsOnDeath(List<Item> containedItems, int amountToKeep) {
		List<Item> itemsDropped = new ArrayList<>(containedItems);
		List<Item> itemsKept = new ArrayList<>();
		List<Item> untradeables = new ArrayList<>();
		// removing untradeable items & lent items from the contained items
		Iterator<Item> it$ = itemsDropped.iterator();
		while (it$.hasNext()) {
			Item deathItem = it$.next();
			if ((ItemRepository.isUntradeable(deathItem.getId()) && !ItemConstants.untradeableDropsOnDeath(deathItem)) || deathItem.getDefinitions().isLended()) {
				untradeables.add(deathItem);
				it$.remove();
			}
		}
		
		// sorting the items dropped based on the price of them
		itemsDropped.sort((o1, o2) -> Integer.compare(o2.getDefinitions().getValue(), o1.getDefinitions().getValue()));
		
		int tempAmountKept = 0;
		k:
		for (Iterator<Item> iterator = itemsDropped.iterator(); iterator.hasNext(); ) {
			Item item = iterator.next();
			for (int i = 0; i <= item.getAmount(); i++) {
				if (tempAmountKept++ == amountToKeep) {
					break k;
				}
				Item saved = new Item(item.getId(), 1);
				itemsKept.add(saved);
				if (item.getAmount() >= 1) {
					item.setAmount(item.getAmount() - 1);
				}
				if (item.getAmount() < 1) {
					iterator.remove();
				}
			}
		}
		
		Map<Integer, List<Item>> items = new HashMap<>();
		items.put(0, itemsDropped);
		items.put(1, itemsKept);
		items.put(2, untradeables);
		return items;
	}
	
}
