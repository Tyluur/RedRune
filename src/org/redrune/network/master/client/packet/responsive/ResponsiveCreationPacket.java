package org.redrune.network.master.client.packet.responsive;

import org.redrune.network.NetworkSession;
import org.redrune.network.lobby.ProtocolType;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;
import org.redrune.utility.backend.CreationResponse;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class ResponsiveCreationPacket extends ResponsiveGamePacket{
	
	/**
	 * The uid of the session who created an account
	 */
	private final String uid;
	
	/**
	 * The response id of the account creation request
	 */
	private final byte responseId;
	
	public ResponsiveCreationPacket(String uid, byte responseId) {
		this.uid = uid;
		this.responseId = responseId;
	}
	
	@Override
	public void read() {
		Optional<NetworkSession> optional = NetworkSession.findByUid(uid);
		if (!optional.isPresent()) {
			System.err.println("Unable to find session by id " + uid);
			return;
		}
		NetworkSession session = optional.get();
		
		// send the response back as long as we could find one, otherwise we send an invalid one because we couldn't identify the response by that id
		ProtocolType.sendCreationResponse(session.getChannel(), CreationResponse.getByValue(responseId).orElse(CreationResponse.BUSY_SERVER));
	}
}
