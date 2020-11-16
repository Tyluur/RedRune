package org.redrune.utility.repository.npc.combat;

import org.redrune.game.content.combat.npc.NPCCombatSwing;
import org.redrune.game.content.combat.npc.swing.DefaultCombatSwing;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.utility.tool.Misc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class NPCCombatSwingRepository {
	
	/**
	 * The map of combat scripts
	 */
	private static final Map<Object, NPCCombatSwing> SWING_REPOSITORY = new ConcurrentHashMap<>();
	
	/**
	 * The default combat swing
	 */
	private static final NPCCombatSwing DEFAULT_SWING = new DefaultCombatSwing();
	
	/**
	 * Loads all combat swings
	 */
	public static void loadAll() {
		for (Object clazz : Misc.getClassesInDirectory(NPCCombatSwing.class.getPackage().getName() + ".swing")) {
			NPCCombatSwing swing = (NPCCombatSwing) clazz;
			for (Object binding : swing.bindings()) {
				SWING_REPOSITORY.put(binding, swing);
			}
		}
		System.out.println("Constructed " + SWING_REPOSITORY.size() + " npc combat swings.");
	}
	
	/**
	 * Fires the right combat swing
	 *
	 * @param npc
	 * 		The npc
	 * @param target
	 * 		The target
	 */
	public static int fire(NPC npc, Entity target) {
		NPCCombatSwing swing = SWING_REPOSITORY.get(npc.getId());
		boolean store = false;
		// we didn't have a swing by the name
		if (swing == null) {
			// try getting it by name
			swing = SWING_REPOSITORY.get(npc.getDefinitions().getName());
			if (swing == null) {
				swing = new DefaultCombatSwing();
			}
			store = true;
		}
		// stores it if we should
		if (store) {
			SWING_REPOSITORY.put(npc.getId(), swing);
		}
		return swing.attack(npc, target);
	}
	
}
