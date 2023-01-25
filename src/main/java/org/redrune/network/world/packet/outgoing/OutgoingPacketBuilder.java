package org.redrune.network.world.packet.outgoing;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;

/**
 * The structure of an outgoing packet.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public interface OutgoingPacketBuilder {
	
	/**
	 * The building of the packet is handled in this method. The {@code PacketBuilder} is converted to a {@code Packet}
	 * via {@link PacketBuilder#toPacket()}
	 *
	 * @return A newly constructed packe
	 */
	Packet build(Player player);
	
}
