package org.redrune.game.node.entity.render.flag.impl;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.network.world.packet.PacketBuilder;

/**
 * Handles the force movement update flag.
 *
 * @author Emperor
 */
public final class ForceMovement extends UpdateFlag {
	
	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
	
	/**
	 * The entity.
	 */
	private final Entity entity;
	
	/**
	 * The movement data.
	 */
	private final int[] movement;
	
	/**
	 * Constructs a new {@code ForceMovement} {@code Object}.
	 *
	 * @param entity
	 * 		The entity.
	 * @param movement
	 * 		The movement data.
	 */
	public ForceMovement(Entity entity, int[] movement) {
		this.entity = entity;
		this.movement = movement;
	}
	
	@Override
	public void write(Player outgoing, PacketBuilder bldr) {
		Location myLocation = entity.getLocation();
		Location fromLocation = entity.getLocation(); //Is this even needed?
		Location toLocation = Location.create(movement[0], movement[1], 0);
		int distfromx = 0;
		int distfromy = 0;
		boolean positiveFromX = false;
		boolean positiveFromY = false;
		int distanceToX = 0;
		int distanceToY = 0;
		boolean positiveToX = false;
		boolean positiveToY = false;
		if (myLocation.getX() < fromLocation.getX()) {
			positiveFromX = true;
		}
		if (myLocation.getY() < fromLocation.getY()) {
			positiveFromY = true;
		}
		if (fromLocation.getX() < toLocation.getX()) {
			positiveToX = true;
		}
		if (fromLocation.getY() < toLocation.getY()) {
			positiveToY = true;
		}
		if (positiveFromX) {
			distfromx = fromLocation.getX() - myLocation.getX();
		} else {
			distfromx = myLocation.getX() - fromLocation.getX();
		}
		if (positiveFromY) {
			distfromy = fromLocation.getY() - myLocation.getY();
		} else {
			distfromy = myLocation.getY() - fromLocation.getY();
		}
		if (positiveToX) {
			distanceToX = toLocation.getX() - fromLocation.getX();
		} else {
			distanceToX = fromLocation.getX() - toLocation.getX();
		}
		if (positiveToY) {
			distanceToY = toLocation.getY() - fromLocation.getY();
		} else {
			distanceToY = fromLocation.getY() - toLocation.getY();
		}
		bldr.writeByteS(positiveFromX ? distfromx : -distfromx);
		bldr.writeByte(positiveFromY ? distfromy : -distfromy);
		bldr.writeByteS(positiveToX ? distanceToX : -distanceToX);
		bldr.writeByteS(positiveToY ? distanceToY : -distanceToY);
		bldr.writeShort(movement[2]);
		bldr.writeLEShortA(movement[3]);
		bldr.writeByteC(movement[4]);
	}
	
	@Override
	public int getOrdinal() {
		return 8;
	}
	
	@Override
	public int getMaskData() {
		return 0x1000;
	}
	
}