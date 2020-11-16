package org.redrune.network.lobby.packet.incoming;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.lobby.packet.outgoing.WorldListBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class WorldRequestPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(33);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		player.getTransmitter().send(new WorldListBuilder(true, true).build(player));
	}
}
