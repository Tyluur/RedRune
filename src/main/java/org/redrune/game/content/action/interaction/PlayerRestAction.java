package org.redrune.game.content.action.interaction;

import org.redrune.game.content.action.Action;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/26/2017
 */
public class PlayerRestAction implements Action {
	
	/**
	 * The rest definitions
	 */
	private static int[][] REST_DEFS = { { 5713, 1549, 5748 }, { 11786, 1550, 11788 }, { 5713, 1551, 2921 } };
	
	// TODO handle facing a musician and resting
	private Entity faceNode = null;
	
	/**
	 * The index that the animation is based on, called into the {@link #REST_DEFS} array.
	 */
	private int index = 0;
	
	@Override
	public boolean start(Player player) {
		if (!process(player)) {
			return false;
		}
		this.index = Misc.random(REST_DEFS.length);
		player.stop(true, true, true, false);
		player.putAttribute("resting", true);
		player.sendAnimation((REST_DEFS[index][0]));
		player.getDetails().getAppearance().setRenderEmote(REST_DEFS[index][1]);
		player.getTransmitter().refreshRunOrbStatus();
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		if (player.getPoisonManager().isPoisoned()) {
			player.getTransmitter().sendMessage("You can't rest while you're poisoned.");
			return false;
		}
		if (player.combatRecently()) {
			player.getTransmitter().sendMessage("You can't rest until 10 seconds after the end of combat.");
			return false;
		}
		return true;
	}
	
	@Override
	public int processOnTicks(Player player) {
		for (int i = 0; i < 3; i++) {
			player.restoreRunEnergy();
		}
		return 1;
	}
	
	@Override
	public void stop(Player player) {
		player.removeAttribute("resting");
		player.sendAnimation(REST_DEFS[index][2]);
		player.getDetails().getAppearance().setRenderEmote(-1);
		
		player.putAttribute("next_emote_end", player.getUpdateMasks().getLastAnimationEndTime() + 600);
		player.getTransmitter().refreshRunOrbStatus();
	}
}
