package org.redrune.game.node.entity.player.link.prayer.drain;

import org.redrune.game.node.entity.player.link.prayer.DrainPrayer;
import org.redrune.utility.rs.constant.PrayerConstants;
import org.redrune.game.node.entity.player.link.prayer.Prayer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public class SapMageDrain implements DrainPrayer {
	
	@Override
	public Prayer getPrayer() {
		return Prayer.SAP_MAGE;
	}
	
	@Override
	public int startAnimationId() {
		return 12569;
	}
	
	@Override
	public int startGraphicsId() {
		return 2220;
	}
	
	@Override
	public int projectileId() {
		return 2221;
	}
	
	@Override
	public int landingGraphicsId() {
		return 2222;
	}
	
	@Override
	public double drainCap() {
		return 0.20;
	}
	
	@Override
	public int[] prayerSlots() {
		return args(PrayerConstants.MAGIC_SLOT);
	}
	
	@Override
	public double[] amounts() {
		return args(0.01);
	}
	
	@Override
	public double raiseCap() {
		return -1;
	}
	
}
