package org.redrune.cache.parse.definition;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.stream.RSInputStream;

import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
@SuppressWarnings("unused")
public final class ObjectDefinition {
	
	@Getter
	@Setter
	private int id;
	
	@Getter
	private String name;
	
	@Getter
	private String[] options;
	
	@Getter
	private int sizeX;
	
	@Getter
	private int sizeY;
	
	@Getter
	private int walkBitFlag;
	
	@Getter
	@Setter
	private int actionCount;
	
	@Getter
	@Setter
	private boolean projectileClipped;
	
	@Getter
	@Setter
	private boolean notClipped;
	
	private short[] modifiedColors;
	
	private int configFileId;
	
	private int configId;
	
	private short[] originalColors;
	
	private int[] childrenIds;
	
	private int[] anIntArray3833 = null;
	
	private int anInt3835;
	
	private int anInt3838 = -1;
	
	private boolean aBoolean3839;
	
	private int anInt3844;
	
	private boolean aBoolean3845;
	
	private int anInt3850;
	
	private int anInt3851;
	
	private int anInt3855;
	
	private int anInt3857;
	
	private int[] anIntArray3859;
	
	private int anInt3860;
	
	private int anInt3865;
	
	private boolean aBoolean3866;
	
	private boolean aBoolean3867;
	
	private boolean aBoolean3870;
	
	private boolean aBoolean3872;
	
	private boolean aBoolean3873;
	
	private int anInt3876;
	
	private int anInt3892;
	
	private boolean aBoolean3894;
	
	private boolean aBoolean3895;
	
	private int anInt3896;
	
	private int anInt3900;
	
	private int anInt3904;
	
	private int anInt3905;
	
	private boolean aBoolean3906;
	
	private int[] anIntArray3908;
	
	private int anInt3913;
	
	private int anInt3921;
	
	private boolean aBoolean3923;
	
	private boolean aBoolean3924;
	
	private boolean secondBool;
	
	private boolean aBoolean3853;
	
	private byte[] aByteArray3858;
	
	private int[] anIntArray3869;
	
	private int thirdInt;
	
	private int anInt3881;
	
	private boolean aBoolean3891;
	
	private int secondInt;
	
	private byte[] aByteArray3899;
	
	private int[][] anIntArrayArray3916;
	
	private short[] aShortArray3919;
	
	private short[] aShortArray3920;
	
	private byte aByte3912;
	
	public ObjectDefinition() {
		anInt3835 = -1;
		anInt3860 = -1;
		configFileId = -1;
		aBoolean3866 = false;
		anInt3851 = -1;
		anInt3865 = 255;
		aBoolean3845 = false;
		aBoolean3867 = false;
		anInt3850 = 0;
		anInt3844 = -1;
		anInt3881 = 0;
		anInt3857 = -1;
		aBoolean3872 = true;
		options = new String[5];
		aBoolean3839 = false;
		anIntArray3869 = null;
		sizeX = 1;
		thirdInt = -1;
		projectileClipped = true;
		aBoolean3895 = true;
		aBoolean3870 = false;
		aBoolean3853 = true;
		secondBool = false;
		actionCount = 2;
		anInt3855 = -1;
		anInt3904 = 0;
		sizeY = 1;
		anInt3876 = -1;
		notClipped = false;
		aBoolean3891 = false;
		anInt3905 = 0;
		name = "null";
		anInt3913 = -1;
		aBoolean3906 = false;
		aBoolean3873 = false;
		anInt3900 = 0;
		secondInt = -1;
		aBoolean3894 = false;
		aByte3912 = (byte) 0;
		anInt3921 = 0;
		configId = -1;
		walkBitFlag = 0;
		anInt3892 = 64;
		aBoolean3923 = false;
		aBoolean3924 = false;
	}
	
