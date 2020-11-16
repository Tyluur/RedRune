package org.redrune.game.node.entity.npc.extension;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.utility.rs.constant.Directions.Direction;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/25/2017
 */
public class RockCrabNPC extends NPC {
	
	/**
	 * The original id of the rock crab
	 */
	private final int originalId;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public RockCrabNPC(int id, Location location, Direction direction) {
		super(id, location, direction);
		this.originalId = id;
		getCombatManager().setAggressiveForced(true);
		getCombatManager().setFindTargetRadius(1);
	}
	
	@Override
	public void startFight(Entity target) {
		if (getId() == originalId) {
			transform(originalId - 1);
			getCombatManager().setFindTargetRadius(16);
		}
		super.startFight(target);
	}
	
	@Override
	public void reset() {
		super.reset();
		// so the npc that re-spawns is the rock crab
		setId(originalId);
		// the that the original only finds 1 tile close-by
		getCombatManager().setFindTargetRadius(1);
	}
}