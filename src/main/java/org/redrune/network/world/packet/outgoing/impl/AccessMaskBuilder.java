package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class AccessMaskBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The interface id.
	 */
	private final int interfaceId;
	
	/**
	 * The secondary child id
	 */
	private final int interfaceId2;
	
	/**
	 * The child id.
	 */
	private final int childId;
	
	/**
	 * The secondary child id
	 */
	private final int childId2;
	
	/**
	 * The minimum slot.
	 */
	private final int min;
	
	/**
	 * The maximum slot.
	 */
	private final int max;
	
	/**
	 * The mask flag
	 */
	private final int maskFlag;
	
	/**
	 * Constructs a new {@code AccessMaskBuilder} {@code Object}.
	 *
	 * @param interfaceId
	 * 		The interface id.
	 * @param childId
	 * 		The child id.
	 * @param min
	 * 		The minimum slot.
	 * @param max
	 * 		The maximum slot
	 * @param childId2
	 * 		The second child id
	 * @param interfaceId2
	 * 		The second interface id
	 */
	public AccessMaskBuilder(int interfaceId, int childId, int interfaceId2, int childId2, int min, int max) {
		this.min = min;
		this.max = max;
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.interfaceId2 = interfaceId2;
		this.childId2 = childId2;
		this.maskFlag = interfaceId2 << 16 | childId2;
	}
	/*
		public void sendUnlockIComponentOptionSlots(int interfaceId, int componentId, int fromSlot, int toSlot, int... optionsSlots) {
		int settingsHash = 0;
		for (int slot : optionsSlots) {
			settingsHash |= 2 << slot;
		}
		sendIComponentSettings(interfaceId, componentId, fromSlot, toSlot, settingsHash);
	}
	 */
	
	public AccessMaskBuilder(int interfaceId, int componentId, int min, int max, int... slots) {
		this(interfaceId, componentId, min, max, getFlagFromSlots(slots));
	}
	
	public AccessMaskBuilder(int interfaceId, int childId, int min, int max, int maskFlag) {
		this.interfaceId = interfaceId;
		this.childId = childId;
		this.min = min;
		this.max = max;
		this.maskFlag = maskFlag;
		
		// unused because mask flag is not generated from these two when we already have it.
		this.interfaceId2 = this.childId2 = -1;
	}
	
	private static int getFlagFromSlots(int... slots) {
		int maskFlag = 0;
		for (int slot : slots) {
			maskFlag |= 2 << slot;
		}
		return maskFlag;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(42);
		bldr.writeLEShort(min);
		bldr.writeLEInt(maskFlag);
		bldr.writeInt1(interfaceId << 16 | childId);
		bldr.writeLEShort(max);
		return bldr.toPacket();
	}
}