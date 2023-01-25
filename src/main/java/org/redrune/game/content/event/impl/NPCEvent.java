package org.redrune.game.content.event.impl;

import org.redrune.game.module.ModuleRepository;
import org.redrune.game.node.entity.link.interaction.NPCInteraction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.content.event.Event;
import org.redrune.game.content.event.context.NPCEventContext;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class NPCEvent extends Event<NPCEventContext> {
	
	@Override
	public void run(Player player, NPCEventContext context) {
		NPC npc = context.getNpc();
		InteractionOption option = context.getOption();
		
		player.getInteractionManager().startInteraction(new NPCInteraction(player, npc));
		npc.getInteractionManager().startInteraction(new NPCInteraction(player, npc));
		
		if (option == InteractionOption.FIRST_OPTION && DialogueRepository.handleNPC(player, npc)) {
			return;
		}
		if (ModuleRepository.handle(player, npc, option)) {
			return;
		}
		player.getTransmitter().sendMessage("Nothing interesting happens.");
	}
	
	@Override
	public boolean canStart(Player player, NPCEventContext context) {
		return !player.getManager().getLocks().isLocked(LockType.NPC_INTERACTION);
	}
}
