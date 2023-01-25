package org.redrune.network.world.packet.outgoing.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class FriendsListBuilder implements OutgoingPacketBuilder {
	
	private final String name;
	
	private final String previousName;
	
	private final int worldId;
	
	private final int clanRank;
	
	private final boolean warn;
	
	private final boolean lobby;
	
	private final boolean online;
	
	/**
	 * Constructs an empty friends list
	 */
	public FriendsListBuilder() {
		this(null, null, -1, 0, false, false, false);
	}
	
	/**
	 * Constructs the packet for the friends list addition
	 *
	 * @param name
	 * 		The name of the friend to add to the list
	 * @param previousName
	 * 		The previous name the friend has (for display names)
	 * @param worldId
	 * 		The id of the world the friend is on
	 * @param clanRank
	 * 		The rank of the friend in your clan chat
	 * @param warn
	 * 		Should we warn of the status change?
	 * @param lobby
	 * 		Are they in the lobby? If they are in the lobby the text will be yellow. If they aren't in the lobby and their
	 * 		world is different from us - the text will be yellow.
	 * @param online
	 * 		If the friend is online
	 */
	public FriendsListBuilder(String name, String previousName, int worldId, int clanRank, boolean warn, boolean lobby, boolean online) {
		if (lobby) {
			worldId = MasterConstants.LOBBY_WORLD_ID;
		}
		if (!online) {
			worldId = 0;
		}
		
		this.name = name;
		this.previousName = previousName;
		this.worldId = worldId;
		this.clanRank = clanRank;
		this.warn = warn;
		this.lobby = lobby;
		this.online = online;
	}
	
	@Override
	public Packet build(Player player) {
		if (name == null) {
			return new PacketBuilder(5, PacketType.VAR_SHORT).toPacket();
		} else {
			PacketBuilder bldr = new PacketBuilder(5, PacketType.VAR_SHORT);
			
			// if we should tell the client that their status changed
			bldr.writeByte(warn ? 0 : 1);
			// current name
			bldr.writeRS2String(name);
			// their last display name
			bldr.writeRS2String(previousName);
			// random world id for the lobby.
			bldr.writeShort(worldId);
			// their clan rank
			bldr.writeByte(clanRank);
			// unknown
			bldr.writeByte(0);
			
			// this writes the player's lobby details
			if (worldId > 0) {
				bldr.writeRS2String((lobby ? "Lobby" : "World " + worldId));
				bldr.writeByte(0);
			}
			return bldr.toPacket();
		}
	}
	
}
