package org.redrune.game.node.entity.player.link.prayer.drain;

import org.redrune.game.node.entity.player.link.prayer.DrainPrayer;
import org.redrune.game.node.entity.player.link.prayer.Prayer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class LeechEnergyDrain implements DrainPrayer {
	
	@Override
	public Prayer getPrayer() {
		return Prayer.LEECH_ENERGY;
	}
	
	@Override
	public int startAnimationId() {
		return 12575;
	}
	
	@Override
	public int startGraphicsId() {
		return -1;
	}
	
	@Override
	public int projectileId() {
		return 2252;
	}
	
	@Override
	public int landingGraphicsId() {
		return 2254;
	}
	
	@Override
	public double drainCap() {
		return 0;
	}
	
	@Override
	public int[] prayerSlots() {
		return args(ENERGY_SLOT);
	}
	
	@Override
	public double[] amounts() {
		return args(0.10);
	}
	
	@Override
	public double raiseCap() {
		return 0;
	}
}
