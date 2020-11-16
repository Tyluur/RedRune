package org.redrune.network.lobby.packet.outgoing;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.list.WorldList;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class WorldListBuilder implements OutgoingPacketBuilder {
	
	private final boolean worldConfiguration;
	
	private final boolean worldStatus;
	
	public WorldListBuilder(boolean worldConfiguration, boolean worldStatus) {
		this.worldConfiguration = worldConfiguration;
		this.worldStatus = worldStatus;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(23, PacketType.VAR_SHORT);
		bldr.writeByte(1);
		bldr.writeByte(worldConfiguration ? 2 : 0);
		bldr.writeByte(worldStatus ? 1 : 0);
		if (worldConfiguration) {
			WorldList.populateConfiguration(bldr);
		}
		if (worldStatus) {
			WorldList.populateStatus(bldr);
		}
		return bldr.toPacket();
	}
}
