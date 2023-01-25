package org.redrune.game.content.dialogue.impl.misc;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.content.dialogue.messages.OptionDialogueMessage;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class WorldMapDialogue extends Dialogue {
	
	@Override
	public void constructMessages(Player player) {
		construct(new OptionDialogueMessage("Open World Map?", new String[] { "Yes", "No" }, () -> {
			action(() -> player.getManager().getInterfaces().openWorldMap());
		}, () -> {
			action(() -> end(player));
		}));
	}
}
