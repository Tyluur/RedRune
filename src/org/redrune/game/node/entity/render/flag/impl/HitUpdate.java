package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.Entity;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;

/**
 * Represents the hit update mask.
 *
 * @author Emperor
 */
public class HitUpdate extends UpdateFlag {
	
	/**
	 * The entity.
	 */
	public final Entity entity;
	
	/**
	 * Constructs a new {@code HitUpdate} {@code Object}.
	 *
	 * @param entity
	 * 		The entity.
	 */
	public HitUpdate(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		final int size = entity.getHitMap().getHitList().size();
		bldr.writeByteA(size); //Amount of hits
		if (size == 0) {
			return;
		}
		int hitpoints = entity.getHealthPoints();
		int maxHitpoints = entity.getMaxHealth();
		if (hitpoints > maxHitpoints) {
			hitpoints = maxHitpoints;
		}
		int hpBarPercentage = (hitpoints == 0 || maxHitpoints == 0) ? 0 : (hitpoints * 255 / maxHitpoints);
		for (Hit hit : entity.getHitMap().getHitList()) {
			if (hit.getSoaked() > 0) {
				bldr.writeSmart(32767);
			}
			int type = hit.getSplat().getMark();
			if (hit.getSplat() != HitSplat.HEALED_DAMAGE) {
				if (hit.getDamage() < 1) {
					type = 8;
				} else if (hit.isCritical()) {
					type += 10;
				}
				if (hit.getSource() == outgoing || entity == outgoing) {
					bldr.writeSmart(type);
				} else {
					bldr.writeSmart(type + 14);
				}
			} else {
				bldr.writeSmart(type);
			}
			bldr.writeSmart(hit.getDamage());
			if (hit.getSoaked() > 0) {
				if (hit.getSource() == outgoing || entity == outgoing) {
					bldr.writeSmart(5);
				} else {
					bldr.writeSmart(19);
				}
				bldr.writeSmart(hit.getSoaked());
			}
			bldr.writeSmart(hit.getDelay());
			if (entity.isNPC()) {
				bldr.writeByteA(hpBarPercentage);
			} else {
				bldr.writeByte(hpBarPercentage);
			}
		}
	}
	
	@Override
	public int getOrdinal() {
		return entity.isNPC() ? 7 : 2;
	}
	
	@Override
	public int getMaskData() {
		return entity.isNPC() ? 0x10 : 0x8;
	}
	
}