package org.redrune.utility.rs;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.Entity;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a damage to hit.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class Hit {
	
	/**
	 * The attributes a hit can have, used for storing data about where the hit came from.
	 */
	@Getter
	private final ConcurrentHashMap<HitAttributes, Object> attributes;
	
	/**
	 * The entity dealing the damage.
	 */
	@Getter
	private final Entity source;
	
	/**
	 * The damage hitsplat.
	 */
	@Getter
	@Setter
	private HitSplat splat;
	
	/**
	 * The amount of damage to be dealt
	 */
	@Getter
	private int damage;
	
	/**
	 * The max hit
	 */
	private double maxHit = -1;
	
	/**
	 * The amount of soaked damage.
	 */
	@Getter
	@Setter
	private int soaked;
	
	/**
	 * The delay on the hit
	 */
	@Getter
	private int delay;
	
	public Hit(Entity source, int damage) {
		this(source, damage, HitSplat.REGULAR_DAMAGE);
	}
	
	public Hit(Entity source, int damage, HitSplat splat) {
		this.source = source;
		this.damage = damage;
		this.splat = splat;
		this.soaked = 0;
		this.attributes = new ConcurrentHashMap<>();
	}
	
	/**
	 * Sets the delay of the hit
	 *
	 * @param ticks
	 * 		The ticks
	 */
	public Hit setDelay(int ticks) {
		// in the hit mask, 1 delay = 20ms...
		// so if we want to delay for 1 tick we have to calculate it
		this.delay = (600 * ticks) / 20;
		return this;
	}
	
	/**
	 * Sets the max hit
	 *
	 * @param maxHit
	 * 		The max hit
	 */
	public Hit setMaxHit(double maxHit) {
		this.maxHit = maxHit;
		return this;
	}
	
	/**
	 * Checks if the hit is critical, based on the max hit and the hit landed.
	 */
	public boolean isCritical() {
		if (maxHit == -1 || damage == 0) {
			return false;
		}
		// we can only set combat splats to critical
		if (!splat.isDefaultCombatSplat()) {
			return false;
		}
		double criticalMinimum = maxHit * 0.90;
		return damage >= criticalMinimum;
	}
	
	/**
	 * Gets an attribute
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param defaultValue
	 * 		The default value to return
	 * @param <K>
	 * 		The attribute tyle
	 */
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(HitAttributes key, K defaultValue) {
		K value = (K) attributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	@Override
	public String toString() {
		return "Hit{" + "source=" + source + ", splat=" + splat + ", damage=" + damage + ", maxHit=" + maxHit + '}';
	}
	
	/**
	 * Represents the damage types.
	 *
	 * @author Emperor
	 */
	public enum HitSplat {
		
		MISSED(8),
		REGULAR_DAMAGE(3),
		MELEE_DAMAGE(0),
		RANGE_DAMAGE(1),
		MAGIC_DAMAGE(2),
		REFLECTED_DAMAGE(4),
		ABSORB_DAMAGE(5),
		POISON_DAMAGE(6),
		DESEASE_DAMAGE(7),
		HEALED_DAMAGE(9),
		CANNON_DAMAGE(13);
		
		@Getter
		@Setter
		private int mark;
		
		HitSplat(int mark) {
			this.setMark(mark);
		}
		
		public boolean isDefaultCombatSplat() {
			return this == MELEE_DAMAGE || this == RANGE_DAMAGE || this == MAGIC_DAMAGE;
		}
	}
	
	/**
	 * Sets the damage
	 *
	 * @param damage
	 * 		The damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
		// update the splat
		if (damage <= 0) {
			setSplat(HitSplat.MISSED);
		}
	}
	
	/**
	 * The possible attributes of the hit
	 */
	public enum HitAttributes {
		HIT_SOUND,
		WEAPON_USED,
		SPECIAL_ATTACK_USED,
		FORCE_LEECH
	}
}
