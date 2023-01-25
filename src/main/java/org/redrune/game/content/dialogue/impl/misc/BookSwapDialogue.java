package org.redrune.game.content.dialogue.impl.misc;

import org.redrune.game.content.dialogue.Dialogue;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.prayer.PrayerBook;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/9/2017
 */
public class BookSwapDialogue extends Dialogue {
	
	@Override
	public void constructMessages(Player player) {
		options(DEFAULT_OPTION, new String[] { "Change spell book", "Change prayer book" }, () -> {
			options("Select a book", new String[] { "Regular spells", "Ancient spells", "Lunar spells" }, () -> {
				player.getCombatDefinitions().setSpellbook(MagicBook.REGULAR);
				notifyChange(true);
			}, () -> {
				player.getCombatDefinitions().setSpellbook(MagicBook.ANCIENTS);
				notifyChange(true);
			}, () -> {
				player.getCombatDefinitions().setSpellbook(MagicBook.LUNARS);
				notifyChange(true);
			});
		}, () -> {
			options("Select a book", new String[] { "Regular prayers", "Curse prayers" }, () -> {
				player.getManager().getPrayers().setBook(PrayerBook.REGULAR);
				notifyChange(false);
			}, () -> {
				player.getManager().getPrayers().setBook(PrayerBook.CURSES);
				notifyChange(false);
			});
		});
	}
	
	/**
	 * Notifies the player of their change in books
	 *
	 * @param spellbook
	 * 		If the change was a spellbook
	 */
	private void notifyChange(boolean spellbook) {
		chatbox("Your " + (spellbook ? "spellbook" : "prayer book") + " has successfully been changed.");
	}
}
