package org.redrune.game.content.combat.player.registry.wrapper.context;

import lombok.Getter;
import org.redrune.game.content.combat.player.swing.MagicCombatSwing;
import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/27/2017
 */
public class CombatSpellContext extends MagicSpellContext {
	
	/**
	 * The target of the spell
	 */
	@Getter
	private final Entity target;
	
	/**
	 * The combat swing of the spell
	 */
	@Getter
	private final MagicCombatSwing swing;
	
	public CombatSpellContext(Entity target, MagicCombatSwing swing) {
		this.target = target;
		this.swing = swing;
	}
}
