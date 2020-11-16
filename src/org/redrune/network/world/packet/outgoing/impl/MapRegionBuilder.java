package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.MapKeyRepository;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class MapRegionBuilder implements OutgoingPacketBuilder {
	
	/**
	 * If the packet is being sent from a login request
	 */
	private final boolean onLogin;
	
	public MapRegionBuilder(boolean onLogin) {
		this.onLogin = onLogin;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(19, PacketType.VAR_SHORT);
		Location pos = player.getLocation();
		if (onLogin) {
			player.getRenderInformation().enterWorld(bldr);
		}
		int regionX = pos.getRegionX();
		int regionY = pos.getRegionY();
		bldr.writeByteC(player.getAttribute(AttributeKey.FORCE_NEXT_MAP_LOAD, false) ? 1 : 0); //Force refresh? 1 : 0
		bldr.writeLEShort(regionY);
		bldr.writeLEShortA(regionX);
		bldr.writeByteS(0); //Scene graph size index.
		for (int regionId : player.getMapRegionsIds()) {
			int[] keys = MapKeyRepository.getKeys(regionId);
			if (keys == null) {
				keys = new int[4];
			}
			for (int i = 0; i < 4; i++) {
				bldr.writeInt(keys[i]);
			}
		}
		return bldr.toPacket();
	}
	
}
