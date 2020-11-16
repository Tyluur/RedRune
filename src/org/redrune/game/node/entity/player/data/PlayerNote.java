package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public class PlayerNote {
	
	/**
	 * The text of the note
	 */
	@Getter
	@Setter
	private String text;
	
	/**
	 * The colour of the note
	 */
	@Getter
	@Setter
	private int colour;
	
	public PlayerNote(String text) {
		this.text = text;
	}
	
}
