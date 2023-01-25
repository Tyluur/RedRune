package org.redrune.game.content.event.context;

import lombok.Getter;
import org.redrune.game.node.Node;
import org.redrune.game.content.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeReachEventContext implements EventContext {
	
	/**
	 * The node we are travelling to
	 */
	@Getter
	private final Node node;
	
	/**
	 * The task to execute
	 */
	@Getter
	private final Runnable task;
	
	public NodeReachEventContext(Node node, Runnable task) {
		this.node = node;
		this.task = task;
	}
}
