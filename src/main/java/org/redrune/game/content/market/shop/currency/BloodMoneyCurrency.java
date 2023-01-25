package org.redrune.game.content.market.shop.currency;

import org.redrune.game.content.market.shop.ShopCurrency;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public class BloodMoneyCurrency implements ShopCurrency {
	
	@Override
	public String name() {
		return "blood money";
	}
	
	@Override
	public int getCurrencyAmount(Player player) {
		return player.getInventory().getItems().getNumberOf(BLOOD_MONEY);
	}
	
	@Override
	public void reduceCurrency(Player player, int amount) {
		player.getInventory().deleteItem(BLOOD_MONEY, amount);
	}
	
	@Override
	public int getBuyPrice(int itemId) {
		switch (itemId) {
			case 11694:
				return 200;
			case 14484:
				return 300;
			case 6585:
				return 50;
			case 15273:
				return 20;
		}
		return 1;
	}
	
	@Override
	public int stockAmount(int itemId) {
		switch (itemId) {
			case 15273:
				return 10;
		}
		return 1;
	}
	
	@Override
	public int itemId() {
		return BLOOD_MONEY;
	}
}
