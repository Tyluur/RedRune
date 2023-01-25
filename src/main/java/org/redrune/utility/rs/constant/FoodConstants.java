package org.redrune.utility.rs.constant;

import lombok.Getter;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;
import org.redrune.utility.tool.RandomFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class FoodConstants {
	
	public enum Food {
		
		/**
		 * Fish
		 */
		CRAFISH(13433, 2),
		
		ANCHOVIE(319, 1),
		
		SHRIMP(315, 3),
		
		KARAMBWANJI(3151, 3),
		
		SARDINE(325, 3),
		
		POISON_KARAMBWANJI(3146, 0, FoodEffect.POISION_KARMAMWANNJI_EFFECT),
		
		KARAMBWANI(3144, 18) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		PURPLE_SWEETS(10476, RandomFunction.percentageChance(30) ? 30 : RandomFunction.percentageChance(50) ? 20 : 10, FoodEffect.PURPLE_SWEET_EFFECT),
		
		SLIMY_EEL(3381, 7 + Misc.random(2)),
		
		RAINBOW_FISH(10136, 11),
		
		CAVE_EEL(5003, 8 + Misc.random(2)),
		
		LAVA_EEL(2149, 7 + Misc.random(2)),
		
		HERRING(347, 5),
		
		MACKEREL(335, 6),
		
		TROUT(333, 7),
		
		COD(339, 7),
		
		PIKE(351, 8),
		
		SALMON(329, 9),
		
		TUNA(361, 10),
		
		LOBSTER(379, 12),
		
		BASS(365, 13),
		
		SWORDFISH(373, 14),
		
		MONKFISH(7946, 16),
		
		SHARK(385, 20),
		
		TURTLE(397, 21),
		
		MANTA(391, 22),
		
		CAVEFISH(15266, 20),
		
		SALVE_EEL(18173, 20),
		
		ROCKTAIL(15272, 23, 0, null, 10),
		
		FURY_SHARK(20429, 28),
		
		/**
		 * Meats
		 */
		CHICKEN(2140, 3),
		
		MEAT(2142, 3),
		
		RABIT(3228, 5),
		
		ROAST_RABIT(7223, 7),
		
		ROASTED_BIRD_MEAT(9980, 6),
		
		CRAB_MEAT(7521, 10),
		
		ROASTED_BEAST_MEAT(9988, 8),
		
		CHOMPY(2878, 10),
		
		JUBBLY(7568, 15),
		
		OOMILE(2343, 14),
		
		/**
		 * Pies
		 */
		REDBERRY_PIE_FULL(2325, 5, 2333),
		
		REDBERRY_PIE_HALF(2333, 5, 2313),
		
		MEAT_PIE_FULL(2327, 6, 2331),
		
		MEAT_PIE_HALF(2331, 6, 2313),
		
		APPLE_PIE_FULL(2323, 7, 2335),
		
		APPLE_PIE_HALF(2335, 7, 2313),
		
		GARDEN_PIE_FULL(7178, 6, 7180, FoodEffect.GARDEN_PIE),
		
		GARDEN_PIE_HALF(7180, 6, 2313, FoodEffect.GARDEN_PIE),
		
		FISH_PIE_FULL(7188, 6, 7190, FoodEffect.FISH_PIE),
		
		FISH_PIE_HALF(7188, 6, 2313, FoodEffect.FISH_PIE),
		
		ADMIRAL_PIE_FULL(7198, 8, 7200, FoodEffect.ADMIRAL_PIE),
		
		ADMIRAL_PIE_HALF(7200, 8, 2313, FoodEffect.ADMIRAL_PIE),
		
		WILD_PIE_FULL(7208, 11, 7210, FoodEffect.WILD_PIE),
		
		WILD_PIE_HALF(7210, 11, 2313, FoodEffect.WILD_PIE),
		
		SUMMER_PIE_FULL(7218, 11, 7220, FoodEffect.SUMMER_PIE),
		
		SUMMER_PIE_HALF(7220, 11, 2313, FoodEffect.SUMMER_PIE),
		
		/**
		 * Stews
		 */
		
		STEW(2003, 11, 1923),
		
		SPICY_STEW(7513, 11, 1923, FoodEffect.SPICY_STEW_EFFECT),
		
		CURRY(2011, 19, 1923),
		
		/**
		 * Pizzas
		 */
		PLAIN_PIZZA_FULL(2289, 7, 2291) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		PLAIN_PIZZA_HALF(2291, 7) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		MEAT_PIZZA_FULL(2293, 8, 2295) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		MEAT_PIZZA_HALF(2295, 8) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		ANCHOVIE_PIZZA_FULL(2297, 9, 2299) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		ANCHOVIE_PIZZA_HALF(2299, 9) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		PINEAPPLE_PIZZA_FULL(2301, 11, 2303) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		PINEAPPLE_PIZZA_HALF(2303, 11) {
			@Override
			public boolean isComboFood() {
				return true;
			}
		},
		
		/**
		 * Potato Toppings
		 */
		SPICEY_SAUCE(7072, 2, 1923),
		
		CHILLI_CON_CARNIE(7062, 14, 1923),
		
		SCRAMBLED_EGG(7078, 5, 1923),
		
		EGG_AND_TOMATO(7064, 8, 1923),
		
		FRIED_ONIONS(7084, 9, 1923),
		
		MUSHROOM_AND_ONIONS(7066, 11, 1923),
		
		FRIED_MUSHROOMS(7082, 5, 1923),
		
		TUNA_AND_CORN(7068, 13, 1923),
		
		/**
		 * Baked Potato
		 */
		BAKED_POTATO(6701, 4),
		
		POTATO_WITH_BUTTER(6703, 14),
		
		CHILLI_POTATO(7054, 14),
		
		POTATO_WITH_CHEESE(6705, 16),
		
		EGG_POTATO(7056, 16),
		
		MUSHROOM_AND_ONION_POTATO(7058, 20),
		
		TUNA_POTATO(7060, 24),
		
		/**
		 * Gnome Food
		 */
		TOAD_CRUNCHIES(2217, 8),
		
		SPICY_CRUNCHIES(2213, 7),
		
		WORM_CRUNCHIES(2205, 8),
		
		CHOCOCHIP_CRUNCHIES(9544, 7),
		
		FRUIT_BATTA(2277, 11),
		
		TOAD_BATTA(2255, 11),
		
		WORM_BATTA(2253, 11),
		
		VEGETABLE_BATTA(2281, 11),
		
		CHEESE_AND_TOMATO_BATTA(9535, 11),
		
		WORM_HOLE(2191, 12),
		
		VEG_BALL(2195, 12),
		
		PRE_MADE_VEG_BALL(2235, 12),
		
		TANGLED_TOAD_LEGS(2187, 15),
		
		CHOCOLATE_BOMB(2185, 15),
		
		/**
		 * Misc
		 */
		CAKE(1891, 4, 1893),
		
		TWO_THIRDS_CAKE(1893, 4, 1895),
		
		SLICE_OF_CAKE(1895, 4),
		
		CHOCOLATE_CAKE(1897, 4, 1899),
		
		TWO_THIRDS_CHOCOLATE_CAKE(1899, 4, 1901),
		
		CHOCOLATE_SLICE(1901, 4),
		
		FISHCAKE(7530, 11),
		
		BREAD(2309, 5),
		
		CABBAGE(1965, 1, FoodEffect.CABAGE_MESSAGE),
		
		ONION(1957, 1, FoodEffect.ONION_MESSAGE),
		
		EVIL_TURNIP(12134, 1),
		
		POT_OF_CREAM(2130, 1),
		
		CHEESE_WHEEL(18789, 2),
		
		THIN_SNAIL_MEAT(3369, 5 + Misc.random(2)),
		
		LEAN_SNAIL_MEAT(3371, 8),
		
		FAT_SNAIL_MEAT(3373, 8 + Misc.random(2));
		
		/**
		 * A map of object ids to foods.
		 */
		private static final Map<Integer, Food> FOOD_MAP = new HashMap<>();
		
		/*
		 * Populates the tree map.
		 */
		static {
			for (final Food food : Food.values()) {
				FOOD_MAP.put(food.id, food);
			}
		}
		
		/**
		 * The food id
		 */
		@Getter
		private int id;
		
		/**
		 * The healing health
		 */
		@Getter
		private int heal;
		
		/**
		 * The new food id if needed
		 */
		@Getter
		private int newId;
		
		/**
		 * The extra hp the food gives
		 */
		@Getter
		private int extraHP;
		
		/**
		 * Our effect
		 */
		@Getter
		private FoodEffect effect;
		
		/**
		 * Represents a food being eaten
		 *
		 * @param id
		 * 		The food id
		 * @param heal
		 * 		The healing health received
		 */
		Food(int id, int heal) {
			this.id = id;
			this.heal = heal;
		}
		
		/**
		 * Represents a part of a food item being eaten (example: cake)
		 *
		 * @param id
		 * 		The food id
		 * @param heal
		 * 		The heal amount
		 * @param newId
		 * 		The new food id
		 */
		Food(int id, int heal, int newId) {
			this(id, heal, newId, null);
		}
		
		Food(int id, int heal, int newId, FoodEffect effect) {
			this(id, heal, newId, effect, 0);
		}
		
		Food(int id, int heal, int newId, FoodEffect effect, int extraHP) {
			this.id = id;
			this.heal = heal;
			this.newId = newId;
			this.effect = effect;
			this.extraHP = extraHP;
		}
		
		Food(int id, int heal, FoodEffect effect) {
			this(id, heal, 0, effect);
		}
		
		/**
		 * Gets the item ids of all the potions in the {@link #FOOD_MAP}
		 */
		public static int[] getAllFoodIds() {
			int[] array = new int[FOOD_MAP.size()];
			int index = 0;
			for (Integer itemId : FOOD_MAP.keySet()) {
				array[index] = itemId;
				index++;
			}
			return array;
		}
		
		/**
		 * Gets a food by an object id.
		 *
		 * @param itemId
		 * 		The object id.
		 * @return The food, or <code>null</code> if the object is not a food.
		 */
		public static Optional<Food> forId(int itemId) {
			return Optional.of(FOOD_MAP.get(itemId));
		}
		
		/**
		 * If the food is a combo food
		 */
		public boolean isComboFood() {
			return false;
		}
		
	}
	
	/**
	 * The enum of possible effects that food can have
	 */
	public enum FoodEffect {
		SUMMER_PIE {
			@Override
			public void fire(Player player) {
				int runEnergy = (int) (player.getVariables().getRunEnergy() * 1.1);
				if (runEnergy > 100) {
					runEnergy = 100;
				}
				player.getVariables().setRunEnergy(runEnergy);
				player.getTransmitter().refreshEnergy();
				int level = player.getSkills().getLevel(SkillConstants.AGILITY);
				int realLevel = player.getSkills().getLevelForXp(SkillConstants.AGILITY);
				player.getSkills().setLevel(SkillConstants.AGILITY, level >= realLevel ? realLevel + 5 : level + 5);
			}
			
		},
		
		GARDEN_PIE {
			@Override
			public void fire(Player player) {
				int level = player.getSkills().getLevel(SkillConstants.FARMING);
				int realLevel = player.getSkills().getLevelForXp(SkillConstants.FARMING);
				player.getSkills().setLevel(SkillConstants.FARMING, level >= realLevel ? realLevel + 3 : level + 3);
			}
			
		},
		
		FISH_PIE {
			@Override
			public void fire(Player player) {
				int level = player.getSkills().getLevel(SkillConstants.FISHING);
				int realLevel = player.getSkills().getLevelForXp(SkillConstants.FISHING);
				player.getSkills().setLevel(SkillConstants.FISHING, level >= realLevel ? realLevel + 3 : level + 3);
			}
		},
		
		ADMIRAL_PIE {
			@Override
			public void fire(Player player) {
				int level = player.getSkills().getLevel(SkillConstants.FISHING);
				int realLevel = player.getSkills().getLevelForXp(SkillConstants.FISHING);
				player.getSkills().setLevel(SkillConstants.FISHING, level >= realLevel ? realLevel + 5 : level + 5);
			}
		},
		
		WILD_PIE {
			@Override
			public void fire(Player player) {
				int level = player.getSkills().getLevel(SkillConstants.SLAYER);
				int realLevel = player.getSkills().getLevelForXp(SkillConstants.SLAYER);
				player.getSkills().setLevel(SkillConstants.SLAYER, level >= realLevel ? realLevel + 4 : level + 4);
				int level2 = player.getSkills().getLevel(SkillConstants.RANGE);
				int realLevel2 = player.getSkills().getLevelForXp(SkillConstants.RANGE);
				player.getSkills().setLevel(SkillConstants.RANGE, level2 >= realLevel2 ? realLevel2 + 4 : level2 + 4);
			}
		},
		
		SPICY_STEW_EFFECT {
			@Override
			public void fire(Player player) {
				if (Misc.random(100) > 5) {
					int level = player.getSkills().getLevel(SkillConstants.COOKING);
					int realLevel = player.getSkills().getLevelForXp(SkillConstants.COOKING);
					player.getSkills().setLevel(SkillConstants.COOKING, level >= realLevel ? realLevel + 6 : level + 6);
				} else {
					int level = player.getSkills().getLevel(SkillConstants.COOKING);
					player.getSkills().setLevel(SkillConstants.COOKING, level <= 6 ? 0 : level - 6);
				}
			}
			
		},
		
		CABAGE_MESSAGE {
			@Override
			public void fire(Player player) {
				player.getTransmitter().sendMessage("You don't really like it much.", true);
			}
		},
		
		ONION_MESSAGE {
			@Override
			public void fire(Player player) {
				player.getTransmitter().sendMessage("It hurts to see a grown " + (player.getDetails().getAppearance().isMale() ? "male" : "female") + "cry.");
			}
		},
		
		POISION_KARMAMWANNJI_EFFECT {
			@Override
			public void fire(Player player) {
				player.getHitMap().applyHit(new Hit(player, 50, HitSplat.POISON_DAMAGE));
			}
		},
		
		PURPLE_SWEET_EFFECT {
			@Override
			public void fire(Player player) {
				player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() + (player.getVariables().getRunEnergy() * 0.10));
			}
		};
		
		/**
		 * Fires the effect of the food
		 *
		 * @param player
		 * 		The player eating the food
		 */
		public void fire(Player player) {
		}
	}
	
}
