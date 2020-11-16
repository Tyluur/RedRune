package org.redrune.game.node.entity.render.flag;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.UpdateMasks;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * The interface implemented by all update flags.
 *
 * @author Emperor
 */
public abstract class UpdateFlag implements Comparable<UpdateFlag> {
	
	/**
	 * Writes the data to the packet specified.
	 *
	 * @param outgoing
	 * 		The player the packet is going to
	 * @param packet
	 * 		The packet
	 */
	public abstract void write(Player outgoing, PacketBuilder packet);
	
	@Override
	public int compareTo(UpdateFlag flag) {
		if (flag == null) {
			return -1;
		}
		if (flag.getOrdinal() == getOrdinal()) {
			return 0;
		}
		if (flag.getOrdinal() < getOrdinal()) {
			return 1;
		}
		return -1;
	}
	
	/**
	 * Gets the mask ordinal.
	 *
	 * @return The ordinal.
	 */
	public abstract int getOrdinal();
	
	@Override
	public boolean equals(Object o) {
		return o instanceof UpdateFlag && getMaskData() == ((UpdateFlag) o).getMaskData() && getOrdinal() == ((UpdateFlag) o).getOrdinal();
	}
	
	/**
	 * Gets the mask data.
	 *
	 * @return The mask data.
	 */
	public abstract int getMaskData();
	
	/**
	 * Used to check if the update flag can be registered.
	 *
	 * @param updateMasks
	 * 		The update masks instance used.
	 * @return {@code True} if the update mask can be registered, {@code false} if not.
	 */
	public boolean canRegister(UpdateMasks updateMasks) {
		return true;
	}
	
}