package org.redrune.game.module.interaction.item;

import org.redrune.game.content.action.interaction.PlayerPotionAction;
import org.redrune.game.module.type.ItemInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.PotionConstants.Potion;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
public class PotionDrinkInteractionModule implements ItemInteractionModule {
	
	@Override
	public int[] itemSubscriptionIds() {
		return Potion.getAllPotionIds();
	}
	
	@Override
	public boolean handle(Player player, Item item, int slotId, InteractionOption option) {
		if (option != InteractionOption.FIRST_OPTION) {
			return true;
		}
		Optional<Potion> optional = Potion.getPotion(item.getId());
		if (!optional.isPresent()) {
			System.out.println("Unable to find potion... [" + item + "][" + option + "]");
			return true;
		}
		player.getManager().getActions().startAction(new PlayerPotionAction(item, slotId, optional.get()));
		return true;
	}
}
