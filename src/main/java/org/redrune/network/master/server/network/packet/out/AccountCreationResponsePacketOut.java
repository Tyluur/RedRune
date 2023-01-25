package org.redrune.network.master.server.network.packet.out;

import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class AccountCreationResponsePacketOut extends WritablePacket {
	
	/**
	 * The uid of the session who attempted to create an account
	 */
	private final String uid;
	
	/**
	 * The response value of the account creation request
	 */
	private final byte responseId;
	
	public AccountCreationResponsePacketOut(String uid, byte responseId) {
		super(ACCOUNT_CREATION_RESPONSE_PACKET_ID);
		this.uid = uid;
		this.responseId = responseId;
	}
	
	@Override
	public WritablePacket create() {
		writeString(uid);
		writeByte(responseId);
		return this;
	}
}
