package org.redrune.utility.rs.constant;

import lombok.Getter;
import org.redrune.game.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
// TODO: antifire, familiar, locations for extremes, healing, prayer renewal delay
public class PotionConstants implements SkillConstants {
	
	/**
	 * The enum with all potions
	 */
	public enum Potion {
		
		ATTACK_POTION(new int[] { 2428, 121, 123, 125 }, Impact.ATTACK_POTION),
		
		STRENGTH_POTION(new int[] { 113, 115, 117, 119 }, Impact.STRENGTH_POTION),
		
		DEFENCE_POTION(new int[] { 2432, 133, 135, 137 }, Impact.DEFENCE_POTION),
		
		RANGE_POTION(new int[] { 2444, 169, 171, 173 }, Impact.RANGE_POTION),
		
		MAGIC_POTION(new int[] { 3040, 3042, 3044, 3046 }, Impact.MAGIC_POTION),
		
		MAGIC_FLASK(new int[] { 23423, 23425, 23427, 23429, 23431, 23433 }, Impact.MAGIC_POTION),
		
		ANTI_POISION(new int[] { 2446, 175, 177, 179 }, Impact.ANTIPOISON),
		
		SUPER_ANTIPOISON(new int[] { 2448, 181, 183, 185 }, Impact.SUPER_ANTIPOISON),
		
		PRAYER_POTION(new int[] { 2434, 139, 141, 143 }, Impact.PRAYER_POTION),
		
		SUPER_ATT_POTION(new int[] { 2436, 145, 147, 149 }, Impact.SUPER_ATT_POTION),
		
		SUPER_STR_POTION(new int[] { 2440, 157, 159, 161 }, Impact.SUPER_STR_POTION),
		
		SUPER_DEF_POTION(new int[] { 2442, 163, 165, 167 }, Impact.SUPER_DEF_POTION),
		
		ENERGY_POTION(new int[] { 3008, 3010, 3012, 3014 }, Impact.ENERGY_POTION),
		
		SUPER_ENERGY(new int[] { 3016, 3018, 3020, 3022 }, Impact.SUPER_ENERGY),
		
		EXTREME_ATT_POTION(new int[] { 15308, 15309, 15310, 15311 }, Impact.EXTREME_ATT_POTION),
		
		EXTREME_STR_POTION(new int[] { 15312, 15313, 15314, 15315 }, Impact.EXTREME_STR_POTION),
		
		EXTREME_DEF_POTION(new int[] { 15316, 15317, 15318, 15319 }, Impact.EXTREME_DEF_POTION),
		
		EXTREME_MAGE_POTION(new int[] { 15320, 15321, 15322, 15323 }, Impact.EXTREME_MAG_POTION),
		
		EXTREME_RANGE_POTION(new int[] { 15324, 15325, 15326, 15327 }, Impact.EXTREME_RAN_POTION),
		
		SUPER_RESTORE_POTION(new int[] { 3024, 3026, 3028, 3030 }, Impact.SUPER_RESTORE),
		
		GUTHIX_REST_POTION(new int[] { 4417, 4419, 4421, 4423 }, Impact.GUTHIX_REST),
		
		SARADOMIN_BREW(new int[] { 6685, 6687, 6689, 6691 }, Impact.SARADOMIN_BREW),
		
		RECOVER_SPECIAL(new int[] { 15300, 15301, 15302, 15303 }, Impact.RECOVER_SPECIAL),
		
		SUPER_PRAYER(new int[] { 15328, 15329, 15330, 15331 }, Impact.SUPER_PRAYER),
		
		OVERLOAD(new int[] { 15332, 15333, 15334, 15335 }, Impact.OVERLOAD),
		
		ANTI_FIRE(new int[] { 2452, 2454, 2456, 2458 }, Impact.ANTI_FIRE),
		
		SUPER_ANTI_FIRE(new int[] { 15304, 15305, 15306, 15307 }, Impact.SUPER_ANTI_FIRE),
		
		SUMMONING_POTION(new int[] { 12140, 12142, 12144, 12146 }, Impact.SUMMONING_POT),
		
		SUMMONING_FLASK(new int[] { 23621, 23623, 23625, 23627, 23629, 23631 }, Impact.SUMMONING_POT),
		
		SANFEW_SERUM(new int[] { 10925, 10927, 10929, 10931 }, Impact.SANFEW_SERUM),
		
