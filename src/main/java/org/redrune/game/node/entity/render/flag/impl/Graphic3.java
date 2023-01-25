package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.utility.rs.Graphics;

/**
 * Represents the secondary animation mask?
 *
 * @author Emperor
 */
public class Graphic3 extends UpdateFlag implements Graphics {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The height.
	 */
	private final int height;
	
	/**
	 * The speed of the graphic
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
	 * Constructs a new {@code Graphic3} {@code Object}.
	 */
	public Graphic3(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.rotation = 0;
		this.npc = npc;
	}
	
	public Graphic3(int id, int height, int speed, int rotation, boolean npc) {
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
			bldr.writeByteA(getSecondarySettings());
		} else {
			bldr.writeLEShort(id);
			bldr.writeInt(getPrimarySettings());
			bldr.writeByteC(getSecondarySettings());
		}
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 9 : 5;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x10000 : 0x100000;
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