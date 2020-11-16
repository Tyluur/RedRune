package org.redrune.game.content.event.impl.item;

import org.redrune.core.EngineWorkingSet;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.AnimationPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.context.item.ItemEventContext;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.event.Event;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class ItemEvent extends Event<ItemEventContext> {
	
	/**
	 * Constructs a new event
	 */
	public ItemEvent() {
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.NONE);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, ItemEventContext context) {
		if (ModuleRepository.handle(player, context.getItem(), context.getSlotId(), context.getOption())) {
			return;
		}
		if (context.getOption().equals(InteractionOption.FIRST_OPTION)) {
			handleItemUsage(player);
		} else if (context.getOption().equals(InteractionOption.SECOND_OPTION)) {
			handleQueuedItemEquipping(player, context);
		} else if (context.getOption().equals(InteractionOption.EXAMINE)) {
			handleItemExamining(player, context.getItem());
		}
	}
	
	@Override
	public boolean canStart(Player player, ItemEventContext context) {
		return !player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION);
	}
	
	/**
	 * Handles the usage of items
	 *
	 * @param player
	 * 		The player
	 */
	private void handleItemUsage(Player player) {

	}
	
	/**
	 * Handles the equipping of items (queued)
	 *
	 * @param player
	 * 		The player
	 */
	private void handleQueuedItemEquipping(Player player, ItemEventContext context) {
		Queue<Integer> queue = player.getAttribute("equip_queue");
		boolean startTask = false;
		if (queue == null) {
			queue = new LinkedBlockingQueue<>();
			startTask = true;
		}
		if (!queue.contains(context.getSlotId())) {
			if (canEquip(player, context.getItem()) != null) {
				queue.add(context.getSlotId());
			}
		}
		player.putAttribute("equip_queue", queue);
		
		// we're not to push the task
		if (!startTask) {
			return;
		}
		boolean shorter = false;
		long lastEndTime = SystemManager.getUpdateWorker().getLastEndTime();
		long difference = System.currentTimeMillis() - lastEndTime;
		if (difference > 400) {
			shorter = true;
		}
		
		Runnable runnable = () -> {
			Queue<Integer> equipQueue = player.getAttribute("equip_queue");
			if (equipQueue == null) {
				return;
			}
			Integer slotId;
			while ((slotId = equipQueue.poll()) != null) {
				Item item = player.getInventory().getItems().get(slotId);
				if (item == null) {
					continue;
				}
				handleItemEquipping(player, item, slotId);
			}
			player.removeAttribute("equip_queue");
		};
		if (shorter) {
			EngineWorkingSet.getScheduledExecutorService().schedule(runnable, 300, TimeUnit.MILLISECONDS);
		} else {
			SystemManager.getScheduler().schedule(new ScheduledTask(1) {
				public void run() {
					runnable.run();
				}
			});
		}
	}
	
	/**
	 * The examining of an item is sent here
	 *
	 * @param player
	 * 		The player
	 */
	public static void handleItemExamining(Player player, Item item) {
		if (item == null) {
			return;
		}
		String examine = ItemRepository.getExamine(item.getId());
		if (examine != null) {
			player.getTransmitter().sendMessage(examine, true);
		} else {
			player.getTransmitter().sendMessage("It's a " + item.getName().toLowerCase() + ".", true);
		}
	}
	
	/**
	 * Checks if the player can equip the weapon
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item
	 */
	private static Object[] canEquip(Player player, Item item) {
		if (item.getDefinitions().isNoted() || !item.getDefinitions().isWearItem(player.getDetails().getAppearance().isMale()) && item.getId() != 4084) {
			player.getTransmitter().sendMessage("You can't wear that.", true);
			return null;
		}
		int targetSlot = EquipConstants.getItemSlot(item.getId());
		if (item.getAmount() == 4084) {
			targetSlot = 3;
		}
		if (targetSlot == -1) {
			player.getTransmitter().sendMessage("You can't wear that.", true);
			return null;
		}
		boolean isTwoHandedWeapon = targetSlot == 3 && EquipConstants.isTwoHanded(item);
		if (isTwoHandedWeapon && !player.getInventory().hasFreeSlots() && player.getEquipment().hasShield()) {
			player.getTransmitter().sendMessage("Not enough free space in your inventory.", true);
			return null;
		}
		HashMap<Integer, Integer> requirements = item.getDefinitions().getWearingRequirements();
		boolean hasRequirements = true;
		if (requirements != null) {
			for (int skillId : requirements.keySet()) {
				if (skillId > 24 || skillId < 0) {
					continue;
				}
				int level = requirements.get(skillId);
				if (level < 0 || level > 120) {
					continue;
				}
				if (player.getSkills().getLevelForXp(skillId) < level) {
					if (hasRequirements) {
						player.getTransmitter().sendMessage("You are not high enough level to use this item.", true);
					}
					hasRequirements = false;
					String name = SkillConstants.SKILL_NAME[skillId].toLowerCase();
					player.getTransmitter().sendMessage("You need to have a" + (name.startsWith("a") ? "n" : "") + " " + name + " level of " + level + ".", true);
				}
			}
		}
		if (!hasRequirements) {
			return null;
		}
		return new Object[] { targetSlot, true, isTwoHandedWeapon };
	}
	
	/**
	 * Handles the equipping of an item
	 *
	 * @param player
	 * 		The player
	 */
	public static void handleItemEquipping(Player player, Item item, int slotId) {
		Object[] equipData = canEquip(player, item);
		if (equipData == null || equipData.length != 3 || !(boolean) equipData[1] || player.isDying() || player.isDead()) {
			return;
		}
		int targetSlot = (int) equipData[0];
		boolean isTwoHandedWeapon = (boolean) equipData[2];
		player.getInventory().getItems().remove(slotId, item);
		if (targetSlot == 3) {
			if (isTwoHandedWeapon && player.getEquipment().getItem(5) != null) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(5))) {
					player.getInventory().getItems().set(slotId, item);
					return;
				}
				player.getEquipment().getItems().set(5, null);
			}
		} else if (targetSlot == 5) {
			if (player.getEquipment().getItem(3) != null && EquipConstants.isTwoHanded(player.getEquipment().getItem(3))) {
				if (!player.getInventory().getItems().add(player.getEquipment().getItem(3))) {
					player.getInventory().getItems().set(slotId, item);
					return;
				}
				player.getEquipment().getItems().set(3, null);
			}
		}
		if (player.getEquipment().getItem(targetSlot) != null && (item.getId() != player.getEquipment().getItem(targetSlot).getId() || !item.getDefinitions().isStackable())) {
			if (player.getInventory().getItems().get(slotId) == null) {
				player.getInventory().getItems().set(slotId, new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			} else {
				player.getInventory().getItems().add(new Item(player.getEquipment().getItem(targetSlot).getId(), player.getEquipment().getItem(targetSlot).getAmount()));
			}
			player.getEquipment().getItems().set(targetSlot, null);
		}
		int oldAmt = 0;
		if (player.getEquipment().getItem(targetSlot) != null) {
			oldAmt = player.getEquipment().getItem(targetSlot).getAmount();
		}
		Item item2 = new Item(item.getId(), oldAmt + item.getAmount());
		player.getEquipment().getItems().set(targetSlot, item2);
		player.getEquipment().refresh(targetSlot, targetSlot == 3 ? 5 : 3);
		player.getInventory().refreshAll();
		player.getCombatDefinitions().setSpecialActivated(false);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		// removing the spell cast
		if (targetSlot == EquipConstants.SLOT_WEAPON) {
			player.getCombatDefinitions().resetSpells(true);
		}
	}
}
