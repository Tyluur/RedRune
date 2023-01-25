package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class BlackCS2ScriptBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The id of the script
	 */
	private final int scriptId;
	
	public BlackCS2ScriptBuilder(int scriptId) {
		this.scriptId = scriptId;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(23, PacketType.VAR_SHORT);
		bldr.writeShortA(0);
		bldr.writeRS2String("");
		bldr.writeInt(scriptId);
		return bldr.toPacket();
	}
}
