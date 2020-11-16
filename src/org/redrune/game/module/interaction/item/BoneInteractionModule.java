package org.redrune.game.module.interaction.item;

import org.redrune.game.content.skills.prayer.Bone;
import org.redrune.game.content.skills.prayer.BoneBuryingAction;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.module.type.ItemInteractionModule;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class BoneInteractionModule implements ItemInteractionModule {
	
	@Override
	public int[] itemSubscriptionIds() {
		return Bone.getItemIds();
	}
	
	@Override
	public boolean handle(Player player, Item item, int slotId, InteractionOption option) {
		if (option != InteractionOption.FIRST_OPTION) {
			return false;
		} else {
			Bone bone = Bone.getBone(item.getId());
			if (bone == null) {
				return false;
			}
			player.getManager().getActions().startAction(new BoneBuryingAction(bone, slotId));
			return true;
		}
	}
	
}