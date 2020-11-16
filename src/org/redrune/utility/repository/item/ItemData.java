package org.redrune.utility.repository.item;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/7/2017
 */
public final class ItemData {
	
	/**
	 * The bonuses of the item
	 */
	@Getter
	@Setter
	private int[] bonuses;
	
	/**
	 * The examine information of the item
	 */
	@Getter
	@Setter
	private String examine;
	
	/**
	 * The weight of the item
	 */
	@Getter
	@Setter
	private double weight;
	
}
