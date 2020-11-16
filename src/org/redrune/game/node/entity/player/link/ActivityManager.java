package org.redrune.game.node.entity.player.link;

import org.redrune.game.content.activity.Activity;
import org.redrune.game.content.activity.impl.TutorialActivity;
import org.redrune.game.content.combat.player.registry.wrapper.magic.TeleportType;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.utility.rs.InteractionOption;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/5/2017
 */
public class ActivityManager {
	
	/**
	 * The default activity class
	 */
	public static final Class<? extends Activity> DEFAULT_ACTIVITY = TutorialActivity.class;
	
	/**
	 * The name of the last activity
	 */
	private String lastActivityName;
	
	/**
	 * The params of the last activity
	 */
	private Object[] lastActivityParameters;
	
	/**
	 * The instance of the player
	 */
	private transient Player player;
	
	/**
	 * The instance of the activity
	 */
	private transient Activity activity;
	
	/**
	 * Ticks the activity
	 */
	public void process() {
		getActivity().ifPresent(Activity::tick);
	}
	
	/**
	 * Sets the default activity
	 */
	public void setDefaultActivity() {
		lastActivityName = DEFAULT_ACTIVITY.getName();
		lastActivityParameters = new Object[0];
	}
	
	/**
	 * Gets the activity
	 */
	public Optional<Activity> getActivity() {
		if (activity == null) {
			return Optional.empty();
		} else {
			return Optional.of(activity);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> Optional<T> getActivityOptional(Class<T> clazz) {
		if (activity == null || !clazz.equals(activity.getClass())) {
			return Optional.empty();
		}
		return Optional.of((T) activity);
	}
	
	/**
	 * Checks if the activity we're in handles the interaction for the node customly
	 *
	 * @param node
	 * 		The node
	 * @param option
	 * 		The option
	 */
	public boolean handlesNodeInteraction(Node node, InteractionOption option) {
		// we don't have an activity, so we assume we can do the interaction
		if (activity == null) {
			return false;
		}
		// if an activity customly handles the option it will be true
		if (activity.handleNodeInteraction(node, option)) {
			return true;
		}
		// we must not have an activity, OR the activity must not have
		// handled the option. thus we should be able to use the option
		return false;
	}
	
	/**
	 * If combat is acceptable in this activity
	 *
	 * @param target
	 * 		The target we're in combat with
	 */
	public boolean combatAcceptable(Entity target) {
		// if we dont have an activity
		if (activity == null) {
			return true;
		}
		// if the activity says no, we will not continue
		if (!activity.combatAcceptable(target)) {
			return false;
		}
		// otherwise we can continue
		return true;
	}
	
	/**
	 * Checks if the activity allows the teleport type
	 *
	 * @param type
	 * 		The type of teleport
	 */
	public boolean teleportationAllowed(TeleportType type) {
		return activity == null || activity.teleportationAllowed(type);
	}
	
	/**
	 * Checks if we can move during the activity
	 *
	 * @param x
	 * 		The x of the movement request
	 * @param y
	 * 		The y of the movement request
	 * @param dir
	 * 		The direction of the movement request
	 */
	public boolean canMove(int x, int y, int dir) {
		return activity == null || activity.canMove(x, y, dir);
	}
	
	/**
	 * If the activity handles the entity's death
	 *
	 * @param entity
	 * 		The entity
	 */
	public boolean handleEntityDeath(Entity entity) {
		return activity != null && activity.handleEntityDeath(entity);
	}
	
	/**
	 * Sets the activity player
	 *
	 * @param player
	 * 		The player
	 */
	public void setPlayer(Player player) {
		this.player = player;
		getActivity().ifPresent(activity -> activity.setPlayer(player));
	}
	
	/**
	 * Handles the login aspect of activities
	 */
	@SuppressWarnings("unchecked")
	public void login() {
		if (lastActivityName == null) {
			return;
		}
		try {
			Class<Activity> clazz = (Class<Activity>) Class.forName(lastActivityName);
			Activity activity = clazz.newInstance();
			startActivity(activity);
			activity.setParameters(lastActivityParameters);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
			System.err.println("Unable to find activity by name '" + lastActivityName + "'");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets and starts an activity
	 *
	 * @param activity
	 * 		The activity
	 */
	public void startActivity(Activity activity) {
		this.activity = activity;
		this.activity.setPlayer(player);
		this.activity.start();
		this.lastActivityName = activity.getClass().getName();
		this.lastActivityParameters = activity.getParameters();
	}
	
	/**
	 * Handles the logout aspect of activities
	 */
	public void logout() {
		Optional<Activity> optional = getActivity();
		if (!optional.isPresent()) {
			lastActivityName = null;
			lastActivityParameters = null;
			return;
		}
		Activity activity = optional.get();
		if (!activity.savesOnLogout()) {
			lastActivityName = null;
			lastActivityParameters = null;
		} else {
			lastActivityName = activity.getClass().getName();
			lastActivityParameters = activity.getParameters();
		}
	}
	
	/**
	 * Ends the activity
	 */
	public void end() {
		this.activity = null;
	}
	
}
