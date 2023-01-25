package org.redrune.game.world.list;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a world's definition.
 *
 * @author Dementhium development team.
 */
class WorldDefinition {
	
	/**
	 * The activity for this world.
	 */
	@Getter
	private final String activity;
	
	/**
	 * The coutry flag.
	 */
	@Getter
	private final int country;
	
	/**
	 * If the world is members.
	 */
	@Getter
	private final int flag;
	
	/**
	 * The ip-address for this world.
	 */
	@Getter
	private final String ip;
	
	/**
	 * The location.
	 */
	@Getter
	private final int location;
	
	/**
	 * The region.
	 */
	@Getter
	private final String region;
	
	/**
	 * The world's id.
	 */
	@Getter
	private final int worldId;
	
	/**
	 * The size of the world
	 */
	@Getter
	@Setter
	private short size;
	
	/**
	 * Constructs a new {@code WorldDefinition} {@code Object}.
	 *
	 * @param worldId
	 * 		The world's id.
	 * @param location
	 * 		The location.
	 * @param flag
	 * 		If the world is members.
	 * @param activity
	 * 		The activity for this world.
	 * @param ip
	 * 		The IP-address.
	 * @param region
	 * 		The region.
	 * @param country
	 * 		The country flag.
	 */
	WorldDefinition(int worldId, int location, int flag, String activity, String ip, String region, int country) {
		this.worldId = worldId;
		this.location = location;
		this.flag = flag;
		this.activity = activity;
		this.ip = ip;
		this.region = region;
		this.country = country;
	}
	
}