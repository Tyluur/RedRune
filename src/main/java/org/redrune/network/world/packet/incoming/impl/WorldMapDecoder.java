package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class WorldMapDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return arguments(63);
	}
	
	@Override
	public void read(Player player, Packet packet) {
/*		int coordinateHash = packet.readInt2();
		int x = coordinateHash >> 14;
		int y = coordinateHash & 0x3fff;
		int plane = coordinateHash >> 28;
		
		System.out.println("x=" + x + ", y=" + y + ", plane=" + plane);*/
	}
}
