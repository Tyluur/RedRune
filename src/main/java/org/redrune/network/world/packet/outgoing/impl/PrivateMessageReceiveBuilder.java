package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.tool.BufferUtils;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class PrivateMessageReceiveBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The player name.
	 */
	private final String name;
	
	/**
	 * The message to receive.
	 */
	private final String message;
	
	/**
	 * The player rights.
	 */
	private final int rights;
	
	public PrivateMessageReceiveBuilder(String name, String message, int rights) {
		this.name = name;
		this.message = message;
		this.rights = rights;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(118, PacketType.VAR_BYTE);
		
		byte[] encryptedData = new byte[message.length() + 1];
		encryptedData[0] = (byte) message.length();
		BufferUtils.huffmanCompress(message, encryptedData, 1);
		
		bldr.writeByte(1);
		bldr.writeRS2String(name); // display name
		bldr.writeRS2String(name); // user name
		for (int i = 0; i < 5; i++) {
			bldr.writeByte(Misc.getRandom(255));
		}
		bldr.writeByte(rights);
		bldr.writeBytes(encryptedData);
		
		return bldr.toPacket();
	}
}
