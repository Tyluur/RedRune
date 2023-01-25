package org.redrune.utility.backend;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public enum CreationResponse {
	
	/**
	 * No return code can describe problem.
	 */
	NONE(2),
	
	/**
	 * The successful response code
	 */
	SUCCESSFUL(2),
	
	/**
	 * The response code that marks a busy server
	 */
	BUSY_SERVER(7),
	
	/**
	 * The response code that says you can't create an account at the moment
	 */
	YOU_CANNOT_CREATE_AT_THE_MOMENT(10),
	
	/***
	 * The response code that says the account is taken
	 */
	ALREADY_TAKEN(20),
	
	/**
	 * The response code that says the email is invalid
	 */
	INVALID_EMAIL(21),
	
	/**
	 * The response code that says the password is invalid
	 */
	INVALID_PASSWORD(30),
	
	/**
	 * The response code that says we can only accept letters and numbers in the password
	 */
	NOT_LETTERS_AND_NUMBERS(31),
	
	/**
	 * The response code that says the password is too similar to the email
	 */
	TOO_SIMILAR(32);
	
	/**
	 * The value.
	 */
	@Getter
	private final byte value;
	
	/**
	 * Constructs a new {@code ReturnCodes} {@code Object}.
	 *
	 * @param value
	 * 		The value.
	 */
	CreationResponse(int value) {
		this.value = (byte) value;
	}
	
	/**
	 * Gets an optional response by the value
	 *
	 * @param value
	 * 		The value to find
	 */
	public static Optional<CreationResponse> getByValue(int value) {
		return Arrays.stream(values()).filter(response -> response.getValue() == value).findAny();
	}
}