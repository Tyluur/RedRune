package org.redrune.game.node.entity.npc.data;

import lombok.Getter;
import org.redrune.game.node.item.Drop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public final class NPCCharacteristics {
	
	/**
	 * The list of drops
	 */
	@Getter
	private final List<Drop> drops;
	
	/**
	 * The map of bonuses the npc has. We use a map because npcs can have the same name but different bonuses, and in
	 * this case we will store the different bonuses in the map [i.e. the case of green dragons]
	 */
	@Getter
	private final Map<Integer, int[]> bonuses;
	
	/**
	 * The combat definitions of the npc. <p>Explanation for combat definitions: @see {@link NPCCombatDefinitions}</p>
	 */
	@Getter
	private final Map<Integer, NPCCombatDefinitions> combatDefinitions;
	
	/**
	 * The list of charm drops
	 */
	@Getter
	private final List<Drop> charmDrops;
	
	public NPCCharacteristics() {
		this.drops = new ArrayList<>();
		this.bonuses = new HashMap<>();
		this.combatDefinitions = new HashMap<>();
		this.charmDrops = new ArrayList<>();
	}
}
