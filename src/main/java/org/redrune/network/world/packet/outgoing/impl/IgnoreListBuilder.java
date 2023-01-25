package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

import java.util.Map;
import java.util.Set;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class IgnoreListBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The ignore list
	 */
	private final Set<String> ignoreList;
	
	/**
	 * The list of display names, with the indexes matching
	 */
	private final Map<String, String> displayNames;
	
	public IgnoreListBuilder(Set<String> ignoreList, Map<String, String> displayNames) {
		this.ignoreList = ignoreList;
		this.displayNames = displayNames;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(11, PacketType.VAR_SHORT);
		bldr.writeByte(ignoreList.size());
		for (final String name : ignoreList) {
			final String previousName = displayNames.getOrDefault(name, "");
			
			bldr.writeRS2String(name);
			bldr.writeRS2String(previousName);
			bldr.writeRS2String(previousName);
			bldr.writeRS2String(name);
		}
		return bldr.toPacket();
	}
}