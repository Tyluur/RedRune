package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.tool.BufferUtils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class PublicChatBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The player index.
	 */
	private final int index;
	
	/**
	 * The player rights.
	 */
	private final int rights;
	
	/**
	 * The message.
	 */
	private final String message;
	
	/**
	 * The effects.
	 */
	private final int effects;
	
	public PublicChatBuilder(int index, int rights, String message, int effects) {
		this.index = index;
		this.rights = rights;
		this.message = message;
		this.effects = effects;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(36, PacketType.VAR_BYTE);
		bldr.writeShort(index);
		bldr.writeShort(effects);
		bldr.writeByte(rights);
		byte[] chatStr = new byte[256];
		chatStr[0] = (byte) message.length();
		byte offset = (byte) (1 + BufferUtils.huffmanCompress(message, chatStr, 1));
		bldr.writeBytes(chatStr, 0, offset);
		return bldr.toPacket();
	}
}
