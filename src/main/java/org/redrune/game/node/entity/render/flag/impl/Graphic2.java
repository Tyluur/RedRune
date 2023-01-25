package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.rs.Graphics;

/**
 * Represents the secondary graphic update flag.
 *
 * @author Emperor
 */
public class Graphic2 extends UpdateFlag implements Graphics {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
	 */
	private final int height;
	
	/**
	 * The graphic speed.
	 */
	private final int speed;
	
	/**
	 * The rotation.
	 */
	private final int rotation;
	
	/**
	 * If we're writing for an npc.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic2} {@code Object}.
	 */
	public Graphic2(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.rotation = 0;
		this.npc = npc;
	}
	
	/**
	 * Constructs a new {@code Graphic2} {@code Object}.
	 *
	 * @param id
	 * 		The graphic id.
	 * @param speed
	 * 		The graphic speed.
	 * @param rotation
	 * 		The rotation.
	 */
	public Graphic2(int id, int height, int speed, int rotation, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.rotation = rotation;
		this.npc = npc;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder blrd) {
		if (npc) {
			blrd.writeShort(id);
			blrd.writeInt2(getPrimarySettings());
			blrd.writeByteS(getSecondarySettings());
		} else {
			blrd.writeShort(id);
			blrd.writeInt2(getPrimarySettings());
			blrd.writeByteC(getSecondarySettings());
		}
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 5 : 0;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x80000 : 0x800;
	}
	
	@Override
	public int id() {
		return id;
	}
	
	@Override
	public boolean npc() {
		return npc;
	}
	
	@Override
	public int height() {
		return height;
	}
	
	@Override
	public int speed() {
		return speed;
	}
	
	@Override
	public int rotation() {
		return rotation;
	}
}
