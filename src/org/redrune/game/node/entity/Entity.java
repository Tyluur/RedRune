package org.redrune.game.node.entity;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.AnimationDefinitionParser;
import org.redrune.cache.parse.definition.AnimationDefinition;
import org.redrune.game.GameFlags;
import org.redrune.game.node.Location;
import org.redrune.game.node.Node;
import org.redrune.game.node.entity.data.EntityDetails;
import org.redrune.game.node.entity.data.EntityHitMap;
import org.redrune.game.node.entity.data.EntityMovement;
import org.redrune.game.node.entity.link.EntityInteractionManager;
import org.redrune.game.node.entity.link.EntityPoisonManager;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.prayer.Prayer;
import org.redrune.game.node.entity.render.UpdateMasks;
import org.redrune.game.node.entity.render.flag.impl.*;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.backend.Priority;
import org.redrune.utility.tool.RandomFunction;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public abstract class Entity extends Node implements EntityDetails {
	
	/**
	 * The size of the map
	 */
	@Getter
	@Setter
	private int mapSize = 0;
	
	/**
	 * If we're at a multi area
	 */
	@Getter
	@Setter
	private boolean atMultiArea;
	
	/**
	 * The poison manager
	 */
	@Getter
	private EntityPoisonManager poisonManager = new EntityPoisonManager();
	
	/**
	 * The {@code HitMap} {@code Object} instance for this entity
	 */
	@Getter
	private transient EntityHitMap hitMap;
	
	/**
	 * The index of the entity
	 */
	@Getter
	@Setter
	private transient int index;
	
	/**
	 * The map of temporary attributes
	 */
	private transient ConcurrentHashMap<Object, Object> attributes;
	
	/**
	 * The instance of the update masks
	 */
	@Getter
	private transient UpdateMasks updateMasks;
	
	/**
	 * The travel manager
	 */
	@Getter
	private transient EntityMovement movement;
	
	/**
	 * The region ids we are in
	 */
	@Getter
	private transient CopyOnWriteArrayList<Integer> mapRegionsIds;
	
	/**
	 * The last region the player was in.
	 */
	@Getter
	@Setter
	private transient Region lastRegion;
	
	/**
	 * The location the player was at when the maps were last loaded
	 */
	@Getter
	@Setter
	private transient Location lastLoadedLocation;
	
	/**
	 * If we're at a dynamic region
	 */
	@Getter
	@Setter
	private transient boolean isAtDynamicRegion;
	
	/**
	 * The entity who we were last attacked by
	 */
	@Getter
	@Setter
	private transient Entity attackedBy;
	
	/**
	 * The attacked by delay
	 */
	@Getter
	@Setter
	private transient long attackedByDelay;
	
	/**
	 * The interaction manager
	 */
	@Getter
	private transient EntityInteractionManager interactionManager;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param location
	 * 		The location of the entity
	 */
	protected Entity(Location location) {
		super(location);
	}
	
	@Override
	public void tick() {
		checkDeathEvent();
		if (isDead() || isDying()) {
			movement.resetWalkSteps();
			return;
		}
		movement.processMovement();
		poisonManager.processPoison();
	}
	
	@Override
	public String toString() {
		return "Entity{" + "index=" + index + ", isPlayer=" + isPlayer() + ", lastRegion=" + lastRegion + ", lastLoadedLocation=" + lastLoadedLocation + '}';
	}
	
	/**
	 * Registers all transient variables
	 */
	public void registerTransients() {
		this.hitMap = new EntityHitMap(this);
		this.updateMasks = new UpdateMasks();
		this.attributes = new ConcurrentHashMap<>();
		this.movement = new EntityMovement(this);
		this.mapRegionsIds = new CopyOnWriteArrayList<>();
		this.interactionManager = new EntityInteractionManager();
		this.poisonManager.setEntity(this);
		this.interactionManager.setEntity(this);
	}
	
	/**
	 * Removes an attribute from the {@link #attributes} map, if it doesn't exist, the default value is returned,
	 *
	 * @param key
	 * 		The key of the attribute to remove
	 * @param defaultValue
	 * 		The default value to return
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(Object key, T defaultValue) {
		T value = (T) attributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Loads all the map regions
	 */
	public void loadMapRegions() {
		mapRegionsIds.clear();
		isAtDynamicRegion = false;
		int chunkX = getLocation().getRegionX();
		int chunkY = getLocation().getRegionY();
		int mapHash = Location.VIEWPORT_SIZES[0] >> 4;
		int minRegionX = (chunkX - mapHash) / 8;
		int minRegionY = (chunkY - mapHash) / 8;
		for (int xCalc = minRegionX < 0 ? 0 : minRegionX; xCalc <= ((chunkX + mapHash) / 8); xCalc++) {
			for (int yCalc = minRegionY < 0 ? 0 : minRegionY; yCalc <= ((chunkY + mapHash) / 8); yCalc++) {
				// the id of the region
				int regionId = yCalc + (xCalc << 8);
				// the region will only load if we are a player
				final Region region = isPlayer() ? RegionManager.getRegionAndLoad(regionId) : RegionManager.getRegion(regionId);
				// if the region is dynamic
				if (region.isDynamic()) {
					isAtDynamicRegion = true;
				}
				mapRegionsIds.add(regionId);
			}
		}
		lastLoadedLocation = new Location(getLocation().getX(), getLocation().getY(), getLocation().getPlane());
	}
	
	/**
	 * If the client needs a map update.
	 */
	public boolean needsMapUpdate() {
		int lastMapRegionX = lastLoadedLocation.getRegionX();
		int lastMapRegionY = lastLoadedLocation.getRegionY();
		int regionX = getLocation().getRegionX();
		int regionY = getLocation().getRegionY();
		int size = ((Location.VIEWPORT_SIZES[0] >> 3) / 2) - 1;
		return Math.abs(lastMapRegionX - regionX) >= size || Math.abs(lastMapRegionY - regionY) >= size;
	}
	
	/**
	 * Teleports to a destination
	 *
	 * @param destination
	 * 		The destination
	 */
	public void teleport(Location destination) {
		putAttribute(AttributeKey.TELEPORT_LOCATION, destination);
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <T> T putAttribute(Object key, T value) {
		attributes.put(key, value);
		return value;
	}
	
	/**
	 * If we are currently teleporting in the game tick.
	 */
	public boolean teleporting() {
		return getAttribute(AttributeKey.TELEPORTED, false);
	}
	
	/**
	 * Gets the attribute from the {@link #attributes} map, and if it doesn't exist, we return the default value
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param defaultValue
	 * 		The default value
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key, T defaultValue) {
		T value = (T) attributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Resets the mask defaults
	 */
	public void resetMasks() {
		sendAnimation(-1);
		sendGraphics(-1);
		turnTo(null);
	}
	
	/**
	 * Sends an animation mask
	 *
	 * @param animationId
	 * 		The id of the animation
	 */
	public void sendAnimation(int animationId) {
		updateMasks.register(new Animation(animationId, 0, isNPC(), Priority.NORMAL));
		//	lastAnimationEnd = Utils.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		AnimationDefinition definition = AnimationDefinitionParser.forId(animationId);
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends a graphic mask
	 *
	 * @param graphicsId
	 * 		The id of the graphic
	 */
	public void sendGraphics(int graphicsId) {
		updateMasks.register(new Graphic(graphicsId, 0, 0, isNPC()));
	}
	
	/**
	 * Turns this entity to the locked on entity.
	 *
	 * @param entity
	 * 		The locked on entity.
	 */
	public void turnTo(Entity entity) {
		int index = entity == null ? -1 : entity.getClientIndex();
		updateMasks.register(new FaceEntityUpdate(index, isNPC()));
	}
	
	/**
	 * Gets the client index of the entity.
	 *
	 * @return The client index.
	 */
	private int getClientIndex() {
		if (isPlayer()) {
			return index + 0x8000;
		}
		return index;
	}
	
	/**
	 * Sends an animation
	 *
	 * @param animation
	 * 		The animation
	 */
	public void sendAnimation(Animation animation) {
		updateMasks.register(animation);
		//	lastAnimationEnd = Utils.currentTimeMillis() + AnimationDefinitions.getAnimationDefinitions(nextAnimation.getIds()[0]).getEmoteTime();
		AnimationDefinition definition = AnimationDefinitionParser.forId(animation.getId());
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends an animation that makes sure that we are no longer animation
	 *
	 * @param animationId
	 * 		The animation
	 */
	public void sendAwaitedAnimation(int animationId) {
		updateMasks.register(new Animation(animationId, 0, isNPC(), Priority.LOWEST));
		
		AnimationDefinition definition = AnimationDefinitionParser.forId(animationId);
		if (definition != null) {
			updateMasks.setLastAnimationEndTime(System.currentTimeMillis() + definition.getEmoteTime());
		}
	}
	
	/**
	 * Sends an animation mask
	 *
	 * @param animationId
	 * 		The id of the animation
	 * @param speed
	 * 		The speed of the animation
	 * @param priority
	 * 		The priority of the animation
	 */
	public void sendAnimation(int animationId, int speed, Priority priority) {
		updateMasks.register(new Animation(animationId, speed, isNPC(), priority));
	}
	
	/**
	 * Sends a graphics mask
	 *
	 * @param graphicsId
	 * 		The id of the graphic
	 * @param height
	 * 		The height to send the graphic
	 * @param speed
	 * 		The speed to send the graphic
	 */
	public void sendGraphics(int graphicsId, int height, int speed) {
		updateMasks.register(new Graphic(graphicsId, height, speed, isNPC()));
	}
	
	/**
	 * Sends the force text message mask
	 *
	 * @param message
	 * 		The message to send
	 */
	public void sendForcedChat(String message) {
		updateMasks.register(new ForceTextUpdate(message, isNPC()));
	}
	
	/**
	 * If we are dead
	 */
	public boolean isDead() {
		return getHealthPoints() <= 0;
	}
	
	/**
	 * If we are currently dying
	 */
	public boolean isDying() {
		return getAttribute("dying", false);
	}
	
	/**
	 * Gets the world the entity is on
	 */
	public byte getWorld() {
		return GameFlags.worldId;
	}
	
	/**
	 * Checks if we are attackable by an entity..
	 *
	 * @param entity
	 * 		The player
	 */
	public boolean attackable(Entity entity) {
		return true;
	}
	
	/**
	 * Restores the entity's hitpoints by 1
	 */
	public boolean restoreHitPoints() {
		int maxHp = getMaxHealth();
		int healthPoints = getHealthPoints();
		if (healthPoints > maxHp) {
			if (isPlayer()) {
				Player player = (Player) this;
				if (player.getManager().getPrayers().prayerOn(Prayer.BERSERKER) && RandomFunction.getRandom(100) <= 15) {
					return false;
				}
			}
			setHealthPoints(healthPoints - 1);
			return true;
		} else if (healthPoints < maxHp) {
			setHealthPoints(healthPoints + 1);
			if (isPlayer()) {
				Player player = toPlayer();
				if (player.getManager().getPrayers().prayerOn(Prayer.RAPID_HEAL) && healthPoints < maxHp) {
					setHealthPoints(healthPoints + 1);
				} else if (player.getManager().getPrayers().prayerOn(Prayer.RAPID_RENEWAL) && healthPoints < maxHp) {
					setHealthPoints(healthPoints + (healthPoints + 4 > maxHp ? maxHp - healthPoints : 4));
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Gets the hitpoints of the entity
	 */
	public int getHealthPoints() {
		return (isPlayer() ? toPlayer().getHealthPoints() : toNPC().getHealthPoints());
	}
	
	/**
	 * Checks if we were in combat recently.
	 */
	public boolean combatRecently() {
		long lastTimeHit = getAttribute(AttributeKey.ATTACKED_BY_TIME, -1L);
		return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTimeHit) <= 10;
	}
	
	/**
	 * Freezes the entity for the amount of ticks
	 *
	 * @param by
	 * 		The entity we were frozen by
	 * @param milliseconds
	 * 		The amount of ticks
	 * @param message
	 * 		The message to send when we're frozen
	 */
	public void freeze(Entity by, long milliseconds, String message) {
		// time we will be unfrozen at
		final long frozenUntil = System.currentTimeMillis() + milliseconds;
		// storing the time
		putAttribute(AttributeKey.FROZEN_UNTIL, frozenUntil);
		// they can't be frozen again instantly.
		putAttribute(AttributeKey.FREEZE_DELAY, frozenUntil + TimeUnit.SECONDS.toMillis(3));
		// stores who froze us [16 tile calc]
		putAttribute(AttributeKey.FROZEN_BY, by);
		// sending the message
		if (isPlayer()) {
			toPlayer().getTransmitter().sendMessage(message, false);
		}
		movement.resetWalkSteps();
	}
	
	/**
	 * Entities cannot be frozen instantly after they have once been frozen. The {@link AttributeKey#FREEZE_DELAY}
	 * attribute stores the delay time for said variable.
	 */
	public boolean freezeDelayed() {
		// when freezing is delayed until
		long delay = getAttribute(AttributeKey.FREEZE_DELAY, -1L);
		// the current tick we're on
		long ticks = System.currentTimeMillis();
		return delay > ticks;
	}
	
	/**
	 * Checks if we are frozen
	 */
	public boolean isFrozen() {
		// who froze us
		Entity frozenBy = getAttribute(AttributeKey.FROZEN_BY);
		// person we're frozen by is too far away or they are no longer existent
		if (frozenBy == null || !frozenBy.isRenderable() || !frozenBy.getLocation().withinDistance(getLocation(), 16)) {
			return false;
		} else {
			// if the time till we're unfrozen has lapsed
			boolean lapsed = System.currentTimeMillis() > getAttribute(AttributeKey.FROZEN_UNTIL, -1L);
			// if it has
			if (lapsed) {
				return false;
			}
			// otherwise we are not frozen
			return true;
		}
	}
	
	/**
	 * Gets an attribute from the {@link #attributes} map
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T getAttribute(Object key) {
		return (T) attributes.get(key);
	}
	
	/**
	 * Unfreezes the entity
	 */
	public void unfreeze() {
		removeAttribute(AttributeKey.FROZEN_BY);
		removeAttribute(AttributeKey.FROZEN_UNTIL);
		removeAttribute(AttributeKey.FREEZE_DELAY);
	}
	
	/**
	 * Removes an attribute from the {@link #attributes} map
	 *
	 * @param key
	 * 		The key
	 * @param <T>
	 * 		The return type
	 */
	@SuppressWarnings("unchecked")
	public <T> T removeAttribute(Object key) {
		if (attributes.containsKey(key)) {
			return (T) attributes.remove(key);
		} else {
			return null;
		}
	}
	
	/**
	 * Checks to see if we're dead (lifepoints <=0 ), and if we are, and we haven't yet started the death event, we
	 * start it.
	 */
	protected void checkDeathEvent() {
		// if we arent dead
		if (!isDead()) {
			return;
		}
		// avoid duplicates of this method being fired
		if (isDying()) {
			return;
		}
		// flag the death
		putAttribute("dying", true);
		// send the death
		fireDeathEvent();
	}
	
	/**
	 * Healths the amount
	 *
	 * @param amount
	 * 		The amount
	 */
	public void heal(int amount) {
		heal(amount, 0);
	}
	
	/**
	 * Heals an amount
	 *
	 * @param amount
	 * 		The amount
	 * @param boost
	 * 		The boost health amount
	 */
	public void heal(int amount, int boost) {
		final int adjusted = getHealthPoints() + amount;
		final int boostAdjust = getMaxHealth() + boost;
		setHealthPoints(adjusted >= boostAdjust ? boostAdjust : adjusted);
	}
	
	/**
	 * Forces the entity to be in a multi area
	 *
	 * @param forced
	 * 		If it should be forced
	 */
	public void setMultiAreaForce(boolean forced) {
		putAttribute(AttributeKey.FORCE_MULTI_AREA, forced);
		checkMultiArea();
	}
	
	/**
	 * Checks if we're at a multi area
	 */
	public void checkMultiArea() {
		boolean atMultiArea = RegionManager.isMultiArea(getLocation());
		// the default return type is false so only set it if its true
		if (multiAreaForced()) {
			atMultiArea = true;
		}
		if (atMultiArea != this.atMultiArea) {
			// TODO: FIRE MULTI EVENT LISTENER
			// this.atMultiArea = atMultiArea;
		}
		this.atMultiArea = atMultiArea;
	}
	
	/**
	 * If the multi area is forced on the entity
	 */
	public Boolean multiAreaForced() {
		return getAttribute(AttributeKey.FORCE_MULTI_AREA, false);
	}
	
	/**
	 * Adds attacked by information
	 *
	 * @param target
	 * 		The entity who attacked us
	 */
	public void addAttackedByDelay(Entity target) {
		setAttackedBy(target);
		if (isNPC()) {
			setAttackedByDelay(System.currentTimeMillis() + toNPC().getCombatDefinitions().getAttackDelay() * 600 + 600);
		} else {
			setAttackedByDelay(System.currentTimeMillis() + 8000);
		}
		putAttribute(AttributeKey.ATTACKED_BY_TIME, System.currentTimeMillis());
	}
	
	/**
	 * Removes attacked by information
	 *
	 * @param entity
	 * 		The entity who attacked us
	 */
	public void removeAttackedByDelay(Entity entity) {
		if (entity == null) {
			return;
		}
		setAttackedBy(null);
		setAttackedByDelay(-1L);
	}
	
	/**
	 * Turns the entity to the object
	 *
	 * @param object
	 * 		The object to turn to
	 */
	public void turnToObject(GameObject object) {
		//		boolean flipped = object.getRotation() == 0 || object.getRotation() == 2;
		
		int xDiff = 0;
		int yDiff = 0;
		
		// levers
		if (object.getType() == 4) {
			xDiff = -1;
		}
		
		Location faceLocation = object.getLocation().transform(xDiff, yDiff, 0);
		getUpdateMasks().register(new FaceLocationUpdate(this, faceLocation));
	}
	
	/**
	 * Turns the entity to a location
	 *
	 * @param location
	 * 		The location
	 */
	public void turnToLocation(Location location) {
		getUpdateMasks().register(new FaceLocationUpdate(this, location));
	}
	
	/**
	 * If we were in combat recently
	 */
	public boolean isUnderCombat() {
		return getAttackedByDelay() + 10000 >= System.currentTimeMillis();
	}
}