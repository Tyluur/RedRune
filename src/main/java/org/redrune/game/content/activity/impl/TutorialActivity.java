package org.redrune.game.content.activity.impl;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportType;
import org.redrune.game.content.play.AppearanceModification;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/20/2017
 */
public class TutorialActivity extends Activity {
	
	@Override
	public void start() {
		AppearanceModification.openCharacterStyling(player);
	}
	
	@Override
	public boolean handleNodeInteraction(Node node, InteractionOption option) {
		return false;
	}
	
	@Override
	public boolean teleportationAllowed(TeleportType type) {
		return false;
	}
	
	@Override
	protected boolean handlePlayerOption(Player target, InteractionOption option) {
		return false;
	}
	
	@Override
	public boolean canMove(int x, int y, int dir) {
		return false;
	}
	
	@Override
	public boolean combatAcceptable(Entity target) {
		return false;
	}
	
	@Override
	public boolean savesOnLogout() {
		return true;
	}
	
	@Override
	public void end() {
		super.end();
	}
}
