package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.utility.tool.Misc;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class PlayerDetails {
	
	/**
	 * The username of the player
	 */
	@Getter
	private final String username;
	
	/**
	 * The set of the rights the player has
	 */
	@Getter
	private final Set<PlayerRight> rights;
	
	/**
	 * The player's appearance
	 */
	@Getter
	private final PlayerAppearance appearance;
	
	/**
	 * The password of the player
	 */
	@Getter
	@Setter
	private String password;
	
	/**
	 * The last ip address the player last from
	 */
	@Getter
	@Setter
	private String lastIp;
	
	/**
	 * Constructs a new {@code Credentials} {@code Object}
	 *
	 * @param username
	 * 		The username
	 */
	public PlayerDetails(String username) {
		this.username = username;
		this.rights = new LinkedHashSet<>();
		this.rights.add(PlayerRight.PLAYER);
		this.appearance = new PlayerAppearance();
	}
	
	/**
	 * Gets the most dominant right. The {@link #rights} are sorted based on the position of the right in the enum
	 * (ordinal), so the first right will be the most dominant  .
	 *
	 * @return A {@code Right} instance
	 */
	public PlayerRight getDominantRight() {
		if (rights.size() != 0) {
			return rights.iterator().next();
		} else {
			System.err.println("Unexpected situation - rights set was empty!");
			return PlayerRight.PLAYER;
		}
	}
	
	/**
	 * If there are donator rights in the {@link #rights} set
	 */
	public boolean isDonator() {
		return rights.contains(PlayerRight.PREMIUM_DONATOR) || rights.contains(PlayerRight.EXTREME_DONATOR);
	}
	
	/**
	 * If this right is a staff right
	 */
	public boolean isStaff() {
		return rights.contains(PlayerRight.OWNER) || rights.contains(PlayerRight.ADMINISTRATOR) || rights.contains(PlayerRight.SERVER_MODERATOR);
	}
	
	/**
	 * If the {@link #rights} set has any of these parameters, this is true
	 *
	 * @param rights
	 * 		The rights
	 */
	public boolean rightsContains(PlayerRight... rights) {
		for (PlayerRight right : rights) {
			if (right == PlayerRight.PLAYER) {
				return true;
			}
			if (this.rights.contains(right)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Stores a new list of rights
	 *
	 * @param rights
	 * 		The rights to store
	 */
	public void storeRights(Set<PlayerRight> rights) {
		this.rights.clear();
		this.rights.addAll(rights);
	}
	
	/**
	 * The display name of the player
	 */
	public String getDisplayName() {
		return Misc.formatPlayerNameForDisplay(username);
	}
	
}