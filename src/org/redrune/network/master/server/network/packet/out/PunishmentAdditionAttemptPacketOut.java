package org.redrune.network.master.server.network.packet.out;

import org.redrune.game.world.punishment.Punishment;
import org.redrune.network.master.network.packet.writeable.WritablePacket;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class PunishmentAdditionAttemptPacketOut extends WritablePacket {
	
	/**
	 * The punishment to write
	 */
	private final Punishment punishment;
	
	public PunishmentAdditionAttemptPacketOut(Punishment punishment) {
		super(PUNISHMENT_ADDITION_ATTEMPT_PACKET_ID);
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
