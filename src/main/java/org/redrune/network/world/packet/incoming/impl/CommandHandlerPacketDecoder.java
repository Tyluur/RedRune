package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.CommandEventContext;
import org.redrune.game.content.event.impl.CommandEvent;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public class CommandHandlerPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(12);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		if (packet.getBuffer().readableBytes() < 1) {
			return;
		}
		packet.readUnsignedByte();
		packet.readUnsignedByte();
		String command = packet.readRS2String();
		String[] args = command.toLowerCase().split(" ");
		EventRepository.executeEvent(player, CommandEvent.class, new CommandEventContext(args, true));
	}
}
