package org.redrune.network.master.utility.rs;

import lombok.Getter;
import org.redrune.network.master.network.MasterSession;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class LoginRequest {
	
	/**
	 * The id of the world this request is for
	 */
	@Getter
	private final byte worldId;
	
	/**
	 * If this request is for a lobby login
	 */
	@Getter
	private final boolean lobby;
	
	/**
	 * The username to log in with
	 */
	@Getter
	private final String username;
	
	/**
	 * The password used
	 */
	@Getter
	private final String password;
	
	/**
	 * The session that the login request came from
	 */
	@Getter
	private final MasterSession session;
	
	/**
	 * The uid of the session the packet came from
	 */
	@Getter
	private final String uuid;
	
	public LoginRequest(byte worldId, boolean lobby, String username, String password, MasterSession session, String uid) {
		this.worldId = worldId;
		this.lobby = lobby;
		this.username = username;
		this.password = password;
		this.session = session;
		this.uuid = uid;
	}
	
	/**
	 * If the request is an account creation request
	 */
	public boolean isCreation() {
		return worldId == -1;
	}
	
	@Override
	public String toString() {
		return "LoginRequest{" + "worldId=" + worldId + ", lobby=" + lobby + ", username='" + username + '\'' + ", password='" + password + '\'' + ", uuid='" + uuid + '\'' + '}';
	}
}
