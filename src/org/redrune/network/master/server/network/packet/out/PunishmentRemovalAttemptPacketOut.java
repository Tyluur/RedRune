package org.redrune.network.master.server.network.packet.out;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class PunishmentRemovalAttemptPacketOut extends WritablePacket {
	
	/**
	 * The punishment that is being attempted to removed
	 */
	private final Punishment punishment;
	
	public PunishmentRemovalAttemptPacketOut(Punishment punishment) {
		super(PUNISHMENT_REMOVAL_ATTEMPT_PACKET_ID);
		this.punishment = punishment;
	}
	
	@Override
	public WritablePacket create() {
		writeString(punishment.getPunisher());
		writeString(punishment.getPunished());
		writeByte((byte) punishment.getType().ordinal());
		writeLong(punishment.getTime());
		return this;
	}
}
