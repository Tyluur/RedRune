package org.redrune.game.content.event.context;

import lombok.Getter;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.content.event.EventContext;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class NPCEventContext implements EventContext {
	
	/**
	 * The npc interacting with
	 */
	@Getter
	private final NPC npc;
	
	/**
	 * The option we selected
	 */
	@Getter
	private final InteractionOption option;
	
	public NPCEventContext(NPC npc, InteractionOption option) {
		this.npc = npc;
		this.option = option;
	}
}
