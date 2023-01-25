package org.redrune.game.module.interaction.item;

import org.redrune.game.content.action.interaction.PlayerFoodAction;
import org.redrune.game.module.type.ItemInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.rs.constant.FoodConstants.Food;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class FoodInteractionModule implements ItemInteractionModule {
	
	@Override
	public int[] itemSubscriptionIds() {
		return Food.getAllFoodIds();
	}
	
	@Override
	public boolean handle(Player player, Item item, int slotId, InteractionOption option) {
		if (option != InteractionOption.FIRST_OPTION) {
			return true;
		}
		Optional<Food> optional = Food.forId(item.getId());
		if (!optional.isPresent()) {
			System.out.println("Unable to find food... [" + item + "][" + option + "]");
			return true;
		}
		player.getManager().getActions().startAction(new PlayerFoodAction(optional.get(), item, slotId));
		return true;
	}
}
