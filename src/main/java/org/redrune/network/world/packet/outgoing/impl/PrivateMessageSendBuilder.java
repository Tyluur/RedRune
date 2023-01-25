package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.tool.BufferUtils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class PrivateMessageSendBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The player's name.
	 */
	private final String name;
	
	/**
	 * The message.
	 */
	private final String message;
	
	public PrivateMessageSendBuilder(String name, String message) {
		this.name = name;
		this.message = message;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(127, PacketType.VAR_BYTE);
		byte[] encryptedData = new byte[message.length() + 1];
		BufferUtils.huffmanCompress(message, encryptedData, 0);
		bldr.writeRS2String(name);
		bldr.writeByte((byte) message.length());
		bldr.writeBytes(encryptedData);
		return bldr.toPacket();
	}
}
