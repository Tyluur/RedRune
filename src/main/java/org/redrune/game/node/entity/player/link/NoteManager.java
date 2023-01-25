package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerNote;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2StringBuilder;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.InterfaceChangeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class NoteManager {
	
	/**
	 * The list of notes
	 */
	@Getter
	private final List<PlayerNote> notes;
	
	/**
	 * The instance of the player
	 */
	@Setter
	private transient Player player;
	
	public NoteManager() {
		notes = new ArrayList<>(30);
	}
	
	/**
	 * Sends the login configurations necessary to view all notes
	 */
	public void sendLoginConfiguration() {
		player.getTransmitter().send(new AccessMaskBuilder(34, 9, 0, 29, 2621470).build(player));
		player.getTransmitter().send(new InterfaceChangeBuilder(34, 3, false).build(player));
		player.getTransmitter().send(new InterfaceChangeBuilder(34, 8, false).build(player));
		player.getTransmitter().send(new InterfaceChangeBuilder(34, 44, false).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(1437, 1).build(player)); // unlocks add notes
		player.getTransmitter().send(new ConfigPacketBuilder(1439, -1).build(player));
		refresh();
	}
	
	/**
	 * Refreshes all notes
	 */
	private void refresh() {
		for (int i = 0; i < 30; i++) {
			player.getTransmitter().send(new CS2StringBuilder(149 + i, notes.size() <= i ? "" : notes.get(i).getText()).build(player));
		}
		player.getTransmitter().send(new ConfigPacketBuilder(1440, getPrimaryColour(this)).build(player));
		player.getTransmitter().send(new ConfigPacketBuilder(1441, getSecondaryColour(this)).build(player));
	}
	
	/**
	 * Gets the primary colour of the notes.
	 *
	 * @param notes
	 * 		The notes.
	 */
	public static int getPrimaryColour(NoteManager notes) {
		int color = 0;
		for (int i = 0; i < 16; i++) {
			if (notes.notes.size() <= i) {
				break;
			}
			color += colourize(notes.notes.get(i).getColour(), i);
		}
		return color;
	}
	
	/**
	 * Gets the secondary colour of the notes.
	 *
	 * @param notes
	 * 		The notes.
	 */
	public static int getSecondaryColour(NoteManager notes) {
		int color = 0;
		for (int i = 0; i < 14; i++) {
			if (notes.notes.size() - 16 <= i) {
				break;
			}
			color += colourize(notes.notes.get(i + 16).getColour(), i);
		}
		return color;
	}
	
	/**
	 * Gets the colour of a note after recolouring it
	 *
	 * @param colour
	 * 		The colour id
	 * @param noteId
	 * 		The note index id
	 */
	public static int colourize(int colour, int noteId) {
		return (int) (Math.pow(4, noteId) * colour);
	}
	
	/**
	 * Adds a note to our list
	 *
	 * @param text
	 * 		The text of the note
	 */
	public boolean add(String text) {
		if (notes.size() >= 30) {
			player.getTransmitter().sendMessage("You may only have 30 notes!");
			return false;
		}
		if (text.length() > 50) {
			player.getTransmitter().sendMessage("You can only enter notes up to 50 characters!");
			return false;
		}
		player.getTransmitter().send(new CS2StringBuilder(149 + notes.size(), text).build(player));
		setCurrentNote(notes.size());
		return notes.add(new PlayerNote(text));
	}
	
	/**
	 * Edits the text of a note
	 *
	 * @param text
	 * 		The text
	 */
	public boolean edit(String text) {
		if (text.length() > 50) {
			player.getTransmitter().sendMessage("You can only enter notes up to 50 characters!", true);
			return false;
		}
		int id = getCurrentNote();
		if (id == -1 || notes.size() <= id) {
			return false;
		}
		notes.get(id).setText(text);
		player.getTransmitter().send(new CS2StringBuilder(149 + id, text).build(player));
		return true;
	}
	
	/**
	 * Gets the current note
	 */
	public int getCurrentNote() {
		return player.getAttribute("CURRENT_NOTE", -1);
	}
	
	/**
	 * Sets the current note
	 */
	public void setCurrentNote(int id) {
		if (id >= 30) {
			return;
		}
		player.putAttribute("CURRENT_NOTE", id);
		player.getTransmitter().send(new ConfigPacketBuilder(1439, id).build(player));
	}
	
	/**
	 * Recolours the note
	 *
	 * @param colour
	 * 		The colour
	 */
	public boolean colour(int colour) {
		int id = getCurrentNote();
		if (id == -1 || notes.size() <= id) {
			return false;
		}
		notes.get(id).setColour(colour);
		if (id < 16) {
			player.getTransmitter().send(new ConfigPacketBuilder(1440, getPrimaryColour(this)).build(player));
		} else {
			player.getTransmitter().send(new ConfigPacketBuilder(1441, getSecondaryColour(this)).build(player));
		}
		return true;
	}
	
	/**
	 * Swaps the slot of a note
	 *
	 * @param from
	 * 		The from slot
	 * @param to
	 * 		The to slot
	 */
	public void switchNotes(int from, int to) {
		if (notes.size() <= from || notes.size() <= to) {
			return;
		}
		notes.set(to, notes.set(from, notes.get(to)));
		refresh();
	}
	
	/**
	 * Delets the current selected note.
	 */
	public void delete() {
		delete(getCurrentNote());
	}
	
	/**
	 * Deletes a note on an index
	 *
	 * @param index
	 * 		The index
	 */
	
	public void delete(int index) {
		if (index == -1 || notes.size() <= index) {
			return;
		}
		notes.remove(index);
		removeCurrentNote();
		refresh();
	}
	
	/**
	 * Removes the current note
	 */
	public void removeCurrentNote() {
		player.removeAttribute("CURRENT_NOTE");
		player.getTransmitter().send(new ConfigPacketBuilder(1439, -1).build(player));
	}
	
	/**
	 * Deletes all the notes
	 */
	public void deleteAll() {
		notes.clear();
		removeCurrentNote();
		refresh();
	}
	
}