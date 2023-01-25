package com.alex.loaders.npcs;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.utils.Constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public final class CacheNPCDefinitions implements Cloneable {

	private static final ConcurrentHashMap<Integer, CacheNPCDefinitions> npcDefinitions = new ConcurrentHashMap<Integer, CacheNPCDefinitions>();

	private boolean loaded;

	public int id;

	public HashMap<Integer, Object> parameters;

	public int unknownInt13;

	public int unknownInt6;

	public int unknownInt15;

	public byte respawnDirection;

	public int size = 1;

	public int[][] unknownArray3;

	public boolean unknownBoolean2;

	public int unknownInt9;

	public int unknownInt4;

	public int[] unknownArray2;

	public int unknownInt7;

	public int renderEmote;

	public boolean unknownBoolean5 = false;

	public int unknownInt20;

	public byte unknownByte1;

	public boolean unknownBoolean3;

	public int unknownInt3;

	public byte unknownByte2;

	public boolean unknownBoolean6;

	public boolean unknownBoolean4;

	public int[] originalModelColors;

	public int combatLevel;

	public byte[] unknownArray1;

	public short unknownShort1;

	public boolean unknownBoolean1;

	public int npcHeight;

	public String name;

	public int[] modifiedTextureColors;

	public byte walkMask;

	public int[] modelIds;

	public int unknownInt1;

	public int unknownInt21;

	public int unknownInt11;

	public int unknownInt17;

	public int unknownInt14;

	public int unknownInt12;

	public int unknownInt8;

	public int headIcons;

	public int unknownInt19;

	public int[] originalTextureColors;

	public int[][] anIntArrayArray882;

	public int unknownInt10;

	public int[] unknownArray4;

	public int unknownInt5;

	public int unknownInt16;

	public boolean isVisibleOnMap;

	public int[] npcChatHeads;

	public short unknownShort2;

	public String[] options;

	public int[] modifiedModelColors;

	public int unknownInt2;

	public int npcWidth;

	public int npcId;

	public int unknownInt18;

	public static final CacheNPCDefinitions getNPCDefinitions(int id, Store store) {
		CacheNPCDefinitions def = npcDefinitions.get(id);
		if (def == null) {
			def = new CacheNPCDefinitions(id);
			def.method694();
			byte[] data = store.getIndexes()[18].getFile(id >>> 134238215, id & 0x7f);
			if (data == null) {
				// System.out.println("Failed loading NPC " + id + ".");
			} else {
				def.readValueLoop(new InputStream(data));
			}
			npcDefinitions.put(id, def);
		}
		return def;
	}

	public static CacheNPCDefinitions getNPCDefinition(Store cache, int npcId) {
		return getNPCDefinition(cache, npcId, true);
	}

	public static CacheNPCDefinitions getNPCDefinition(Store cache, int npcId, boolean load) {
		return new CacheNPCDefinitions(cache, npcId, load);
	}

	public CacheNPCDefinitions(Store cache, int id, boolean load) {
		this.id = id;
		setDefaultVariableValues();
		setDefaultOptions();
		if (load) {
			loadNPCDefinition(cache);
		}
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void setDefaultOptions() {
		options = new String[] { "Talk-to", null, null, null, null };
	}

	private void setDefaultVariableValues() {
		name = "null";
		combatLevel = 0;
		isVisibleOnMap = true;
		renderEmote = -1;
		respawnDirection = (byte) 7;
		size = 1;
		unknownInt9 = -1;
		unknownInt4 = -1;
		unknownInt15 = -1;
		unknownInt7 = -1;
		unknownInt3 = 32;
		unknownInt6 = -1;
		unknownInt1 = 0;
		walkMask = (byte) 0;
		unknownInt20 = 255;
		unknownInt11 = -1;
		unknownBoolean3 = true;
		unknownShort1 = (short) 0;
		unknownInt8 = -1;
		unknownByte1 = (byte) -96;
		unknownInt12 = 0;
		unknownInt17 = -1;
		unknownBoolean4 = true;
		unknownInt21 = -1;
		unknownInt14 = -1;
		unknownInt13 = -1;
		npcHeight = 128;
		headIcons = -1;
		unknownBoolean6 = false;
		unknownInt5 = -1;
		unknownByte2 = (byte) -16;
		unknownBoolean1 = false;
		unknownInt16 = -1;
		unknownInt10 = -1;
		unknownBoolean2 = true;
		unknownInt19 = -1;
		npcWidth = 128;
		unknownShort2 = (short) 0;
		unknownInt2 = 0;
		unknownInt18 = -1;
	}

	private void loadNPCDefinition(Store cache) {
		byte[] data = cache.getIndexes()[Constants.NPC_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			//System.out.println("FAILED LOADING NPC " + id);
			return;
		}
		try {
			readOpcodeValues(new InputStream(data));
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		loaded = true;
	}

	private void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	public int getArchiveId() {
		return id >>> 134238215;
	}

	public int getFileId() {
		return 0x7f & id;
	}

	public void write(Store store) {
		store.getIndexes()[Constants.NPC_DEFINITIONS_INDEX].putFile(getArchiveId(), getFileId(), encode());
	}

	public void method694() {
		if (modelIds == null) {
			modelIds = new int[0];
		}
	}

	private void readValueLoop(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	public boolean unknownBoolean7;

	public int[] unknownArray5;

	private void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			int i = stream.readUnsignedByte();
			modelIds = new int[i];
			for (int i_66_ = 0; i_66_ < i; i_66_++) {
				modelIds[i_66_] = stream.readBigSmart();
				if ((modelIds[i_66_] ^ 0xffffffff) == -65536) {
					modelIds[i_66_] = -1;
				}
			}
		} else if (opcode == 2) {
			name = stream.readString();
		} else if (opcode == 12) {
			size = stream.readUnsignedByte();
		} else if (opcode >= 30 && opcode < 36) {
			options[opcode - 30] = stream.readString();
			if (options[opcode - 30].equalsIgnoreCase("Hidden")) {
				options[opcode - 30] = null;
			}
		} else if (opcode == 40) {
			int i = stream.readUnsignedByte();
			originalModelColors = new int[i];
			modifiedModelColors = new int[i];
			for (int i_65_ = 0; (i ^ 0xffffffff) < (i_65_ ^ 0xffffffff); i_65_++) {
				modifiedModelColors[i_65_] = stream.readUnsignedShort();
				originalModelColors[i_65_] = stream.readUnsignedShort();
			}
		}
		if (opcode == 41) {
			int i = stream.readUnsignedByte();
			originalTextureColors = new int[i];
			modifiedTextureColors = new int[i];
			for (int i_54_ = 0; (i_54_ ^ 0xffffffff) > (i ^ 0xffffffff); i_54_++) {
				originalTextureColors[i_54_] = stream.readUnsignedShort();
				modifiedTextureColors[i_54_] = stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int i = stream.readUnsignedByte();
			unknownArray1 = new byte[i];
			for (int i_55_ = 0; i > i_55_; i_55_++) {
				unknownArray1[i_55_] = (byte) stream.readByte();
			}
		} else if (opcode == 60) {
			int i = stream.readUnsignedByte();
			npcChatHeads = new int[i];
			for (int i_64_ = 0; (i_64_ ^ 0xffffffff) > (i ^ 0xffffffff); i_64_++) {
				npcChatHeads[i_64_] = stream.readBigSmart();
			}
		} else if (opcode == 93) {
			isVisibleOnMap = false;
		} else if (opcode == 95) {
			combatLevel = stream.readUnsignedShort();
		} else if (opcode == 97) {
			npcHeight = stream.readUnsignedShort();
		} else if (opcode == 98) {
			npcWidth = stream.readUnsignedShort();
		} else if (opcode == 99) {
			unknownBoolean1 = true;
		} else if (opcode == 100) {
			unknownInt1 = stream.readByte();
		} else if (opcode == 101) {
			unknownInt2 = stream.readByte() * 5;
		} else if (opcode == 102) {
			headIcons = stream.readUnsignedShort();
		} else if (opcode == 103) {
			unknownInt3 = stream.readUnsignedShort();
		} else if (opcode == 106 || opcode == 118) {
			unknownInt4 = stream.readUnsignedShort();
			if (unknownInt4 == 65535) {
				unknownInt4 = -1;
			}
			unknownInt5 = stream.readUnsignedShort();
			if (unknownInt5 == 65535) {
				unknownInt5 = -1;
			}
			int i = -1;
			if (opcode == 118) {
				i = stream.readUnsignedShort();
				if ((i ^ 0xffffffff) == -65536) {
					i = -1;
				}
			}
			int i_56_ = stream.readUnsignedByte();
			unknownArray2 = new int[2 + i_56_];
			for (int i_57_ = 0; i_56_ >= i_57_; i_57_++) {
				unknownArray2[i_57_] = stream.readUnsignedShort();
				if (unknownArray2[i_57_] == 65535) {
					unknownArray2[i_57_] = -1;
				}
			}
			unknownArray2[i_56_ - -1] = i;
		} else if (opcode == 107) {
			unknownBoolean2 = false;
		} else if (opcode == 109) {
			unknownBoolean3 = false;
		} else if (opcode == 111) {
			unknownBoolean4 = false;
		} else if (opcode == 113) {
			unknownShort1 = (short) (stream.readUnsignedShort());
			unknownShort2 = (short) (stream.readUnsignedShort());
		} else if (opcode == 114) {
			unknownByte1 = (byte) (stream.readByte());
			unknownByte2 = (byte) (stream.readByte());
		} else if (opcode == 119) {
			walkMask = (byte) (stream.readByte());
		} else if (opcode == 121) {
			unknownArray3 = (new int[modelIds.length][]);
			int i = (stream.readUnsignedByte());
			for (int i_62_ = 0; ((i_62_ ^ 0xffffffff) > (i ^ 0xffffffff)); i_62_++) {
				int i_63_ = (stream.readUnsignedByte());
				int[] is = (unknownArray3[i_63_] = (new int[3]));
				is[0] = (stream.readByte());
				is[1] = (stream.readByte());
				is[2] = (stream.readByte());
			}
		} else if (opcode == 122) {
			unknownInt6 = (stream.readBigSmart());
		} else if (opcode == 123) {
			unknownInt7 = (stream.readUnsignedShort());
		} else if (opcode == 125) {
			respawnDirection = (byte) (stream.readByte());
		} else if (opcode == 127) {
			renderEmote = (stream.readUnsignedShort());
		} else if (opcode == 128) {
			stream.readUnsignedByte();
		} else if (opcode == 134) {
			unknownInt8 = (stream.readUnsignedShort());
			if (unknownInt8 == 65535) {
				unknownInt8 = -1;
			}
			unknownInt9 = (stream.readUnsignedShort());
			if (unknownInt9 == 65535) {
				unknownInt9 = -1;
			}
			unknownInt10 = (stream.readUnsignedShort());
			if ((unknownInt10 ^ 0xffffffff) == -65536) {
				unknownInt10 = -1;
			}
			unknownInt11 = (stream.readUnsignedShort());
			if ((unknownInt11 ^ 0xffffffff) == -65536) {
				unknownInt11 = -1;
			}
			unknownInt12 = (stream.readUnsignedByte());
		} else if (opcode == 135) {
			unknownInt13 = stream.readUnsignedByte();
			unknownInt14 = stream.readUnsignedShort();
		} else if (opcode == 136) {
			unknownInt15 = stream.readUnsignedByte();
			unknownInt16 = stream.readUnsignedShort();
		} else if (opcode == 137) {
			unknownInt17 = stream.readUnsignedShort();
		} else if (opcode == 138) {
			unknownInt18 = stream.readBigSmart();
		} else if (opcode == 139) {
			unknownInt19 = stream.readBigSmart();
		} else if (opcode == 140) {
			unknownInt20 = stream.readUnsignedByte();
		} else if (opcode == 141) {
			unknownBoolean5 = true;
		} else if (opcode == 142) {
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 143) {
			unknownBoolean6 = true;
		} else if (opcode >= 150 && opcode < 155) {
			options[opcode - 150] = stream.readString();
			if (options[opcode - 150].equalsIgnoreCase("Hidden")) {
				options[opcode - 150] = null;
			}
		} else if (opcode == 155) {
			int aByte821 = stream.readByte();
			int aByte824 = stream.readByte();
			int aByte843 = stream.readByte();
			int aByte855 = stream.readByte();
		} else if (opcode == 158) {
			byte aByte833 = (byte) 1;
		} else if (opcode == 159) {
			byte aByte833 = (byte) 0;
		} else if (opcode == 160) {
			int i = stream.readUnsignedByte();
			unknownArray4 = new int[i];
			for (int i_58_ = 0; i > i_58_; i_58_++) {
				unknownArray4[i_58_] = stream.readUnsignedShort();
			}
		} else if (opcode == 162) {
			unknownBoolean7 = true;
		} else if (opcode == 163) {
			int anInt864 = stream.readUnsignedByte();
		} else if (opcode == 164) {
			int anInt848 = stream.readUnsignedShort();
			int anInt837 = stream.readUnsignedShort();
		} else if (opcode == 165) {
			int anInt847 = stream.readUnsignedByte();
		} else if (opcode == 168) {
			int anInt828 = stream.readUnsignedByte();
		} else if (opcode >= 170 && opcode < 176) {
			if (null == unknownArray5) {
				unknownArray5 = new int[6];
				Arrays.fill(unknownArray5, -1);
			}
			int i_44_ = (short) stream.readUnsignedShort();
			if (i_44_ == 65535) {
				i_44_ = -1;
			}
			unknownArray5[opcode - 170] = i_44_;
		} else if (opcode == 179) {
			stream.readUnsignedByte();
			stream.readUnsignedByte();
			stream.readUnsignedByte();
			stream.readUnsignedByte();
			stream.readUnsignedByte();
			stream.readUnsignedByte();
		} else if (opcode == 249) {
			int i = stream.readUnsignedByte();
			if (parameters == null) {
				parameters = new HashMap<Integer, Object>(i);
			}
			for (int i_60_ = 0; i > i_60_; i_60_++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value;
				if (stringInstance) {
					value = stream.readString();
				} else {
					value = stream.readInt();
				}
				parameters.put(key, value);
			}
		}
	}

	public static final void clearNPCDefinitions() {
		npcDefinitions.clear();
	}

	public CacheNPCDefinitions(int id) {
		this.id = id;
		unknownInt9 = -1;
		unknownInt4 = -1;
		unknownInt15 = -1;
		unknownInt7 = -1;
		unknownInt3 = 32;
		combatLevel = -1;
		unknownInt6 = -1;
		name = "null";
		unknownInt1 = 0;
		walkMask = (byte) 0;
		unknownInt20 = 255;
		unknownInt11 = -1;
		unknownBoolean3 = true;
		unknownShort1 = (short) 0;
		unknownInt8 = -1;
		unknownByte1 = (byte) -96;
		unknownInt12 = 0;
		unknownInt17 = -1;
		renderEmote = -1;
		respawnDirection = (byte) 7;
		unknownBoolean4 = true;
		unknownInt21 = -1;
		unknownInt14 = -1;
		unknownInt13 = -1;
		npcHeight = 128;
		headIcons = -1;
		unknownBoolean6 = false;
		unknownInt5 = -1;
		unknownByte2 = (byte) -16;
		unknownBoolean1 = false;
		isVisibleOnMap = true;
		unknownInt16 = -1;
		unknownInt10 = -1;
		unknownBoolean2 = true;
		unknownInt19 = -1;
		npcWidth = 128;
		unknownShort2 = (short) 0;
		options = new String[5];
		unknownInt2 = 0;
		unknownInt18 = -1;
	}

	public String toString() {
		return id + " - " + name;
	}

	public boolean hasMarkOption() {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("mark")) {
				return true;
			}
		}
		return false;
	}

	public boolean hasOption(String op) {
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase(op)) {
				return true;
			}
		}
		return false;
	}

	public byte getRespawnDirection() {
		return respawnDirection;
	}

	public void setRespawnDirection(byte respawnDirection) {
		this.respawnDirection = respawnDirection;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRenderEmote() {
		return renderEmote;
	}

	public void setRenderEmote(int renderEmote) {
		this.renderEmote = renderEmote;
	}

	public boolean isVisibleOnMap() {
		return isVisibleOnMap;
	}

	public void setVisibleOnMap(boolean isVisibleOnMap) {
		this.isVisibleOnMap = isVisibleOnMap;
	}

	public String[] getOptions() {
		return options;
	}

	public void setOptions(String[] options) {
		this.options = options;
	}

	public int getNpcId() {
		return npcId;
	}

	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	public boolean hasAttackOption() {
		if (id == 14899) {
			return true;
		}
		for (String option : options) {
			if (option != null && option.equalsIgnoreCase("attack")) {
				return true;
			}
		}
		return false;
	}

	public byte[] encode() {
		OutputStream stream = new OutputStream();

		stream.writeByte(1);
		stream.writeByte(modelIds.length);
		for (int index = 0; index < modelIds.length; index++) {
			stream.writeBigSmart(modelIds[index]);
		}

		if (!name.equals("null")) {
			stream.writeByte(2);
			stream.writeString(name);
		}

		if (size != 1) {
			stream.writeByte(12);
			stream.writeByte(size);
		}

		for (int index = 0; index < options.length; index++) {
			if (options[index] == null || options[index] == "Hidden") {
				continue;
			}
			stream.writeByte(30 + index);
			stream.writeString(options[index]);
		}

		if (originalModelColors != null && modifiedModelColors != null) {
			stream.writeByte(40);
			stream.writeByte(originalModelColors.length);
			for (int index = 0; index < originalModelColors.length; index++) {
				stream.writeShort(originalModelColors[index]);
				stream.writeShort(modifiedModelColors[index]);
			}
		}

		if (originalTextureColors != null && modifiedTextureColors != null) {
			stream.writeByte(41);
			stream.writeByte(originalTextureColors.length);
			for (int index = 0; index < originalTextureColors.length; index++) {
				stream.writeShort(originalTextureColors[index]);
				stream.writeShort(modifiedTextureColors[index]);
			}
		}

		if (unknownArray1 != null) {
			stream.writeByte(42);
			stream.writeByte(unknownArray1.length);
			for (int index = 0; index < unknownArray1.length; index++) {
				stream.writeByte(unknownArray1[index]);
			}
		}

		if (npcChatHeads != null) {
			stream.writeByte(60);
			stream.writeByte(npcChatHeads.length);
			for (int index = 0; index < npcChatHeads.length; index++) {
				stream.writeBigSmart(npcChatHeads[index]);
			}
		}

		if (isVisibleOnMap) {
			stream.writeByte(93);
		}

		if (combatLevel != 0) {
			stream.writeByte(95);
			stream.writeShort(combatLevel);
		}

		if (npcHeight != 0) {
			stream.writeByte(97);
			stream.writeShort(npcHeight);
		}

		if (npcWidth != 0) {
			stream.writeByte(98);
			stream.writeShort(npcWidth);
		}

		if (unknownBoolean1) {
			stream.writeByte(99);
		}

		if (unknownInt1 != 0) {
			stream.writeByte(100);
			stream.writeByte(unknownInt1);
		}

		if (unknownInt2 != 0) {
			stream.writeByte(101);
			stream.writeByte(unknownInt2 / 5);
		}

		if (headIcons != 0) {
			stream.writeByte(102);
			stream.writeShort(headIcons);
		}

		//TODO: FEW OPCODES HERE

		if (walkMask != -1) {
			stream.writeByte(119);
			stream.writeByte(walkMask);
		}

		if (respawnDirection != (byte) 7) {
			stream.writeByte(125);
			stream.writeByte(respawnDirection);
		}

		if (renderEmote != -1) {
			stream.writeByte(127);
			stream.writeShort(renderEmote);
		}

		//TODO: FEW OPCODES HERE

		if (parameters != null) {
			stream.writeByte(249);
			stream.writeByte(parameters.size());
			for (int key : parameters.keySet()) {
				Object value = parameters.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInt(key);
				if (value instanceof String) {
					stream.writeString((String) value);
				} else {
					stream.writeInt((Integer) value);
				}
			}
		}
		// end
		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	public int getCombatLevel() {
		return combatLevel;
	}

	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void resetTextureColors() {
		originalTextureColors = null;
		modifiedTextureColors = null;
	}

	public void changeTextureColor(int originalModelColor, int modifiedModelColor) {
		if (originalTextureColors != null) {
			for (int i = 0; i < originalTextureColors.length; i++) {
				if (originalTextureColors[i] == originalModelColor) {
					modifiedTextureColors[i] = modifiedModelColor;
					return;
				}
			}
			int[] newOriginalModelColors = Arrays.copyOf(originalTextureColors, originalTextureColors.length + 1);
			int[] newModifiedModelColors = Arrays.copyOf(modifiedTextureColors, modifiedTextureColors.length + 1);
			newOriginalModelColors[newOriginalModelColors.length - 1] = originalModelColor;
			newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
			originalTextureColors = newOriginalModelColors;
			modifiedTextureColors = newModifiedModelColors;
		} else {
			originalTextureColors = new int[] { originalModelColor };
			modifiedTextureColors = new int[] { modifiedModelColor };
		}
	}

	public void resetModelColors() {
		originalModelColors = null;
		modifiedModelColors = null;
	}

	public void changeModelColor(int originalModelColor, int modifiedModelColor) {
		if (originalModelColors != null) {
			for (int i = 0; i < originalModelColors.length; i++) {
				if (originalModelColors[i] == originalModelColor) {
					modifiedModelColors[i] = modifiedModelColor;
					return;
				}
			}
			int[] newOriginalModelColors = Arrays.copyOf(originalModelColors, originalModelColors.length + 1);
			int[] newModifiedModelColors = Arrays.copyOf(modifiedModelColors, modifiedModelColors.length + 1);
			newOriginalModelColors[newOriginalModelColors.length - 1] = originalModelColor;
			newModifiedModelColors[newModifiedModelColors.length - 1] = modifiedModelColor;
			originalModelColors = newOriginalModelColors;
			modifiedModelColors = newModifiedModelColors;
		} else {
			originalModelColors = new int[] { originalModelColor };
			modifiedModelColors = new int[] { modifiedModelColor };
		}
	}
}
