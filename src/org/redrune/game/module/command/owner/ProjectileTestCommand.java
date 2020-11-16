package org.redrune.game.module.command.owner;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.world.World;
import org.redrune.utility.rs.Projectile;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/29/2017
 */
@CommandManifest(description = "Sends a projectile to a close entity")
public class ProjectileTestCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("sendprojectile");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		Entity target = null;
		for (int i = 0; i <= World.get().getPlayers().size(); i++) {
			Player p = World.get().getPlayers().get(i);
			if (p == null) {
				continue;
			}
			if (p.getIndex() == player.getIndex()) {
				continue;
			}
			target = p;
		}
		if (target == null) {
			return;
		}
		boolean speedDefined = boolParam(args, 1);
		Projectile projectile;
		if (speedDefined) {
			final int projectileId = intParam(args, 2);
			final int startHeight = intParam(args, 3);
			final int endHeight = intParam(args, 4);
			final int delay = intParam(args, 5);
			final int angle = intParam(args, 6);
			final int offset = intParam(args, 7);
			projectile = ProjectileManager.createSpeedDefinedProjectile(player, target, projectileId, startHeight, endHeight, delay, angle, offset);
		} else {
			final int projectileId = intParam(args, 2);
			final int startHeight = intParam(args, 3);
			final int endHeight = intParam(args, 4);
			final int delay = intParam(args, 5);
			final int speed = intParam(args, 6);
			final int angle = intParam(args, 7);
			final int offset = intParam(args, 8);
			
			projectile = new Projectile(player, target, projectileId, startHeight, endHeight, delay, speed, angle, offset);
		}
		
		/*projectile = new Projectile(player, target, projectileId, startHeight, endHeight, delay, intParam(args, 5), intParam(args, 6), intParam(args, 7));
		*/
		System.out.println(projectile);
		ProjectileManager.sendProjectile(projectile);
	}
}
