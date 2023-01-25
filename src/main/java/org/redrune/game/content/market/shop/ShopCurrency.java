package org.redrune.game.content.market.shop;

import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.ItemConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public interface ShopCurrency extends ItemConstants {
	
	/**
	 * The name of the currency
	 */
	String name();
	
	/**
	 * Gets the amount of the currency the player has
	 *
	 * @param player
	 * 		The player
	 */
	int getCurrencyAmount(Player player);
	
	/**
	 * Reduces the amount of the currency this player has
	 *
	 * @param player
	 * 		The player
	 * @param amount
	 * 		The amount to reduce by
	 */
	void reduceCurrency(Player player, int amount);
	
	/**
	 * Gets the buy price of any item is found in this method
	 */
	int getBuyPrice(int itemId);
	
	/**
	 * The stock amount of an item that is purchased. This is used for buying things that come in more than 1 amount
	 * [500x noted sharks]
	 *
	 * @param itemId
	 * 		The id of the item
	 */
	default int stockAmount(int itemId) {
		return 1;
	}
	
	/**
	 * Checks that an item is buyable by a player
	 *
	 * @param player
	 * 		The player
	 * @param itemId
	 * 		The id of the item
	 */
	default boolean buyable(Player player, int itemId) {
		return true;
	}
	
	/**
	 * Gets the price of the item being sold
	 *
	 * @param buyPrice
	 * 		The price
	 */
	default int getSellValue(int buyPrice) {
		return (int) (buyPrice * 0.75);
	}
	
	/**
	 * The id of the currency item.
	 */
	default int itemId() {
		return -1;
	}
	
}
