package org.redrune.network.lobby.packet.incoming;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.world.packet.outgoing.impl.MessageBuilder;

import static org.redrune.game.GameConstants.*;
import static org.redrune.network.world.packet.outgoing.impl.MessageBuilder.URL_MESSAGE_IDENTIFIER;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class LobbyWebsitePacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(62);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int id = packet.readByte();
		String name = packet.readRS2String();
		String url = packet.readRS2String();
		int unknown = packet.readByte();
		
		String destinationUrl = null;
		switch(id) {
			case 101:
				destinationUrl = EMAIL_MODIFICATION_URL;
				break;
			case 116:
				destinationUrl = INBOX_URL;
				break;
			case 100:
				destinationUrl = DONATION_URL;
				break;
		}
		
		if (destinationUrl != null) {
			player.getTransmitter().send(new MessageBuilder(URL_MESSAGE_IDENTIFIER, destinationUrl).build(player));
		}
	}
}
