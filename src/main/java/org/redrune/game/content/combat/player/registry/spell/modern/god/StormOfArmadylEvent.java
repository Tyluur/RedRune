package org.redrune.game.content.combat.player.registry.spell.modern.god;

import org.redrune.game.content.ProjectileManager;
import org.redrune.game.content.combat.player.registry.wrapper.context.CombatSpellContext;
import org.redrune.game.content.combat.player.registry.wrapper.magic.CombatSpellEvent;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/7/2017
 */
public class StormOfArmadylEvent implements CombatSpellEvent {
	
	@Override
	public int delay(Player player) {
		return player.getEquipment().getWeaponId() == 21777 ? 4 : 5;
	}
	
	@Override
	public int animationId() {
		return 10546;
	}
	
	@Override
	public int hitGfx() {
		return 1019;
	}
	
	@Override
	public int maxHit(Player player, Entity target) {
		return 160 + minimumHit(player);
	}
	
	@Override
	public int spellId() {
		return 99;
	}
	
	@Override
	public double exp() {
		return 70;
	}
	
	@Override
	public MagicBook book() {
		return MagicBook.REGULAR;
	}
	
	@Override
	public int minimumHit(Player player) {
		int start = 77;
		int level = player.getSkills().getLevelForXp(SkillConstants.MAGIC);
		int difference = level - start;
		return (difference / 2) * 10;
	}
	
	@Override
	public void cast(Player player, CombatSpellContext context) {
		player.sendGraphics(457);
		ProjectileManager.sendProjectile(ProjectileManager.createSpeedDefinedProjectile(player, context.getTarget(), 1019, 30, 26, 52, 0, 0));
		context.getSwing().sendSpell(player, context.getTarget(), this);
		if (context.getTarget().isPlayer()) {
			context.getTarget().toPlayer().getSkills().setLevel(SkillConstants.DEFENCE, context.getTarget().toPlayer().getSkills().getLevel(SkillConstants.DEFENCE) - 1);
		}
	}
}
