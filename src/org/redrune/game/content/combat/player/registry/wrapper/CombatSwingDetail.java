package org.redrune.game.content.combat.player.registry.wrapper;

import lombok.Getter;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;

import java.util.function.Consumer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/1/2017
 */
public class CombatSwingDetail {
	
	/**
	 * The source of the swing
	 */
	@Getter
	private final Entity source;
	
	/**
	 * The target of the swing
	 */
	@Getter
	private final Entity target;
	
	/**
	 * The hit of the swing
	 */
	@Getter
	private final Hit hit;
	
	public CombatSwingDetail(Entity source, Entity target, Hit hit) {
		this.source = source;
		this.target = target;
		this.hit = hit;
	}
	
	/**
	 * Accepts the swing to the consumer
	 *
	 * @param consumer
	 * 		The consumer
	 */
	public CombatSwingDetail consume(Consumer<CombatSwingDetail> consumer) {
		consumer.accept(this);
		return this;
	}
}
