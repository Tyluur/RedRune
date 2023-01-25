package org.redrune.game.node.entity.npc;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.data.NPCCharacteristics;
import org.redrune.game.node.entity.npc.data.NPCCombatDefinitions;
import org.redrune.game.node.entity.npc.link.CombatManager;
import org.redrune.game.node.entity.npc.link.DropManager;
import org.redrune.game.node.entity.npc.render.flag.impl.TransformationUpdate;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.prayer.DrainPrayer;
import org.redrune.game.node.entity.player.link.prayer.Prayer;
import org.redrune.game.node.entity.player.link.prayer.PrayerEffectRepository;
import org.redrune.game.node.entity.player.link.prayer.PrayerManager;
import org.redrune.game.node.item.Drop;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.World;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.repository.npc.characteristic.NPCCharacteristicRepository;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.constant.Directions.Direction;
import org.redrune.utility.rs.constant.NPCConstants;
import org.redrune.utility.tool.Misc;
import org.redrune.utility.tool.RandomFunction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.redrune.game.node.entity.player.link.prayer.Prayer.SOULSPLIT;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class NPC extends Entity {
	
	/**
	 * The location we were spawned at
	 */
	@Getter
	private final Location spawnLocation;
	
	/**
	 * The instance of the combat manager
	 */
	@Getter
	private final CombatManager combatManager;
	
	/**
	 * The id of the npc
	 */
	@Getter
	@Setter
	private int id;
	
	/**
	 * The direction the npc is facing
	 */
	@Getter
	@Setter
	private int faceDirection;
	
	/**
	 * The amount of health points the npc has
	 */
	@Getter
	private int healthPoints = 0;
	
	/**
	 * The cache definitions of the npc
	 */
	@Setter
	private NPCDefinition definitions;
	
	/**
	 * The characteristics of the npc
	 */
	@Getter
	@Setter
	private NPCCharacteristics characteristics;
	
	/**
	 * If the npc respawns
	 */
	@Getter
	@Setter
	private boolean respawnable = true;
	
	/**
	 * If run is toggled
	 */
	@Getter
	@Setter
	private boolean runToggled;
	
	/**
	 * If we are force walking
	 */
	@Getter
	@Setter
	private boolean forceWalking;
	
	/**
	 * The type of walking this npc does
	 */
	@Getter
	@Setter
	private int walkType;
	
	/**
	 * Constructs a new {@code Entity}
	 *
	 * @param id
	 * 		The id of the npc
	 */
	public NPC(int id, Location location, Direction direction) {
		super(location);
		// necessary for all npcs
		this.id = id;
		this.spawnLocation = location;
		this.faceDirection = direction.ordinal();
		this.characteristics = NPCCharacteristicRepository.getCharacteristics(this);
		this.combatManager = new CombatManager(this);
		setHealthPoints(getMaxHealth());
		// required for map region data
		super.registerTransients();
		super.loadMapRegions();
		loadWalkType();
		// e won't ever be in combat with an npc that doesn't have an attack option
		setHealthPoints(getMaxHealth());
		// used to update the flags
		RegionManager.updateEntityRegion(this);
		checkMultiArea();
	}
	
	/**
	 * Gets the {@link NPCDefinition}s of this npc
	 */
	public NPCDefinition getDefinitions() {
		if (definitions == null) {
			definitions = NPCDefinitionParser.forId(id);
		}
		return definitions;
	}
	
	@Override
	public int getMaxHealth() {
		return getCombatDefinitions().getHitpoints();
	}
	
	@Override
	public void setHealthPoints(int healthPoints) {
		this.healthPoints = healthPoints;
	}
	
	@Override
	public void receiveHit(Hit hit) {
		// only hit splats we care about are combat ones
		if (!hit.getSplat().isDefaultCombatSplat()) {
			return;
		}
		StaticCombatFormulae.autoRetaliate(hit.getSource(), this);
		// adjust hit so we don't hit too high
		if (hit.getDamage() > getHealthPoints()) {
			hit.setDamage(getHealthPoints());
		}
		// if our hp is so low that we're dead
		boolean dead = (healthPoints = healthPoints - hit.getDamage()) <= 0;
		// fires death event
		if (dead) {
			checkDeathEvent();
		}
	}
	
	@Override
	public boolean fighting() {
		return combatManager.getCombat().fighting();
	}
	
	@Override
	public void fireDeathEvent() {
		resetMasks();
		sendAnimation(getCombatDefinitions().getDeathAnim());
		getMovement().resetWalkSteps();
		int goalTicks = getCombatDefinitions().getDeathDelay();
		if (goalTicks <= 1) {
			goalTicks = 2;
		}
		SystemManager.getScheduler().schedule(new ScheduledTask(1, goalTicks) {
			
			@Override
			public void run() {
				if (getTicksPassed() == 1) {
					sendAnimation(getCombatDefinitions().getDeathAnim());
				} else if (getTicksPassed() == getGoalTicks()) {
					deregister();
					sendDrops();
					reset();
					restoreAll();
					respawn();
				}
			}
		});
	}
	
	/**
	 * Resets the components
	 */
	public void reset() {
		resetMasks();
	}
	
	@Override
	public void restoreAll() {
		setHealthPoints(getMaxHealth());
		sendAnimation(-1);
		removeAttribute("dying");
	}
	
	/**
	 * Gets the combat definitions of the npc. <p>This value is never null because on construct we always set a npc
	 * combat definition regardless of whether they exist in file.</p>
	 */
	public NPCCombatDefinitions getCombatDefinitions() {
		return characteristics.getCombatDefinitions().get(getId());
	}
	
	@Override
	public void tick() {
		super.tick();
		// before anything else we must make sure we aren't dead
		if (isDead() || isDying()) {
			//System.out.println("Skipped " + this + " [hp=" + getHealthPoints() + ", max=" + getMaxHealth() + ", dead=" + isDead() + ", dying=" + isDying() + "]");
			return;
		}
		// we are in combat
		if (combatManager.getCombat().process()) {
			return;
		}
		// we couldn't set a target
		if (fireAggressiveCheck()) {
			return;
		}
		// we are frozen
		if (isFrozen()) {
			return;
		}
		// we can walk back home
		if (getLocation().getDistance(spawnLocation) > 16) {
			traverseOriginalTile();
		} else if (getWalkType() != NPCConstants.NO_WALK) {
			int walkValue = getWalkType() & NPCConstants.NORMAL_WALK;
			// if the npc isn't a type that walks, we ignore the next blocks
			if (walkValue == 0) {
				return;
			}
			// we don't randomly walk if we're interacting
			if (getInteractionManager().hasInteraction()) {
				return;
			}
			// if we're lucky & the npc is a type that walks
			boolean randomWalk = Math.random() * 1000.0 < 100.0;
			// make sure we should walk
			if (!randomWalk) {
				return;
			}
			int moveX = getRandomTileDistance();
			int moveY = getRandomTileDistance();
			// reset the steps
			getMovement().resetWalkSteps();
			// add the random tile to the movement queue [we only walk around the spawn location]
			getMovement().addWalkSteps(getSpawnLocation().getX() + moveX, getSpawnLocation().getY() + moveY);
		}
	}
	
	/**
	 * Walks back to the original tile
	 */
	public void traverseOriginalTile() {
		// we walk back to the original tile [1 tile per tick]
		combatManager.getCombat().reset();
		// calculates and paths to the spawn loc
		getMovement().addLocationPath(spawnLocation, 25, true);
		// we can't walk back home in a good amount of time
		if (!canWalkHomeTimely()) {
			setForceWalking(true);
			// within 16 ticks [about 10 seconds] we arrive at the spawn location
			SystemManager.getScheduler().schedule(new ScheduledTask(16) {
				@Override
				public void run() {
					teleport(spawnLocation);
					setForceWalking(false);
				}
			});
		}
	}
	
	/**
	 * If we can walk home at a good time
	 */
	private boolean canWalkHomeTimely() {
		int distance = getLocation().getDistance(spawnLocation);
		return distance <= 16;
	}
	
	@Override
	public String toString() {
		return "[id=" + id + ", name=" + getDefinitions().getName() + ", location=" + getLocation() + "]";
	}
	
	@Override
	public boolean attackable(Entity entity) {
		// if we don't have an attack option
		if (!combatManager.isHasAttackOption()) {
			return false;
		}
		// TODO implement slayer requirements in the npc classes
		return true;
	}
	
	@Override
	protected void checkDeathEvent() {
		// we won't ever die if we don't have an attack option
		if (!combatManager.isHasAttackOption()) {
			return;
		}
		super.checkDeathEvent();
	}
	
	@Override
	public void register() {
		getRegion().addEntity(this);
		
		setRenderable(true);
	}
	
	@Override
	public void deregister() {
		removeAttackedByDelay(getHitMap().getMostDamageEntity());
		getRegion().removeEntity(this);
		World.get().getNpcs().remove(this);
		
		setRenderable(false);
	}
	
	@Override
	public NPC toNPC() {
		return this;
	}
	
	@Override
	public int getSize() {
		return getDefinitions().getSize();
	}
	
	/**
	 * Fires the respawn event
	 *
	 * @param id
	 * 		The id of the npc respawning
	 * @param delay
	 * 		The delay to wait
	 * @param location
	 * 		The location to respawn them
	 * @param direction
	 * 		The direction to face when re-spawned
	 */
	private static void fireRespawnEvent(int id, int delay, Location location, Direction direction) {
		SystemManager.getScheduler().schedule(new ScheduledTask(delay) {
			@Override
			public void run() {
				World.get().addNPC(id, location, direction);
			}
		});
	}
	
	/**
	 * Sends the item drops to the ground. We find the player who did the most damage to us in order to find who to
	 * display the drops to.
	 */
	private void sendDrops() {
		Entity mostDamageEntity = getHitMap().getMostDamageEntity();
		// the killer wasn't set for some reason
		if (mostDamageEntity == null) {
			return;
		}
		// TODO: familiar did the damage so we set the killer to the owner's familiar
		if (!(mostDamageEntity.isPlayer())) {
			System.out.println("mostDamage = " + mostDamageEntity);
			return;
		}
		Player killer = mostDamageEntity.toPlayer();
		List<Drop> dropList = DropManager.generateDrops(killer, this, characteristics.getDrops());
		// TODO: send drops to clan
		dropList.forEach(drop -> sendDrop(killer, drop));
		// sends the charm drop
		dropCharms(killer);
	}
	
	/**
	 * Drops a random charm
	 *
	 * @param killer
	 * 		The killer
	 */
	private void dropCharms(Player killer) {
		List<Drop> charms = characteristics.getCharmDrops();
		if (charms.isEmpty()) {
			return;
		}
		Collections.shuffle(charms);
		double random = (RandomFunction.getRandomDouble(99) + 1) / 100;
		for (Drop charm : charms) {
			if (random <= charm.getRate()) {
				sendDrop(killer, charm);
				break;
			}
		}
	}
	
	/**
	 * Sends the drop to the floor for the killer
	 *
	 * @param killer
	 * 		The killer
	 * @param drop
	 * 		The drop
	 */
	private void sendDrop(Player killer, Drop drop) {
		Item item;
		// if the item is stack-able we generate a different amount
		if (ItemDefinitionParser.forId(drop.getItemId()).isStackable()) {
			item = new Item(drop.getItemId(), Misc.random(drop.getMinAmount(), drop.getMaxAmount()));
		} else {
			item = new Item(drop.getItemId(), drop.getMinAmount() + Misc.getRandom(drop.getExtraAmount()));
		}
		RegionManager.addFloorItem(item.getId(), item.getAmount(), 200, getDropTile(), killer.getDetails().getUsername());
	}
	
	/**
	 * Gets the tile we should drop the items on
	 */
	private Location getDropTile() {
		return new Location(getLocation().getCoordFaceX(getSize()), getLocation().getCoordFaceY(getSize()), getLocation().getPlane());
	}
	
	/**
	 * Gets the middle world tile
	 */
	public Location getMiddleWorldTile() {
		int size = getSize();
		return new Location(getLocation().getCoordFaceX(size), getLocation().getCoordFaceY(size), getLocation().getPlane());
	}
	
	/**
	 * Finds the possible targets we can fight
	 *
	 * @param npcs
	 * 		If we should add npcs to the  list
	 * @param players
	 * 		If we should add players to the list
	 */
	private List<Entity> possibleTargets(boolean npcs, boolean players) {
		List<Entity> targets = new ArrayList<>();
		boolean atWild = PvPLocation.isAtWild(getLocation());
		int maxDistance = getSize() * 2;
		Region region = getRegion();
		if (players) {
			for (Player player : region.getPlayers()) {
				if (player == null || !player.isRenderable()) {
					continue;
				}
				// no longer aggressive
				if (region.getTimeSpent(player) > NPCConstants.UNAGGRESSIVE_REGION_TIME) {
					continue;
				}
				// Most monsters in the wilderness are aggressive regardless of level
				if (!getCombatManager().isAggressiveForced() && !atWild && player.getSkills().getCombatLevelWithSummoning() >= getCombatLevel() * 2) {
					continue;
				}
				// check that we can attack them regardless of multi player flags
				boolean single = !getCombatManager().isForceMultiAttacked() && (!isAtMultiArea() || !player.isAtMultiArea());
				if (single && (player.getAttackedBy() != this && (player.getAttackedByDelay() > System.currentTimeMillis() || player.getAttribute(AttributeKey.FIND_TARGET_DELAY, -1L) > System.currentTimeMillis()))) {
					continue;
				}
				// out of bounds
				if (!Misc.isOnRange(getLocation().getX(), getLocation().getY(), getSize(), player.getLocation().getX(), player.getLocation().getY(), player.getSize(), getCombatManager().getFindTargetRadius() > 0 ? getCombatManager().getFindTargetRadius() : maxDistance)) {
					continue;
				}
				// we cant clip to them
				if (!getMovement().clippedProjectileToNode(player, false)) {
					continue;
				}
				targets.add(player);
			}
		}
		if (npcs) {
			for (NPC npc : region.getNpcs()) {
				if (npc == null || !npc.isRenderable() || !npc.getDefinitions().hasAttackOption()) {
					continue;
				}
				// the multi area check
				if ((!isAtMultiArea() || !npc.isAtMultiArea()) && npc.getAttackedBy() != this && npc.getAttackedByDelay() > System.currentTimeMillis()) {
					continue;
				}
				// out of bounds
				if (!Misc.isOnRange(getLocation().getX(), getLocation().getY(), getSize(), npc.getLocation().getX(), npc.getLocation().getY(), npc.getSize(), getCombatManager().getFindTargetRadius() > 0 ? getCombatManager().getFindTargetRadius() : maxDistance)) {
					continue;
				}
				// if we can't clip to that target
				if (!getMovement().clippedProjectileToNode(npc, false)) {
					continue;
				}
				targets.add(npc);
			}
		}
		return targets;
	}
	
	/**
	 * Gets the combat level of this npc
	 */
	public int getCombatLevel() {
		return definitions.getCombatLevel();
	}
	
	/**
	 * Attempts to start combat with the best entity we could find to start combat with, if we are an npc who initiates
	 * fights.
	 */
	private boolean fireAggressiveCheck() {
		if (!PvPLocation.isAtWild(getLocation()) && !getCombatManager().isAggressiveForced()) {
			NPCCombatDefinitions combatDefinitions = getCombatDefinitions();
			if (combatDefinitions.getAggressivenessType() == NPCConstants.PASSIVE_AGGRESSIVE) {
				return false;
			}
		}
		List<Entity> possibleTargets = possibleTargets(false, true);
		// we don't want to check if the list is empty
		if (possibleTargets.isEmpty()) {
			return false;
		}
		// find the target
		Entity target = possibleTargets.get(RandomFunction.random(possibleTargets.size()));
		// sets the attacked by def
		target.setAttackedBy(this);
		// can't be found as target for the next 10 secs
		target.putAttribute(AttributeKey.FIND_TARGET_DELAY, System.currentTimeMillis() + 10_000);
		// starts the fight with the target
		startFight(target);
		return true;
	}
	
	/**
	 * Starts a fight with the target
	 *
	 * @param target
	 * 		The target
	 */
	public void startFight(Entity target) {
		// we won't fight if we are force walking
		if (forceWalking) {
			return;
		}
		combatManager.getCombat().setTarget(target);
	}
	
	/**
	 * Gets the bonus at an index. <p>To find out the sorting of the bonuses list, see {@link
	 * org.redrune.utility.rs.constant.BonusConstants} order. NPC bonuses only reach the 10th index [range defence]. The
	 * bonuses are always defined because on construct we set the bonuses regardless of whether they exist in file.</p>
	 *
	 * @param index
	 * 		The index
	 */
	public int getBonus(int index) {
		int[] bonuses = getBonuses();
		// verifying that we're in bounds
		if (index < 0 || index >= bonuses.length) {
			return 0;
		} else {
			return bonuses[index];
		}
	}
	
	/**
	 * Loads the walk type from the cache
	 */
	private void loadWalkType() {
		int forceWalkType = NPCConstants.getChangedWalkType(id);
		int type = (forceWalkType == -1 ? getDefinitions().getWalkMask() : forceWalkType);
		// sets the type, with priority to the one forced
		setWalkType(type);
	}
	
	/**
	 * Gets the random distance that the npc will walk around. For instance, most npcs walk around an area of 10 tiles,
	 * in some cases we want to reduce this range so the npc is always in the scope of players who teleport to that
	 * region [npcs that move around but still want to be seen]
	 */
	private int getRandomTileDistance() {
		switch (id) {
			case 6138:
				return RandomFunction.random(-1, 1);
			case 14332:
				return RandomFunction.random(-2, 2);
			case 1263:
			case 961:
				return RandomFunction.random(-3, 3);
			default:
				return RandomFunction.random(-5, 5);
		}
	}
	
	/**
	 * Gets the bonuses array, maxed length of 10.
	 */
	public int[] getBonuses() {
		return characteristics.getBonuses().get(getId());
	}
	
	/**
	 * Transforms the npc to a different one
	 *
	 * @param targetId
	 * 		The id of the npc to transform into
	 */
	public void transform(int targetId) {
		try {
			getUpdateMasks().register(new TransformationUpdate(targetId));
			
			setId(targetId);
			setDefinitions(NPCDefinition.readDefinitions(targetId));
			setCharacteristics(NPCCharacteristicRepository.getCharacteristics(this));
			setHealthPoints(getMaxHealth());
			combatManager.setHasAttackOption(definitions.hasAttackOption());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Respawns the npc
	 */
	private void respawn() {
		// only fire the respawn event for a respawnable npc
		if (isRespawnable()) {
			int respawnDelay = getCombatDefinitions().getRespawnDelay();
			// ensure the respawn delay is set well
			if (respawnDelay <= 0) {
				respawnDelay = 30;
			}
			fireRespawnEvent(getId(), respawnDelay, spawnLocation, Direction.values()[faceDirection]);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NPC) {
			NPC n = (NPC) obj;
			return n.getId() == id && n.getIndex() == getIndex();
		}
		return super.equals(obj);
	}
	
	/**
	 * Handles the prayer effects
	 *
	 * @param hit
	 * 		The hit
	 */
	public void handlePrayerEffects(Hit hit) {
		if (!hit.getSource().isPlayer()) {
			return;
		}
		Player source = hit.getSource().toPlayer();
		// no prayers on
		if (source.getManager().getPrayers().getPrayersActiveCount() == 0) {
			return;
		}
		// apply soulsplit only when a hit lands
		if (hit.getDamage() > 0 && source.getManager().getPrayers().prayerOn(SOULSPLIT)) {
			PrayerManager.handleSoulsplit(this, hit);
		}
		// the instance of the sources prayer
		PrayerManager sourcePrayer = source.getManager().getPrayers();
		
		// we only want to find the drain prayers
		List<Prayer> drainers = sourcePrayer.getActivePrayers().stream().filter(Prayer::isDrainer).collect(Collectors.toList());
		
		// the message to sent
		String message = null;
		
		// loops through all the drain prayers
		for (Prayer prayer : drainers) {
			// the chance for the prayer to effect, saps have a higher chance
			int chance = prayer.isSap() ? 6 : 8;
			
			// calculate chance based on whether its a leech or a sap
			boolean shouldUse = Misc.getRandom(chance) == chance - 1;
			
			// if we aren't lucky enough to use the effect
			if (!shouldUse) {
				continue;
			}
			
			// the optional drain instance
			Optional<DrainPrayer> optional = PrayerEffectRepository.getDrainPrayer(prayer);
			if (!optional.isPresent()) {
				System.out.println("Unable to find drain for " + prayer);
				continue;
			}
			// the drain prayer
			DrainPrayer drain = optional.get();
			// if the source has maxed its drain and the receiver has reached its least bonuses
			if (sourcePrayer.maxed(null, prayer, drain.raiseSource())) {
				message = ("Your opponent has been weakened so much that your " + (prayer.isSap() ? "sap" : "leech") + " curse has no effect.");
			} else {
				source.sendAnimation(drain.startAnimationId());
				if (drain.startGraphicsId() > 0) {
					source.sendGraphics(drain.startGraphicsId());
				}
				sourcePrayer.modify(null, sourcePrayer, drain.prayerSlots(), drain.amounts(), drain.drainCap(), drain.raiseCap(), drain.raiseSource());
				sourcePrayer.visualizeLeech(source, this, drain.projectileId(), drain.landingGraphicsId());
			}
		}
		if (message != null) {
			source.getTransmitter().sendMessage(message, false);
		}
	}
	
}
