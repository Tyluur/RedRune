package org.redrune.network.master.server.network.packet.out;

import org.redrune.game.GameConstants;
import org.redrune.network.master.network.packet.writeable.WritablePacket;
import org.redrune.utility.backend.SecureOperations;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class LoginResponsePacketOut extends WritablePacket {
	
	/**
	 * The uid of the session
	 */
	private final String uid;
	
	/**
	 * The response code
	 */
	private final byte responseCode;
	
	/**
	 * The username of the login request
	 */
	private final String username;
	
	/**
	 * If the response is to the lobby
	 */
	private final boolean lobby;
	
	/**
	 * The data of the file to transmit over the network
	 */
	private final byte[] data;
	
	/**
	 * The id of the row that the player's sql data is in
	 */
	private final int rowId;
	
	public LoginResponsePacketOut(String uid, byte responseCode, String fileText, String username, boolean lobby, int rowId) {
		super(LOGIN_RESPONSE_PACKET_ID);
		this.uid = uid;
		this.responseCode = responseCode;
		this.username = username;
		this.lobby = lobby;
		this.data = SecureOperations.getCompressedEncrypted(fileText, GameConstants.FILE_ENCRYPTION_KEY);
		this.rowId = rowId;
	}
	
	@Override
	public WritablePacket create() {
		writeString(uid);
		writeByte(responseCode);
		writeByte((byte) (lobby ? 1 : 0));
		writeString(username);
		writeInt(rowId);
		writeInt(data.length);
		for (byte datum : data) {
			writeByte(datum);
		}
		return this;
	}
}
