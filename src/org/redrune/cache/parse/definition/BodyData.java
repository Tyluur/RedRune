package org.redrune.cache.parse.definition;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.stream.RSInputStream;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class BodyData {
	
	/**
	 * The parts data.
	 */
	@Getter
	private int[] partsData;
	
	/**
	 * Contains weapon data.
	 */
	@Getter
	private int[] weaponData;
	
	/**
	 * Contains shield data.
	 */
	@Getter
	private int[] shieldData;
	
	/**
	 * Something to do with weapon display.
	 */
	@Getter
	@Setter
	private int somethingWithWeaponDisplay;
	
	/**
	 * Something to do with shield display.
	 */
	@Getter
	@Setter
	private int somethingWithShieldDisplay;
	
	/**
	 * Constructs a new {@code BodyData} {@code Object}.
	 */
	public BodyData() {
		this.partsData = new int[0];
		this.somethingWithWeaponDisplay = -1;
		this.somethingWithShieldDisplay = -1;
	}
	
	/**
	 * Parses body data from the given buffer.
	 *
	 * @param buffer
	 * 		The buffer.
	 */
	public void parse(RSInputStream buffer) {
		for (; ; ) {
			byte opcode;
			try {
				opcode = (byte) buffer.readUnsignedByte();
			} catch (IOException e) {
				e.printStackTrace();
				opcode = 0;
			}
			if (opcode == 0) {
				break;
			}
			this.parse(opcode, buffer);
		}
	}
	
	/**
	 * Parses the current opcode.
	 *
	 * @param opcode
	 * 		The opcode.
	 * @param buffer
	 * 		The buffer to parse from.
	 */
	public void parse(byte opcode, RSInputStream buffer) {
		try {
			if (opcode == 1) {
				int length = buffer.readUnsignedByte();
				this.partsData = new int[length];
				for (int i = 0; i < this.partsData.length; i++) {
					this.partsData[i] = buffer.readUnsignedByte();
				}
			} else if (opcode == 3) {
				setSomethingWithShieldDisplay(buffer.readUnsignedByte());
			} else if (opcode == 4) {
				setSomethingWithWeaponDisplay(buffer.readUnsignedByte());
			} else if (opcode == 5) {
				int length = buffer.readUnsignedByte();
				this.shieldData = new int[length];
				for (int i = 0; i < this.shieldData.length; i++) {
					this.shieldData[i] = buffer.readUnsignedByte();
				}
			} else if (opcode == 6) {
				int length = buffer.readUnsignedByte();
				this.weaponData = new int[length];
				for (int i = 0; i < this.weaponData.length; i++) {
					this.weaponData[i] = buffer.readUnsignedByte();
				}
			} else {
				throw new Exception("Unknown opcode:" + opcode);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
}