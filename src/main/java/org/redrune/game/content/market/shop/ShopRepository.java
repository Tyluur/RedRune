package org.redrune.game.content.market.shop;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/15/2017
 */
public final class ShopRepository {
	
	/**
	 * The map of all shops, the key is the shop identifier
	 */
	private static final Map<Integer, Shop> SHOPS = new HashMap<>();
	
	/**
	 * The map of all shop currencies
	 */
	private static final Map<String, ShopCurrency> SHOP_CURRENCIES = new HashMap<>();
	
	/**
	 * The location of the shop file
	 */
	private static final String SHOP_FILE_LOCATION = "./data/repository/item/shops.json";
	
	/**
	 * All data from external sources in relevance to shops is loaded in this method.
	 */
	public static void load() {
		List<Shop> shops = Misc.loadGsonData(new File(SHOP_FILE_LOCATION));
		if (shops == null) {
			System.out.println("Unable to load shops from " + SHOP_FILE_LOCATION + "!");
			return;
		}
		// quick example testing instead of gson parsing
		shops.add(new Shop(1, "Blood Money Shop", Arrays.asList(new Item(11694), new Item(14484), new Item(6585), new Item(11732), new Item(15273, 10)), "BloodMoney"));
		shops.add(new Shop(2, "Gold Ticket Shop", Arrays.asList(new Item(11694), new Item(14484), new Item(6585), new Item(11732)), "GoldTicket"));
		
		Misc.getClassesInDirectory(ShopRepository.class.getPackage().getName() + ".currency").stream().filter(ShopCurrency.class::isInstance).forEach(clazz -> {
			ShopCurrency currency = (ShopCurrency) clazz;
			SHOP_CURRENCIES.put(currency.getClass().getSimpleName(), currency);
		});
		shops.forEach(ShopRepository::loadShopCurrency);
		System.out.println("Loaded " + SHOPS.size() + " game shops, and " + SHOP_CURRENCIES.size() + " currencies...");
	}
	
	/**
	 * Loads the currency of a shop
	 *
	 * @param shop
	 * 		The shop
	 */
	private static void loadShopCurrency(Shop shop) {
		String currencyName = shop.getCurrencyName() + "Currency";
		ShopCurrency currency = SHOP_CURRENCIES.get(currencyName);
		if (currency == null) {
			System.out.println("Unable to find currency {" + currencyName + "} for shop {" + shop.getName() + "}");
			return;
		}
		shop.setCurrency(currency);
		if (!SHOPS.containsKey(shop.getIdentifier())) {
			SHOPS.put(shop.getIdentifier(), shop);
		} else {
			System.out.println("Unable to load shop #" + shop.getIdentifier() + " - " + SHOPS.get(shop.getIdentifier()).getName() + " was using it already...");
		}
	}
	
	/**
	 * Opens a shop
	 *
	 * @param player
	 * 		The player opening the shop
	 * @param identifier
	 * 		The identifier of the shop
	 */
	public static void open(Player player, int identifier) {
		Shop shop = SHOPS.get(identifier);
		if (shop == null) {
			System.out.println("Unable to find shop #" + identifier);
			return;
		}
		shop.open(player);
	}
	
}
