package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class LoginRequestPacketOut extends WritablePacket {
	
	/**
	 * If the player is logging into the world's lobby or actual world
	 */
	private final boolean lobby;
	
	/**
	 * The of the world the player is in
	 */
	private final byte worldId;
	
	/**
	 * The name of the player logging in
	 */
	private final String username;
	
	/**
	 * The password entered
	 */
	private final String password;
	
	/**
	 * The uuid of the session
	 */
	private final String uid;
	
	public LoginRequestPacketOut(byte worldId, boolean lobby, String username, String password, String uid) {
		super(LOGIN_REQUEST_PACKET_ID);
		this.username = username;
		this.lobby = lobby;
		this.worldId = worldId;
		this.password = password;
		this.uid = uid;
	}
	
	@Override
	public WritablePacket create() {
		writeByte(worldId);
		writeByte((byte) (lobby ? 1 : 0));
		writeString(username);
		writeString(password);
		writeString(uid);
		return this;
	}
}
