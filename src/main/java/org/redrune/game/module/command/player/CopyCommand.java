package org.redrune.game.module.command.player;

import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.world.World;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/20/2017
 */
@CommandManifest(description = "Copies the gear of a player online.", types = { String.class })
public class CopyCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("copy");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		
		Optional<Player> optional = World.get().getPlayerByUsername(getCompleted(args, 1));
		if (!optional.isPresent()) {
			player.getTransmitter().sendMessage("Couldn't find player...");
			return;
		}
		Player target = optional.get();
		System.arraycopy(target.getInventory().getItems().toArray(), 0, player.getInventory().getItems().toArray(), 0, player.getInventory().getItems().toArray().length);
		System.arraycopy(target.getEquipment().getItems().toArray(), 0, player.getEquipment().getItems().toArray(), 0, player.getEquipment().getItems().toArray().length);
		player.getInventory().refresh();
		player.getEquipment().refreshAll();
		player.getSkills().passLevels(target);
		
		player.getSkills().updateAllSkills();
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
}
