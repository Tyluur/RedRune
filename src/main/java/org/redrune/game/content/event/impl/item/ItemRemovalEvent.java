package org.redrune.game.content.event.impl.item;

import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.AnimationPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.context.item.ItemRemovalContext;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.content.event.Event;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class ItemRemovalEvent extends Event<ItemRemovalContext> {
	
	public ItemRemovalEvent() {
		setWalkablePolicy(WalkablePolicy.RESET);
		setInterfacePolicy(InterfacePolicy.CLOSE);
		setAnimationPolicy(AnimationPolicy.RESET);
		setActionPolicy(ActionPolicy.RESET);
	}
	
	@Override
	public void run(Player player, ItemRemovalContext context) {
		int slotId = context.getSlotId();
		if (slotId >= 15) {
			return;
		}
		Item item = player.getEquipment().getItem(slotId);
		if (item == null || !player.getInventory().addItem(item.getId(), item.getAmount())) {
			return;
		}
		player.getEquipment().getItems().set(slotId, null);
		player.getEquipment().refresh(slotId);
		player.getUpdateMasks().register(new AppearanceUpdate(player));
		if (slotId == 3) {
			player.getCombatDefinitions().setSpecialActivated(false);
		}
	}
	
	@Override
	public boolean canStart(Player player, ItemRemovalContext context) {
		return !player.getManager().getLocks().isLocked(LockType.ITEM_INTERACTION);
	}
}
