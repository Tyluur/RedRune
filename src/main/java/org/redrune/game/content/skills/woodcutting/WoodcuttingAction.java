package org.redrune.game.content.skills.woodcutting;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.action.Action;
import org.redrune.game.content.skills.firemaking.FiremakingAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.Region;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.tool.Misc;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.skills.firemaking.Fire;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class WoodcuttingAction implements Action {
	
	/**
	 * The tree object
	 */
	private GameObject tree;
	
	/**
	 * The tree definitions of the tee we're chopping
	 */
	private TreeDefinitions definitions;
	
	/**
	 * The hatchet definitions of the hatchet we have
	 */
	private HatchetDefinitions hatchet;
	
	public WoodcuttingAction(GameObject tree, TreeDefinitions definitions) {
		this.tree = tree;
		this.definitions = definitions;
	}
	
	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getTransmitter().sendMessage("You swing your hatchet at the " + (TreeDefinitions.IVY == definitions ? "ivy" : "tree") + "...", true);
		setDelay(player, getWoodcuttingDelay(player));
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		player.sendAnimation(hatchet.getAnimationId());
		return player.getRegion().findAnyGameObject(tree).isPresent();
	}
	
	@Override
	public int processOnTicks(Player player) {
		addLog(player, definitions, hatchet);
		// its not time for the tree to be cut down
		if (Misc.getRandom(definitions.getRandomLifeProbability()) != 0) {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendAnimation(-1);
				player.getTransmitter().sendMessage("Not enough space in your inventory.");
				return -1;
			}
			return getWoodcuttingDelay(player);
		} else {
			// the tree should be cut down
			int time = definitions.getRespawnDelay();
			
			// first we spawn the stump
			RegionManager.spawnTimedObject(new GameObject(definitions.getStumpId(), tree.getType(), tree.getRotation(), tree.getLocation()), time);
			
			// then we schedule the tree to spawn
			SystemManager.getScheduler().schedule(new ScheduledTask(time) {
				@Override
				public void run() {
					player.getRegion().spawnObject(tree);
				}
			});
			
			// then we remove the tre leaves from the game
			if (tree.getLocation().getPlane() < 3 && definitions != TreeDefinitions.IVY) {
				removeTreeLeaves(player, time);
			}
			player.sendAnimation(-1);
			return -1;
		}
	}
	
	@Override
	public void stop(Player player) {
		setDelay(player, 3);
	}
	
	/**
	 * Adds the log to the players inventory
	 *
	 * @param player
	 * 		The player
	 * @param definitions
	 * 		The tree we're cutting
	 * @param hatchet
	 * 		The hatchet
	 */
	private static void addLog(Player player, TreeDefinitions definitions, HatchetDefinitions hatchet) {
		String logName = ItemDefinitionParser.forId(definitions.getLogsId()).getName().toLowerCase();
		double xpBoost = 1.00;
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_CHEST) == 10939) {
			xpBoost += 0.008;
		}
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_LEGS) == 10940) {
			xpBoost += 0.006;
		}
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_HAT) == 10941) {
			xpBoost += 0.004;
		}
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_FEET) == 10933) {
			xpBoost += 0.002;
		}
		player.getSkills().addExperienceWithMultiplier((short) 8, definitions.getXp() * xpBoost);
		boolean adzed = false;
		if (hatchet != null && hatchet == HatchetDefinitions.INFERNO && Misc.getRandom(10) >= 8) {
			Fire fire = Fire.getFireInstance(definitions.getLogsId());
			if (fire != null) {
				boolean cantCreateFire = FiremakingAction.badFireLocation(player);
				if (!cantCreateFire) {
					RegionManager.addTimedGamedObject(new GameObject(fire.getObjectId(), 10, 0, player.getLocation()), 592, 1, fire.getLife());
				}
				player.getTransmitter().sendMessage("You chop some " + logName + ". The heat of the inferno adze incinerates them.", true);
				adzed = true;
			}
		} else {
			player.getInventory().addItem(definitions.getLogsId(), 1);
		}
		if (definitions == TreeDefinitions.IVY) {
			player.getTransmitter().sendMessage("You successfully cut an ivy vine.", true);
		} else if (!adzed) {
			player.getTransmitter().sendMessage("You chop some " + logName + ".", true);
		}
	}
	
	/**
	 * Handles the removal of the tree leaves
	 *
	 * @param player
	 * 		The player cutting the tee
	 * @param time
	 * 		The time for the tree
	 */
	private void removeTreeLeaves(Player player, int time) {
		Region region = RegionManager.getRegion(tree.getLocation().getRegionId());
		
		Optional<GameObject> optional = region.findAnyGameObject(-1, tree.getLocation().getX() - 1, tree.getLocation().getY() - 1, tree.getLocation().getPlane() + 1, -1);
		if (!optional.isPresent()) {
			optional = region.findAnyGameObject(-1, tree.getLocation().getX(), tree.getLocation().getY() - 1, tree.getLocation().getPlane() + 1, -1);
			if (!optional.isPresent()) {
				optional = region.findAnyGameObject(-1, tree.getLocation().getX() - 1, tree.getLocation().getY(), tree.getLocation().getPlane() + 1, -1);
				if (!optional.isPresent()) {
					optional = region.findAnyGameObject(-1, tree.getLocation().getX(), tree.getLocation().getY(), tree.getLocation().getPlane() + 1, -1);
				}
			}
		}
		// leaves must be added again after
		if (optional.isPresent()) {
			final GameObject leaves = optional.get();
			player.getRegion().removeObject(leaves);
			SystemManager.getScheduler().schedule(new ScheduledTask(time) {
				@Override
				public void run() {
					if (!player.getRegion().findAnyGameObject(leaves).isPresent()) {
						return;
					}
					player.getRegion().spawnObject(leaves);
				}
			});
		} else {
			System.out.println("Unable to find leaves of tree: { " + tree + " }");
		}
	}
	
	private boolean checkAll(Player player) {
		hatchet = getHatchet(player);
		if (hatchet == null) {
			player.getTransmitter().sendMessage("You dont have the required level to use that axe or you don't have a hatchet.");
			return false;
		}
		if (!hasWoodcuttingLevel(player)) {
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getTransmitter().sendMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}
	
	/**
	 * The delay to chop with
	 *
	 * @param player
	 * 		The player
	 */
	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = 0;
		int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(8) + summoningBonus) - Misc.getRandom(hatchet.getSpeedModifier());
		if (wcTimer < 1 + definitions.getLogRandomTime()) {
			wcTimer = 1 + Misc.getRandom(definitions.getLogRandomTime());
		}
		return wcTimer;
	}
	
	/**
	 * Gets a hatchet from the players inv/equip
	 *
	 * @param player
	 * 		The player
	 */
	private static HatchetDefinitions getHatchet(Player player) {
		HatchetDefinitions hatchet = null;
		for (HatchetDefinitions def : HatchetDefinitions.values()) {
			if (hatchet == HatchetDefinitions.INFERNO) {
				if (player.getSkills().getLevel(SkillConstants.FIREMAKING) < 92) {
					continue;
				}
			}
			if ((player.getInventory().getItems().contains(def.getItemId()) || player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON) == def.getItemId()) && player.getSkills().getLevelForXp(SkillConstants.WOODCUTTING) >= def.getLevelRequired()) {
				hatchet = def;
			}
		}
		return hatchet;
	}
	
	/**
	 * If the player has the level for the tree {@link #definitions}
	 *
	 * @param player
	 * 		The player
	 */
	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(8)) {
			player.getTransmitter().sendMessage("You need a woodcutting level of " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		return true;
	}
}
