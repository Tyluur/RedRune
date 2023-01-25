package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.backend.ReturnCode;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class LoginResponseCodeBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The value of the login response
	 */
	private final int value;
	
	public LoginResponseCodeBuilder(int value) {
		this.value = value;
	}
	
	public LoginResponseCodeBuilder(ReturnCode returnCode) {
		this.value = returnCode.getValue();
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder builder = new PacketBuilder();
		builder.writeByte(value);
		return builder.toPacket();
	}
}
