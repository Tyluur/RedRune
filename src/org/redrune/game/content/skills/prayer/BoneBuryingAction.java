package org.redrune.game.content.skills.prayer;

import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.LockManager.LockType;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Objects;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public class BoneBuryingAction implements Action {
	
	/**
	 * The animation of burying a bone
	 */
	public static final int BURY_ANIMATION = 827;
	
	/**
	 * The bone the player is burying
	 */
	private final Bone bone;
	
	/**
	 * The slot id the bone was clicked on
	 */
	private final int slotId;
	
	/**
	 * The item found in the slot id
	 */
	private Item item;
	
	public BoneBuryingAction(Bone bone, int slotId) {
		this.bone = bone;
		this.slotId = slotId;
	}
	
	@Override
	public boolean start(Player player) {
		item = player.getInventory().getItems().get(slotId);
		final boolean check = canCheck();
		if (!check) {
			return false;
		}
		player.sendAnimation(BURY_ANIMATION);
		player.getManager().getLocks().lockIndefinitely(LockType.MOVEMENT, LockType.ITEM_INTERACTION);
		player.getTransmitter().sendMessage("You dig a hole in the ground...", true);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		return canCheck();
	}
	
	@Override
	public int processOnTicks(Player player) {
		if (!canCheck() || !Objects.equals(player.getInventory().getItems().get(slotId), item)) {
			return -1;
		}
		player.getManager().getLocks().unlock(LockType.MOVEMENT, LockType.ITEM_INTERACTION);
		player.getTransmitter().sendMessage("You bury the " + item.getName().toLowerCase() + ".");
		player.getSkills().addExperienceWithMultiplier(SkillConstants.PRAYER, bone.getExperience());
		player.getInventory().deleteItem(slotId, item);
		return 3;
	}
	
	@Override
	public void stop(Player player) {
	
	}
	
	/**
	 * Checks to make sure everything is good to bury the bone.
	 */
	private boolean canCheck() {
		return bone != null && item != null && item.getId() == bone.getItemId();
	}
}
