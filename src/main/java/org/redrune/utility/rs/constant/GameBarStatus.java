package org.redrune.utility.rs.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/12/2017
 */
public enum GameBarStatus {
	
	/**
	 * This status flag means that the selected option is on 'on'
	 */
	ON(0),
	
	/**
	 * This status flag means that the selected option is on 'friends'
	 */
	FRIENDS(1),
	
	/**
	 * This status flag means that the selected option is on 'off'
	 */
	OFF(2),
	
	/**
	 * This status flag means that the selected option is on 'hide'
	 */
	HIDE(3),
	
	/**
	 * This flag is only used for the 'game' option, this is the flag that means we should not filter messages
	 */
	NO_FILTER(0),
	
	/**
	 * This flag is only used for the 'game' option, this is the flag that means we should filter messages
	 */
	FILTER(1);
	
	/**
	 * The value to send for this status
	 */
	@Getter
	private final byte value;
	
	GameBarStatus(int value) {
		this.value = (byte) value;
	}
	
	/**
	 * Finds the first game bar with the value we want
	 *
	 * @param value
	 * 		The value
	 */
	public static Optional<GameBarStatus> byValue(byte value) {
		return Arrays.stream(values()).filter(bar -> bar.value == value).findFirst();
	}
	
}
