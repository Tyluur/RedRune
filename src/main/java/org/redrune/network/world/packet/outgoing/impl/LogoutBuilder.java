package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/10/2017
 */
public class LogoutBuilder implements OutgoingPacketBuilder {
	
	/**
	 * If we are logging out to the lobby
	 */
	private final boolean lobby;
	
	/**
	 * Constructs a new logout
	 *
	 * @param lobby
	 * 		If we are logging out to the lobby
	 */
	public LogoutBuilder(boolean lobby) {
		this.lobby = lobby;
	}
	
	@Override
	public Packet build(Player player) {
		return new PacketBuilder(lobby ? 59 : 126).toPacket();
	}
}
