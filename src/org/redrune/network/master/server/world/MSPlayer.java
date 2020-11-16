package org.redrune.network.master.server.world;

import lombok.Getter;
import org.redrune.network.master.MasterConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/18/2017
 */
public final class MSPlayer implements MasterConstants {
	
	/**
	 * The name of the player in the lobby server
	 */
	@Getter
	private final String username;
	
	/**
	 * The id of the world the player is on
	 */
	@Getter
	private final byte worldId;
	
	/**
	 * The uid of the player
	 */
	@Getter
	private final String uid;
	
	public MSPlayer(String username, String uid, byte worldId) {
		this.username = username;
		this.uid = uid;
		this.worldId = worldId;
	}
	
	@Override
	public String toString() {
		return "MSPlayer{" + "username='" + username + '\'' + ", worldId=" + worldId + ", uid='" + uid + '\'' + '}';
	}
	
}
