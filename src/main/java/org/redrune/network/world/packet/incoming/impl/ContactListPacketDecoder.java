package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public class ContactListPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet opcodes
	 */
	public static final int ADD_FRIEND = 31, REMOVE_FRIEND = 35, ADD_IGNORE = 68, REMOVE_IGNORE = 2;
	
	@Override
	public int[] bindings() {
		return arguments(ADD_FRIEND, REMOVE_FRIEND, ADD_IGNORE, REMOVE_IGNORE);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		String name = Misc.formatPlayerNameForProtocol(packet.readRS2String());
		switch (packet.getOpcode()) {
			case ADD_FRIEND:
				player.getManager().getContacts().addFriend(name);
				break;
			case ADD_IGNORE:
				player.getManager().getContacts().addIgnore(name);
				break;
			case REMOVE_FRIEND:
				player.getManager().getContacts().removeFriend(name);
				break;
			case REMOVE_IGNORE:
				player.getManager().getContacts().removeIgnore(name);
				break;
		}
	}
}
