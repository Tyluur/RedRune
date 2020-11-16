package org.redrune.network.master.client.packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public abstract class ResponsiveGamePacket {
	
	/**
	 * Reads the game packet
	 */
	public abstract void read();
	
	public ResponsiveGamePacket() {
	}
}
