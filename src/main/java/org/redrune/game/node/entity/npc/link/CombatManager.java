package org.redrune.game.node.entity.npc.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.content.combat.npc.NPCCombat;
import org.redrune.game.node.entity.npc.NPC;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/21/2017
 */
public class CombatManager {
	
	/**
	 * The instance of the combat
	 */
	@Getter
	private final NPCCombat combat;
	
	/**
	 * If we're forced to be aggressive
	 */
	@Getter
	@Setter
	private boolean aggressiveForced;
	
	/**
	 * If we have an attack option
	 */
	@Getter
	@Setter
	private boolean hasAttackOption;
	
	/**
	 * If the npc does not follow during combat
	 */
	@Getter
	@Setter
	private boolean cantFollowDuringCombat;
	
	/**
	 * If the npc does not check the distance for combat
	 */
	@Getter
	@Setter
	private boolean noDistanceCheck;
	
	/**
	 * If we follow the target very closely in combat
	 */
	@Getter
	@Setter
	private boolean forceFollowClose;
	
	/**
	 * The radius in which targets are found
	 */
	@Getter
	@Setter
	private int findTargetRadius;
	
	/**
	 * If we use an intelligent route finder in combat
	 */
	@Getter
	@Setter
	private boolean intelligentRouteFinder;
	
	/**
	 * If we can be attacked by multiple people by force
	 */
	@Getter
	@Setter
	private boolean forceMultiAttacked;
	
	public CombatManager(NPC npc) {
		this.combat = new NPCCombat(npc);
		this.hasAttackOption = npc.getDefinitions().hasAttackOption();
	}
}
