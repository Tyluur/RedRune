package org.redrune.utility.rs.input;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public enum InputType {
	/**
	 * The integer input event type. Only numbers are allowed
	 */
	INTEGER(108),
	/**
	 * The name input event type. 12 characters max
	 */
	NAME(109),
	/**
	 * The long text input event type. This can be entered for a long time
	 */
	LONG_TEXT(110);
	
	/**
	 * The script id
	 */
	@Getter
	private final int scriptId;
	
	InputType(int scriptId) {
		this.scriptId = scriptId;
	}
	
	/**
	 * Gets the attribute key name
	 */
	public String getName() {
		return "input_" + name().toLowerCase();
	}
	
}
