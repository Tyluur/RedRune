package org.redrune.game.content.event.impl;

import org.redrune.game.content.event.context.CommandEventContext;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class CommandEvent extends Event<CommandEventContext> {
	
	public CommandEvent() {
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setWalkablePolicy(WalkablePolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, CommandEventContext context) {
		CommandRepository.processEntry(player, context.getArguments(), context.isConsole());
	}
}
