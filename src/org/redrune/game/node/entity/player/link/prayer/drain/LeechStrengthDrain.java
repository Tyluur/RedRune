package org.redrune.game.node.entity.player.link.prayer.drain;

import org.redrune.game.node.entity.player.link.prayer.DrainPrayer;
import org.redrune.game.node.entity.player.link.prayer.Prayer;
import org.redrune.utility.rs.constant.PrayerConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class LeechStrengthDrain implements DrainPrayer {
	
	@Override
	public Prayer getPrayer() {
		return Prayer.LEECH_STRENGTH;
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
		return 2248;
	}
	
	@Override
	public int landingGraphicsId() {
		return 2250;
	}
	
	@Override
	public double drainCap() {
		return 0.25;
	}
	
	@Override
	public int[] prayerSlots() {
		return args(PrayerConstants.STRENGTH_SLOT);
	}
	
	@Override
	public double[] amounts() {
		return args(0.01);
	}
	
	@Override
	public double raiseCap() {
		return 0.10;
	}
}
