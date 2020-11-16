package org.redrune.cache.parse.definition;

import lombok.Getter;
import lombok.Setter;

import java.nio.ByteBuffer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
public final class VarBitDefinition {
	
	/**
	 * The id of the varbit
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * The base var of the varbit
	 */
	@Getter
	@Setter
	private int baseVar;
	
	/**
	 * The start bit of the varbit
	 */
	@Getter
	@Setter
	private int startBit;
	
	/**
	 * The end bit of the varbit
	 */
	@Getter
	@Setter
	private int endBit;
	
	/**
	 * Reads the value loop of the varp
	 *
	 * @param stream
	 * 		The stream
	 */
	public void readValueLoop(ByteBuffer stream) {
		for (; ; ) {
			int opcode = stream.get() & 0xFF;
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}
	
	/**
	 * Reads the values from the stream
	 *
	 * @param stream
	 * 		The stream
	 * @param opcode
	 * 		The opcode
	 */
	private void readValues(ByteBuffer stream, int opcode) {
		if (opcode == 1) {
			baseVar = stream.getShort() & 0xFFFF;
			startBit = stream.get();
			endBit = stream.get();
		}
	}
}
