package com.alex.loaders.items;

import com.alex.io.InputStream;
import com.alex.io.OutputStream;
import com.alex.store.Store;
import com.alex.utils.Constants;
import lombok.Setter;

import java.util.HashMap;

@SuppressWarnings("unused")
public final class CacheItemDefinitions {

	private int id;

	private int modelId;

	private String name;

	private int modelZoom;

	private int modelRotation1;

	private int modelRotation2;

	private int modelOffset1;

	private int modelOffset2;

	@Setter
	private int stackable;

	private int value;

	private boolean membersOnly;

	private int maleEquip1;

	private int femaleEquip1;

	private int maleEquip2;

	private int femaleEquip2;

	private String[] groundOptions;

	private String[] inventoryOptions;

	private boolean showInGrandExchange;

	private int[] originalModelColors;

	private int[] modifiedModelColors;

	private short[] originalTextureColors;

	private short[] modifiedTextureColors;

	private byte[] recolourPallete;

	private int[] unknownArray2;

	private int maleEquipModelId3;

	private int femaleEquipModelId3;

	private int maleDialogueModel;

	private int femaleDialogueModel;

	private int maleDialogueHat;

	private int femaleDialogueHat;

	private int unknownValue3;

	private int rotationZoom;

	private int dummyItem;

	private int certId;

	private int certTemplateId;

	private int[] stackIds;

	private int[] stackAmounts;

	private int modelVerticesX;

	private int modelVerticesY;

	private int modelVerticesZ;

	private int modelLighting;

	private int modelShadowing;

	private int teamId;

	private int lendId;

	private int lendTemplateId;

	private int unknownInt12;

	private int unknownInt13;

	private int unknownInt14;

	private int unknownInt15;

	private int unknownInt16;

	private int unknownInt17;

	private int unknownInt18;

	private int unknownInt19;

	private int unknownInt20;

	private int unknownInt21;

	private int unknownInt22;

	private int unknownInt23;

	private int unknownInt24;

	private int unknownInt25;

	private int equipSlot;

	private int equipType;

	private int unknownValue1;

	private int unknownValue2;

	private HashMap<Integer, Object> clientScriptData;

	public static CacheItemDefinitions getItemDefinition(Store cache, int itemId) {
		return new CacheItemDefinitions(cache, itemId);
	}

	public CacheItemDefinitions(Store store, int id) {
		this.id = id;
		setDefaultsVariableValues();
		setDefaultOptions();
		loadItemDefinitions(store);
	}

	private final void readOpcodeValues(InputStream stream) {
		while (true) {
			int opcode = stream.readUnsignedByte();
			if (opcode == 0) {
				break;
			}
			readValues(stream, opcode);
		}
	}

