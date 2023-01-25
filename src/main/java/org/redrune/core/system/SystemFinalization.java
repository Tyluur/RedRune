package org.redrune.core.system;

import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class SystemFinalization extends Thread {
	
	@Override
	public void run() {
		int count = 0;
		try {
			for (Player player : World.get().getPlayers()) {
				if (player == null) {
					continue;
				}
				player.save();
				count++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("System shutdown has been executed and saved " + count + " players.");
	}
}
