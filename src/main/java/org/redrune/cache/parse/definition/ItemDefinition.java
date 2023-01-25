package org.redrune.cache.parse.definition;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheConstants;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.tool.BufferUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/19/2017
 */
@SuppressWarnings("unused")
public final class ItemDefinition {
	
	private static final int[] ITEM_REQS = { 540, 542, 697, 538 };
	
	@Getter
	private int id;
	
	@Getter
	@Setter
	private int equipId;
	
	@Getter
	private String name = "null";
	
	// options
	@Getter
	private String[] groundOptions;
	
	@Getter
	private String[] inventoryOptions;
	
	// extra information
	@Getter
	private int stackable;
	
	@Getter
	private int value;
	
	@Getter
	private boolean membersOnly;
	
	private boolean loaded;
	
	private int interfaceModelId;
	
	@Getter
	@Setter
	private int equipSlot;
	
	@Getter
	@Setter
	private int equipType;
	
	// wearing model information
	@Getter
	private int maleWornModelId1 = -1;
	
	@Getter
	private int femaleWornModelId1;
	
	@Getter
	private int maleWornModelId2 = -1;
	
	private int femaleWornModelId2;
	
	// model information
	private int[] originalModelColors;
	
	private int[] modifiedModelColors;
	
	// model size information
	private int modelZoom;
	
	private int modelRotation1;
	
	private int modelRotation2;
	
	private int modelOffset1;
	
	private int modelOffset2;
	
	private short[] textureColour1;
	
	private short[] textureColour2;
	
	private byte[] unknownArray1;
	
	private int[] unknownArray2;
	
	// extra information, not used for newer items
	@Getter
	private boolean unnoted;
	
	private int colourEquip1;
	
	private int colourEquip2;
	
	@Getter
	private int noteId = -1;
	
	@Getter
	private int noteTemplateId = -1;
	
	private int[] stackIds;
	
	private int[] stackAmounts;
	
	private int teamId;
	
	@Getter
	private int lendId = -1;
	
	@Getter
	private boolean lended = false;
	
	private int lendTemplateId = -1;
	
	private int recolourId = -1; // not sure
	
	private int recolourTemplateId = -1;
	
	private HashMap<Integer, Object> clientScriptData;
	
	private Map<Integer, Integer> levelRequirements;
	
	private Map<Integer, Integer> itemRequirements = new HashMap<>();
	
	public ItemDefinition(int id) {
		this.id = id;
	}
	
	public boolean isLoaded() {
		return loaded;
	}
	
	public void loadItemDefinition() throws IOException {
		setDefaultsVariableValules();
		setDefaultOptions();
		byte[] is = CacheManager.getData(CacheConstants.ITEMDEF_IDX_ID, id >>> 8, id & 0xFF);
		readOpcodeValues(ByteBuffer.wrap(is));
		if (noteTemplateId != -1) // done
		{
			transferNoteDefinition(ItemDefinitionParser.forId(noteId), ItemDefinitionParser.forId(noteTemplateId));
		}
		if (lendTemplateId != -1) // done
		{
			transferLendDefinition(ItemDefinitionParser.forId(lendId), ItemDefinitionParser.forId(lendTemplateId));
		}
		if (recolourTemplateId != -1) // need to finish
		{
			transferRecolourDefinition(ItemDefinitionParser.forId(recolourId), ItemDefinitionParser.forId(recolourTemplateId));
		}
		
		if (clientScriptData == null) {
			return;
		}
		
		int levelId = -1, levelReq = -1, itemId = -1;
		for (int opcode2 : clientScriptData.keySet()) {
			Object value = clientScriptData.get(opcode2);
			if (value instanceof String) {
				continue;
			}
			int val = (Integer) value;
			if (opcode2 >= 749 && opcode2 < 797) {
				if (opcode2 % 2 == 0) {
					levelReq = val;
				} else {
					levelId = val;
				}
				if (levelId != -1 && levelReq != -1) {
					if (levelRequirements == null) {
						levelRequirements = new HashMap<>();
					}
					if (name.toLowerCase().contains("infinity")) {
						levelRequirements.put(1, 25);
						levelRequirements.put(6, 50);
					} else if (id == 2503) {
						levelRequirements.put((int) SkillConstants.DEFENCE, 40);
						levelRequirements.put((int) SkillConstants.RANGE, 70);
					} else if (id == 7462) {
						levelRequirements.put((int) SkillConstants.DEFENCE, 45);
					} else if (id == 20072) {
						levelRequirements.put((int) SkillConstants.ATTACK, 60);
						levelRequirements.put((int) SkillConstants.DEFENCE, 60);
					} else {
						levelRequirements.put(levelId, levelReq);
					}
					levelId = levelReq = -1;
				}
			}
			for (int reqOpcode : ITEM_REQS) {
				if (opcode2 == reqOpcode) {
					itemId = val;
				} else if (opcode2 - 1 == reqOpcode && itemId != -1) {
					if (itemRequirements == null) {
						itemRequirements = new HashMap<>();
					}
					itemRequirements.put(itemId, val);
					itemId = -1;
				}
			}
			switch (opcode2) {
				case 394:
					if (levelRequirements == null) {
						levelRequirements = new HashMap<>();
					}
					levelRequirements.put((int) SkillConstants.SUMMONING, val);
					break;
			}
		}
		loaded = true;
	}
	
