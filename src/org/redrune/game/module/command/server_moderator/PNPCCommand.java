package org.redrune.game.module.command.server_moderator;

import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@CommandManifest(description = "Transforms you into an npc", types = { Integer.class })
public class PNPCCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("pnpc");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		player.getDetails().getAppearance().setNpcId(intParam(args, 1));
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
}
