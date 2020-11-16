package org.redrune.game.content.action.interaction;

import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.FoodConstants.Food;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class PlayerFoodAction implements Action {
	
	/**
	 * The food instance
	 */
	private final Food food;
	
	/**
	 * The item
	 */
	private final Item item;
	
	/**
	 * The slot the item is in
	 */
	private final int slot;
	
	public PlayerFoodAction(Food food, Item item, int slot) {
		this.food = food;
		this.item = item;
		this.slot = slot;
	}
	
	@Override
	public boolean start(Player player) {
		if (!canEat(player)) {
			return false;
		}
		
		// we reduce the food amount, then update the health, then fire the food effects
		int foodDelay = reduceFood(player);
		updateHealth(player);
		fireFoodEffect(player);
		
		// visual
		player.sendAnimation(829);
		// store data
		setDelay(player, 2);
		player.putAttribute(AttributeKey.FOOD_DELAY, System.currentTimeMillis() + foodDelay);
		return true;
	}
	
	/**
	 * Reduces the amount of the food the player has
	 *
	 * @param player
	 * 		The player
	 * @return The delay from eating the food
	 */
	private int reduceFood(Player player) {
		String name = ItemDefinitionParser.forId(food.getId()).getName().toLowerCase();
		player.getTransmitter().sendMessage("You eat the " + name + ".");
		int foodDelay = name.contains("half") ? 600 : 1800;
		
		if (food != Food.PURPLE_SWEETS) {
			player.getInventory().getItems().set(slot, food.getNewId() == 0 ? null : new Item(food.getNewId(), 1));
		} else {
			if (item.getAmount() == 1) {
				player.getInventory().getItems().set(slot, null);
			} else {
				player.getInventory().getItems().set(slot, item.reduceAmount(1));
			}
		}
		player.getInventory().refresh(slot);
		return foodDelay;
	}
	
	/**
	 * Updates the health of the player for the food
	 *
	 * @param player
	 * 		The player
	 */
	private void updateHealth(Player player) {
		int hp = player.getHealthPoints();
		player.heal(food.getHeal() * (food != Food.PURPLE_SWEETS ? 10 : 1), food.getExtraHP() * (food != Food.PURPLE_SWEETS ? 10 : 1));
		if (player.getHealthPoints() > hp) {
			player.getTransmitter().sendMessage("It heals some health.");
		}
	}
	
	/**
	 * Fires the food effect
	 *
	 * @param player
	 * 		The player
	 */
	private void fireFoodEffect(Player player) {
		if (food.getEffect() != null) {
			food.getEffect().fire(player);
		}
	}
	
	@Override
	public boolean process(Player player) {
		return true;
	}
	
	@Override
	public int processOnTicks(Player player) {
		return -1;
	}
	
	@Override
	public void stop(Player player) {
	
	}
	
	/**
	 * Checks if the player can eat the food
	 *
	 * @param player
	 * 		The player
	 */
	private boolean canEat(Player player) {
		if (food == null) {
			return false;
		}
		// that item is interacted with differently in the activity
		if (player.getManager().getActivities().handlesNodeInteraction(item, InteractionOption.FIRST_OPTION)) {
			return false;
		}
		// can't eat yet
		if (player.getAttribute(AttributeKey.FOOD_DELAY, -1L) > System.currentTimeMillis()) {
			if (food.equals(Food.KARAMBWANI)) {
				if (player.getAttribute("combo_food_eat") != null) {
					if (System.currentTimeMillis() - (long) player.getAttribute("combo_food_eat") <= 1200) {
						return true;
					}
				}
				player.putAttribute("combo_food_eat", System.currentTimeMillis());
			}
			return false;
		}
		return true;
	}
}
