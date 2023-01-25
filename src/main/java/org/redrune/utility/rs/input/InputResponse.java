package org.redrune.utility.rs.input;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
@FunctionalInterface
public interface InputResponse {
	
	/**
	 * Runs the response
	 *
	 * @param input
	 * 		The input characters
	 */
	void run(String input);
	
	/**
	 * Gets the {@code Integer} value of the input from the {@code String} {@code Object} input
	 *
	 * @param input
	 * 		The string of text input
	 */
	static int getInput(String input) {
		try {
			return Integer.valueOf(input);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
