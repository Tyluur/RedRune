package org.redrune.core.task.impl;

import org.redrune.core.task.ScheduledTask;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/28/2017
 */
public class HitpointsRestorationTask extends ScheduledTask {
	
	public HitpointsRestorationTask() {
		super(10, -1);
	}
	
	@Override
	public void run() {
		for (Player player : World.get().getPlayers()) {
			if (player == null || player.isDead() || !player.isRenderable()) {
				continue;
			}
			player.restoreHitPoints();
		}
		for (NPC npc : World.get().getNpcs()) {
			if (npc == null || npc.isDead() || !npc.isRenderable()) {
				continue;
			}
			npc.restoreHitPoints();
		}
	}
}
