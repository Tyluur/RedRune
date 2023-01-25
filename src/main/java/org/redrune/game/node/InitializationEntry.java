package org.redrune.game.node;

import lombok.Getter;
import lombok.Setter;

/**
 * Wraps around a node to represent its entry in a queue.
 *
 * @author Emperor
 */
public class InitializationEntry {
	
	/**
	 * The node.
	 */
	@Getter
	private final Node node;
	
	/**
	 * If the node is being removed from the game, rather than added.
	 */
	@Getter
	@Setter
	private boolean removal;
	
	/**
	 * Constructs a new {@code InitializationEntry} {@code Object}.
	 *
	 * @param node
	 * 		The node.
	 */
	public InitializationEntry(Node node) {
		this(node, false);
	}
	
	/**
	 * Constructs a new {@code InitializationEntry} {@code Object}.
	 *
	 * @param node
	 * 		The node.
	 * @param removal
	 * 		If the node should be removed from the game, rather than added.
	 */
	public InitializationEntry(Node node, boolean removal) {
		this.node = node;
		this.removal = removal;
	}
	
	@Override
	public int hashCode() {
		if (node != null) {
			return node.hashCode();
		}
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof InitializationEntry)) {
			return false;
		}
		return ((InitializationEntry) o).node.equals(node);
	}
	
	/**
	 * Initializes the node.
	 *
	 * @return The node instance.
	 */
	public Node initialize() {
		node.setRenderable(true);
		return node;
	}
	
}