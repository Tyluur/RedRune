package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.GameConstants;
import org.redrune.game.content.skills.LevelUp;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.SkillPacketBuilder;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class PlayerSkills implements SkillConstants {
	
	/**
	 * The array of levels the player has in the skills
	 */
	private final short[] level = new short[25];
	
	/**
	 * The array of experience the player has in the skills
	 */
	private final double[] experience = new double[25];
	
	/**
	 * The amount of experience we have obtained
	 */
	@Getter
	@Setter
	private double counterExperience;
	
	/**
	 * The levels the player has advanced
	 */
	@Setter
	private transient Map<Short, Integer> levelsAdvanced;
	
	/**
	 * The player who owns this class
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Constructs a new {@code PlayerSkills} {@code Object}
	 */
	public PlayerSkills() {
		for (int i = 0; i < level.length; i++) {
			level[i] = 1;
		}
		level[HITPOINTS] = 10;
		experience[HITPOINTS] = getXPForLevel(10);
		level[HERBLORE] = 3;
		experience[HERBLORE] = getXPForLevel(3);
	}
	
	/**
	 * Gets the required experience for a level
	 *
	 * @param level
	 * 		The level
	 */
	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}
	
	/**
	 * Gets the level in a skill by the desired amount of experience
	 *
	 * @param experience
	 * 		The experience
	 * @param skill
	 * 		The skill
	 */
	public static int getLevelByExperience(double experience, int skill) {
		int points = 0;
		int output;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= experience) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	/**
	 * Adds experience to the skill without multiplier effects
	 *
	 * @param skillId
	 * 		The id of the skill
	 * @param experience
	 * 		The amount of exp to add
	 */
	public PlayerSkills addExperienceNoMultiplier(short skillId, double experience) {
		trackExperienceChange(skillId, experience);
		return this;
	}
	
	/**
	 * Tracks experience change in a skillId
	 *
	 * @param skillId
	 * 		The skill's id number
	 * @param experience
	 * 		The amount of experience gained
	 */
	private void trackExperienceChange(int skillId, double experience) {
		if (player.getVariables().isExperienceLocked()) {
			return;
		}
		int oldLevel = getLevelForXp(skillId);
		double oldExp = getExperience(skillId);
		this.experience[skillId] += experience;
		counterExperience += experience;
		updateExperienceCounter();
		if (this.experience[skillId] > MAXIMUM_EXP) {
			this.experience[skillId] = MAXIMUM_EXP;
		}
		int newLevel = getLevelForXp(skillId);
		double newExp = getExperience(skillId);
		int levelDiff = newLevel - oldLevel;
		if (newLevel > oldLevel) {
			level[skillId] += levelDiff;
			LevelUp.sendCongratulations(player, skillId);
			if (skillId == SUMMONING || skillId <= MAGIC) {
				player.getUpdateMasks().register(new AppearanceUpdate(player));
			}
			addLevelsAdvanced((short) skillId, levelDiff);
		}
		updateSkill(skillId);
	}
	
	/**
	 * Gets the level the player has in a skill, by the amount of experience the player has. This is used in cases where
	 * the level has reduced due to draining or other modifications, and the original level is still important.
	 *
	 * @param skill
	 * 		The skill
	 */
	public int getLevelForXp(int skill) {
		double exp = experience[skill];
		int points = 0;
		int output;
		for (int lvl = 1; lvl <= (skill == DUNGEONEERING ? 120 : 99); lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return skill == DUNGEONEERING ? 120 : 99;
	}
	
	/**
	 * Gets the amount of experience in a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 */
	public double getExperience(int skillId) {
		return experience[skillId];
	}
	
	/**
	 * Updates the experience counter with the amount of experience we've obtained
	 */
	private void updateExperienceCounter() {
		player.getTransmitter().send(new ConfigPacketBuilder(1801, (int) (counterExperience * 10D)).build(player));
	}
	
	/**
	 * Adds levels advanced information for the skill
	 *
	 * @param skillId
	 * 		The id of the skill
	 */
	private void addLevelsAdvanced(short skillId, int levelsAdvanced) {
		Integer amount = this.levelsAdvanced.get(skillId);
		if (amount == null) {
			amount = levelsAdvanced;
		} else {
			amount = amount + levelsAdvanced;
		}
		this.levelsAdvanced.put(skillId, amount);
	}
	
	/**
	 * Updates the skill details in the client for the parameterized skill
	 *
	 * @param skill
	 * 		The skill
	 */
	private void updateSkill(int skill) {
		player.getTransmitter().send(new SkillPacketBuilder(skill).build(player));
	}
	
	/**
	 * Adds experience to the skill with multiplier effects
	 *
	 * @param skillId
	 * 		The id of the skill
	 * @param experience
	 * 		The amount of exp to add
	 */
	public PlayerSkills addExperienceWithMultiplier(short skillId, double experience) {
		switch (skillId) {
			case ATTACK:
			case STRENGTH:
			case DEFENCE:
			case MAGIC:
			case RANGE:
			case HITPOINTS:
				experience = experience * GameConstants.COMBAT_EXPERIENCE_MULTIPLIER;
				break;
			case PRAYER:
				experience = experience * GameConstants.PRAYER_EXPERIENCE_MULTIPLIER;
				break;
			default:
				experience = experience * GameConstants.SKILL_EXPERIENCE_MULTIPLIER;
				break;
		}
		trackExperienceChange(skillId, experience);
		return this;
	}
	
	/**
	 * Refreshes all skill components
	 */
	public void refreshAll() {
		for (int skill = 0; skill < level.length; skill++) {
			updateSkill(skill);
		}
		updateExperienceCounter();
	}
	
	/**
	 * Resets the experience counter data
	 */
	public void resetExperienceCounter() {
		counterExperience = 0;
		updateExperienceCounter();
	}
	
	/**
	 * Gets the combat level with summoning addition
	 */
	public int getCombatLevelWithSummoning() {
		return getCombatLevel() + getSummoningCombatLevel();
	}
	
	/**
	 * Gets the combat level in a skill
	 */
	public int getCombatLevel() {
		int attack = getLevelForXp(0);
		int defence = getLevelForXp(1);
		int strength = getLevelForXp(2);
		int hp = getLevelForXp(3);
		int prayer = getLevelForXp(5);
		int ranged = getLevelForXp(4);
		int magic = getLevelForXp(6);
		double base = 0.25 * (defence + hp + Math.floor(prayer / 2));
		double meleeC = 0.325 * (attack + strength);
		double rangeC = 0.325 * (Math.floor(ranged / 2) + ranged);
		double mageC = 0.325 * (Math.floor(magic / 2) + magic);
		return (int) Math.floor(base + Math.max(meleeC, Math.max(rangeC, mageC)));
	}
	
	/**
	 * Gets the summoning additive combat level
	 */
	public int getSummoningCombatLevel() {
		return getLevelForXp(SUMMONING) / 8;
	}
	
	/**
	 * Gets the level in a certain skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 */
	public int getLevel(int skillId) {
		return level[skillId];
	}
	
	/**
	 * Sets the experience in a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 * @param newExp
	 * 		The new experience amount to set
	 */
	public void setXp(int skillId, double newExp) {
		experience[skillId] = newExp;
		updateSkill(skillId);
	}
	
	/**
	 * Sets the level of a skillId
	 *
	 * @param skillId
	 * 		The id of the skillId
	 * @param newLevel
	 * 		The new level of the skillId to set
	 */
	public void setLevel(int skillId, int newLevel) {
		level[skillId] = (short) newLevel;
		updateSkill(skillId);
	}
	
	/**
	 * Gets the amount of levels the player has advanced since opening their skill guide
	 *
	 * @param skillId
	 * 		The id of the skill
	 * @param reset
	 * 		If we should reset the levels advanced
	 */
	public int getLevelsAdvanced(short skillId, boolean reset) {
		Integer amount = levelsAdvanced.get(skillId);
		if (amount == null) {
			return 0;
		} else {
			if (reset) {
				levelsAdvanced.remove(skillId);
			}
			return amount;
		}
	}
	
	/**
	 * Restores all skills
	 */
	public void restoreAll() {
		for (int skill = 0; skill < level.length; skill++) {
			restoreSkill(skill);
			updateSkill(skill);
		}
	}
	
	/**
	 * Restores a skill to its original state
	 *
	 * @param skill
	 * 		The skill
	 */
	private void restoreSkill(int skill) {
		if (skill == HITPOINTS) {
			player.heal(getLevelForXp(skill) * 10);
		} else if (skill == PRAYER) {
			player.getManager().getPrayers().restorePrayer(getLevelForXp(skill) * 10);
		} else {
			level[skill] = (short) getLevelForXp(skill);
		}
	}
	
	/**
	 * Drains a skill level with a cap on it
	 *
	 * @param skill
	 * 		The skill id to drain
	 * @param drainAmount
	 * 		The amount to drain
	 * @param drainCap
	 * 		The amount we are capped by
	 */
	public void drainLevel(int skill, double drainAmount, double drainCap) {
		int skillLevel = level[skill];
		int levelForXp = getLevelForXp(skill);
		int lowestAllowed = levelForXp - (int) Math.round(levelForXp * drainCap);
		// can no longer drain past this
		if (skillLevel <= lowestAllowed) {
			return;
		}
		int drain = (int) Math.round(levelForXp * drainAmount);
		drainLevel(skill, drain);
	}
	
	/**
	 * Drains a level
	 *
	 * @param skill
	 * 		The skill
	 * @param drain
	 * 		The amount to drain
	 */
	public int drainLevel(int skill, int drain) {
		int drainLeft = drain - level[skill];
		if (drainLeft < 0) {
			drainLeft = 0;
		}
		level[skill] -= drain;
		if (level[skill] < 0) {
			level[skill] = 0;
		}
		updateSkill(skill);
		return drainLeft;
	}
	
	public void passLevels(Player p) {
		System.arraycopy(p.getSkills().level, 0, this.level, 0, p.getSkills().level.length);
		System.arraycopy(p.getSkills().experience, 0, this.experience, 0, p.getSkills().experience.length);
	}
	
	public void updateAllSkills() {
		for (int skill = 0; skill < level.length; skill++) {
			updateSkill(skill);
		}
	}
}
