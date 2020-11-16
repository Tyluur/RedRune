package org.redrune.game.node.entity.npc.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.utility.rs.constant.NPCConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class NPCCombatDefinitions {
	
	/**
	 * The maximum amount of hitpoints the npc has
	 */
	@Getter
	@Setter
	private int hitpoints;
	
	/**
	 * The default attack animation
	 */
	@Getter
	@Setter
	private int attackAnim;
	
	/**
	 * The default defence animation
	 */
	@Getter
	@Setter
	private int defenceAnim;
	
	/**
	 * The animation to perform on death
	 */
	@Getter
	@Setter
	private int deathAnim;
	
	/**
	 * How long we should wait between each attack
	 */
	@Getter
	@Setter
	private int attackDelay;
	
	/**
	 * The amount of ticks between when the death animation starts and when the npc is removed
	 */
	@Getter
	@Setter
	private int deathDelay;
	
	/**
	 * The amount of ticks between when the npc is removed and when they are added again
	 */
	@Getter
	@Setter
	private int respawnDelay;
	
	/**
	 * The maximum hit of the npc
	 */
	@Getter
	@Setter
	private int maxHit;
	
	/**
	 * The combat style the npc uses
	 */
	@Getter
	@Setter
	private int attackStyle;
	
	/**
	 * The graphic id that is visualized on the npc every time combat is ticked
	 */
	@Getter
	@Setter
	private int attackGfx;
	
	/**
	 * The projectile that is sent from the npc every time combat is ticked
	 */
	@Getter
	@Setter
	private int attackProjectile;
	
	/**
	 * The type of aggressiveness to have
	 */
	@Getter
	@Setter
	private int aggressivenessType;
	
	/**
	 * Constructs default npc combat definitions. All npcs must be attackable so we will have definitions that are
	 * empty, like these.
	 */
	public NPCCombatDefinitions() {
		this.hitpoints = 100;
		this.attackAnim = -1;
		this.defenceAnim = -1;
		this.deathAnim = -1;
		this.attackDelay = 3;
		this.deathDelay = 3;
		this.respawnDelay = 30;
		this.maxHit = 10;
		this.attackStyle = NPCConstants.MELEE_COMBAT_STYLE;
		this.attackGfx = -1;
		this.attackProjectile = -1;
		this.aggressivenessType = NPCConstants.PASSIVE_AGGRESSIVE;
	}
	
	/**
	 * Constructs a combat definition with all defined types.
	 */
	public NPCCombatDefinitions(int hitpoints, int attackAnim, int defenceAnim, int deathAnim, int attackDelay, int deathDelay, int respawnDelay, int maxHit, int attackStyle, int attackGfx, int attackProjectile, int aggressivenessType) {
		this.hitpoints = hitpoints;
		this.attackAnim = attackAnim;
		this.defenceAnim = defenceAnim;
		this.deathAnim = deathAnim;
		this.attackDelay = attackDelay;
		this.deathDelay = deathDelay;
		this.respawnDelay = respawnDelay;
		this.maxHit = maxHit;
		this.attackStyle = attackStyle;
		this.attackGfx = attackGfx;
		this.attackProjectile = attackProjectile;
		this.aggressivenessType = aggressivenessType;
	}
	
	public void swap(NPCCombatDefinitions other) {
		if (other.hitpoints > 0) {
			this.hitpoints = other.hitpoints;
		}
		if (other.attackAnim > 0) {
			this.attackAnim = other.attackAnim;
		}
		if (other.defenceAnim > 0) {
			this.defenceAnim = other.defenceAnim;
		}
		if (other.deathAnim > 0) {
			this.deathAnim = other.deathAnim;
		}
		if (other.attackDelay > 0) {
			this.attackDelay = other.attackDelay;
		}
		if (other.deathDelay > 0) {
			this.deathDelay = other.deathDelay;
		}
		if (other.respawnDelay > 0) {
			this.respawnDelay = other.respawnDelay;
		}
		if (other.maxHit > 0) {
			this.maxHit = other.maxHit;
		}
		if (other.attackStyle != this.attackStyle) {
			this.attackStyle = other.attackStyle;
		}
		if (other.attackGfx != this.attackGfx) {
			this.attackGfx = other.attackGfx;
		}
		if (other.attackProjectile != this.attackProjectile) {
			this.attackProjectile = other.attackProjectile;
		}
		if (other.aggressivenessType != this.aggressivenessType) {
			this.aggressivenessType = other.aggressivenessType;
		}
	}
	
	@Override
	public String toString() {
		return "NPCCombatDefinitions{" + "hitpoints=" + hitpoints + ", attackAnim=" + attackAnim + ", defenceAnim=" + defenceAnim + ", deathAnim=" + deathAnim + ", attackDelay=" + attackDelay + ", deathDelay=" + deathDelay + ", respawnDelay=" + respawnDelay + ", maxHit=" + maxHit + ", attackStyle=" + attackStyle + ", attackGfx=" + attackGfx + ", attackProjectile=" + attackProjectile + ", aggressivenessType=" + aggressivenessType + '}';
	}
}
