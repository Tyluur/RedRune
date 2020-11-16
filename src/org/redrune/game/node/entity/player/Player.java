package org.redrune.game.node.entity.player;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.SequencialUpdate;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.GameConstants;
import org.redrune.game.content.action.interaction.PlayerCombatAction;
import org.redrune.game.content.activity.impl.pvp.WildernessActivity;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.node.NodeInteractionTask;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.render.NPCRendering;
import org.redrune.game.node.entity.player.data.*;
import org.redrune.game.node.entity.player.link.prayer.Prayer;
import org.redrune.game.node.entity.player.render.PlayerRendering;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.node.item.Item;
import org.redrune.game.world.World;
import org.redrune.game.world.region.RegionManager;
import org.redrune.network.lobby.packet.outgoing.LobbyResponseBuilder;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.PlayerFilePacketOut;
import org.redrune.network.master.utility.Utility;
import org.redrune.network.world.Transmitter;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.world.packet.outgoing.impl.DynamicMapRegionBuilder;
import org.redrune.network.world.packet.outgoing.impl.MapRegionBuilder;
import org.redrune.network.world.packet.outgoing.impl.PlayerOptionPacketBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.utility.rs.constant.HeadIcons.SkullIcon;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * The player that renderable in the game.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class Player extends Entity {
	
	/**
	 * The credentials of the player
	 */
	@Getter
	private final PlayerDetails details;
	
	/**
	 * The skills of the player
	 */
	@Getter
	private final PlayerSkills skills;
	
	/**
	 * The inventory of the player
	 */
	@Getter
	private final PlayerInventory inventory;
	
	/**
	 * The equipment of the player
	 */
	@Getter
	private final PlayerEquipment equipment;
	
	/**
	 * The bank of the player
	 */
	@Getter
	private final PlayerBank bank;
	
	/**
	 * The manager instance
	 */
	@Getter
	private final PlayerManager manager;
	
	/**
	 * The variables of the player that are saved
	 */
	@Getter
	private final PlayerVariables variables;
	
	/**
	 * The combat definitions of the entity. These are saved
	 */
	@Getter
	private final PlayerCombatDefinitions combatDefinitions = new PlayerCombatDefinitions();
	
	/**
	 * The networkSession attached to the player
	 */
	@Getter
	@Setter
	private transient WorldSession session;
	
	/**
	 * The network transmitter object
	 */
	@Getter
	private transient Transmitter transmitter;
	
	/**
	 * The render information object
	 */
	@Getter
	private transient RenderInformation renderInformation;
	
	/**
	 * The path event
	 */
	@Getter
	@Setter
	private transient NodeInteractionTask interactionTask;
	
	public Player(String username) {
		super(GameConstants.HOME_LOCATION);
		this.skills = new PlayerSkills();
		this.details = new PlayerDetails(username);
		this.equipment = new PlayerEquipment();
		this.inventory = new PlayerInventory();
		this.variables = new PlayerVariables();
		this.manager = new PlayerManager();
		this.bank = new PlayerBank();
	}
	
	@Override
	public void register() {
		registerTransients();
		
		World.get().getPlayers().add(this);
		
		transmitter.sendLoginComponents();
		
		// renderable must be after this because of map region building...
		setRenderable(true);
		
		getUpdateMasks().register(new AppearanceUpdate(this));
		SequencialUpdate.getRenderablePlayers().add(this);
		RegionManager.updateEntityRegion(this);
		checkMultiArea();
		
		manager.getContacts().pushLoginStatusChange();
		
		System.out.println("Player registered to game:\t" + this);
	}
	
	@Override
	public void deregister() {
		manager.getActivities().logout();
		setRenderable(false);
		save();
		
		manager.getContacts().sendMyStatusChange(false);
		session.notifyDisconnection(getWorld());
		
		World.get().removePlayer(this);
		RegionManager.updateEntityRegion(this);
		SequencialUpdate.getRenderablePlayers().remove(this);
		
		System.out.println("Player de-registered from game:\t" + this);
	}
	
	@Override
	public Player toPlayer() {
		return this;
	}
	
	@Override
	public int getSize() {
		return 1;
	}
	
	@Override
	public int getMaxHealth() {
		return skills.getLevelForXp(SkillConstants.HITPOINTS) * 10 + equipment.getMaxHealthBoost();
	}
	
	@Override
	public void tick() {
		if (isDead() || isDying()) {
			return;
		}
		super.tick();
		checkInteractionTask();
		getRegion().increaseTimeSpent(this);
		manager.getActions().process();
		manager.getPrayers().process();
		manager.getHintIcons().process();
		manager.getActivities().process();
		processSkull();
		refreshTimers();
	}
	
	@Override
	public String toString() {
		return "[username=" + details.getUsername() + ", loc=" + getLocation() + ", right=" + details.getDominantRight() + "]";
	}
	
	/**
	 * Generates the transient objects (objects which will not save)
	 */
	@Override
	public void registerTransients() {
		super.registerTransients();
		
		this.manager.registerTransients(this);
		this.transmitter = new Transmitter(this);
		this.renderInformation = new RenderInformation(this);
		
		// actual player objects
		this.skills.setPlayer(this);
		this.equipment.setPlayer(this);
		this.inventory.setPlayer(this);
		this.bank.setPlayer(this);
		this.combatDefinitions.setPlayer(this);
	}
	
	@Override
	public void loadMapRegions() {
		boolean wasAtDynamicRegion = isAtDynamicRegion();
		super.loadMapRegions();
		if (isAtDynamicRegion()) {
			transmitter.send(new DynamicMapRegionBuilder().build(this));
			if (!wasAtDynamicRegion) {
				getRenderInformation().getLocalNpcs().clear();
			}
		} else {
			transmitter.send(new MapRegionBuilder(!isRenderable()).build(this));
			if (wasAtDynamicRegion) {
				getRenderInformation().getLocalNpcs().clear();
			}
		}
		removeAttribute(AttributeKey.FORCE_NEXT_MAP_LOAD);
	}
	
	@Override
	public boolean restoreHitPoints() {
		boolean update = super.restoreHitPoints();
		if (update) {
			if (getManager().getPrayers().prayerOn(Prayer.RAPID_HEAL)) {
				super.restoreHitPoints();
			}
			if (getAttribute("resting", false)) {
				super.restoreHitPoints();
			}
			transmitter.refreshHealthPoints();
		}
		return update;
	}
	
	/**
	 * Gets the amount of health points we have
	 */
	@Override
	public int getHealthPoints() {
		return variables.getHealthPoints();
	}
	
	@Override
	public void setHealthPoints(int healthPoints) {
		variables.setHealthPoints(healthPoints);
		transmitter.refreshHealthPoints();
	}
	
	@Override
	public void receiveHit(Hit hit) {
		// only hitsplats we care about are combat ones
		if (!hit.getSplat().isDefaultCombatSplat()) {
			return;
		}
		StaticCombatFormulae.autoRetaliate(hit.getSource(), this);
		// adjust hit so we don't hit too high
		if (hit.getDamage() > getHealthPoints()) {
			hit.setDamage(getHealthPoints());
		}
		// prayers handle the hit first
		manager.getPrayers().handleHit(hit);
		// absorption after prayer so the actual hit isn't affected
		equipment.handleAbsorption(hit);
		// the player is unhittable so we don't do this
		if (!getAttribute("unhittable", false)) {
			// if we should die after damage lands
			if (variables.reduceHealth(hit.getDamage())) {
				checkDeathEvent();
			}
		}
		// we don't remove the attribute when the hit is received in case the damage < 4
		if (getAttribute("cast_veng", false) && hit.getDamage() >= 4) {
			removeAttribute("cast_veng");
			sendForcedChat("Taste vengeance!");
			hit.getSource().getHitMap().applyHit(new Hit(this, (int) Math.floor(hit.getDamage() * 0.75), HitSplat.REGULAR_DAMAGE));
		}
		// the hit is no longer modifiable, the actual damage received will be stored now.
		transmitter.refreshHealthPoints();
	}
	
	@Override
	public boolean fighting() {
		return manager.getActions().getAction() instanceof PlayerCombatAction;
	}
	
	/**
	 * Fire this later
	 */
	@Override
	public void fireDeathEvent() {
		// if the activity should handle death instead
		if (manager.getActivities().handleEntityDeath(this)) {
			return;
		}
		// vars
		final Player player = this;
		final Entity killer = getHitMap().getMostDamageEntity();
		
		// we can't walk anymore
		getMovement().resetWalkSteps();
		// we can't do anything while dying.
		manager.getLocks().lockAll();
		// the event
		SystemManager.getScheduler().schedule(new ScheduledTask(1, 6) {
			@Override
			public void run() {
				if (getTicksPassed() == 1) {
					sendAnimation(836);
				} else if (getTicksPassed() == 2) {
					transmitter.sendMessage("Oh dear, you have died.");
					/*if (source instanceof Player) {
						Player killer = (Player) source;
						killer.setAttackedByDelay(4);
					}*/
				} else if (getTicksPassed() == 5) {
					Item[] kept = WildernessActivity.sendDeathContainer(player, killer);
					
					player.equipment.getItems().clear();
					player.inventory.getItems().clear();
					player.equipment.sendContainer();
					player.inventory.sendContainer();
					player.restoreAll();
					
					teleport(GameConstants.DEATH_LOCATION);
					sendAnimation(-1);
					
					// add the items we should've kept to our inventory...
					if (kept != null) {
						for (Item item : kept) {
							player.inventory.addItem(item.getId(), item.getAmount());
						}
					}
				} else if (getTicksPassed() == 6) {
					// TODO:	getPackets().sendMusicEffect(90);
				}
			}
		});
	}
	
	@Override
	public void restoreAll() {
		skills.restoreAll();
		manager.getHintIcons().removeAll();
		manager.getActions().stopAction();
		variables.setRunEnergy(100);
		setAttackedBy(null);
		removeSkull();
		removeAttribute("dying");
		
		getCombatDefinitions().setSpecialEnergy((byte) 100);
		getCombatDefinitions().setSpecialActivated(false);
		getCombatDefinitions().resetSpells(true);
		unfreeze();
		transmitter.sendSettings();
		
		getUpdateMasks().register(new AppearanceUpdate(this));
		manager.getLocks().unlockAll();
	}
	
	@Override
	public void checkMultiArea() {
		super.checkMultiArea();
		transmitter.send(new CS2ConfigBuilder(616, isAtMultiArea() ? 1 : 0).build(this));
	}
	
	/**
	 * Checks if the interaction task should be removed, after processing it.
	 */
	public void checkInteractionTask() {
		if (interactionTask != null && interactionTask.process(this)) {
			setInteractionTask(null);
		}
	}
	
	/**
	 * Processes the skull
	 */
	private void processSkull() {
		// we must have a skull
		if (variables.getSkullIconTimer() == -1L) {
			return;
		}
		// skull timer has lapsed so we reset it
		if (System.currentTimeMillis() > variables.getSkullIconTimer()) {
			removeSkull();
		}
	}
	
	/**
	 * Refreshes the onscreen timers
	 */
	public void refreshTimers() {
		Long lastTimeCast = getAttribute("LAST_VENG", -1L);
		Long frozenUtil = getAttribute(AttributeKey.FROZEN_UNTIL, -1L);
		boolean showVeng = lastTimeCast != -1 && lastTimeCast + 30_000 > System.currentTimeMillis();
		boolean showFreeze = isFrozen();
		int interfaceId = 614;
		int combatOverlayInterface = manager.getInterfaces().getCombatOverlayInterface();
		if (showVeng || showFreeze) {
			if (combatOverlayInterface == -1) {
				manager.getInterfaces().sendCombatOverlay(interfaceId);
				for (int i = 3; i <= 6; i++) {
					manager.getInterfaces().sendInterfaceComponentChange(interfaceId, i, true);
				}
				manager.getInterfaces().sendInterfaceText(interfaceId, 7, "");
				manager.getInterfaces().sendInterfaceText(interfaceId, 8, "");
			}
		} else {
			if (combatOverlayInterface != -1) {
				manager.getInterfaces().closeCombatOverlay();
			}
		}
		if (combatOverlayInterface == interfaceId) {
			if (showVeng) {
				long difference = (lastTimeCast + 30_000) - System.currentTimeMillis();
				long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
				
				manager.getInterfaces().sendInterfaceComponentChange(interfaceId, 5, false);
				manager.getInterfaces().sendInterfaceText(interfaceId, 8, "" + seconds);
			} else {
				manager.getInterfaces().sendInterfaceComponentChange(interfaceId, 5, true);
				manager.getInterfaces().sendInterfaceText(interfaceId, 8, "");
			}
			if (showFreeze) {
				long difference = frozenUtil - System.currentTimeMillis();
				long seconds = TimeUnit.MILLISECONDS.toSeconds(difference);
				
				manager.getInterfaces().sendInterfaceComponentChange(interfaceId, 4, false);
				manager.getInterfaces().sendInterfaceText(interfaceId, 7, "" + seconds);
			} else {
				
				manager.getInterfaces().sendInterfaceComponentChange(interfaceId, 4, true);
				manager.getInterfaces().sendInterfaceText(interfaceId, 7, "");
			}
		}
	}
	
	/**
	 * Removes the skull
	 */
	private void removeSkull() {
		variables.setSkullIcon(this, SkullIcon.NONE);
		variables.setSkullIconTimer(-1L);
	}
	
	/**
	 * Calls the termination of a player to start, using an attempt-based system
	 */
	public void terminate() {
		terminate(0);
	}
	
	/**
	 * Terminates the player, using an attempt-based system
	 *
	 * @param attempt
	 * 		The attempt
	 */
	private void terminate(final int attempt) {
		if ((isDead() || isDying() || isUnderCombat()) && attempt < 6) {
			System.out.println("scheduled termination because ");
			SystemManager.getScheduler().schedule(new ScheduledTask(16) {
				@Override
				public void run() {
					terminate(attempt + 1);
				}
			});
			return;
		}
		deregister();
	}
	
	/**
	 * Registers a player to the lobby
	 */
	public void registerToLobby() {
		registerTransients();
		World.get().getPlayers().add(this);
		
		manager.getWebManager().handleLogin();
		session.write(new LobbyResponseBuilder().build(this));
		manager.getContacts().sendLogin();
		manager.getContacts().pushLoginStatusChange();
		System.out.println("Player registered to lobby:\t" + this);
	}
	
	/**
	 * Sends the updating required
	 */
	public void sendUpdating() {
		PlayerRendering playerRendering = getAttribute("player_rendering", null);
		NPCRendering npcRendering = getAttribute("npc_rendering", null);
		if (playerRendering == null) {
			putAttribute("player_rendering", playerRendering = new PlayerRendering());
		}
		if (npcRendering == null) {
			putAttribute("npc_rendering", npcRendering = new NPCRendering());
		}
		transmitter.send(playerRendering.build(this));
		transmitter.send(npcRendering.build(this));
	}
	
	/**
	 * Stops specified events
	 *
	 * @param actions
	 * 		If we should stop actions
	 * @param travel
	 * 		If we should stop travels
	 * @param interfaces
	 * 		If we should stop interfaces
	 * @param animations
	 * 		If we should stop animations
	 */
	public void stop(boolean actions, boolean travel, boolean interfaces, boolean animations) {
		if (animations) {
			sendAnimation(-1);
		}
		if (interfaces) {
			manager.getInterfaces().closeAll();
		}
		if (travel) {
			setInteractionTask(null);
			getMovement().resetWalkSteps();
			transmitter.sendMinimapFlagReset();
		}
		if (actions) {
			getManager().getActions().stopAction();
		}
		turnTo(null);
	}
	
	/**
	 * De-registers a player from the lobby
	 */
	public void deregisterLobby() {
		manager.getContacts().sendMyStatusChange(false);
		session.notifyDisconnection(getWorld());
		
		setRenderable(false);
		
		World.get().removePlayer(this);
		System.out.println("Player deregistered from lobby:" + this);
	}
	
	/**
	 * Restores the run energy by 1.
	 */
	public void restoreRunEnergy() {
		if (getMovement().getNextRunDirection() != -1 || getVariables().getRunEnergy() >= 100) {
			return;
		}
		getVariables().setRunEnergy(getVariables().getRunEnergy() + 1);
		transmitter.refreshEnergy();
		transmitter.refreshRunOrbStatus();
	}
	
	/**
	 * Updates the fight area flag
	 */
	public void setInFightArea(boolean inFightArea) {
		variables.setInFightArea(inFightArea);
		session.write(new PlayerOptionPacketBuilder(inFightArea ? "Attack" : "null", true, 1).build(this));
		//	TODO: getPackets().sendPlayerUnderNPCPriority(inFightArea);
	}
	
	/**
	 * This method finds all the items that the player contains on them.
	 */
	public CopyOnWriteArrayList<Item> findContainedItems() {
		CopyOnWriteArrayList<Item> containedItems = new CopyOnWriteArrayList<>();
		for (int i = 0; i < 14; i++) {
			final Item item = equipment.getItem(i);
			if (item != null && item.getId() != -1 && item.getAmount() != -1) {
				containedItems.add(new Item(item.getId(), item.getAmount()));
			}
		}
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItems().get(i);
			if (item != null && item.getId() != -1 && item.getAmount() != -1) {
				containedItems.add(new Item(item.getId(), item.getAmount()));
			}
		}
		return containedItems;
	}
	
	/**
	 * Saves the player
	 */
	public void save() {
		MasterCommunication.write(new PlayerFilePacketOut(details.getUsername(), Utility.getJsonText(this, true)));
	}
	
	/**
	 * Handles the logging out of a player
	 *
	 * @param lobby
	 * 		If the player should be sent to the lobby
	 */
	public void logout(boolean lobby) {
		long currentTime = System.currentTimeMillis();
		if (getAttackedByDelay() + 10000 > currentTime) {
			transmitter.sendUnrepeatingMessages("You can't log out until 10 seconds after the end of combat.");
			return;
		}
		transmitter.sendLogout(lobby);
	}
	
	/**
	 * Sets the skull for the duration
	 *
	 * @param icon
	 * 		The icon
	 * @param delay
	 * 		The delay
	 */
	public void setSkull(SkullIcon icon, long delay) {
		variables.setSkullIcon(this, icon);
		variables.setSkullIconTimer(System.currentTimeMillis() + delay);
	}
	
	/**
	 * Sets data for the first time an account is made
	 */
	public void setCreationData() {
		manager.getActivities().setDefaultActivity();
		bank.setDefaultBank();
	}
}