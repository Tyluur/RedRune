package org.redrune.network.master.client.packet.out;

import org.redrune.game.GameConstants;
import org.redrune.network.master.network.packet.writeable.WritablePacket;
import org.redrune.utility.backend.SecureOperations;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public class PlayerFilePacketOut extends WritablePacket {
	
	/**
	 * The name of the file
	 */
	private final String fileName;
	
	/**
	 * The contents of the file
	 */
	private final byte[] fileContents;
	
	public PlayerFilePacketOut(String fileName, String fileContents) {
		super(PLAYER_FILE_UPDATE_PACKET_ID);
		this.fileName = fileName;
		this.fileContents = SecureOperations.getCompressedEncrypted(fileContents, GameConstants.FILE_ENCRYPTION_KEY);
	}
	
	@Override
	public WritablePacket create() {
		writeString(fileName);
		writeInt(fileContents.length);
		for (byte content : fileContents) {
			writeByte(content);
		}
		return this;
	}
	
}