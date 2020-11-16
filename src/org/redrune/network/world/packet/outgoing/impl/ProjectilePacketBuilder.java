package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.rs.Projectile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public class ProjectilePacketBuilder implements OutgoingPacketBuilder {
	
	/**
	 * The projectile we're creating
	 */
	private final Projectile projectile;
	
	public ProjectilePacketBuilder(Projectile projectile) {
		this.projectile = projectile;
	}
	
	@Override
	public Packet build(Player player) {
		PacketBuilder bldr = new PacketBuilder(27);
		Location end = projectile.isLocationBased() ? projectile.getEndLocation() : projectile.getVictim().getLocation();
		Location start = projectile.getSourceLocation();
		int localX = start.getLocalX(player.getLastLoadedLocation());
		int localY = start.getLocalY(player.getLastLoadedLocation());
		int offsetX = localX - ((localX >> 3) << 3);
		int offsetY = localY - ((localY >> 3) << 3);
		player.getTransmitter().send(new TileLocationUpdate(start).build(player));
		bldr.writeByte((offsetX << 3) | offsetY);
		bldr.writeByte(end.getX() - start.getX());
		bldr.writeByte(end.getY() - start.getY());
		bldr.writeShort(projectile.getVictim() != null ? (projectile.getVictim().isPlayer() ? -(projectile.getVictim().getIndex() + 1) : (projectile.getVictim().getIndex() + 1)) : 0);
		bldr.writeShort(projectile.getProjectileId());
		bldr.writeByte(projectile.getStartHeight());
		bldr.writeByte(projectile.getEndHeight());
		bldr.writeShort(projectile.getDelay());
		bldr.writeShort(projectile.getSpeed());
		bldr.writeByte(projectile.getAngle());
		bldr.writeShort(projectile.getCreatorSize() * 64 + projectile.getStartDistanceOffset() * 64);
		return bldr.toPacket();
	}
}
