package org.redrune.game.content.dialogue.impl.misc;

import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/10/2017
 */
public class SimpleNPCMessage extends Dialogue {
	
	@Override
	public void constructMessages(Player player) {
		this.chattingId = parameter(0);
		
		String[] messages = new String[getParameters().length - 1];
		for (int i = 0; i < messages.length; i++) {
			messages[i] = (String) getParameters()[i + 1];
		}
		npc(chattingId, NORMAL, messages);
	}
}
