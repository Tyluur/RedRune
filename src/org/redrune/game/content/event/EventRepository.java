package org.redrune.game.content.event;

import org.redrune.game.content.event.EventPolicy.ActionPolicy;
import org.redrune.game.content.event.EventPolicy.AnimationPolicy;
import org.redrune.game.content.event.EventPolicy.InterfacePolicy;
import org.redrune.game.content.event.EventPolicy.WalkablePolicy;
import org.redrune.game.content.event.impl.*;
import org.redrune.game.content.event.impl.item.*;
import org.redrune.game.node.entity.player.Player;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class that stores all events
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/27/2017
 */
public final class EventRepository {
	
	/**
	 * The map of all events
	 */
	private static final Map<String, Event> EVENT_MAP = new ConcurrentHashMap<>();
	
	/**
	 * Executes an event for a player
	 *
	 * @param player
	 * 		The player
	 * @param clazz
	 * 		The class of the event
	 * @param context
	 * 		The context
	 */
	@SuppressWarnings("unchecked")
	public static void executeEvent(Player player, Class<? extends Event> clazz, EventContext context) {
		Event event = EVENT_MAP.get(clazz.getSimpleName());
		if (event == null) {
			System.out.println("Unable to identify event for class {" + clazz + "}");
			return;
		}
		if (!sendPreExecuteFlags(player, event, context)) {
			return;
		}
		event.run(player, context);
	}
	
	/**
	 * Handles the policies before the event is executed
	 *
	 * @param player
	 * 		The player
	 * @param event
	 * 		The event
	 * @return True if we should start the event
	 */
	@SuppressWarnings("unchecked")
	private static boolean sendPreExecuteFlags(Player player, Event event, EventContext context) {
		final boolean stopWalk = event.getWalkablePolicy() == WalkablePolicy.RESET;
		final boolean stopInterfaces = event.getInterfacePolicy() == InterfacePolicy.CLOSE;
		final boolean stopActions = event.getActionPolicy() == ActionPolicy.RESET;
		final boolean stopAnimation = event.getAnimationPolicy() == AnimationPolicy.RESET;
		if (!event.canStart(player, context)) {
			return false;
		}
		player.stop(stopActions, stopWalk, stopInterfaces, stopAnimation);
		return true;
	}
	
	/**
	 * Registers all game events
	 */
	public static void registerEvents(boolean reload) {
		if (reload) {
			EVENT_MAP.clear();
		}
		try {
			registerEvent(CommandEvent.class);
			registerEvent(NodeReachEvent.class);
			registerEvent(NPCEvent.class);
			registerEvent(ObjectEvent.class);
			registerEvent(WalkEvent.class);
			registerEvent(FloorItemPickupEvent.class);
			registerEvent(FloorItemUsageEvent.class);
			registerEvent(ItemEvent.class);
			registerEvent(ItemOnItemEvent.class);
			registerEvent(ItemRemovalEvent.class);
			registerEvent(ItemDropEvent.class);
		} catch (IllegalAccessException | InstantiationException e) {
			e.printStackTrace();
		}
		System.out.println("Registered " + EVENT_MAP.size() + " events.");
	}
	
	/**
	 * Registers an event
	 */
	private static void registerEvent(Class<? extends Event> clazz) throws IllegalAccessException, InstantiationException {
		final Event event = clazz.newInstance();
		EVENT_MAP.put(clazz.getSimpleName(), event);
	}
}
