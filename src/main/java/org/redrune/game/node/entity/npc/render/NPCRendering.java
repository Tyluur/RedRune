package org.redrune.game.node.entity.npc.render;

import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.tool.Misc;

import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class NPCRendering implements OutgoingPacketBuilder {
	
	@Override
	public Packet build(Player player) {
		// the packet for npc rendering
		PacketBuilder bldr = new PacketBuilder(6, PacketType.VAR_SHORT);
		
		// The update block. Any updates that are pending will be added to this block.
		PacketBuilder updateBlock = new PacketBuilder();
		
		List<NPC> localNpcs = player.getRenderInformation().getLocalNpcs();
		bldr.startBitAccess();
		bldr.writeBits(8, localNpcs.size());
		for (Iterator<NPC> it = localNpcs.iterator(); it.hasNext(); ) {
			NPC npc = it.next();
			if (!npc.isRenderable() || !npc.getLocation().isWithinDistance(player.getLocation()) || npc.teleporting()) {
				// Signify the client that this npc needs to be removed.
				bldr.writeBits(1, 1);
				bldr.writeBits(2, 3);
				// remove it from our list
				it.remove();
			} else {
				updateNPCMovement(npc, bldr);
				// Update the npc is required, since it is conditionally valid.
				if (npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(player, updateBlock, npc);
				}
			}
		}
		
		for (Integer regionId : player.getMapRegionsIds()) {
			Region region = RegionManager.getRegion(regionId);
			CopyOnWriteArraySet<NPC> npcs = region.getNpcs();
			for (NPC npc : npcs) {
				if (localNpcs.size() >= 255) {
					break;
				}
				if (!npc.isRenderable() || !npc.getLocation().isWithinDistance(player.getLocation()) || localNpcs.contains(npc)) {
					continue;
				}
				localNpcs.add(npc);
				addNewNpc(player, npc, bldr);
				if (npc.getUpdateMasks() != null && npc.getUpdateMasks().isUpdateRequired()) {
					updateNPC(player, updateBlock, npc);
				}
			}
		}
		bldr.writeBits(15, 32767);
		return bldr.finishBitAccess().writeBytes(updateBlock.getBuffer()).toPacket();
	}
	
	/**
	 * Updates an npcs movement.
	 *
	 * @param npc
	 * 		The npc.
	 * @param builder
	 * 		The buffer.
	 */
	private static void updateNPCMovement(NPC npc, PacketBuilder builder) {
		boolean needUpdate = npc.getUpdateMasks().isUpdateRequired();
		boolean walkUpdate = npc.getMovement().getNextWalkDirection() != -1;
		builder.writeBits(1, (needUpdate || walkUpdate) ? 1 : 0);
		if (walkUpdate) {
			builder.writeBits(2, npc.getMovement().getNextRunDirection() == -1 ? 1 : 2);
			if (npc.getMovement().getNextRunDirection() != -1) {
				builder.writeBits(1, 1);
			}
			builder.writeBits(3, Misc.getNpcMoveDirection(npc.getMovement().getNextWalkDirection()));
			if (npc.getMovement().getNextRunDirection() != -1) {
				builder.writeBits(3, Misc.getNpcMoveDirection(npc.getMovement().getNextRunDirection()));
			}
			builder.writeBits(1, needUpdate ? 1 : 0);
		} else if (needUpdate) {
			builder.writeBits(2, 0);
		}
	}
	
	/**
	 * Writes the NPC flag-based updating.
	 *
	 * @param packet
	 * 		The packet to write on.
	 * @param npc
	 * 		The npc.
	 */
	private static void updateNPC(Player player, PacketBuilder packet, NPC npc) {
		int maskdata = 0;
		PriorityQueue<UpdateFlag> flags = new PriorityQueue<>(npc.getUpdateMasks().getFlagQueue());
		for (UpdateFlag flag : flags) {
			maskdata |= flag.getMaskData();
		}
		if (maskdata > 128) {
			maskdata |= 0x2;
		}
		if (maskdata > 32768) {
			maskdata |= 0x2000;
		}
		packet.writeByte((byte) maskdata);
		if (maskdata > 128) {
			packet.writeByte((byte) (maskdata >> 8));
		}
		if (maskdata > 32768) {
			packet.writeByte((byte) (maskdata >> 16));
		}
		while (!flags.isEmpty()) {
			flags.poll().write(player, packet);
		}
	}
	
	/**
	 * Adds an NPC.
	 *
	 * @param player
	 * 		The player.
	 * @param npc
	 * 		The npc.
	 * @param buf
	 * 		The outgoing packet.
	 */
	private static void addNewNpc(Player player, NPC npc, PacketBuilder buf) {
		try {
			buf.writeBits(15, npc.getIndex());
			buf.writeBits(1, npc.getUpdateMasks().isUpdateRequired() || npc.teleporting() ? 1 : 0);
			buf.writeBits(2, npc.getLocation().getPlane());
			int xDelta = npc.getLocation().getX() - player.getLocation().getX();
			int yDelta = npc.getLocation().getY() - player.getLocation().getY();
			if (xDelta < 0) {
				xDelta += 32;
			}
			if (yDelta < 0) {
				yDelta += 32;
			}
			buf.writeBits(5, xDelta);
			buf.writeBits(1, 0);
			buf.writeBits(3, npc.getFaceDirection());
			buf.writeBits(5, yDelta);
			buf.writeBits(15, npc.getId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