	private void setDefaultsVariableValules() {
	
	}
	
	private void setDefaultOptions() {
		groundOptions = new String[] { null, null, "take", null, null };
		inventoryOptions = new String[] { null, null, null, null, "drop" };
	}
	
	private void readOpcodeValues(ByteBuffer buffer) {
		while (true) {
			int opcode = buffer.get() & 0xFF;
			if (opcode == 0) {
				break;
			}
			readValues(buffer, opcode);
		}
	}
	
	private void transferNoteDefinition(ItemDefinition reference, ItemDefinition templateReference) {
		membersOnly = reference.membersOnly;
		interfaceModelId = templateReference.interfaceModelId;
		originalModelColors = templateReference.originalModelColors;
		name = reference.name;
		modelOffset2 = templateReference.modelOffset2;
		textureColour1 = templateReference.textureColour1;
		value = reference.value;
		modelRotation2 = templateReference.modelRotation2;
		stackable = 1;
		modifiedModelColors = templateReference.modifiedModelColors;
		modelRotation1 = templateReference.modelRotation1;
		modelZoom = templateReference.modelZoom;
		textureColour1 = templateReference.textureColour1;
	}
	
	private void transferLendDefinition(ItemDefinition reference, ItemDefinition templateReference) {
		femaleWornModelId1 = reference.femaleWornModelId1;
		maleWornModelId2 = reference.maleWornModelId2;
		membersOnly = reference.membersOnly;
		interfaceModelId = templateReference.interfaceModelId;
		textureColour2 = reference.textureColour2;
		groundOptions = reference.groundOptions;
		unknownArray1 = reference.unknownArray1;
		modelRotation1 = templateReference.modelRotation1;
		modelRotation2 = templateReference.modelRotation2;
		originalModelColors = reference.originalModelColors;
		name = reference.name;
		maleWornModelId1 = reference.maleWornModelId1;
		colourEquip1 = reference.colourEquip1;
		teamId = reference.teamId;
		modelOffset2 = templateReference.modelOffset2;
		clientScriptData = reference.clientScriptData;
		modifiedModelColors = reference.modifiedModelColors;
		colourEquip2 = reference.colourEquip2;
		modelOffset1 = templateReference.modelOffset1;
		textureColour1 = reference.textureColour1;
		value = 0;
		modelZoom = templateReference.modelZoom;
		inventoryOptions = new String[5];
		femaleWornModelId2 = reference.femaleWornModelId2;
		levelRequirements = reference.levelRequirements;
		itemRequirements = reference.itemRequirements;
		if (reference.inventoryOptions != null) {
			inventoryOptions = reference.inventoryOptions.clone();
		}
		inventoryOptions[4] = "Discard";
		lended = true;
	}
	
	private void transferRecolourDefinition(ItemDefinition reference, ItemDefinition templateReference) {
		femaleWornModelId2 = reference.femaleWornModelId2;
		inventoryOptions = new String[5];
		modelRotation2 = templateReference.modelRotation2;
		name = reference.name;
		maleWornModelId1 = reference.maleWornModelId1;
		modelOffset2 = templateReference.modelOffset2;
		femaleWornModelId1 = reference.femaleWornModelId1;
		maleWornModelId2 = reference.maleWornModelId2;
		modelOffset1 = templateReference.modelOffset1;
		unknownArray1 = reference.unknownArray1;
		stackable = reference.stackable;
		modelRotation1 = templateReference.modelRotation1;
		textureColour1 = reference.textureColour1;
		colourEquip1 = reference.colourEquip1;
		textureColour2 = reference.textureColour2;
		modifiedModelColors = reference.modifiedModelColors;
		modelZoom = templateReference.modelZoom;
		colourEquip2 = reference.colourEquip2;
		teamId = reference.teamId;
		value = 0;
		groundOptions = reference.groundOptions;
		originalModelColors = reference.originalModelColors;
		membersOnly = reference.membersOnly;
		clientScriptData = reference.clientScriptData;
		interfaceModelId = templateReference.interfaceModelId;
		if (reference.inventoryOptions != null) {
			inventoryOptions = reference.inventoryOptions.clone();
		}
	}
	