		PRAYER_RENEWAL(new int[] { 21630, 21632, 21634, 21636 }, Impact.PRAYER_RENEWAL),
		
		PRAYER_RENEWAL_FLASK(new int[] { 23609, 23611, 23613, 23615, 23617, 23619 }, Impact.PRAYER_RENEWAL),
		
		ATTACK_FLASK(new int[] { 23195, 23197, 23199, 23201, 23203, 23205 }, Impact.ATTACK_POTION),
		
		STRENGTH_FLASK(new int[] { 23207, 23209, 23211, 23213, 23215, 23217 }, Impact.STRENGTH_POTION),
		
		RESTORE_FLASK(new int[] { 23219, 23221, 23223, 23225, 23227, 23229 }, Impact.RESTORE_POTION),
		
		DEFENCE_FLASK(new int[] { 23231, 23233, 23235, 23237, 23239, 23241 }, Impact.DEFENCE_POTION),
		
		PRAYER_FLASK(new int[] { 23243, 23245, 23247, 23249, 23251, 23253 }, Impact.PRAYER_POTION),
		
		SUPER_ATTACK_FLASK(new int[] { 23255, 23257, 23259, 23261, 23263, 23265 }, Impact.SUPER_ATT_POTION),
		
		FISHING_FLASK(new int[] { 23267, 23269, 23271, 23273, 23275, 23277 }, Impact.FISHING_POTION),
		
		SUPER_STRENGTH_FLASK(new int[] { 23279, 23281, 23283, 23285, 23287, 23289 }, Impact.SUPER_STR_POTION),
		
		SUPER_DEFENCE_FLASK(new int[] { 23291, 23293, 23295, 23297, 23299, 23301 }, Impact.SUPER_DEF_POTION),
		
		RANGING_FLASK(new int[] { 23303, 23305, 23307, 23309, 23311, 23313 }, Impact.RANGE_POTION),
		
		ANTIPOISON_FLASK(new int[] { 23315, 23317, 23319, 23321, 23323, 23325 }, Impact.ANTIPOISON),
		
		SUPER_ANTIPOISON_FLASK(new int[] { 23327, 23329, 23331, 23333, 23335, 23337 }, Impact.SUPER_ANTIPOISON),
		
		SARADOMIN_BREW_FLASK(new int[] { 23351, 23353, 23355, 23357, 23359, 23361 }, Impact.SARADOMIN_BREW),
		
		ANTIFIRE_FLASK(new int[] { 23363, 23365, 23367, 23369, 23371, 23373 }, Impact.ANTI_FIRE),
		
		ENERGY_FLASK(new int[] { 23375, 23377, 23379, 23381, 23383, 23385 }, Impact.ENERGY_POTION),
		
		SUPER_ENERGY_FLASK(new int[] { 23387, 23389, 23391, 23393, 23395, 23397 }, Impact.SUPER_ENERGY),
		
		SUPER_RESTORE_FLASK(new int[] { 23399, 23401, 23403, 23405, 23407, 23409 }, Impact.SUPER_RESTORE),
		
		RECOVER_SPECIAL_FLASK(new int[] { 23483, 23484, 23485, 23486, 23487, 23488 }, Impact.RECOVER_SPECIAL),
		
		EXTREME_ATTACK_FLASK(new int[] { 23495, 23496, 23497, 23498, 23499, 23500 }, Impact.EXTREME_ATT_POTION),
		
		EXTREME_STRENGTH_FLASK(new int[] { 23501, 23502, 23503, 23504, 23505, 23506 }, Impact.EXTREME_STR_POTION),
		
		EXTREME_DEFENCE_FLASK(new int[] { 23507, 23508, 23509, 23510, 23511, 23512 }, Impact.EXTREME_DEF_POTION),
		
		EXTREME_MAGIC_FLASK(new int[] { 23513, 23514, 23515, 23516, 23517, 23518 }, Impact.EXTREME_MAG_POTION),
		
		EXTREME_RANGING_FLASK(new int[] { 23519, 23520, 23521, 23522, 23523, 23524 }, Impact.EXTREME_RAN_POTION),
		
		SUPER_PRAYER_FLASK(new int[] { 23525, 23526, 23527, 23528, 23529, 23530 }, Impact.SUPER_PRAYER),
		
