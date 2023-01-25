package org.redrune.game.content.market.shop.currency;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.market.shop.ShopCurrency;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public class GoldTicketCurrency implements ShopCurrency {
	
	@Override
	public String name() {
		return "gold ticket";
	}
	
	@Override
	public int getCurrencyAmount(Player player) {
		return player.getInventory().getItems().getNumberOf(GOLD_TICKET);
	}
	
	@Override
	public void reduceCurrency(Player player, int amount) {
		player.getInventory().deleteItem(GOLD_TICKET, amount);
	}
	
	@Override
	public int getBuyPrice(int itemId) {
		switch (itemId) {
			default:
				return Integer.MAX_VALUE;
		}
	}
	
	@Override
	public int itemId() {
		return GOLD_TICKET;
	}
}
