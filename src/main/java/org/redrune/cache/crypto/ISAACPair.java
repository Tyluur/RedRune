package org.redrune.cache.crypto;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a ISAAC key pair, for both input and output.
 *
 * @author `Discardedx2
 */
public final class ISAACPair {
	
	/**
	 * The input cipher.
	 */
	@Getter
	@Setter
	private ISAACCipher input;
	
	/**
	 * The output cipher.
	 */
	@Getter
	@Setter
	private ISAACCipher output;
	
	/**
	 * Constructs a new {@code ISAACPair} {@code Object}.
	 *
	 * @param input
	 * 		The input cipher.
	 * @param output
	 * 		The output cipher.
	 */
	public ISAACPair(ISAACCipher input, ISAACCipher output) {
		this.input = input;
		this.output = output;
	}
}
