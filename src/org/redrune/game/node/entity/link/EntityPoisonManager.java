package org.redrune.game.node.entity.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.utility.rs.RSTimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public final class EntityPoisonManager {
	
	/**
	 * The amount of damage to apply
	 */
	private int poisonDamage;
	
	/**
	 * The delay between poison times. This is set to 30 ticks (10 seconds) after poison damage applied
	 */
	private int delay;
	
	/**
	 * The amount of ticks that poison has processed
	 */
	private int tickAmount;
	
	/**
	 * The entity that poison is working on
	 */
	@Getter
	@Setter
	private transient Entity entity;
	
	/**
	 * The time we are immune to poison until
	 */
	@Getter
	@Setter
	private long poisonImmunity;
	
	/**
	 * Makes the entity poisoned
	 *
	 * @param startDamage
	 * 		The amount of damage to start with
	 */
	public void makePoisoned(int startDamage) {
		// we don't reduce to a weaker poison
		if (poisonDamage > startDamage) {
			return;
		}
		// if we're immune to poison
		if (poisonImmunity > System.currentTimeMillis()) {
			return;
		}
		if (entity.isPlayer()) {
			Player player = entity.toPlayer();
			if (poisonDamage == 0) {
				player.getTransmitter().sendMessage("<col=990000>You have been poisoned!</col>");
			}
		}
		delay = 3;
		tickAmount = 0;
		poisonDamage = startDamage;
		refresh();
	}
	
	/**
	 * Refreshes the orb with poison data
	 */
	public void refresh() {
		if (entity.isPlayer()) {
			Player player = ((Player) entity);
			player.getTransmitter().send(new ConfigPacketBuilder(102, isPoisoned() ? 1 : 0).build(player));
		}
	}
	
	/**
	 * If we are poisoned
	 */
	public boolean isPoisoned() {
		return poisonDamage >= 10;
	}
	
	/**
	 * Processes the poison every tick.
	 */
	public void processPoison() {
		if (entity.isDead() || !isPoisoned()) {
			return;
		}
		tickAmount++;
		// reduce delay
		if (delay > 0) {
			delay--;
			return;
		}
		// poison is uneffective after 3 minutes
		if (tickAmount > 0 && RSTimeUnit.TICKS.toSeconds(tickAmount) >= 180) {
			if (entity.isPlayer()) {
				entity.toPlayer().getTransmitter().sendMessage("The poison has wore off.");
			}
			reset();
			return;
		}
		// we aren't hit if we have an interface open
		if (entity.isPlayer()) {
			Player player = ((Player) entity);
			// inter opened we dont poison while inter opened like at rs
			if (player.getManager().getInterfaces().getScreenInterface() != -1) {
				return;
			}
		}
		// hits the player
		entity.getHitMap().applyHit(new Hit(entity, poisonDamage, HitSplat.POISON_DAMAGE));
		poisonDamage -= 2;
		// poison is appended at 15 seconds
		if (isPoisoned()) {
			delay = 3;
			return;
		}
		// we aren't poisoned anymore
		reset();
		if (entity.isPlayer()) {
			entity.toPlayer().getTransmitter().sendMessage("The poison has wore off.");
		}
	}
	
	/**
	 * Adds the amount of time we are immune to poison
	 *
	 * @param time
	 * 		The time
	 */
	public void addPoisonImmune(long time) {
		poisonImmunity = time + System.currentTimeMillis();
		reset();
	}
	
	/**
	 * Removes all poison damage, ticks, and calls {@link #refresh()}
	 */
	public void reset() {
		poisonDamage = 0;
		delay = 0;
		refresh();
	}
}
