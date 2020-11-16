package org.redrune.cache.parse.definition;

import lombok.Getter;
import lombok.Setter;
import org.redrune.utility.tool.BufferUtils;

import java.nio.ByteBuffer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
@SuppressWarnings("unused")
public final class AnimationDefinition {
	
	private static float aFloat406;
	
	private static long aLong411 = 0L;
	
	private int anInt399 = -1;
	
	private int[] anIntArray400;
	
	private int weaponDisplayed;
	
	private int[] anIntArray402;
	
	private int anInt403;
	
	private int anInt404 = -1;
	
	private int[] anIntArray405;
	
	private boolean isTweened;
	
	private int anInt408;
	
	private boolean aBoolean409;
	
	private int anInt410;
	
	private int shieldDisplayed;
	
	private boolean aBoolean413;
	
	private boolean[] aBooleanArray414;
	
	private int[] anIntArray415;
	
	private int[] anIntArray416;
	
	private int walkingProperties;
	
	private int[][] anIntArrayArray418;
	
	private int[] anIntArray419;
	
	@Getter
	@Setter
	private int id;
	
	public AnimationDefinition() {
		anInt403 = 99;
		anInt410 = 2;
		aBoolean413 = false;
		aBoolean409 = false;
		walkingProperties = -1;
		anInt408 = 5;
		isTweened = false;
		weaponDisplayed = -1;
		shieldDisplayed = -1;
	}
	
	public final void init(ByteBuffer buff) {
		for (; ; ) {
			int opc = buff.get() & 0xff;
			if (opc == 0) {
				break;
			}
			readOpcode(opc, buff);
		}
	}
	
	private void readOpcode(int opcode, ByteBuffer buff) {
		if (opcode == 1) {
			int i_2_ = buff.getShort();
			anIntArray405 = new int[i_2_];
			for (int i_3_ = 0; i_3_ < i_2_; i_3_++) {
				anIntArray405[i_3_] = buff.getShort();
			}
			anIntArray402 = new int[i_2_];
			for (int i_4_ = 0; i_4_ < i_2_; i_4_++) {
				anIntArray402[i_4_] = buff.getShort();
			}
			for (int i_5_ = 0; i_2_ > i_5_; i_5_++) {
				anIntArray402[i_5_] = (buff.getShort() << 16) + anIntArray402[i_5_];
			}
		} else if (opcode == 2) {
			anInt399 = buff.getShort();
		} else if (opcode == 3) {
			aBooleanArray414 = new boolean[256];
			int length = buff.get() & 0xff;
			for (int i = 0; length > i; i++) {
				final byte b = (byte) (buff.get() & 0xff);
				if (b < 0 || b >= aBooleanArray414.length) {
					continue;
				}
				aBooleanArray414[b] = true;
			}
		} else if (opcode == 5) {
			anInt408 = buff.get() & 0xff;
		} else if (opcode == 6) {
			shieldDisplayed = buff.getShort();
		} else if (opcode == 7) {
			weaponDisplayed = buff.getShort();
		} else if (opcode == 8) {
			anInt403 = buff.get() & 0xff;
		} else if (opcode == 9) {
			anInt404 = buff.get() & 0xff;
		} else if (opcode == 10) {
			walkingProperties = buff.get() & 0xff;
		} else if (opcode == 11) {
			anInt410 = buff.get() & 0xff;
		} else if (opcode == 12) {
			int i_8_ = buff.get() & 0xff;
			anIntArray419 = new int[i_8_];
			for (int i_9_ = 0; i_9_ < i_8_; i_9_++) {
				anIntArray419[i_9_] = buff.getShort();
			}
			for (int i_10_ = 0; i_8_ > i_10_; i_10_++) {
				anIntArray419[i_10_] = (buff.getShort() << 16) + anIntArray419[i_10_];
			}
		} else if (opcode == 13) {
			int i_11_ = buff.getShort();
			anIntArrayArray418 = new int[i_11_][];
			for (int i_12_ = 0; i_12_ < i_11_; i_12_++) {
				int i_13_ = buff.get() & 0xff;
				if (i_13_ > 0) {
					anIntArrayArray418[i_12_] = new int[i_13_];
					anIntArrayArray418[i_12_][0] = BufferUtils.read24BitInt(buff);
					for (int i_14_ = 1; i_13_ > i_14_; i_14_++) {
						anIntArrayArray418[i_12_][i_14_] = buff.getShort();
					}
				}
			}
		} else if (opcode == 14) {
			aBoolean413 = true;
		} else if (opcode == 15) {
			isTweened = true;
		} else if (opcode == 18) {
			aBoolean409 = true;
		} else if (opcode == 19) {
			if (anIntArray416 == null) {
				anIntArray416 = (new int[anIntArrayArray418.length]);
				for (int i_15_ = 0; (i_15_ < anIntArrayArray418.length); i_15_++) {
					anIntArray416[i_15_] = 255;
				}
			}
			anIntArray416[buff.get()] = buff.get() & 0xff;
		} else if (opcode == 20) {
			if (anIntArray415 == null || anIntArray400 == null) {
				anIntArray415 = (new int[anIntArrayArray418.length]);
				anIntArray400 = (new int[anIntArrayArray418.length]);
				for (int i_16_ = 0; (anIntArrayArray418.length > i_16_); i_16_++) {
					anIntArray415[i_16_] = 256;
					anIntArray400[i_16_] = 256;
				}
			}
			int i_17_ = buff.get() & 0xff;
			anIntArray415[i_17_] = buff.getShort();
			anIntArray400[i_17_] = buff.getShort();
		}
	}
	
	public final void method544() {
		if (anInt404 == -1) {
			if (aBooleanArray414 != null) {
				anInt404 = 2;
			} else {
				anInt404 = 0;
			}
		}
		if (walkingProperties == -1) {
			if (aBooleanArray414 != null) {
				walkingProperties = 2;
			} else {
				walkingProperties = 0;
			}
		}
	}
	
	public int getEmoteTime() {
		if (anIntArray405 == null) {
			return 0;
		}
		int ms = 0;
		for (int i : anIntArray405) {
			ms += i;
		}
		return ms * 30;
	}
	
}
