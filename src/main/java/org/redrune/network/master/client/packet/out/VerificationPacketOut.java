package org.redrune.network.master.client.packet.out;

import org.redrune.network.master.client.MCFlags;
import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class VerificationPacketOut extends WritablePacket {
	
	public VerificationPacketOut() {
		super(VERIFICATION_ATTEMPT_PACKET_ID);
	}
	
	@Override
	public WritablePacket create() {
		final byte worldId = MCFlags.worldId;
		writeByte(worldId);
		writeString(KEYS[worldId]);
		return this;
	}
}
