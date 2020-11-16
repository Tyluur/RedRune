package org.redrune.network.master.client.packet.in;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.world.packet.outgoing.impl.PrivateMessageSendBuilder;
import org.redrune.utility.tool.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.PRIVATE_MESSAGE_DELIVERY_PACKET_ID })
public class PrivateMessageDeliveredPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String sourceName = packet.readString();
		String deliveryName = packet.readString();
		String message = packet.readString();
		
		Optional<Player> optional = World.get().getPlayerByUsername(sourceName);
		if (!optional.isPresent()) {
			System.out.println("Unable to find player by name " + sourceName);
			return;
		}
		Player player = optional.get();
		player.getTransmitter().send(new PrivateMessageSendBuilder(Misc.formatPlayerNameForDisplay(deliveryName), Misc.formatTextToSentence(message)).build(player));
	}
}
