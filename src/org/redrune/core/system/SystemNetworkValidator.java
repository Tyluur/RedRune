package org.redrune.core.system;

import io.netty.channel.Channel;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.NetworkSession;
import org.redrune.network.world.WorldSession;

/**
 * This class handles the validation of all connected sessions to our network.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/17/2017
 */
public class SystemNetworkValidator implements Runnable {
	
	@Override
	public void run() {
		try {
			for (Player player : World.get().getPlayers()) {
				if (player == null) {
					continue;
				}
				checkSessionLegibility(player.getSession());
			}
			for (NetworkSession session : NetworkSession.getAllSessions()) {
				if (!(session instanceof WorldSession)) {
					continue;
				}
				checkSessionLegibility((WorldSession) session);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/**
	 * Check to see that a session is legible to exist still
	 *
	 * @param session
	 * 		The session
	 */
	private static void checkSessionLegibility(WorldSession session) {
		Channel channel = session.getChannel();
		boolean disconnect = false;
		// verify the channel is good
		if (!channel.isOpen() || !channel.isWritable() || !channel.isRegistered() || !channel.isActive()) {
			System.out.println("Invalid channel connected:\t" + channel);
			disconnect = true;
		}
		// if they haven't set a player and were existent for a while
		if (session.getPlayer() == null && session.getElapsedCreationTime() >= 5_000) {
			System.out.println("Session without player attached for too long:\t" + session);
			disconnect = true;
		}
		if (disconnect) {
			System.out.println("Disconnected session " + session);
			channel.disconnect();
		}
	}
}
