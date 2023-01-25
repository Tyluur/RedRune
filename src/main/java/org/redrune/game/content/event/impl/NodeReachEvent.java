package org.redrune.game.content.event.impl;

import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.node.NodeInteractionTask;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.content.event.Event;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NodeReachEvent extends Event<NodeReachEventContext> {
	
	/**
	 * Constructs a new event
	 */
	public NodeReachEvent() {
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, NodeReachEventContext context) {
		player.setInteractionTask(new NodeInteractionTask(context.getNode(), context.getTask(), context.getNode().isGameObject() || context.getNode().isNPC()));
		player.checkInteractionTask();
	}
	
	@Override
	public boolean canStart(Player player, NodeReachEventContext context) {
		if (context.getNode().isNPC()) {
			if (player.getManager().getLocks().isLocked(LockType.NPC_INTERACTION)) {
				return false;
			}
		} else if (context.getNode().isItem()) {
			if (player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION)) {
				return false;
			}
		} else if (context.getNode().isGameObject()) {
			if (player.getManager().getLocks().isLocked(LockType.OBJECT_INTERACTION)) {
				return false;
			}
		} else if (context.getNode().isPlayer()) {
			if (player.getManager().getLocks().isLocked(LockType.PLAYER_INTERACTION)) {
				return false;
			}
		} else if (player.isFrozen()) {
			return false;
		}
		return true;
	}
}
