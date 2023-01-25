package org.redrune.network.master.server.network.packet.in;

import org.redrune.game.GameConstants;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.master.utility.rs.LoginConstants;
import org.redrune.utility.backend.SecureOperations;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = { PacketConstants.PLAYER_FILE_UPDATE_PACKET_ID })
public class PlayerFilePacketIn implements ReadablePacket<MSSession> {
	
	@Override
	public void read(MSSession session, IncomingPacket packet) {
		long start = System.currentTimeMillis();
		String fileName = packet.readString();
		int fileLength = packet.readInt();
		byte[] data = new byte[fileLength];
		for (int i = 0; i < fileLength; i++) {
			data[i] = (byte) packet.readByte();
		}
		try {
			String fileContents = new String(SecureOperations.getDecryptedDecompressed(data, GameConstants.FILE_ENCRYPTION_KEY), "UTF-8");
			Utility.saveData(new File(LoginConstants.getLocation(fileName)), fileContents);
			System.out.println("Saved " + fileName + " in " + (System.currentTimeMillis() - start) + " ms");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
