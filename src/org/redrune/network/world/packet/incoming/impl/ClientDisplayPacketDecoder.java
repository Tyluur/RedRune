package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.tool.Misc;
import org.redrune.network.world.packet.Packet;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class ClientDisplayPacketDecoder implements IncomingPacketDecoder {
	
	@Override
	public int[] bindings() {
		return Misc.arguments(34);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		int screenSizeMode = packet.readByte();
		int screenSizeX = packet.readShort();
		int screenSizeY = packet.readShort();
		int displayMode = packet.readByte();
		if (screenSizeMode < 0 || screenSizeMode > 3) {
			return;
		}
		boolean send = false;
		if (screenSizeMode != player.getSession().getViewComponents().getScreenSizeMode()) {
			send = true;
		}
		player.getSession().getViewComponents().setScreenSizeMode(screenSizeMode);
		player.getSession().getViewComponents().setScreenSizeX(screenSizeX);
		player.getSession().getViewComponents().setScreenSizeY(screenSizeY);
		player.getSession().getViewComponents().setDisplayMode(displayMode);
		if (send) {
			player.getManager().getInterfaces().sendLogin();
			player.getManager().getInterfaces().sendInterface(742, true);
		}
	}
}
