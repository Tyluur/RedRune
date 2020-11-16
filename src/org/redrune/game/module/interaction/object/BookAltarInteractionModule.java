package org.redrune.game.module.interaction.object;

import org.redrune.game.content.dialogue.impl.misc.BookSwapDialogue;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class BookAltarInteractionModule implements ObjectInteractionModule {
	
	@Override
	public int[] objectSubscriptionIds() {
		return arguments(53739);
	}
	
	@Override
	public boolean handle(Player player, GameObject object, InteractionOption option) {
		player.getManager().getDialogues().startDialogue(new BookSwapDialogue());
		return true;
	}
}