		OVERLOAD_FLASK(new int[] { 23531, 23532, 23533, 23534, 23535, 23536 }, Impact.OVERLOAD),
		
		SANFEW_SERUM_FLASK(new int[] { 23567, 23569, 23571, 23573, 23575, 23577 }, Impact.SANFEW_SERUM);
		
		/**
		 * The ids of the potions
		 */
		@Getter
		private final int[] itemIds;
		
		/**
		 * The effect of the potion
		 */
		@Getter
		private final Impact impact;
		
		Potion(int[] itemIds, Impact impact) {
			this.itemIds = itemIds;
			this.impact = impact;
		}
		
		/**
		 * The map of potions, using this for small speed benefits
		 */
		private static final Map<Integer, Potion> POTION_MAP = new HashMap<>();
		
		/**
		 * Gets a potion by its item id
		 *
		 * @param itemId
		 * 		The item id of the potion
		 */
		public static Optional<Potion> getPotion(int itemId) {
			Optional<Entry<Integer, Potion>> entryOptional = POTION_MAP.entrySet().stream().filter(entry -> entry.getKey() == itemId).findFirst();
			return entryOptional.map(Entry::getValue);
		}
		
		/**
		 * Gets the item ids of all the potions in the {@link #POTION_MAP}
		 */
		public static int[] getAllPotionIds() {
			int[] array = new int[POTION_MAP.size()];
			int index = 0;
			for (Integer itemId : POTION_MAP.keySet()) {
				array[index] = itemId;
				index++;
			}
			return array;
		}
		
		static {
			POTION_MAP.clear();
			for (Potion potion : values()) {
				for (int itemId : potion.itemIds) {
					if (POTION_MAP.containsKey(itemId)) {
						System.out.println("Attempted duplicate entry of potion #" + itemId);
					} else {
						POTION_MAP.put(itemId, potion);
					}
				}
			}
		}
	}
	
	/**
	 * The enum of the impacts of potions
	 */
	public enum Impact {
		ATTACK_POTION(ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		FISHING_POTION(FISHING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (level + 3);
			}
		},
		ZAMORAK_BREW(ATTACK) {
			int toAdd;
			
			@Override
			public void extra(Player player) {
				toAdd = (player.getSkills().getLevelForXp(ATTACK));
				player.getSkills().setLevel(ATTACK, toAdd);
			}
			
		},
		SANFEW_SERUM(ATTACK, STRENGTH, DEFENCE, MAGIC, RANGE, AGILITY, COOKING, CRAFTING, FARMING, FIREMAKING, FISHING, FLETCHING, HERBLORE, MINING, RUNECRAFTING, SLAYER, SMITHING, THIEVING, WOODCUTTING, SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel) {
					return actualLevel;
				}
				if (actualLevel + boost > realLevel) {
					return realLevel;
				}
				return actualLevel + boost;
			}
			
