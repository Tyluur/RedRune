package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class AccountCreationRequestPacketOut extends WritablePacket {
	
	/**
	 * The username of the account that should be created
	 */
	private final String username;
	
	/**
	 * The password of the account that should be created
	 */
	private final String password;
	
	/**
	 * The uid of the session that wants to create an account
	 */
	private final String uid;
	
	public AccountCreationRequestPacketOut(String username, String password, String uid) {
		super(ACCOUNT_CREATION_REQUEST_PACKET_ID);
		this.username = username;
		this.password = password;
		this.uid = uid;
	}
	
	@Override
	public WritablePacket create() {
		writeString(username);
		writeString(password);
		writeString(uid);
		return this;
	}
}
