package org.redrune.game.node.item;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class Item extends Node {
	
	/**
	 * The item id.
	 */
	@Getter
	@Setter
	private short id;
	
	/**
	 * The item amount.
	 */
	@Getter
	@Setter
	private int amount;
	
	/**
	 * The item definition.
	 */
	@Setter
	private transient ItemDefinition definitions;
	
	/**
	 * Constructs an item with a location. This is only to be used for floor item child class management
	 *
	 * @param location
	 * 		The location of the item
	 */
	public Item(Location location) {
		super(location);
		this.id = -1;
		this.amount = -1;
	}
	
	/**
	 * Constructs a new {@code Item} {@code Object}.
	 *
	 * @param id
	 * 		The item id.
	 */
	public Item(int id) {
		this(id, 1);
	}
	
	/**
	 * Constructs a new {@code Item} {@code Object}.
	 *
	 * @param id
	 * 		The item id.
	 * @param amount
	 * 		The amount of this item.
	 */
	public Item(int id, int amount) {
		super(null);
		this.id = (short) id;
		this.amount = amount;
		this.definitions = ItemDefinitionParser.forId(id);
	}
	
	@Override
	public boolean equals(Object o) {
		return o.getClass() == Item.class && ((Item) o).id == id && ((Item) o).getAmount() == amount;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getName() + ", amount=" + amount + "]";
	}
	
	/**
	 * Gets the name of the item
	 */
	public String getName() {
		return getDefinitions().getName();
	}
	
	@Override
	public void register() {
	
	}
	
	@Override
	public void deregister() {
	
	}
	
	@Override
	public Item toItem() {
		return this;
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	/**
	 * Reduces the amount of the item
	 *
	 * @param amount
	 * 		The amount to reduce by
	 */
	public Item reduceAmount(int amount) {
		this.amount -= amount;
		// make sure we're not less than 0
		if (this.amount < 0) {
			this.amount = 0;
		}
		return this;
	}
	
	/**
	 * Gets the definitions, cached.
	 */
	public ItemDefinition getDefinitions() {
		if (definitions == null) {
			definitions = ItemDefinitionParser.forId(id);
		}
		return definitions;
	}
}
