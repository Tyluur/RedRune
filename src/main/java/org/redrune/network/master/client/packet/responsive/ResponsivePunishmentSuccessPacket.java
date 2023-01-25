package org.redrune.network.master.client.packet.responsive;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.game.world.punishment.Punishment;
import org.redrune.network.master.client.packet.ResponsiveGamePacket;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class ResponsivePunishmentSuccessPacket extends ResponsiveGamePacket {
	
	/**
	 * The id of the player who was punished
	 */
	private final byte worldId;
	
	/**
	 * The punishment
	 */
	private final Punishment punishment;
	
	/**
	 * The message
	 */
	private final String message;
	
	public ResponsivePunishmentSuccessPacket(byte worldId, Punishment punishment, String message) {
		this.worldId = worldId;
		this.punishment = punishment;
		this.message = message;
	}
	
	@Override
	public void read() {
		Optional<Player> optional = World.get().getPlayerByUsername(punishment.getPunisher());
		if (!optional.isPresent()) {
			return;
		}
		Player player = optional.get();
		player.getTransmitter().sendMessage(message);
	}
}
