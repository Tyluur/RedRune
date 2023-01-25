package org.redrune.game.module.command.administrator;

import org.redrune.game.node.entity.player.link.prayer.PrayerBook;
import org.redrune.game.module.command.CommandManifest;
import org.redrune.game.module.command.CommandModule;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/26/2017
 */
@CommandManifest(description = "Sets the prayer book", types = Integer.class)
public class SetPrayerBookCommand extends CommandModule {
	
	@Override
	public String[] identifiers() {
		return arguments("setprayerbook");
	}
	
	@Override
	public void handle(Player player, String[] args, boolean console) {
		int ordinal = intParam(args, 1);
		PrayerBook[] values = PrayerBook.values();
		if (ordinal < 0 || ordinal >= values.length) {
			player.getTransmitter().sendMessage("You entered an invalid prayer book id.");
			return;
		}
		PrayerBook book = values[ordinal];
		player.getManager().getPrayers().setBook(book);
	}
}
