package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.Region;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.game.node.Location;
import org.redrune.game.world.region.DynamicRegion;
import org.redrune.game.world.region.RegionManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/4/2017
 */
public class DynamicMapRegionBuilder implements OutgoingPacketBuilder {
	/*
		OutputStream stream = new OutputStream();
		stream.writePacketVarShort(player, 128);
		int middleChunkX = player.getChunkX();
		int middleChunkY = player.getChunkY();
		stream.writeShort(middleChunkY);
		stream.writeByte(player.getMapSize());
		stream.write128Byte(player.isForceNextMapLoadRefresh() ? 1 : 0);
		stream.write128Byte(2);
		stream.writeShortLE(middleChunkX);
		stream.initBitAccess();

		int sceneLength = GameConstants.MAP_SIZES[player.getMapSize()] >> 4;
		// the regionids(maps files) that will be used to load this scene
		int[] regionIds = new int[4 * sceneLength * sceneLength];
		int newRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int realChunkX = (middleChunkX - sceneLength); realChunkX <= ((middleChunkX + sceneLength)); realChunkX++) {
				int regionX = realChunkX / 8;
				y:
				for (int realChunkY = (middleChunkY - sceneLength); realChunkY <= ((middleChunkY + sceneLength)); realChunkY++) {
					int regionY = realChunkY / 8;
					int regionId = (regionX << 8) + regionY;
					Region region = World.getRegions().get(regionId);
					int newChunkX;
					int newChunkY;
					int newPlane;
					int rotation;
					if (region instanceof DynamicRegion) { // generated map
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] pallete = dynamicRegion.getRegionCoords()[plane][realChunkX - (regionX * 8)][realChunkY - (regionY * 8)];
						newChunkX = pallete[0];
						newChunkY = pallete[1];
						newPlane = pallete[2];
						rotation = pallete[3];
					} else { // real map
						newChunkX = realChunkX;
						newChunkY = realChunkY;
						newPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid chunk, not built chunk
					if (newChunkX == 0 || newChunkY == 0) { stream.writeBits(1, 0); } else {
						stream.writeBits(1, 1);
						// chunk encoding = (x << 14) | (y << 3) | (plane <<
						// 24), theres addition of two more bits for rotation
						stream.writeBits(26, (rotation << 1) | (newPlane << 24) | (newChunkX << 14) | (newChunkY << 3));
						int newRegionId = (((newChunkX / 8) << 8) + (newChunkY / 8));
						for (int index = 0; index < newRegionIdsCount; index++) {
							if (regionIds[index] == newRegionId) { continue y; }
						}
						regionIds[newRegionIdsCount++] = newRegionId;
					}

				}
			}
		}
		stream.finishBitAccess();
		for (int index = 0; index < newRegionIdsCount; index++) {
			int[] xteas = MapArchiveKeys.getKey(regionIds[index]);
			if (xteas == null) {
				xteas = new int[4];
			}
			for (int keyIndex = 0; keyIndex < 4; keyIndex++) {
				stream.writeInt(xteas[keyIndex]);
			}
		}
		stream.endPacketVarShort();
		session.write(stream);
	 */
	@Override
	public Packet build(Player player) {
		final PacketBuilder bldr = new PacketBuilder(134, PacketType.VAR_SHORT);
		final int middleChunkX = player.getLocation().getRegionX();
		final int middleChunkY = player.getLocation().getRegionY();
		final int mapSize = player.getMapSize();
		
		bldr.writeByteS(2); // map load type
		bldr.writeShort(middleChunkY);
		bldr.writeLEShort(mapSize);
		bldr.writeByteS(player.getAttribute(AttributeKey.FORCE_NEXT_MAP_LOAD, false) ? 1 : 0);
		bldr.writeByte(middleChunkX);
		bldr.startBitAccess();
		
		System.out.println("2, " + middleChunkY + ", " + mapSize + ", " + player.getAttribute(AttributeKey.FORCE_NEXT_MAP_LOAD, false) + ", " + middleChunkX);
		
		int sceneLength = Location.VIEWPORT_SIZES[mapSize] >> 4;
		// the region ids (maps files) that will be used to load this scene
		int[] regionIds = new int[4 * sceneLength * sceneLength];
		int newRegionIdsCount = 0;
		for (int plane = 0; plane < 4; plane++) {
			for (int realChunkX = (middleChunkX - sceneLength); realChunkX <= ((middleChunkX + sceneLength)); realChunkX++) {
				int regionX = realChunkX / 8;
				y:
				for (int realChunkY = (middleChunkY - sceneLength); realChunkY <= ((middleChunkY + sceneLength)); realChunkY++) {
					int regionY = realChunkY / 8;
					int regionId = (regionX << 8) + regionY;
					Region region = RegionManager.getRegions().get(regionId);
					int newChunkX;
					int newChunkY;
					int newPlane;
					int rotation;
					// generated map
					if (region instanceof DynamicRegion) {
						DynamicRegion dynamicRegion = (DynamicRegion) region;
						int[] pallete = dynamicRegion.getRegionCoords()[plane][realChunkX - (regionX * 8)][realChunkY - (regionY * 8)];
						newChunkX = pallete[0];
						newChunkY = pallete[1];
						newPlane = pallete[2];
						rotation = pallete[3];
					} else { // real map
						newChunkX = realChunkX;
						newChunkY = realChunkY;
						newPlane = plane;
						rotation = 0;// no rotation
					}
					// invalid chunk, not built chunk
					if (newChunkX == 0 || newChunkY == 0) {
						bldr.writeBits(1, 0);
					} else {
						bldr.writeBits(1, 1);
						// chunk encoding = (x << 14) | (y << 3) | (plane << 24), theres addition of two more bits for rotation
						bldr.writeBits(26, (rotation << 1) | (newPlane << 24) | (newChunkX << 14) | (newChunkY << 3));
						int newRegionId = (((newChunkX / 8) << 8) + (newChunkY / 8));
						for (int index = 0; index < newRegionIdsCount; index++) {
							if (regionIds[index] == newRegionId) {
								continue y;
							}
						}
						regionIds[newRegionIdsCount++] = newRegionId;
					}
				}
			}
		}
		bldr.finishBitAccess();
		// writing the xtea keys
		for (int index = 0; index < newRegionIdsCount; index++) {
			int[] keys = MapKeyRepository.getKeys(regionIds[index]);
			if (keys == null) {
				keys = new int[4];
			}
			for (int keyIndex = 0; keyIndex < 4; keyIndex++) {
				bldr.writeInt(keys[keyIndex]);
			}
		}
		return bldr.toPacket();
	}
}