	private void readValues(ByteBuffer buffer, int opcode) {
		if (opcode == 1) {
			interfaceModelId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 2) {
			name = BufferUtils.readRS2String(buffer);
		} else if (opcode == 4) {
			modelZoom = buffer.getShort() & 0xFFFF;
		} else if (opcode == 5) {
			modelRotation1 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 6) {
			modelRotation2 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 7) {
			modelOffset1 = buffer.getShort() & 0xFFFF;
			if (modelOffset1 > 32767) {
				modelOffset1 -= 65536;
			}
			modelOffset1 <<= 0;
		} else if (opcode == 8) {
			modelOffset2 = buffer.getShort() & 0xFFFF;
			if (modelOffset2 > 32767) {
				modelOffset2 -= 65536;
			}
			modelOffset2 <<= 0;
		} else if (opcode == 11) {
			stackable = 1;
		} else if (opcode == 12) {
			value = buffer.getInt();
		} else if (opcode == 16) {
			membersOnly = true;
		} else if (opcode == 18) {
			buffer.getShort();
		} else if (opcode == 23) {
			maleWornModelId1 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 24) {
			femaleWornModelId1 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 25) {
			maleWornModelId2 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 26) {
			femaleWornModelId2 = buffer.getShort() & 0xFFFF;
		} else if (opcode >= 30 && opcode < 35) {
			groundOptions[opcode - 30] = BufferUtils.readRS2String(buffer);
		} else if (opcode >= 35 && opcode < 40) {
			inventoryOptions[opcode - 35] = BufferUtils.readRS2String(buffer);
		} else if (opcode == 40) {
			int length = buffer.get();
			originalModelColors = new int[length];
			modifiedModelColors = new int[length];
			for (int index = 0; index < length; index++) {
				originalModelColors[index] = buffer.getShort();
				modifiedModelColors[index] = buffer.getShort();
			}
		} else if (opcode == 41) {
			int length = buffer.get();
			textureColour1 = new short[length];
			textureColour2 = new short[length];
			for (int index = 0; index < length; index++) {
				textureColour1[index] = buffer.getShort();
				textureColour2[index] = buffer.getShort();
			}
		} else if (opcode == 42) {
			int length = buffer.get();
			unknownArray1 = new byte[length];
			for (int index = 0; index < length; index++) {
				unknownArray1[index] = buffer.get();
			}
		} else if (opcode == 65) {
			unnoted = true;
		} else if (opcode == 78) {
			colourEquip1 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 79) {
			colourEquip2 = buffer.getShort() & 0xFFFF;
		} else if (opcode == 90) {
			buffer.getShort();
		} else if (opcode == 91) {
			buffer.getShort();
		} else if (opcode == 92) {
			buffer.getShort();
		} else if (opcode == 93) {
			buffer.getShort();
		} else if (opcode == 95) {
			buffer.getShort();
		} else if (opcode == 96) {
			buffer.get();
		} else if (opcode == 97) {
			noteId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 98) {
			noteTemplateId = buffer.getShort() & 0xFFFF;
		} else if (opcode >= 100 && opcode < 110) {
			if (stackIds == null) {
				stackIds = new int[10];
				stackAmounts = new int[10];
			}
			stackIds[opcode - 100] = buffer.getShort() & 0xFFFF;
			stackAmounts[opcode - 100] = buffer.getShort() & 0xFFFF;
		} else if (opcode == 110) {
			buffer.getShort();
		} else if (opcode == 111) {
			buffer.getShort();
		} else if (opcode == 112) {
			buffer.getShort();
		} else if (opcode == 113) {
			buffer.get();
		} else if (opcode == 114) {
			buffer.get();
		} else if (opcode == 115) {
			teamId = buffer.get();
		} else if (opcode == 121) {
			lendId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 122) {
			lendTemplateId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 125) {
			buffer.get();
			buffer.get();
			buffer.get();
		} else if (opcode == 126) {
			buffer.get();
			buffer.get();
			buffer.get();
		} else if (opcode == 127) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 128) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 129) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 130) {
			buffer.get();
			buffer.getShort();
		} else if (opcode == 132) {
			int length = buffer.get();
			unknownArray2 = new int[length];
			for (int index = 0; index < length; index++) {
				unknownArray2[index] = buffer.getShort() & 0xFFFF;
			}
		} else if (opcode == 134) {
			buffer.get();
		} else if (opcode == 139) {
			recolourId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 140) {
			recolourTemplateId = buffer.getShort() & 0xFFFF;
		} else if (opcode == 249) {
			int length = buffer.get();
			if (clientScriptData == null) {
				clientScriptData = new HashMap<>();
			}
			for (int index = 0; index < length; index++) {
				boolean string = buffer.get() == 1;
				int key = BufferUtils.getMediumInt(buffer);
				Object value = string ? BufferUtils.readRS2String(buffer) : buffer.getInt();
				clientScriptData.put(key, value);
			}
		} else {
			System.out.println("Unhandled opcode! opcode:" + opcode);
		}
	}
	
	public boolean hasSpecialBar() {
		if (clientScriptData == null) {
			return false;
		}
		Object specialBar = clientScriptData.get(687);
		return specialBar != null && specialBar instanceof Integer && (Integer) specialBar == 1;
	}
	
	public int getGroupId() {
		if (clientScriptData == null) {
			return 0;
		}
		Object specialBar = clientScriptData.get(686);
		if (specialBar != null && specialBar instanceof Integer) {
			return (Integer) specialBar;
		}
		return 0;
	}
	
	public int getRenderAnimId() {
		if (clientScriptData == null) {
			return 1426;
		}
		Object animId = clientScriptData.get(644);
		if (animId != null && animId instanceof Integer) {
			return (Integer) animId;
		}
		return 1426;
	}
	
	public int getQuestId() {
		if (clientScriptData == null) {
			return -1;
		}
		Object questId = clientScriptData.get(861);
		if (questId != null && questId instanceof Integer) {
			return (Integer) questId;
		}
		return -1;
	}
	
	/**
	 * Prints all fields in this class.
	 */
	public void printFields() {
		for (Field field : getClass().getDeclaredFields()) {
			if ((field.getModifiers() & 8) != 0) {
				continue;
			}
			try {
				System.out.println(field.getName() + ": " + getValue(field));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		System.out.println("-- CS2ScriptData --");
		for (int key : clientScriptData.keySet()) {
			Object o = clientScriptData.get(key);
			System.out.println("CS2Script - [key=" + key + ", value=" + o + "].");
		}
		System.out.println("-- end of " + getClass().getSimpleName() + " fields --");
	}
	
	private Object getValue(Field field) throws Throwable {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == int[][].class) {
			return Arrays.toString((int[][]) field.get(this));
		} else if (type == int[].class) {
			return Arrays.toString((int[]) field.get(this));
		} else if (type == byte[].class) {
			return Arrays.toString((byte[]) field.get(this));
		} else if (type == short[].class) {
			return Arrays.toString((short[]) field.get(this));
		} else if (type == double[].class) {
			return Arrays.toString((double[]) field.get(this));
		} else if (type == float[].class) {
			return Arrays.toString((float[]) field.get(this));
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(this));
		}
		return field.get(this);
	}
	
	public HashMap<Integer, Integer> getWearingRequirements() {
		HashMap<Integer, Integer> skills = new HashMap<>();
		if (clientScriptData == null) {
			return skills;
		}
		int nextLevel = -1;
		int nextSkill = -1;
		for (int key : clientScriptData.keySet()) {
			Object value = clientScriptData.get(key);
			if (value instanceof String) {
				continue;
			}
			if (key >= 749 && key < 797) {
				if (key % 2 == 0) {
					nextLevel = (Integer) value;
				} else {
					nextSkill = (Integer) value;
				}
				if (nextLevel != -1 && nextSkill != -1) {
					if (nextSkill >= SkillConstants.SKILL_NAME.length) {
						skills.put(nextLevel, nextSkill);
					} else {
						skills.put(nextSkill, nextLevel);
					}
					nextLevel = -1;
					nextSkill = -1;
				}
			}
		}
		return skills;
	}
	
	public boolean isStackable() {
		return stackable == 1;
	}
	
	public boolean isNoted() {
		return noteTemplateId != -1;
	}
	
	public boolean isWearItem() {
		if (inventoryOptions == null) {
			return false;
		}
		for (String option : inventoryOptions) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip")) {
				return equipSlot != -1;
			}
		}
		return false;
	}
	
	public boolean isWearItem(boolean male) {
		if (inventoryOptions == null) {
			return false;
		}
		if (EquipConstants.getItemSlot(id) != EquipConstants.SLOT_RING && EquipConstants.getItemSlot(id) != EquipConstants.SLOT_ARROWS && EquipConstants.getItemSlot(id) != EquipConstants.SLOT_AURA && (male ? getMaleWornModelId1() == -1 : getFemaleWornModelId1() == -1)) {
			return false;
		}
		for (String option : inventoryOptions) {
			if (option == null) {
				continue;
			}
			if (option.equalsIgnoreCase("wield") || option.equalsIgnoreCase("wear") || option.equalsIgnoreCase("equip")) {
				return equipSlot != -1;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the item has the selected inventory option
	 *
	 * @param option
	 * 		The option to look for
	 */
	public boolean hasOption(String option) {
		for (String inventoryOption : inventoryOptions) {
			if (inventoryOption != null && inventoryOption.equalsIgnoreCase(option)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if these are the definitions of an edible item
	 */
	public boolean isEdible() {
		return hasOption("eat") || hasOption("drink");
	}
}