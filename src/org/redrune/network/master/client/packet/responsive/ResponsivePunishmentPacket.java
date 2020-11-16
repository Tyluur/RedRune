package org.redrune.network.master.client.packet.responsive;

import org.redrune.game.GameFlags;
import org.redrune.game.world.punishment.Punishment;
import org.redrune.game.world.punishment.PunishmentHandler;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;
import org.redrune.network.master.client.packet.out.PunishmentAdditionSuccessPacketOut;
import org.redrune.network.master.client.packet.out.PunishmentRemovalSuccessPacketOut;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class ResponsivePunishmentPacket extends ResponsiveGamePacket {
	
	/**
	 * The punishment
	 */
	private final Punishment punishment;
	
	public ResponsivePunishmentPacket(Punishment punishment) {
		this.punishment = punishment;
	}
	
	@Override
	public void read() {
		// when the time is set to 0, this is a punishment removal
		boolean addition = punishment.getTime() != 0;
		
		if (addition) {
			boolean success = PunishmentHandler.addPunishment(punishment);
			String message = PunishmentHandler.getMessage(punishment, true, success);
			
			if (success) {
				MasterCommunication.write(new PunishmentAdditionSuccessPacketOut(GameFlags.worldId, punishment, message));
			}
		} else {
			boolean success = PunishmentHandler.deletePunishment(punishment);
			String message = PunishmentHandler.getMessage(punishment, false, success);
			
			if (success) {
				MasterCommunication.write(new PunishmentRemovalSuccessPacketOut(GameFlags.worldId, punishment, message));
			}
		}
	}
	
}