			@Override
			public void extra(Player player) {
				player.getManager().getPrayers().restorePrayer((int) (player.getSkills().getLevelForXp(PRAYER) * 0.33 * 10));
			}
		},
		
		SUPER_RESTORE(ATTACK, STRENGTH, DEFENCE, MAGIC, RANGE, AGILITY, COOKING, CRAFTING, FARMING, FIREMAKING, FISHING, FLETCHING, HERBLORE, MINING, RUNECRAFTING, SLAYER, SMITHING, THIEVING, WOODCUTTING, SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel) {
					return actualLevel;
				}
				if (actualLevel + boost > realLevel) {
					return realLevel;
				}
				return actualLevel + boost;
			}
			
			@Override
			public void extra(Player player) {
				player.getManager().getPrayers().restorePrayer((int) (player.getSkills().getLevelForXp(PRAYER) * 0.33 * 10));
			}
			
		},
		SUMMONING_POT(SUMMONING) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int restore = (int) (Math.floor(player.getSkills().getLevelForXp(SUMMONING) * 0.25) + 7);
				if (actualLevel + restore > realLevel) {
					return realLevel;
				}
				return actualLevel + restore;
			}
			
			@Override
			public void extra(Player player) {
				/*Familiar familiar = player.getFamiliar();
				if (familiar != null) {
					familiar.restoreSpecialAttack(15);
				}*/
			}
		},
		ANTIPOISON {
			@Override
			public void extra(Player player) {
				player.getPoisonManager().addPoisonImmune(180_000);
				player.getTransmitter().sendMessage("You are now immune to poison.");
			}
		},
		SUPER_ANTIPOISON {
			@Override
			public void extra(Player player) {
				player.getPoisonManager().addPoisonImmune(360_000);
				player.getTransmitter().sendMessage("You are now immune to poison.");
			}
		},
		ENERGY_POTION {
			@Override
			public void extra(Player player) {
				double restoredEnergy = player.getVariables().getRunEnergy() + 20;
				player.getVariables().setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
				player.getTransmitter().refreshEnergy();
			}
		},
		SUPER_ENERGY() {
			@Override
			public void extra(Player player) {
				double restoredEnergy = player.getVariables().getRunEnergy() + 40;
				player.getVariables().setRunEnergy(restoredEnergy > 100 ? 100 : restoredEnergy);
				player.getTransmitter().refreshEnergy();
			}
		},
		ANTI_FIRE {
			@Override
			public void extra(final Player player) {
				/*player.addFireImmune(360000);
				final long current = player.getFireImmune();
				player.getPackets().sendMessage("You are now immune to dragonfire.");
				WorldTasksManager.schedule(new WorldTask() {
					boolean stop = false;
					
					@Override
					public void run() {
						if (current != player.getFireImmune()) {
							stop();
							return;
						}
						if (!stop) {
							player.getPackets().sendMessage("<col=480000>Your antifire potion is about to run out...</col>");
							stop = true;
						} else {
							stop();
							player.getPackets().sendMessage("<col=480000>Your antifire potion has ran out...</col>");
						}
					}
				}, 500, 100);*/
			}
		},
		SUPER_ANTI_FIRE() {
			@Override
			public void extra(final Player player) {
				/*player.addFireImmune(720000);
				final long current = player.getFireImmune();
				player.getPackets().sendMessage("You are now immune to dragonfire.");
				WorldTasksManager.schedule(new WorldTask() {
					boolean stop = false;
					
					@Override
					public void run() {
						if (current != player.getFireImmune()) {
							stop();
							return;
						}
						if (!stop) {
							player.getPackets().sendMessage("<col=480000>Your antifire potion is about to run out...</col>");
							stop = true;
						} else {
							stop();
							player.getPackets().sendMessage("<col=480000>Your antifire potion has ran out...</col>");
						}
					}
				}, 1000, 100);*/
			}
		},
		STRENGTH_POTION(STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		DEFENCE_POTION(DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 3 + (realLevel * 0.1));
			}
		},
		RANGE_POTION(RANGE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.1));
			}
		},
		MAGIC_POTION(MAGIC) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 5;
			}
		},
		PRAYER_POTION() {
			@Override
			public void extra(Player player) {
				player.getManager().getPrayers().restorePrayer((int) (Math.floor(player.getSkills().getLevelForXp(PRAYER) * 2.5) + 70));
			}
		},
		SUPER_STR_POTION(STRENGTH) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_DEF_POTION(DEFENCE) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		SUPER_ATT_POTION(ATTACK) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.15));
			}
		},
		EXTREME_STR_POTION(STRENGTH) {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_DEF_POTION(DEFENCE) {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_ATT_POTION(ATTACK) {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 5 + (realLevel * 0.22));
			}
		},
		EXTREME_RAN_POTION(RANGE) {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return (int) (level + 4 + (Math.floor(realLevel / 5.2)));
			}
		},
		EXTREME_MAG_POTION(MAGIC) {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int level = actualLevel > realLevel ? realLevel : actualLevel;
				return level + 7;
			}
		},
		RECOVER_SPECIAL() {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}*/
				Long time = player.getAttribute("Recover_Special_Pot", -1L);
				if (time != null && System.currentTimeMillis() - time < 30000) {
					long unlockedAt = (time + 30000) - System.currentTimeMillis();
					player.getTransmitter().sendMessage("You may only use this pot every 30 seconds. " + TimeUnit.MILLISECONDS.toSeconds(unlockedAt) + " seconds remaining.");
					return false;
				}
				return true;
			}
			
			@Override
			public void extra(Player player) {
				player.putAttribute("Recover_Special_Pot", System.currentTimeMillis());
				player.getCombatDefinitions().reduceSpecial(-25);
			}
		},
		GUTHIX_REST() {
			@Override
			public void extra(Player player) {
				player.getVariables().setRunEnergy((int) (player.getVariables().getRunEnergy() * 0.05) + player.getVariables().getRunEnergy());
				if (player.getVariables().getRunEnergy() >= 100) {
					player.getVariables().setRunEnergy(100);
				}
				player.getTransmitter().refreshEnergy();
				//player.getPoison().reset();
				player.heal(50);
			}
			
		},
		SARADOMIN_BREW("You drink some of the foul liquid.", ATTACK, DEFENCE, STRENGTH, MAGIC, RANGE) {
			@Override
			public boolean canDrink(Player player) {
				return true;
			}
			
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				if (skillId == DEFENCE) {
					int boost = (int) (realLevel * 0.20);
					int level = actualLevel > realLevel ? realLevel : actualLevel;
					return level + boost;
				} else {
					return (int) (actualLevel * 0.96) - 1;
				}
			}
			
			@Override
			public void extra(Player player) {
				int hitpointsModification = (int) (player.getMaxHealth() * 0.15);
				player.heal(hitpointsModification + 20, hitpointsModification);
			}
		},
		
		OVERLOAD() {
			@Override
			public boolean canDrink(Player player) {
				/*if (player.getControllerManager().getController() instanceof Wilderness || player.getControllerManager().getController() instanceof CrucibleControler || FfaZone.isOverloadChanged(player)) {
					player.getPackets().sendMessage("You cannot drink this potion here.");
					return false;
				}
						if (player.getOverloadDelay() > 0) {
					player.getPackets().sendMessage(
							"You may only use this potion every five minutes.");
					return false;
				}*/
				/*if (player.getHealthPoints() <= 500 || player.getOverloadDelay() > 480) {
					player.getPackets().sendMessage("You need more than 500 life points to survive the power of overload.");
					return false;
				}*/
				return true;
			}
			
			@Override
			public void extra(final Player player) {
				/*player.setOverloadDelay(501);
				WorldTasksManager.schedule(new WorldTask() {
					int count = 4;
					
					@Override
					public void run() {
						if (count == 0) {
							stop();
						}
						player.setNextAnimation(new Animation(3170));
						player.setNextGraphics(new Graphics(560));
						player.applyHit(new Hit(player, 100, HitLook.REGULAR_DAMAGE, 0));
						count--;
					}
				}, 0, 2);*/
			}
		},
		SUPER_PRAYER() {
			@Override
			public void extra(Player player) {
				player.getManager().getPrayers().restorePrayer((int) (Math.floor(player.getSkills().getLevelForXp(PRAYER) * 3.5) + 70));
			}
		},
		PRAYER_RENEWAL() {
			@Override
			public void extra(Player player) {
				//player.setPrayerRenewalDelay(501);
			}
		},
		RESTORE_POTION(ATTACK, STRENGTH, MAGIC, RANGE, PRAYER) {
			@Override
			public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
				int boost = (int) (realLevel * 0.33);
				if (actualLevel > realLevel) {
					return actualLevel;
				}
				if (actualLevel + boost > realLevel) {
					return realLevel;
				}
				return actualLevel + boost;
			}
		};
		
		/**
		 * The skills that are impacted by this potion
		 */
		@Getter
		private final int[] impactedSkills;
		
		@Getter
		private final String drinkMessage;
		
		Impact(int... impactedSkills) {
			this(null, impactedSkills);
		}
		
		Impact(String drinkMessage, int... impactedSkills) {
			this.drinkMessage = drinkMessage;
			this.impactedSkills = impactedSkills;
		}
		
		/**
		 * Gets the new level of the affected skill
		 *
		 * @param player
		 * 		The player
		 * @param skillId
		 * 		The id of the skill
		 * @param actualLevel
		 * 		The current level
		 * @param realLevel
		 * 		The real level
		 */
		public int getAffectedSkill(Player player, int skillId, int actualLevel, int realLevel) {
			return actualLevel;
		}
		
		/**
		 * Checks if the player can drink the potion. They can usually always drink
		 *
		 * @param player
		 * 		The player
		 */
		public boolean canDrink(Player player) {
			return true;
		}
		
		/**
		 * Handles the extra effects of a potion, these are usually not used
		 */
		public void extra(Player player) {
		}
	}
	
}
