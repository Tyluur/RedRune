package org.redrune.utility.repository.npc.characteristic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.data.NPCCharacteristics;
import org.redrune.game.node.entity.npc.data.NPCCombatDefinitions;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.NPCConstants;
import org.redrune.utility.tool.Misc;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class NPCCharacteristicRepository {
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The location of the folder that has all npc characteristics
	 */
	private static final String CHARACTERISTIC_FOLDER_LOCATION = "./data/repository/npc/characteristics/";
	
	/**
	 * The map of characteristics
	 */
	private static final Map<Integer, NPCCharacteristics> CHARACTERISTICS_MAP = new ConcurrentHashMap<>();
	
	public static void main(String[] args) throws IOException {
		Cache.init();
		final List<String> lines = Files.readAllLines(new File("alotic_defs.txt").toPath(), Charset.defaultCharset());
		
		String header = null;
		List<Integer> idList = new ArrayList<>();
		NPCCombatDefinitions combatDefinitions = new NPCCombatDefinitions();
		int[] bonuses = new int[10];
		
		int count = 0;
		
		for (String line : lines) {
			try {
				// we're going onto a new name
				if (line.startsWith("#")) {
					// this means this is that we're replacing an existing name
					if (header != null) {
						count += dumpAll(idList, combatDefinitions, bonuses);
						bonuses = new int[10];
						idList = new ArrayList<>();
						combatDefinitions = new NPCCombatDefinitions();
					}
					header = line;
				} else if (line.startsWith("id")) {
					String[] split = line.split("id=");
					String idText = split[1];
					String[] idSplit = idText.split(",");
					for (String id : idSplit) {
						idList.add(Integer.parseInt(id));
					}
				} else if (line.startsWith("maxHp")) {
					String[] split = line.split("maxHp=");
					String hpText = split[1];
					Integer hpValue = Integer.parseInt(hpText);
					combatDefinitions.setHitpoints(hpValue);
				} else if (line.startsWith("maxDamage")) {
					String[] split = line.split("maxDamage=");
					String splitText = split[1];
					Integer maxDamage = Integer.parseInt(splitText);
					combatDefinitions.setMaxHit(maxDamage);
				} else if (line.startsWith("style")) {
					String[] split = line.split("style=");
					String splitText = split[1];
					int attackStyle;
					switch (splitText) {
						default:
							attackStyle = NPCConstants.MELEE_COMBAT_STYLE;
							break;
						case "RANGED":
							attackStyle = NPCConstants.RANGE_COMBAT_STYLE;
							break;
						case "MAGIC":
							attackStyle = NPCConstants.MAGIC_COMBAT_STYLE;
							break;
					}
					combatDefinitions.setAttackStyle(attackStyle);
				} else if (line.startsWith("attackTicks")) {
					String[] split = line.split("attackTicks=");
					String splitText = split[1];
					Integer attackTicks = Integer.parseInt(splitText);
					combatDefinitions.setAttackDelay(attackTicks);
				} else if (line.startsWith("deathTicks")) {
					String[] split = line.split("deathTicks=");
					String splitText = split[1];
					Integer deathTicks = Integer.parseInt(splitText);
					combatDefinitions.setDeathDelay(deathTicks);
				} else if (line.startsWith("respawnTicks")) {
					String[] split = line.split("respawnTicks=");
					String splitText = split[1];
					Integer respawnTicks = Integer.parseInt(splitText);
					combatDefinitions.setRespawnDelay(respawnTicks);
				} else if (line.startsWith("attackAnimation")) {
					String[] split = line.split("attackAnimation=");
					String splitText = split[1];
					Integer attackAnimation = Integer.parseInt(splitText);
					combatDefinitions.setAttackAnim(attackAnimation);
				} else if (line.startsWith("defendAnimation")) {
					String[] split = line.split("defendAnimation=");
					String splitText = split[1];
					Integer defendAnimation = Integer.parseInt(splitText);
					combatDefinitions.setDefenceAnim(defendAnimation);
				} else if (line.startsWith("deathAnimation")) {
					String[] split = line.split("deathAnimation=");
					String splitText = split[1];
					Integer deathAnimation = Integer.parseInt(splitText);
					combatDefinitions.setDeathAnim(deathAnimation);
				} else if (line.startsWith("meleeAttDef")) {
					String[] split = line.split("meleeAttDef=");
					String splitText = split[1];
					String[] bonusSplit = splitText.split(",");
					for (int i = 0; i < bonusSplit.length; i++) {
						String value = bonusSplit[i];
						Integer bonusValue = Integer.parseInt(value);
						if (i == 0) {
							bonuses[BonusConstants.STAB_ATTACK] = bonusValue;
						} else if (i == 1) {
							bonuses[BonusConstants.STAB_DEFENCE] = bonusValue;
						}
					}
				} else if (line.startsWith("magicAttDef")) {
					String[] split = line.split("magicAttDef=");
					String splitText = split[1];
					String[] bonusSplit = splitText.split(",");
					for (int i = 0; i < bonusSplit.length; i++) {
						String value = bonusSplit[i];
						Integer bonusValue = Integer.parseInt(value);
						if (i == 0) {
							bonuses[BonusConstants.MAGIC_ATTACK] = bonusValue;
						} else if (i == 1) {
							bonuses[BonusConstants.MAGIC_DEFENCE] = bonusValue;
						}
					}
				} else if (line.startsWith("rangedAttDef")) {
					String[] split = line.split("rangedAttDef=");
					String splitText = split[1];
					String[] bonusSplit = splitText.split(",");
					for (int i = 0; i < bonusSplit.length; i++) {
						String value = bonusSplit[i];
						Integer bonusValue = Integer.parseInt(value);
						if (i == 0) {
							bonuses[BonusConstants.RANGE_ATTACK] = bonusValue;
						} else if (i == 1) {
							bonuses[BonusConstants.RANGE_DEFENCE] = bonusValue;
						}
					}
				}
			} catch (NumberFormatException e) {
				System.out.println("Unable to parse line " + line);
				e.printStackTrace();
			}
		}
		System.out.println("Finished conversion of " + count + " npc combat definitions.");
	}
	
	private static int dumpAll(List<Integer> idList, NPCCombatDefinitions combatDefinitions, int[] bonuses) {
		for (int id : idList) {
			NPCDefinition npcDefinition = NPCDefinitionParser.forId(id);
			if (npcDefinition == null) {
				continue;
			}
			String name = npcDefinition.getName().trim();
			NPCCharacteristics characteristics = loadFileCharacteristics(name);
			if (characteristics == null) {
				characteristics = new NPCCharacteristics();
			}
			// this way we only change the definitions that differ from our current ones
			NPCCombatDefinitions existing = characteristics.getCombatDefinitions().get(id);
			if (existing == null) {
				existing = combatDefinitions;
			}
			existing.swap(combatDefinitions);
			characteristics.getCombatDefinitions().put(id, existing);
			
			boolean containsData = false;
			for (int bonus : bonuses) {
				if (bonus != 0) {
					containsData = true;
					break;
				}
			}
			if (containsData) {
				characteristics.getBonuses().put(id, bonuses);
			}
			Misc.saveData(new File(getFileLocation(name)), characteristics);
		}
		return idList.size();
	}
	
	/**
	 * Gets the {@code NPCCharacteristics} of an {@code NPC} using caching to reduce speed
	 *
	 * @param npc
	 * 		The npc
	 */
	public static NPCCharacteristics getCharacteristics(NPC npc) {
		// the id of the npc
		int npcId = npc.getId();
		// using caching with the map
		if (CHARACTERISTICS_MAP.containsKey(npcId)) {
			//			System.out.println("we returned an already loaded characteristic for " + npc);
			return CHARACTERISTICS_MAP.get(npcId);
		} else {
			// we first check it from the file
			NPCCharacteristics characteristics = loadFileCharacteristics(npc.getDefinitions().getName());
			// if we couldn't load anything from the file, we will create a blank characteristics file
			// this makes it so we avoid loading another copy of the file
			if (characteristics == null) {
				characteristics = new NPCCharacteristics();
				//				System.out.println("unable to load characteristics from file for " + npc);
			} else {
				//				System.out.println("loaded characteristics from file for " + npc);
			}
			// we have no bonuses set, so we generate all 0 default bonuses [ensures we always generate bonuses]
			if (!characteristics.getBonuses().containsKey(npcId)) {
				characteristics.getBonuses().put(npcId, findFirstBonuses(characteristics.getBonuses()).orElse(new int[10]));
			}
			// we have no combat definitions, so we will store default ones for this npc [ensures we always generate bonuses]
			if (!characteristics.getCombatDefinitions().containsKey(npcId)) {
				characteristics.getCombatDefinitions().put(npcId, findBestDefinition(characteristics.getCombatDefinitions()).orElse(new NPCCombatDefinitions()));
			}
			// cache the data
			CHARACTERISTICS_MAP.put(npcId, characteristics);
			return characteristics;
		}
	}
	
	/**
	 * Finds the first definition in the map, this is only used to set definitions of an npc who doesn't have any
	 *
	 * @param map
	 * 		The map
	 */
	private static Optional<NPCCombatDefinitions> findBestDefinition(Map<Integer, NPCCombatDefinitions> map) {
		for (NPCCombatDefinitions def : map.values()) {
			if (def != null) {
				return Optional.of(def);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Finds the first bonus in the map, this is only used to set definitions of an npc who doesn't have any
	 *
	 * @param map
	 * 		The map
	 */
	private static Optional<int[]> findFirstBonuses(Map<Integer, int[]> map) {
		for (int[] def : map.values()) {
			if (def != null) {
				return Optional.of(def);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Constructs a {@code NPCCharacteristics} object from the npc's characteristics file.
	 *
	 * @param name
	 * 		The name of the npc
	 */
	private static NPCCharacteristics loadFileCharacteristics(String name) {
		// create the file
		File file = new File(getFileLocation(name));
		// if the file is not present, we can't load characteristics
		if (!file.exists()) {
			return null;
		}
		// get the text that is in the json file
		String text = Misc.getText(file.getAbsolutePath());
		// constructs the file from the serialized json file
		return GSON.fromJson(text, NPCCharacteristics.class);
	}
	
	/**
	 * Gets the location that the characteristics of the file should be in.
	 *
	 * @param name
	 * 		The name of the npc
	 */
	private static String getFileLocation(String name) {
		return CHARACTERISTIC_FOLDER_LOCATION + name + ".json";
	}
	
}
