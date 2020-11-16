package org.redrune.game.node.item;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;

/**
 * This class represents an item that will be dropped from an npc.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class Drop {
	
	/**
	 * The array of possible charms to drop
	 */
	public static final int[] CHARMS = { 12158, 12159, 12160, 12163 };
	
	/**
	 * The id of the item that is being dropped
	 */
	@Getter
	@Setter
	private int itemId;
	
	/**
	 * The minimum amount of the item to drop
	 */
	@Getter
	@Setter
	private int minAmount;
	
	/**
	 * The maximum amount of the item to drop
	 */
	@Getter
	@Setter
	private int maxAmount;
	
	/**
	 * The rate at which this item will  be dropped
	 */
	@Getter
	@Setter
	private double rate;
	
	public Drop(int itemId, double rate, int minAmount, int maxAmount) {
		this.itemId = itemId;
		this.rate = rate;
		this.minAmount = minAmount;
		this.maxAmount = maxAmount;
	}
	
	@Override
	public String toString() {
		return "Drop{" + "itemId=" + itemId + ", minAmount=" + minAmount + ", maxAmount=" + maxAmount + ", rate=" + rate + '}';
	}
	
	/**
	 * If the drop is a bone
	 */
	public boolean isBone() {
		String name = ItemDefinitionParser.forId(itemId).getName().toLowerCase();
		return name.contains("bone");
	}
	
	public int getExtraAmount() {
		return maxAmount - minAmount;
	}
}
