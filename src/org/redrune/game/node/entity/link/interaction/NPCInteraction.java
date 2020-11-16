package org.redrune.game.node.entity.link.interaction;

import org.redrune.game.content.event.EventListener;
import org.redrune.game.content.event.EventListener.EventType;
import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class NPCInteraction extends Interaction {
	
	public NPCInteraction(Entity source, Entity target) {
		super(source, target);
	}
	
	@Override
	public void start() {
		source.turnTo(target);
		source.getMovement().getWalkSteps().clear();
		target.turnTo(source);
		target.getMovement().getWalkSteps().clear();
		EventListener.setListener(source, this::end, EventType.MOVE, EventType.DAMAGE, EventType.SCREEN_INTERFACE_CLOSE);
	}
	
	@Override
	public void request() {
	
	}
	
	@Override
	public void end() {
		source.turnTo(null);
		target.turnTo(null);
		source.getInteractionManager().end();
		target.getInteractionManager().end();
	}
}
