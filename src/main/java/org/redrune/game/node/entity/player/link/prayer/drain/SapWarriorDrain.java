package org.redrune.game.node.entity.player.link.prayer.drain;

import org.redrune.game.node.entity.player.link.prayer.DrainPrayer;
import org.redrune.game.node.entity.player.link.prayer.Prayer;

import static org.redrune.game.node.entity.player.link.prayer.Prayer.SAP_WARRIOR;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class SapWarriorDrain implements DrainPrayer {
	
	@Override
	public Prayer getPrayer() {
		return SAP_WARRIOR;
	}
	
	@Override
	public int startAnimationId() {
		return 12569;
	}
	
	@Override
	public int startGraphicsId() {
		return 2214;
	}
	
	@Override
	public int projectileId() {
		return 2215;
	}
	
	@Override
	public int landingGraphicsId() {
		return 2216;
	}
	
	@Override
	public double drainCap() {
		return 0.20;
	}
	
	@Override
	public int[] prayerSlots() {
		return args(ATTACK_SLOT, STRENGTH_SLOT, DEFENCE_SLOT);
	}
	
	@Override
	public double[] amounts() {
		return args(0.01, 0.01, 0.01);
	}
	
	@Override
	public double raiseCap() {
		return -1;
	}
	
}