	public void readValueLoop(RSInputStream stream) throws IOException {
		for (; ; ) {
			int opcode = stream.readByte() & 0xff;
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}
	
	private void readValues(RSInputStream stream, int opcode) throws IOException {
		if (opcode == 1 || opcode == 5) {
			int i_73_ = stream.readUnsignedByte();
			anIntArrayArray3916 = new int[i_73_][];
			aByteArray3899 = new byte[i_73_];
			for (int i_74_ = 0; i_74_ < i_73_; i_74_++) {
				aByteArray3899[i_74_] = stream.readByte();
				int i_75_ = stream.readUnsignedByte();
				anIntArrayArray3916[i_74_] = new int[i_75_];
				for (int i_76_ = 0; i_75_ > i_76_; i_76_++) {
					anIntArrayArray3916[i_74_][i_76_] = stream.readUnsignedShort();
				}
			}
			if (opcode == 5) {
				method3297(stream);
			}
		} else if (opcode == 2) {
			name = stream.readRS2String();
		} else if (opcode == 14) {
			sizeX = stream.readUnsignedByte();
		} else if (opcode == 15) {
			sizeY = stream.readUnsignedByte();
		} else if (opcode == 17) {
			projectileClipped = false;
			actionCount = 0;
		} else if (opcode == 18) {
			projectileClipped = false;
		} else if (opcode == 19) {
			secondInt = stream.readUnsignedByte();
		} else if (opcode == 21) {
			aByte3912 = (byte) 1;
		} else if (opcode == 22) {
			aBoolean3867 = true;
		} else if (opcode == 23) {
			thirdInt = 1;
		} else if (opcode == 24) {
			anInt3876 = stream.readUnsignedShort();
			if (anInt3876 == 65535) {
				anInt3876 = -1;
			}
		} else if (opcode == 27) {
			actionCount = 1;
		} else if (opcode == 28) {
			anInt3892 = (stream.readUnsignedByte() << 2);
		} else if (opcode == 29) {
			stream.readByte();
		} else if (opcode == 39) {
			stream.readByte();
		} else if (opcode >= 30 && opcode < 35) {
			options[opcode - 30] = stream.readRS2String();
		} else if (opcode == 40) {
			int length = (stream.readUnsignedByte());
			originalColors = new short[length];
			modifiedColors = new short[length];
			for (int i = 0; length > i; i++) {
				originalColors[i] = (short) (stream.readUnsignedShort());
				modifiedColors[i] = (short) (stream.readUnsignedShort());
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			aShortArray3920 = new short[length];
			aShortArray3919 = new short[length];
			for (int i = 0; length > i; i++) {
				aShortArray3920[i] = (short) stream.readUnsignedShort();
				aShortArray3919[i] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i = stream.readUnsignedByte();
			aByteArray3858 = new byte[i];
			for (int i_70_ = 0; i_70_ < i; i_70_++) {
				aByteArray3858[i_70_] = stream.readByte();
			}
		} else if (opcode == 62) {
			aBoolean3839 = true;
		} else if (opcode == 64) {
			aBoolean3872 = false;
		} else if (opcode == 65) {
			stream.readUnsignedShort();
		} else if (opcode == 66) {
			stream.readUnsignedShort();
		} else if (opcode == 67) {
			stream.readUnsignedShort();
		} else if (opcode == 69) {
			walkBitFlag = stream.readUnsignedByte();
		} else if (opcode == 70) {
			stream.readShort();
		} else if (opcode == 71) {
			stream.readShort();
		} else if (opcode == 72) {
			stream.readShort();
		} else if (opcode == 73) {
			secondBool = true;
		} else if (opcode == 74) {
			notClipped = true;
		} else if (opcode == 75) {
			anInt3855 = stream.readUnsignedByte();
		} else if (opcode == 77 || opcode == 92) {
			configFileId = stream.readUnsignedShort();
			if (configFileId == 65535) {
				configFileId = -1;
			}
			configId = stream.readUnsignedShort();
			if (configId == 65535) {
				configId = -1;
			}
			int i_66_ = -1;
			if (opcode == 92) {
				i_66_ = stream.readUnsignedShort();
				if (i_66_ == 65535) {
					i_66_ = -1;
				}
			}
			int i_67_ = stream.readUnsignedByte();
			childrenIds = new int[i_67_ + 2];
			for (int i_68_ = 0; i_67_ >= i_68_; i_68_++) {
				childrenIds[i_68_] = stream.readUnsignedShort();
				if (childrenIds[i_68_] == 65535) {
					childrenIds[i_68_] = -1;
				}
			}
			childrenIds[i_67_ + 1] = i_66_;
		} else if (opcode == 78) {
			anInt3860 = stream.readUnsignedShort();
			anInt3904 = stream.readUnsignedByte();
		} else if (opcode == 79) {
			anInt3900 = stream.readUnsignedShort();
			anInt3905 = stream.readUnsignedShort();
			anInt3904 = stream.readUnsignedByte();
			int i_64_ = stream.readUnsignedByte();
			anIntArray3859 = new int[i_64_];
			for (int i_65_ = 0; i_65_ < i_64_; i_65_++) {
				anIntArray3859[i_65_] = stream.readUnsignedShort();
			}
		} else if (opcode == 81) {
			aByte3912 = (byte) 2;
			stream.readUnsignedByte();
		} else if (opcode == 82) {
			aBoolean3891 = true;
		} else if (opcode == 88) {
			aBoolean3853 = false;
		} else if (opcode == 89) {
			aBoolean3895 = false;
		} else if (opcode == 90) {
			aBoolean3870 = true;
		} else if (opcode == 91) {
			aBoolean3873 = true;
		} else if (opcode == 93) {
			aByte3912 = (byte) 3;
			stream.readUnsignedShort();
		} else if (opcode == 94) {
			aByte3912 = (byte) 4;
		} else if (opcode == 95) {
			aByte3912 = (byte) 5;
			stream.readShort();
		} else if (opcode == 96) {
			aBoolean3924 = true;
		} else if (opcode == 97) {
			aBoolean3866 = true;
		} else if (opcode == 98) {
			aBoolean3923 = true;
		} else if (opcode == 99) {
			anInt3857 = stream.readUnsignedByte();
			anInt3835 = stream.readUnsignedShort();
		} else if (opcode == 100) {
			anInt3844 = stream.readUnsignedByte();
			anInt3913 = stream.readUnsignedShort();
		} else if (opcode == 101) {
			anInt3850 = stream.readUnsignedByte();
		} else if (opcode == 102) {
			anInt3838 = stream.readUnsignedShort();
		} else if (opcode == 103) {
			thirdInt = 0;
		} else if (opcode == 104) {
			anInt3865 = stream.readUnsignedByte();
		} else if (opcode == 105) {
			aBoolean3906 = true;
		} else if (opcode == 106) {
			int i_55_ = stream.readUnsignedByte();
			anIntArray3869 = new int[i_55_];
			anIntArray3833 = new int[i_55_];
			for (int i_56_ = 0; i_56_ < i_55_; i_56_++) {
				anIntArray3833[i_56_] = stream.readUnsignedShort();
				int i_57_ = stream.readUnsignedByte();
				anIntArray3869[i_56_] = i_57_;
				anInt3881 += i_57_;
			}
		} else if (opcode == 107) {
			anInt3851 = stream.readUnsignedShort();
		} else if (opcode >= 150 && opcode < 155) {
			options[opcode - 150] = stream.readRS2String();
		} else if (opcode == 160) {
			int i_62_ = stream.readUnsignedByte();
			anIntArray3908 = new int[i_62_];
			for (int i_63_ = 0; i_62_ > i_63_; i_63_++) {
				anIntArray3908[i_63_] = stream.readUnsignedShort();
			}
		} else if (opcode == 162) {
			aByte3912 = (byte) 3;
			stream.readInt();
		} else if (opcode == 163) {
			stream.readByte();
			stream.readByte();
			stream.readByte();
			stream.readByte();
		} else if (opcode == 164) {
			stream.readShort();
		} else if (opcode == 165) {
			stream.readShort();
		} else if (opcode == 166) {
			stream.readShort();
		} else if (opcode == 167) {
			anInt3921 = stream.readUnsignedShort();
		} else if (opcode == 168) {
			aBoolean3894 = true;
		} else if (opcode == 169) {
			aBoolean3845 = true;
		} else if (opcode == 170) {
			stream.readUnsignedSmart();
		} else if (opcode == 171) {
			stream.readUnsignedSmart();
		} else if (opcode == 173) {
			stream.readUnsignedShort();
			stream.readUnsignedShort();
		} else if (opcode == 177) {
			// something
			// =
			// true
		} else if (opcode == 178) {
			stream.readUnsignedByte();
		} else if (opcode == 249) {
			int i_58_ = stream.readUnsignedByte();
			for (int i_60_ = 0; i_60_ < i_58_; i_60_++) {
				boolean bool = stream.readUnsignedByte() == 1;
				stream.read24BitInt();
				// int
				// length
				// =
				// stream.read24BitInt();
				if (!bool) {
					stream.readInt();
				} else {
					stream.readRS2String();
				}
			}
		}
	}
	
	private void method3297(RSInputStream stream) throws IOException {
		int length = stream.readUnsignedByte();
		for (int index = 0; index < length; index++) {
			stream.skip(1);
			stream.skip(stream.readUnsignedByte() * 2);
		}
	}
	
	public final void method3287() {
		if (secondInt == -1) {
			secondInt = 0;
			if (aByteArray3899 != null && aByteArray3899.length == 1 && aByteArray3899[0] == 10) {
				secondInt = 1;
			}
			for (int i_13_ = 0; i_13_ < 5; i_13_++) {
				if (options[i_13_] != null) {
					secondInt = 1;
					break;
				}
			}
		}
		if (anInt3855 == -1) {
			anInt3855 = actionCount != 0 ? 1 : 0;
		}
	}
	
	public byte getAByte3912() {
		return aByte3912;
	}
	
	public boolean hasOption(int index, String option) {
		return options != null && options.length > index && options[index] != null && options[index].equalsIgnoreCase(option);
	}
	
	public boolean hasOption(String option) {
		if (options == null) {
			return false;
		}
		for (String optionText : options) {
			if (optionText == null) {
				continue;
			}
			if (optionText.equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
}
