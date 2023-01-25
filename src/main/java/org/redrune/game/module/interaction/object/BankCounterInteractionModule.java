package org.redrune.game.module.interaction.object;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class BankCounterInteractionModule implements ObjectInteractionModule {
	
	@Override
	public int[] objectSubscriptionIds() {
		return arguments(42378, 42217, 42377);
	}
	
	@Override
	public boolean handle(Player player, GameObject object, InteractionOption option) {
		player.getBank().open();
		return true;
	}
}
