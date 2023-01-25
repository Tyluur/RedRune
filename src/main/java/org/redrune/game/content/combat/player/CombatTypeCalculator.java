package org.redrune.game.content.combat.player;

import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public interface CombatTypeCalculator extends EquipConstants, SkillConstants, BonusConstants {
	
	/**
	 * Finds the total aggressive boost of this combat type
	 *
	 * @param player
	 * 		The player
	 * @param params
	 * 		Other possible parameters of this method.
	 */
	double totalAggressiveBoost(Player player, Object... params);
	
	/**
	 * Finds the total defensive boost of this combat type
	 *
	 * @param entity
	 * 		The player
	 * @param params
	 * 		Other possible parameters of this method.
	 */
	double totalDefensiveBoost(Entity entity, Object... params);
	
	/**
	 * Finds the maximum amount of damage the player can do
	 *
	 * @param player
	 * 		The player
	 * @param params
	 * 		Other possible parameters of this method.
	 */
	double maximumDamageAppendable(Player player, Object... params);
}
