package org.redrune.game.content.action.interaction;

import org.redrune.game.node.entity.player.Player;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.action.Action;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.constant.PotionConstants.Potion;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
public class PlayerPotionAction implements Action {
	
	/**
	 * The item that is the potion
	 */
	private final Item item;
	
	/**
	 * The slot of the potion in the inventory
	 */
	private final int slot;
	
	/**
	 * The potion instance
	 */
	private final Potion potion;
	
	public PlayerPotionAction(Item item, int slot, Potion potion) {
		this.slot = slot;
		this.potion = potion;
		this.item = item;
	}
	
	@Override
	public boolean start(Player player) {
		if (!canSip(player)) {
			return true;
		}
		String name = item.getDefinitions().getName();
		int index = name.indexOf("(");
		int dosesLeft = 0;
		if (index != -1) {
			dosesLeft = Integer.parseInt(name.substring(index).replace("(", "").replace(")", "")) - 1;
		}
		int toPot = potion.getItemIds().length - dosesLeft;
		if (name.contains("flask") && dosesLeft <= 0 && toPot >= potion.getItemIds().length) {
			player.getInventory().deleteItem(slot, new Item(potion.getItemIds()[5]));
		} else {
			player.getInventory().getItems().set(slot, new Item(dosesLeft > 0 && toPot < potion.getItemIds().length ? potion.getItemIds()[toPot] : 229, 1));
		}
		player.getInventory().refresh(slot);
		for (int skillId : potion.getImpact().getImpactedSkills()) {
			player.getSkills().setLevel(skillId, potion.getImpact().getAffectedSkill(player, skillId, player.getSkills().getLevel(skillId), player.getSkills().getLevelForXp(skillId)));
		}
		potion.getImpact().extra(player);
//		NOTE: NO COMBAT DELAY IN 2011 player.getManager().getActions().addDelay(3);
		player.sendAnimation(829);
		player.getTransmitter().sendMessage(potion.getImpact().getDrinkMessage() != null ? potion.getImpact().getDrinkMessage() : "You drink some of your " + name.toLowerCase().replace(" (1)", "").replace(" (2)", "").replace(" (3)", "").replace(" (4)", "").replace(" (5)", "").replace(" (6)", "") + ".", true);
		player.getTransmitter().sendMessage(dosesLeft == 0 ? "You have finished your " + (name.contains("flask") ? "flask and the glass shatters to peices." : "potion.") : "You have " + dosesLeft + " dose of potion left.", true);
		player.putAttribute("next_sip_allowed", SystemManager.getUpdateWorker().getTicks() + 2);
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		return true;
	}
	
	@Override
	public int processOnTicks(Player player) {
		return -1;
	}
	
	@Override
	public void stop(Player player) {
		
	}
	
	/**
	 * If the player can sip
	 *
	 * @param player
	 * 		The player
	 */
	private boolean canSip(Player player) {
		return SystemManager.getUpdateWorker().getTicks() > player.getAttribute("next_sip_allowed", -1L);
	}
}
