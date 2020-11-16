package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.render.flag.UpdateFlag;

/**
 * Represents the face location update mask.
 *
 * @author Emperor
 */
public class FaceLocationUpdate extends UpdateFlag {
	
	/**
	 * The location to face.
	 */
	private final Location location;
	
	/**
	 * If the entity is an NPC.
	 */
	private final boolean npc;
	
	/**
	 * The entity's location.
	 */
	private final Location currentLocation;
	
	/**
	 * Constructs a new {@code FaceLocationUpdate} {@code Object}.
	 *
	 * @param entity
	 * 		The entity facing a location.
	 * @param location
	 * 		The location to face.
	 */
	public FaceLocationUpdate(Entity entity, Location location) {
		this.npc = entity.isNPC();
		this.location = location;
		this.currentLocation = entity.getLocation();
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		if (npc) {
			bldr.writeLEShortA(location.getX() << 1);
			bldr.writeLEShortA(location.getY() << 1);
		} else {
			final int dX = currentLocation.getX() - location.getX();
			final int dY = currentLocation.getY() - location.getY();
			bldr.writeLEShortA(((int) (Math.atan2(dX, dY) * 2607.5945876176133)) & 0x3fff);
		}
	}
	
	@Override
	public int getOrdinal() {
		return npc ? 13 : 14;
	}
	
	@Override
	public int getMaskData() {
		return 0x20;
	}
	
}