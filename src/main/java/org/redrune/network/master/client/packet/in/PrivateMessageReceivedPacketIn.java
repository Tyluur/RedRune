package org.redrune.network.master.client.packet.in;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.world.packet.outgoing.impl.PrivateMessageReceiveBuilder;
import org.redrune.utility.tool.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
@Readable(packetIds = { PacketConstants.PRIVATE_MESSAGE_RECEIVED_PACKET_ID })
public class PrivateMessageReceivedPacketIn implements ReadablePacket<MCSession> {
	
	@Override
	public void read(MCSession session, IncomingPacket packet) {
		String fromName = packet.readString();
		String deliveryName = packet.readString();
		byte fromClientRights = (byte) (packet.readByte());
		String message = packet.readString();
		
		Optional<Player> optional = World.get().getPlayerByUsername(deliveryName);
		if (!optional.isPresent()) {
			System.out.println("unable to find player in the world with username " + deliveryName);
			return;
		}
		Player player = optional.get();
		player.getTransmitter().send(new PrivateMessageReceiveBuilder(Misc.formatPlayerNameForDisplay(fromName), Misc.formatTextToSentence(message), fromClientRights).build(player));
	}
}
