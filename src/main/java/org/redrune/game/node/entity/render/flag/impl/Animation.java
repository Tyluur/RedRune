package org.redrune.game.node.entity.render.flag.impl;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.render.UpdateMasks;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.backend.Priority;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;

/**
 * Represents an animation update flag.
 *
 * @author Emperor
 */
public class Animation extends UpdateFlag {
	
	/**
	 * The animation id.
	 */
	@Getter
	private final int id;
	
	/**
	 * The speed of the animation.
	 */
	@Getter
	private final int speed;
	
	/**
	 * If the entity is an NPC.
	 */
	@Getter
	@Setter
	private boolean npc;
	
	/**
	 * The priority.
	 */
	@Getter
	@Setter
	private Priority priority;
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 * @param speed
	 * 		The speed of the animation.
	 * @param npc
	 * 		If the entity is an NPC.
	 */
	public Animation(int id, int speed, boolean npc) {
		this(id, speed, npc, Priority.NORMAL);
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 * @param speed
	 * 		The speed of the animation.
	 * @param npc
	 * 		If the entity is an NPC.
	 * @param priority
	 * 		The animation priority.
	 */
	public Animation(int id, int speed, boolean npc, Priority priority) {
		this.id = id;
		this.speed = speed;
		this.npc = npc;
		this.priority = priority;
	}
	
	/**
	 * Constructs a new {@code Animation} {@code Object}.
	 *
	 * @param id
	 * 		The animation id.
	 */
	
	public Animation(int id) {
		this(id, 0, false, Priority.NORMAL);
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		if (npc) {
			for (int i = 0; i < 4; i++) {
				bldr.writeLEShortA(id);
			}
			bldr.writeByteA(speed << 16);
		} else {
			for (int i = 0; i < 4; i++) {
				bldr.writeShortA(id);
			}
			bldr.writeByte(speed << 16);
		}
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 6 : 15;
	}
	
	@Override
	public int getMaskData() {
		return npc ? 0x1 : 0x10;
	}
	
	@Override
	public boolean canRegister(UpdateMasks updateMasks) {
		switch(priority) {
			case LOWEST:
				if (updateMasks.getLastAnimationEndTime() > System.currentTimeMillis()) {
					return false;
				}
				break;
			default:
				return true;
		}
		return true;
	}
	
	public static Animation create(int animationId) {
		return new Animation(animationId);
	}
	
}
