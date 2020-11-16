package org.redrune.network.master.server.network.packet.out;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class PunishmentRemovalSuccessDeliveryPacketOut extends WritablePacket {
	
	/**
	 * The id of the world that successfully applied the punishment
	 */
	private final byte worldId;
	
	/**
	 * The punishment
	 */
	private final Punishment punishment;
	
	/**
	 * The message of succession
	 */
	private final String message;
	
	public PunishmentRemovalSuccessDeliveryPacketOut(byte worldId, Punishment punishment, String message) {
		super(PUNISHMENT_REMOVAL_SUCCESS_DELIVERY_PACKET);
		this.worldId = worldId;
		this.punishment = punishment;
		this.message = message;
	}
	
	@Override
	public WritablePacket create() {
		writeByte(worldId);
		writeString(message);
		writeString(punishment.getPunisher());
		writeString(punishment.getPunished());
		writeByte((byte) punishment.getType().ordinal());
		writeLong(punishment.getTime());
		return this;
	}
}