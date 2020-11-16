package org.redrune.game.node.entity.player.render;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.RenderInformation;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.entity.player.render.update.GlobalUpdateStage;
import org.redrune.game.node.entity.player.render.update.LocalUpdateStage;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.constant.Directions.DirectionUtilities;
import org.redrune.utility.tool.Misc;

import java.util.PriorityQueue;

/**
 * Represents the player rendering outgoing packet.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/17
 */
public class PlayerRendering implements OutgoingPacketBuilder {
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(112, PacketType.VAR_SHORT);
		writePlayerRendering(player, bldr);
		return bldr.toPacket();
	}
	
	/**
	 * Writes the player rendering on the packet.
	 *
	 * @param player
	 * 		The player.
	 * @param packet
	 * 		The packet.
	 */
	private static void writePlayerRendering(Player player, PacketBuilder packet) {
		RenderInformation info = player.getRenderInformation();
		PacketBuilder flagBased = new PacketBuilder();
		int skipCount = -1;
		packet.startBitAccess();
		for (int i = 0; i < info.localsCount; i++) {
			int index = info.getLocals()[i];
			LocalUpdateStage stage = LocalUpdateStage.getStage(player, World.get().getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				updateLocalPlayer(player, World.get().getPlayers().get(index), packet, stage, flagBased, index);
			}
		}
		putSkip(skipCount, packet);
		skipCount = -1;
		packet.finishBitAccess();
		packet.startBitAccess();
		for (int i = 0; i < info.globalsCount; i++) {
			int index = info.getGlobals()[i];
			GlobalUpdateStage stage = GlobalUpdateStage.getStage(player, World.get().getPlayers().get(index));
			if (stage == null) {
				skipCount++;
			} else {
				putSkip(skipCount, packet);
				skipCount = -1;
				updateGlobalPlayer(player, World.get().getPlayers().get(index), packet, stage, flagBased);
			}
		}
		putSkip(skipCount, packet);
		packet.finishBitAccess();
		packet.writeBytes(flagBased.getBuffer());
	}
	
	/**
	 * Updates a local player
	 *
	 * @param player
	 * 		The player we're writing for
	 * @param p
	 * 		The player to update
	 * @param buffer
	 * 		The buffer
	 * @param stage
	 * 		The stage
	 * @param flagBased
	 * 		The flag based buffer
	 * @param index
	 * 		The index
	 */
	private static void updateLocalPlayer(Player player, Player p, PacketBuilder buffer, LocalUpdateStage stage, PacketBuilder flagBased, int index) {
		if (stage != LocalUpdateStage.WALKING && stage != LocalUpdateStage.RUNNING) {
			buffer.writeBits(1, 1);
			buffer.writeBits(1, stage.ordinal() == 0 ? 0 : (p.getUpdateMasks().isUpdateRequired() ? 1 : 0));
			buffer.writeBits(2, stage.ordinal() % 4);
		}
		switch (stage) {
			case REMOVE_PLAYER:
				if (p != null) {
					int updateType = getGlobalUpdateType(p);
					if (updateType != 0) {
						updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.values()[updateType], flagBased);
					} else {
						buffer.writeBits(1, 0);
					}
				} else {
					buffer.writeBits(1, 0);
				}
				player.getRenderInformation().getIsLocal()[index] = false;
				break;
			case WALKING:
			case RUNNING:
				int dx = RegionManager.DIRECTION_DELTA_X[p.getMovement().getNextWalkDirection()];
				int dy = RegionManager.DIRECTION_DELTA_Y[p.getMovement().getNextWalkDirection()];
				boolean running;
				int opcode;
				boolean needUpdate = p.getUpdateMasks().isUpdateRequired();
				if (p.getMovement().getNextRunDirection() != -1) {
					dx += RegionManager.DIRECTION_DELTA_X[p.getMovement().getNextRunDirection()];
					dy += RegionManager.DIRECTION_DELTA_Y[p.getMovement().getNextRunDirection()];
					opcode = Misc.getPlayerRunningDirection(dx, dy);
					if (opcode == -1) {
						running = false;
						opcode = Misc.getPlayerWalkingDirection(dx, dy);
					} else {
						running = true;
					}
				} else {
					running = false;
					opcode = Misc.getPlayerWalkingDirection(dx, dy);
				}
				p.putAttribute("last_direction", opcode);
				buffer.writeBits(1, 1);
				buffer.writeBits(1, needUpdate ? 1 : 0);
				buffer.writeBits(2, running ? 2 : 1);
				buffer.writeBits(running ? 4 : 3, opcode);
				break;
			case TELEPORTED:
				Location delta = Location.getDelta(p.getRenderInformation().getLastLocation(), p.getLocation());
				int deltaX = delta.getX() < 0 ? -delta.getX() : delta.getX();
				int deltaY = delta.getY() < 0 ? -delta.getY() : delta.getY();
				if (deltaX <= 15 && deltaY <= 15) {
					buffer.writeBits(1, 0);
					int deltaZ;
					deltaX = delta.getX() < 0 ? delta.getX() + 32 : delta.getX();
					deltaY = delta.getY() < 0 ? delta.getY() + 32 : delta.getY();
					deltaZ = delta.getPlane();
					buffer.writeBits(12, (deltaY & 0x1f) | ((deltaX & 0x1f) << 5) | ((deltaZ & 0x3) << 10));
				} else {
					buffer.writeBits(1, 1);
					buffer.writeBits(30, (delta.getY() & 0x3fff) | ((delta.getX() & 0x3fff) << 14) | ((delta.getPlane() & 0x3) << 28));
				}
				break;
			default:
				break;
		}
		if (p != null && stage != LocalUpdateStage.REMOVE_PLAYER && p.getUpdateMasks().isUpdateRequired()) {
			writeMasks(player, p, flagBased, false);
		}
	}
	
	private static byte getGlobalUpdateType(Player player) {
		Location delta = Location.getDelta(World.get().getHash((short) player.getIndex()), player.getLocation().getRegionLocation());
		if (delta.getX() == 0 && delta.getY() == 0 && delta.getPlane() == 0) {
			return 0; // no update needed
		} else if (delta.getX() == 0 && delta.getY() == 0 && delta.getPlane() != 0) {
			return 1; // z update requested
		} else if (delta.getX() >= -1 && delta.getX() <= 1 && delta.getY() >= -1 && delta.getY() <= 1 && DirectionUtilities.RegionMovementType[delta.getX() + 1][delta.getY() + 1] != -1) {
			return 2; // map direction update requed
		} else {
			return 3; // full update requed
		}
	}
	
	/**
	 * Updates the global player
	 *
	 * @param player
	 * 		The player
	 * @param updatable
	 * 		The player to update
	 * @param buffer
	 * 		The buffer
	 * @param stage
	 * 		The global update stage
	 * @param flagBased
	 * 		The flag based buffer
	 */
	private static void updateGlobalPlayer(Player player, Player updatable, PacketBuilder buffer, GlobalUpdateStage stage, PacketBuilder flagBased) {
		buffer.writeBits(1, 1);
		buffer.writeBits(2, stage.ordinal());
//		System.err.println("player " + player + ", updatable=" + updatable + ", stage = " + stage);
		if (stage == GlobalUpdateStage.ADD_PLAYER) {
			byte updateType = getGlobalUpdateType(updatable);
//			System.err.println("player " + player + " updateType = " + updateType);
			if (updateType != 0) {
				updateGlobalPlayer(player, updatable, buffer, GlobalUpdateStage.values()[updateType], flagBased);
			} else {
				buffer.writeBits(1, 0);
				//					updateGlobalPlayer(player, p, buffer, GlobalUpdateStage.TELEPORTED, flagBased);
			}
			buffer.writeBits(6, updatable.getLocation().getX() - (updatable.getLocation().getRegionX() << 6)); // 6
			buffer.writeBits(6, updatable.getLocation().getY() - (updatable.getLocation().getRegionY() << 6)); // 6
			buffer.writeBits(1, 1);
			player.getRenderInformation().getIsLocal()[updatable.getIndex()] = true;
			writeMasks(player, updatable, flagBased, true);
		} else if (stage == GlobalUpdateStage.HEIGHT_UPDATED) {
			Location hashLoc = World.get().getHash((short) updatable.getIndex());
			int z = updatable.getLocation().getPlane() - hashLoc.getPlane();
			buffer.writeBits(2, (z & 0x3));
		} else if (stage == GlobalUpdateStage.MAP_REGION_DIRECTION) {
			Location hashLoc = World.get().getHash((short) updatable.getIndex());
			Location delta = Location.getDelta(hashLoc, updatable.getLocation().getRegionLocation());
			int z = updatable.getLocation().getPlane() - hashLoc.getPlane();
			buffer.writeBits(5, ((z & 0x3) << 3) | (DirectionUtilities.RegionMovementType[delta.getX() + 1][delta.getY() + 1] & 0x7));
		} else if (stage == GlobalUpdateStage.TELEPORTED) {
			Location hashLoc = World.get().getHash((short) updatable.getIndex());
			Location delta = Location.getDelta(hashLoc, updatable.getLocation().getRegionLocation());
			int hash = ((delta.getY() & 0xFF) | ((delta.getX() & 0xFF) << 8) | ((delta.getPlane() & 0x3) << 16)) & 0x3FFFF;
			buffer.writeBits(18, hash);
		}
	}
	
	/**
	 * Writes the update masks for a player on this packet.
	 *
	 * @param writingFor
	 * 		The player we're writing for.
	 * @param updatable
	 * 		The player to update.
	 * @param composer
	 * 		The packet to write on.
	 * @param forceSync
	 * 		If we should force the appearance update mask.
	 */
	private static void writeMasks(Player writingFor, Player updatable, PacketBuilder composer, boolean forceSync) {
		int maskData = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<>(updatable.getUpdateMasks().getFlagQueue());
		for (UpdateFlag flag : flags) {
			maskData |= flag.getMaskData();
		}
		if (forceSync && (maskData & 0x2) == 0) {
			maskData |= 0x2;
			flags.add(new AppearanceUpdate(updatable));
		}
		if (maskData > 128) {
			maskData |= 0x1;
		}
		if (maskData > 32768) {
			maskData |= 0x200;
		}
		composer.writeByte((byte) maskData);
		if (maskData > 128) {
			composer.writeByte((byte) (maskData >> 8));
		}
		if (maskData > 32768) {
			composer.writeByte((byte) (maskData >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(writingFor, composer);
		}
	}
	
	/**
	 * Puts the skipcount on the packet.
	 *
	 * @param skipCount
	 * 		The skip count.
	 * @param packet
	 * 		The packet to write on.
	 */
	private static void putSkip(int skipCount, PacketBuilder packet) {
		if (skipCount > -1) {
			packet.writeBits(1, 0);
			if (skipCount == 0) {
				packet.writeBits(2, 0);
			} else if (skipCount < 32) {
				packet.writeBits(2, 1);
				packet.writeBits(5, skipCount);
			} else if (skipCount < 256) {
				packet.writeBits(2, 2);
				packet.writeBits(8, skipCount);
			} else if (skipCount < 2048) {
				packet.writeBits(2, 3);
				packet.writeBits(11, skipCount);
			}
		}
	}
	
}