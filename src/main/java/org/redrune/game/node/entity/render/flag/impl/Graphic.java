package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.utility.rs.Graphics;

/**
 * Represents the graphic 1 update flag.
 *
 * @author Emperor
 */
public class Graphic extends UpdateFlag implements Graphics {
	
	/**
	 * The graphic id.
	 */
	private final int id;
	
	/**
	 * The graphic height.
	 */
	private final int height;
	
	/**
	 * The speed.
	 */
	private final int speed;
	
	/**
	 * The rotation of the graphic
	 */
	private final int rotation;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * Constructs a new {@code Graphic} {@code Object}.
	 *
	 * @param id
	 * 		The graphic id.
	 * @param height
	 * 		The graphic height.
	 * @param speed
	 * 		The speed.
	 * @param npc
	 * 		If the entity is an NPC.
	 */
	public Graphic(int id, int height, int speed, boolean npc) {
		this.id = id;
		this.height = height;
		this.speed = speed;
		this.npc = npc;
		this.rotation = 0;
	}
	
	/**
	 * Constructs a new graphic
	 *
	 * @param id
	 * 		The id of the graphic
	 */
	public Graphic(int id) {
		this.id = id;
		this.height = 0;
		this.speed = 0;
		this.npc = false;
		this.rotation = 0;
	}
	
	public Graphic(int id, int height, int speed, int rotation, boolean npc) {
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
			bldr.writeByteS(getSecondarySettings());
		}
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 2 : 13;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x1000 : 0x10000;
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
	
	public static Graphic create(int i) {
		return new Graphic(i);
	}
}