	private final void loadItemDefinitions(Store store) {
		byte[] data = store.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			return;
		}
		readOpcodeValues(new InputStream(data));
	}

	public void printData(Store store) {
		byte[] data = store.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].getFile(getArchiveId(), getFileId());
		if (data == null) {
			return;
		}
		//System.out.println(Arrays.toString(data));
		//System.out.println(Arrays.toString(encode()));
	}

	public void write(Store store, boolean rewriteTable) {
		store.getIndexes()[Constants.ITEM_DEFINITIONS_INDEX].putFile(getArchiveId(), getFileId(), Constants.GZIP_COMPRESSION, encode(), null, rewriteTable, true, -1, -1);
	}

	public byte[] encode() {
		OutputStream stream = new OutputStream();

		stream.writeByte(1);
		stream.writeShort(modelId);

		if (!getName().equals("null") && certTemplateId == -1) {
			stream.writeByte(2);
			stream.writeString(getName());
		}

		if (modelZoom != 2000) {
			stream.writeByte(4);
			stream.writeShort(modelZoom);
		}

		if (modelRotation1 != 0) {
			stream.writeByte(5);
			stream.writeShort(modelRotation1);
		}

		if (modelRotation2 != 0) {
			stream.writeByte(6);
			stream.writeShort(modelRotation2);
		}

		if (modelOffset1 != 0) {
			stream.writeByte(7);
			int value = modelOffset1;
			if (value < 0) {
				value += 65536;
			}
			stream.writeShort(value);
		}

		if (modelOffset2 != 0) {
			stream.writeByte(8);
			int value = modelOffset2;
			if (value < 0) {
				value += 65536;
			}
			stream.writeShort(value);
		}

		if (stackable >= 1 && certTemplateId == -1) {
			stream.writeByte(11);
		}

		if (value != 1 && lendTemplateId == -1) {
			stream.writeByte(12);
			stream.writeInt(value);
		}
		
		// TODO:
		/*
		int[] equipInfo = null;
		try {
			equipInfo = CacheFixer.getEquipInfo(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int equipSlot = equipInfo[0];
		if (equipSlot != -1) {
			stream.writeByte(13);
			stream.writeByte(equipSlot);
		}

		int equipType = equipInfo[1];
		if (equipType != -1) {
			stream.writeByte(14);
			stream.writeByte(equipType);
		}*/

		if (membersOnly && certTemplateId == -1) {
			stream.writeByte(16);
		}

		boolean isExchangeable = true; // TODO: /*ItemProperties.isExchangeable(id);*/
		if (isExchangeable) {
			System.out.println(name + " is exchangeable.");
		}
		stream.writeByte(17);
		stream.writeByte(isExchangeable ? 1 : 0);

		if (unknownInt18 != -1) {
			stream.writeByte(18);
			stream.writeShort(unknownInt18);
		}

		if (maleEquip1 != -1) {
			stream.writeByte(23);
			stream.writeShort(maleEquip1);
		}

		if (maleEquip2 != -1) {
			stream.writeByte(24);
			stream.writeShort(maleEquip2);
		}

		if (femaleEquip1 != -1) {
			stream.writeByte(25);
			stream.writeShort(femaleEquip1);
		}

		if (femaleEquip2 != -1) {
			stream.writeByte(26);
			stream.writeShort(femaleEquip2);
		}

		for (int index = 0; index < 4; index++) {
			if (groundOptions[index] == null || (index == 2 && groundOptions[index].equals("take"))) {
				continue;
			}
			stream.writeByte(30 + index);
			stream.writeString(groundOptions[index]);
		}

		for (int index = 0; index < 4; index++) {
			if (getInventoryOptions()[index] == null || (index == 4 && getInventoryOptions()[index].equals("drop"))) {
				continue;
			}
			stream.writeByte(35 + index);
			stream.writeString(getInventoryOptions()[index]);
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

		if (recolourPallete != null) {
			stream.writeByte(42);
			stream.writeByte(recolourPallete.length);
			for (byte element : recolourPallete) {
				stream.writeByte(element);
			}
		}
		if (isShowInGrandExchange()) {
			stream.writeByte(65);
		}

		if (maleEquipModelId3 != -1) {
			stream.writeByte(78);
			stream.writeShort(maleEquipModelId3);
		}

		if (femaleEquipModelId3 != -1) {
			stream.writeByte(79);
			stream.writeShort(femaleEquipModelId3);
		}

		if (maleDialogueModel != -1) {
			stream.writeByte(90);
			stream.writeShort(maleDialogueModel);
		}

		if (femaleDialogueModel != -1) {
			stream.writeByte(91);
			stream.writeShort(femaleDialogueModel);
		}

		if (maleDialogueHat != -1) {
			stream.writeByte(92);
			stream.writeShort(maleDialogueHat);
		}

		if (femaleDialogueHat != -1) {
			stream.writeByte(93);
			stream.writeShort(femaleDialogueHat);
		}

		if (rotationZoom != 0) {
			stream.writeByte(95);
			stream.writeShort(rotationZoom);
		}

		if (dummyItem != 0) {
			stream.writeByte(96);
			stream.writeByte(dummyItem);
		}

		if (certId != -1) {
			stream.writeByte(97);
			stream.writeShort(certId);
		}

		if (certTemplateId != -1) {
			stream.writeByte(98);
			stream.writeShort(certTemplateId);
		}

		if (stackIds != null && stackAmounts != null) {
			for (int index = 0; index < stackIds.length; index++) {
				if (stackIds[index] == 0 && stackAmounts[index] == 0) {
					continue;
				}
				stream.writeByte(100 + index);
				stream.writeShort(stackIds[index]);
				stream.writeShort(stackAmounts[index]);
			}
		}

		if (modelVerticesX != 128) {
			stream.writeByte(110);
			stream.writeShort(modelVerticesX);
		}

		if (modelVerticesY != 128) {
			stream.writeByte(111);
			stream.writeShort(modelVerticesY);
		}

		if (modelVerticesZ != 128) {
			stream.writeByte(112);
			stream.writeShort(modelVerticesZ);
		}

		if (modelLighting != 0) {
			stream.writeByte(113);
			stream.writeByte(modelLighting);
		}

		if (modelShadowing != 0) {
			stream.writeByte(114);
			stream.writeByte(modelShadowing);
		}

		if (teamId != 0) {
			stream.writeByte(115);
			stream.writeByte(teamId);
		}

		if (lendId != -1) {
			stream.writeByte(121);
			stream.writeShort(lendId);
		}

		if (lendTemplateId != -1) {
			stream.writeByte(122);
			stream.writeShort(lendTemplateId);
		}

		if (unknownInt12 != 0 && unknownInt13 != 0 && unknownInt14 != 0) {
			stream.writeByte(125);
			stream.writeByte(unknownInt12 >> 2);
			stream.writeByte(unknownInt13 >> 2);
			stream.writeByte(unknownInt14 >> 2);
		}

		if (unknownInt15 != 0 && unknownInt16 != 0 && unknownInt17 != 0) {
			stream.writeByte(126);
			stream.writeByte(unknownInt15 >> 2);
			stream.writeByte(unknownInt16 >> 2);
			stream.writeByte(unknownInt17 >> 2);
		}
		if (unknownInt19 != -1 && unknownInt18 != -1) {
			stream.writeByte(127);
			stream.writeByte(unknownInt18);
			stream.writeShort(unknownInt19);
		}

		if (unknownInt20 != -1 && unknownInt21 != -1) {
			stream.writeByte(128);
			stream.writeByte(unknownInt20);
			stream.writeShort(unknownInt21);
		}

		if (unknownInt24 != -1 && unknownInt25 != -1) {
			stream.writeByte(129);
			stream.writeByte(unknownInt24);
			stream.writeShort(unknownInt25);
		}
		if (unknownInt22 != -1 && unknownInt23 != -1) {
			stream.writeByte(130);
			stream.writeByte(unknownInt22);
			stream.writeShort(unknownInt23);
		}

		if (unknownArray2 != null) {
			stream.writeByte(132);
			stream.writeByte(unknownArray2.length);
			for (int element : unknownArray2) {
				stream.writeShort(element);
			}
		}

		if (clientScriptData != null) {
			stream.writeByte(249);
			stream.writeByte(clientScriptData.size());
			for (int key : clientScriptData.keySet()) {
				Object value = clientScriptData.get(key);
				stream.writeByte(value instanceof String ? 1 : 0);
				stream.write24BitInteger(key);
				if (value instanceof String) {
					stream.writeString((String) value);
				} else {
					stream.writeInt((Integer) value);
				}
			}
		}

		if (unknownValue3 != 0) {
			stream.writeByte(134);
			stream.writeByte(unknownValue3);
		}

		if (unknownValue2 != -1) {
			stream.writeByte(139);
			stream.writeShort(unknownValue2);
		}

		if (unknownValue1 != -1) {
			stream.writeByte(140);
			stream.writeShort(unknownValue1);
		}

		// end
		stream.writeByte(0);

		byte[] data = new byte[stream.getOffset()];
		stream.setOffset(0);
		stream.getBytes(data, 0, data.length);
		return data;
	}

	public int getArchiveId() {
		return id >>> 8;
	}

	public int getFileId() {
		return 0xff & id;
	}

	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		setInventoryOptions(new String[] { null, null, null, null, "drop" });
	}

	public void setDefaultsVariableValues() {
		maleEquip1 = -1;
		unknownInt24 = -1;
		maleEquip2 = -1;
		rotationZoom = 0;
		lendTemplateId = -1;
		unknownInt25 = -1;
		unknownValue2 = -1;
		maleEquipModelId3 = -1;
		modelLighting = 0;
		modelShadowing = 0;
		femaleDialogueModel = -1;
		modelZoom = 2000;
		unknownInt18 = -1;
		teamId = 0;
		membersOnly = false;
		modelVerticesY = 128;
		modelOffset1 = 0;
		setName("null");
		unknownInt23 = -1;
		modelVerticesX = 128;
		maleDialogueHat = -1;
		femaleDialogueHat = -1;
		unknownInt18 = -1;
		unknownInt20 = -1;
		unknownInt21 = -1;
		modelRotation2 = 0;
		unknownInt14 = 0;
		unknownInt19 = -1;
		unknownInt22 = -1;
		unknownInt16 = 0;
		femaleEquip2 = -1;
		modelOffset2 = 0;
		unknownInt15 = 0;
		maleDialogueModel = -1;
		unknownValue3 = 0;
		stackable = 0;
		modelVerticesZ = 128;
		femaleEquipModelId3 = -1;
		certTemplateId = -1;
		certId = -1;
		value = 1;
		dummyItem = 0;
		unknownValue1 = -1;
		modelRotation1 = 0;
		lendId = -1;
		femaleEquip1 = -1;
		unknownInt13 = 0;
		unknownInt17 = 0;
		unknownInt12 = 0;
		equipSlot = -1;
		equipType = -1;
		setShowInGrandExchange(false);
	}

	private final void readValues(InputStream stream, int opcode) {
		if (opcode == 1) {
			modelId = stream.readUnsignedShort();
		} else if (opcode == 2) {
			setName(stream.readString());
		} else if (opcode == 4) {
			modelZoom = stream.readUnsignedShort();
		} else if (opcode == 5) {
			modelRotation1 = stream.readUnsignedShort();
		} else if (opcode == 6) {
			modelRotation2 = stream.readUnsignedShort();
		} else if (opcode == 7) {
			modelOffset1 = stream.readUnsignedShort();
			if (modelOffset1 > 32767) {
				modelOffset1 -= 65536;
			}
		} else if (opcode == 8) {
			modelOffset2 = stream.readUnsignedShort();
			if (modelOffset2 > 32767) {
				modelOffset2 -= 65536;
			}
		} else if (opcode == 11) {
			stackable = 1;
		} else if (opcode == 12) {
			value = stream.readInt();
		} else if (opcode == 13) {
			equipSlot = stream.readUnsignedByte();
		} else if (opcode == 14) {
			equipType = stream.readUnsignedByte();
		} else if (opcode == 15) {
			stream.readUnsignedByte();
		} else if (opcode == 16) {
			membersOnly = true;
		} else if (opcode == 17) {
			stream.readUnsignedByte();
		} else if (opcode == 18) {
			unknownInt18 = stream.readUnsignedShort();
		} else if (opcode == 23) {
			maleEquip1 = stream.readUnsignedShort();
		} else if (opcode == 24) {
			maleEquip2 = stream.readUnsignedShort();
		} else if (opcode == 25) {
			femaleEquip1 = stream.readUnsignedShort();
		} else if (opcode == 26) {
			femaleEquip2 = stream.readUnsignedShort();
		} else if (opcode >= 30 && opcode < 35) {
			groundOptions[opcode - 30] = stream.readString();
		} else if (opcode >= 35 && opcode < 40) {
			getInventoryOptions()[opcode - 35] = stream.readString();
		} else if (opcode == 40) {
			int length = stream.readUnsignedByte();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = stream.readUnsignedShort();
				modifiedModelColors[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 41) {
			int length = stream.readUnsignedByte();
			originalTextureColors = new short[length];
			modifiedTextureColors = new short[length];
			for (int index = 0; index < length; index++) {
				originalTextureColors[index] = (short) stream.readUnsignedShort();
				modifiedTextureColors[index] = (short) stream.readUnsignedShort();
			}
		} else if (opcode == 42) {
			int length = stream.readUnsignedByte();
			recolourPallete = new byte[length];
			for (int index = 0; index < length; index++) {
				recolourPallete[index] = (byte) stream.readByte();
			}
		} else if (opcode == 65) {
			setShowInGrandExchange(true);
		} else if (opcode == 78) {
			maleEquipModelId3 = stream.readUnsignedShort();
		} else if (opcode == 79) {
			femaleEquipModelId3 = stream.readUnsignedShort();
		} else if (opcode == 90) {
			maleDialogueModel = stream.readUnsignedShort();
		} else if (opcode == 91) {
			femaleDialogueModel = stream.readUnsignedShort();
		} else if (opcode == 92) {
			maleDialogueHat = stream.readUnsignedShort();
		} else if (opcode == 93) {
			femaleDialogueHat = stream.readUnsignedShort();
		} else if (opcode == 95) {
			rotationZoom = stream.readUnsignedShort();
		} else if (opcode == 96) {
			dummyItem = stream.readUnsignedByte();
		} else if (opcode == 97) {
			certId = stream.readUnsignedShort();
		} else if (opcode == 98) {
			certTemplateId = stream.readUnsignedShort();
		} else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = stream.readUnsignedShort();
			stackAmounts[opcode - 100] = stream.readUnsignedShort();
		} else if (opcode == 110) {
			modelVerticesX = stream.readUnsignedShort();
		} else if (opcode == 111) {
			modelVerticesY = stream.readUnsignedShort();
		} else if (opcode == 112) {
			modelVerticesZ = stream.readUnsignedShort();
		} else if (opcode == 113) {
			modelLighting = stream.readByte();
		} else if (opcode == 114) {
			modelShadowing = stream.readByte() * 5;
		} else if (opcode == 115) {
			teamId = stream.readUnsignedByte();
		} else if (opcode == 121) {
			lendId = stream.readUnsignedShort();
		} else if (opcode == 122) {
			lendTemplateId = stream.readUnsignedShort();
		} else if (opcode == 125) {
			unknownInt12 = stream.readByte() << 2;
			unknownInt13 = stream.readByte() << 2;
			unknownInt14 = stream.readByte() << 2;
		} else if (opcode == 126) {
			unknownInt15 = stream.readByte() << 2;
			unknownInt16 = stream.readByte() << 2;
			unknownInt17 = stream.readByte() << 2;
		} else if (opcode == 127) {
			unknownInt18 = stream.readUnsignedByte();
			unknownInt19 = stream.readUnsignedShort();
		} else if (opcode == 128) {
			unknownInt20 = stream.readUnsignedByte();
			unknownInt21 = stream.readUnsignedShort();
		} else if (opcode == 129) {
			unknownInt24 = stream.readUnsignedByte();
			unknownInt25 = stream.readUnsignedShort();
		} else if (opcode == 130) {
			unknownInt22 = stream.readUnsignedByte();
			unknownInt23 = stream.readUnsignedShort();
		} else if (opcode == 132) {
			int length = stream.readUnsignedByte();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++) {
				unknownArray2[index] = stream.readUnsignedShort();
			}
		} else if (opcode == 134) {
			unknownValue3 = stream.readUnsignedByte();
		} else if (opcode == 139) {
			unknownValue2 = stream.readUnsignedShort();
		} else if (opcode == 140) {
			unknownValue1 = stream.readUnsignedShort();
		} else if (opcode == 249) {
			int length = stream.readUnsignedByte();
			if (clientScriptData == null) {
				clientScriptData = new HashMap<Integer, Object>(length);
			}
			for (int index = 0; index < length; index++) {
				boolean stringInstance = stream.readUnsignedByte() == 1;
				int key = stream.read24BitInt();
				Object value = stringInstance ? stream.readString() : stream.readInt();
				clientScriptData.put(key, value);
			}
		} else {
			throw new RuntimeException("MISSING OPCODE " + opcode + " FOR ITEM " + id);
		}
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 * 		the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the inventoryOptions
	 */
	public String[] getInventoryOptions() {
		return inventoryOptions;
	}

	/**
	 * @param inventoryOptions
	 * 		the inventoryOptions to set
	 */
	public void setInventoryOptions(String[] inventoryOptions) {
		this.inventoryOptions = inventoryOptions;
	}

	/**
	 * @return the showInGrandExchange
	 */
	public boolean isShowInGrandExchange() {
		return showInGrandExchange;
	}

	/**
	 * @param showInGrandExchange
	 * 		the showInGrandExchange to set
	 */
	public void setShowInGrandExchange(boolean showInGrandExchange) {
		this.showInGrandExchange = showInGrandExchange;
	}
}