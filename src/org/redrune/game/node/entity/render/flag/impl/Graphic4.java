package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.utility.rs.Graphics;

/**
 * Represents the fourth gfx update mask.
 *
 * @author Emperor
 */
public class Graphic4 extends UpdateFlag implements Graphics {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
	 */
	private final int height;
	
	/**
	 * The speed of the graphics
	 */
	private final int speed;
	
	/**
	 * The rotation.
	 */
	private final int rotation;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic4} {@code Object}.
	 */
	public Graphic4(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.rotation = 0;
		this.npc = npc;
	}
	
	public Graphic4(int id, int height, int speed, int rotation, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.rotation = rotation;
		this.npc = npc;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		if (npc) {
			bldr.writeLEShortA(id);
			bldr.writeLEInt(getPrimarySettings());
			bldr.writeByteC(getSecondarySettings());
		} else {
			bldr.writeLEShort(id);
			bldr.writeInt2(getPrimarySettings());
			bldr.writeByteA(getSecondarySettings());
		}
	}
	
	@Override
	public int getOrdinal() {
		return 12;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x4 : 0x80;
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