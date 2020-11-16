package org.redrune.utility.rs;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;

/**
 * Represents a projectile to send.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class Projectile {
	
	/**
	 * The source node.
	 */
	@Getter
	@Setter
	private Entity source;
	
	/**
	 * The source's centered location.
	 */
	@Getter
	@Setter
	private Location sourceLocation;
	
	/**
	 * The victim.
	 */
	@Getter
	@Setter
	private Entity victim;
	
	/**
	 * The projectile's gfx id.
	 */
	@Getter
	@Setter
	private int projectileId;
	
	/**
	 * The start height.
	 */
	@Getter
	@Setter
	private int startHeight;
	
	/**
	 * The ending height.
	 */
	@Getter
	@Setter
	private int endHeight;
	
	/**
	 * The delay.
	 */
	@Getter
	@Setter
	private int delay;
	
	/**
	 * The speed.
	 */
	@Getter
	@Setter
	private int speed;
	
	/**
	 * The angle.
	 */
	@Getter
	@Setter
	private int angle;
	
	/**
	 * The size of the creator
	 */
	@Getter
	@Setter
	private int creatorSize = 1;
	
	/**
	 * The distance to start.
	 */
	@Getter
	@Setter
	private int startDistanceOffset;
	
	/**
	 * The end location (used for location based projectiles).
	 */
	@Getter
	@Setter
	private Location endLocation;
	
	/**
	 * Constructs a new projectile.
	 *
	 * @param source
	 * 		The source node.
	 * @param victim
	 * 		The entity victim.
	 * @param projectileId
	 * 		The projectile gfx id.
	 * @param startHeight
	 * 		The start height.
	 * @param endHeight
	 * 		The end height.
	 * @param delay
	 * 		The type of the projectile.
	 * @param speed
	 * 		The projectile speed.
	 * @param angle
	 * 		The projectile angle.
	 * @param startDistanceOffset
	 * 		The distance offset.
	 */
	public Projectile(Entity source, Entity victim, int projectileId, int startHeight, int endHeight, int delay, int speed, int angle, int startDistanceOffset) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.delay = delay;
		this.speed = speed;
		this.angle = angle;
		this.setCreatorSize(source.getSize());
		this.startDistanceOffset = startDistanceOffset;
//		System.out.println("projectileId = [" + projectileId + "], startHeight = [" + startHeight + "], endHeight = [" + endHeight + "], delay = [" + delay + "], speed = [" + speed + "], angle = [" + angle + "], startDistanceOffset = [" + startDistanceOffset + "], sourceLocation=[" + sourceLocation + "]");
	}
	
	/**
	 * Gets the source location on construction.
	 *
	 * @param entity
	 * 		The node.
	 * @return The centered location.
	 */
	public static Location getLocation(Entity entity) {
		if (entity == null) {
			return null;
		}
		if (entity.isNPC()) {
			int size = entity.getSize() >> 1;
			return entity.toNPC().getLocation().transform(size, size, 0);
		}
		return entity.getLocation();
	}
	
	@Override
	public String toString() {
		return "Projectile{" + "source=" + source + ", sourceLocation=" + sourceLocation + ", victim=" + victim + ", projectileId=" + projectileId + ", startHeight=" + startHeight + ", endHeight=" + endHeight + ", delay=" + delay + ", speed=" + speed + ", angle=" + angle + ", creatorSize=" + creatorSize + ", startDistanceOffset=" + startDistanceOffset + ", endLocation=" + endLocation + '}';
	}
	
	/**
	 * Checks if the projectile is location based.
	 *
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isLocationBased() {
		return endLocation != null;
	}
}