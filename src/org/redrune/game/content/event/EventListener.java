package org.redrune.game.content.event;

import org.redrune.game.node.entity.Entity;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class EventListener {
	
	/**
	 * Sets the listener for a type of event
	 *
	 * @param entity
	 * 		The entity
	 * @param types
	 * 		The types of events
	 * @param task
	 * 		The task
	 */
	public static void setListener(Entity entity, Runnable task, EventType... types) {
		for (EventType type : types) {
			entity.putAttribute(type.key(), task);
		}
	}
	
	/**
	 * Fires the listener for an event
	 *
	 * @param entity
	 * 		The entity
	 * @param types
	 * 		The types of events
	 */
	public static void fireListener(Entity entity, EventType... types) {
		for (EventType type : types) {
			Runnable task = entity.removeAttribute(type.key());
			if (task == null) {
				continue;
			}
			task.run();
		}
	}
	
	/**
	 * The types of events
	 */
	public enum EventType {
		MOVE,
		DAMAGE,
		SCREEN_INTERFACE_CLOSE,
		SCREEN_INTERFACE_OPEN;
		
		public String key() {
			return name().toLowerCase() + "_event_listener";
		}
	}
	
}