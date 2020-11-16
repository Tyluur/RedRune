package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import org.redrune.game.node.entity.player.Player;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The rights the player can have
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum PlayerRight {
	
	OWNER(2, 19),
	ADMINISTRATOR(2, 14) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER);
		}
	},
	DEVELOPER(2, 10) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER);
		}
	},
	COMMUNITY_MANAGER(2, 4) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER);
		}
	},
	ADVERTISEMENT_TEAM(2, 20) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER);
		}
	},
	GLOBAL_MODERATOR(1, 11) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER);
		}
	},
	SERVER_MODERATOR(1, 7) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER, GLOBAL_MODERATOR);
		}
	},
	FORUM_MODERATOR(6) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER, GLOBAL_MODERATOR);
		}
	},
	TRIAL_MODERATOR(16) {
		@Override
		public void create() {
			addOtherRights(GLOBAL_MODERATOR, FORUM_MODERATOR);
		}
	},
	SERVER_ASSISTANT(12) {
		@Override
		public void create() {
			addOtherRights(OWNER, COMMUNITY_MANAGER, ADMINISTRATOR, DEVELOPER, GLOBAL_MODERATOR, SERVER_MODERATOR);
		}
	},
	EXTREME_DONATOR(9),
	PREMIUM_DONATOR(8),
	YOUTUBER(18),
	VETERAN(13),
	GRAPHIC_DESIGNER(17),
	THEME_EDITOR(22),
	RESPECTED_MEMBER(15),
	BETA_TESTER(21),
	PLAYER(3);
	
	/**
	 * The rights the player has in the client
	 */
	@Getter
	private final byte clientRight;
	
	/**
	 * The member group id of the right
	 */
	@Getter
	private final byte memberGroupId;
	
	/**
	 * The rights that can also access this right
	 */
	private final Set<PlayerRight> rightsWithAccess;
	
	/**
	 * Constructs a right with a client right of 0
	 *
	 * @param memberGroupId
	 * 		The id of the member group for the forum
	 */
	PlayerRight(int memberGroupId) {
		this(0, memberGroupId);
	}
	
	/**
	 * Constructs a right
	 *
	 * @param clientRight
	 * 		The client right
	 * @param memberGroupId
	 * 		The id of the member group for the forum
	 */
	PlayerRight(int clientRight, int memberGroupId) {
		this.clientRight = (byte) clientRight;
		this.memberGroupId = (byte) memberGroupId;
		this.rightsWithAccess = new LinkedHashSet<>();
		this.rightsWithAccess.add(this);
		this.create();
	}
	
	/**
	 * Called on the creation of a right, due to enums not being able to call other values below them while
	 * constructing
	 */
	public void create() {
	
	}
	
	/**
	 * Finds the right optional by the {@link PlayerRight#name}.
	 *
	 * @param name
	 * 		The name to look for.
	 */
	public static Optional<PlayerRight> getRightByName(String name) {
		return Arrays.stream(values()).filter(right -> right.name().equalsIgnoreCase(name)).findFirst();
	}
	
	/**
	 * Finds the right by the group id
	 *
	 * @param memberGroupId
	 * 		The group id to look for
	 */
	public static Optional<PlayerRight> getRightByGroupId(int memberGroupId) {
		return Arrays.stream(values()).filter(right -> right.getMemberGroupId() == memberGroupId).findFirst();
	}
	
	/**
	 * Adds other rights that can access this right
	 */
	public void addOtherRights(PlayerRight... rights) {
		this.rightsWithAccess.addAll(Arrays.asList(rights));
	}
	
	/**
	 * Checking that the player has access to this right
	 *
	 * @param player
	 * 		The player
	 */
	public final boolean playerHasRights(Player player) {
		for (PlayerRight right : rightsWithAccess) {
			if (player.getDetails().rightsContains(right)) {
				return true;
			}
		}
		return false;
	}
	
}