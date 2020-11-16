package org.redrune.game.world.punishment;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public enum PunishmentType {
	
	MUTE {
		@Override
		public boolean add(World world, Player player, Punishment punishment) {
			if (player.getVariables().addPunishment(punishment)) {
				player.getTransmitter().sendMessage("You have been muted.");
				return true;
			}
			return false;
		}
		
		@Override
		public boolean remove(World world, Player player, Punishment punishment) {
			if (player.getVariables().removePunishment(punishment)) {
				player.getTransmitter().sendMessage("You have been unmuted.");
				return true;
			}
			return false;
		}
	},
	BAN {
		@Override
		public boolean add(World world, Player player, Punishment punishment) {
			if (player.getVariables().addPunishment(punishment)) {
				//player.deregister();
				player.getTransmitter().sendLogout(false);
				return true;
			}
			return false;
		}
		
		@Override
		public boolean remove(World world, Player player, Punishment punishment) {
			// we will only be unbanning players who are logged off
			if (player.getVariables().removePunishment(punishment)) {
				player.save();
				return true;
			}
			return false;
		}
	},
	ADDRESS_MUTE {
		@Override
		public boolean add(World world, Player player, Punishment punishment) {
			return false;
		}
		
		@Override
		public boolean remove(World world, Player player, Punishment punishment) {
			return false;
		}
	},
	ADDRESS_BAN {
		@Override
		public boolean add(World world, Player player, Punishment punishment) {
			return false;
		}
		
		@Override
		public boolean remove(World world, Player player, Punishment punishment) {
			return false;
		}
	};
	
	/**
	 * Handles the addition of a punishment to the world
	 *
	 * @param world
	 * 		The world to add the punishment to
	 * @param player
	 * 		The player
	 * @param punishment
	 * 		The punishment to add
	 */
	public abstract boolean add(World world, Player player, Punishment punishment);
	
	/**
	 * Handles the removal of a punishment
	 *
	 * @param world
	 * 		The world to add the punishment to
	 * @param player
	 * 		The player
	 * @param punishment
	 * 		The punishment to add
	 */
	public abstract boolean remove(World world, Player player, Punishment punishment);
}
