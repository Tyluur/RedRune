package org.redrune.network.lobby.packet.incoming;

import org.redrune.network.world.packet.incoming.impl.CommunicationsPacketDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class StatusPacketDecoder extends CommunicationsPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(BAR_SETTINGS, PRIVATE_MESSAGE);
	}
	
}
