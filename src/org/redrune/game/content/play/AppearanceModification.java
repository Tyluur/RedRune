package org.redrune.game.content.play;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/19/2017
 */
public class AppearanceModification {
	
	/**
	 * Opens the character styling interface
	 *
	 * @param player
	 * 		The player to open the interface for
	 */
	public static void openCharacterStyling(Player player) {
		
		int interfaceId = 1028;
		
		/*
		
		// min max i c i2 c2
		player.getIOSession().write(new AccessMask(player, 2, 1028, 45, 0, 204, 0));
		player.getIOSession().write(new AccessMask(player, 2, 1028, 111, 0, 204, 0));
		player.getIOSession().write(new AccessMask(player, 2, 1028, 107, 0, 204, 0));
		 */
//		player.getTransmitter().send(new AccessMaskBuilder(45, 0, 2, 1028, 204, 0).build(player));
		
		for (int i = 0; i < 185; i++) {
			player.getTransmitter().send(new AccessMaskBuilder(interfaceId, i, 0, 250, 0).build(player));
		}
		
		player.getTransmitter().send(new AccessMaskBuilder(interfaceId, 45, 0, 11, new int[] { 0 }).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(interfaceId, 107, 0, 50, new int[] { 0 }).build(player));
		player.getTransmitter().send(new AccessMaskBuilder(interfaceId, 111, 0, 250, new int[] { 0 }).build(player));
		
		player.getManager().getInterfaces().sendWindowPane(interfaceId, 0);
		
//		player.getTransmitter().send(new ConfigPacketBuilder(8093, player.getDetails().getAppearance().isMale() ? 0 : 1).build(player));
	}
	
}
