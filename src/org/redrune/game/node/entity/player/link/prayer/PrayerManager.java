package org.redrune.game.node.entity.player.link.prayer;

import lombok.Getter;
import lombok.Setter;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.ScheduledTask;
import org.redrune.game.content.ProjectileManager;
import org.redrune.game.node.entity.Entity;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2ConfigBuilder;
import org.redrune.network.world.packet.outgoing.impl.ConfigFilePacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.Projectile;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.HeadIcons.PrayerIcon;
import org.redrune.utility.rs.constant.PrayerConstants;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.tool.Misc;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static org.redrune.game.node.entity.player.link.prayer.Prayer.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public final class PrayerManager implements SkillConstants, PrayerConstants {
	
	/**
	 * The list of prayers that are active
	 */
	@Getter
	private final CopyOnWriteArraySet<Prayer> activePrayers = new CopyOnWriteArraySet<>();
	
	/**
	 * The set of quick prayers
	 */
	@Getter
	private final CopyOnWriteArraySet<Prayer> quickPrayers = new CopyOnWriteArraySet<>();
	
	/**
	 * The book the player is using
	 */
	@Getter
	private PrayerBook book = PrayerBook.REGULAR;
	
	/**
	 * The modifiers that affect bonus rates because of leech/sap prayers
	 */
	@Getter
	private double[] modifiers = new double[5];
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * If quick prayers are being set
	 */
	private transient boolean settingQuickPrayers;
	
	/**
	 * The id of the head icon
	 */
	@Getter
	private transient PrayerIcon icon = PrayerIcon.NONE;
	
	/**
	 * The next drain information per prayer
	 */
	private transient long[] nextDrain;
	
	/**
	 * Sends login configurations for prayer books
	 */
	public void sendLoginConfigurations() {
		this.nextDrain = new long[30];
		sendPrayerSelection();
		sendBook();
		resetStatAdjustments();
	}
	
	/**
	 * Sends the prayer selection tab, this is based on a config.
	 */
	private void sendPrayerSelection() {
		player.getTransmitter().send(new CS2ConfigBuilder(181, settingQuickPrayers ? 1 : 0).build(player));
	}
	
	/**
	 * Sends the prayer book we're on
	 */
	private void sendBook() {
		// closes all the prayers we have
		activePrayers.forEach(prayer -> closePrayers(prayer, settingQuickPrayers));
		// shows the right book
		player.getTransmitter().send(new ConfigPacketBuilder(1584, book == PrayerBook.CURSES ? 1 : 0).build(player));
		// allows us to click on the prayer interface
		sendAccessMasks();
	}
	
	/**
	 * Refreshes the stat adjustment panel beneath the prayers
	 */
	private void resetStatAdjustments() {
		for (int skillSlot = 0; skillSlot < 5; skillSlot++) {
			adjustStat(skillSlot, 0);
		}
	}
	
	/**
	 * Sends the prayer book access masks
	 */
	private void sendAccessMasks() {
		if (settingQuickPrayers) {
			player.getTransmitter().send(new AccessMaskBuilder(271, 42, 0, 2, 0, 29).build(player));
		} else {
			player.getTransmitter().send(new AccessMaskBuilder(271, 8, 0, 2, 0, 30).build(player));
		}
	}
	
	/**
	 * Modifies the stat adjustment on the client
	 *
	 * @param stat
	 * 		The stat
	 * @param percentage
	 * 		The percentage
	 */
	public void adjustStat(int stat, int percentage) {
		player.getTransmitter().send(new ConfigFilePacketBuilder(6857 + stat, 30 + percentage).build(player));
	}
	
	/**
	 * Sets the book the player is using
	 *
	 * @param book
	 * 		The book
	 */
	public void setBook(PrayerBook book) {
		this.book = book;
		sendBook();
		updateHeadIcon();
		refreshActivatedConfigs();
	}
	
	/**
	 * Handles the setting of prayers
	 *
	 * @param componentId
	 * 		The component clicked
	 * @param slotId
	 * 		The slot clicked
	 */
	public void handlePrayerSettings(int componentId, int slotId) {
		if (componentId == 8) {
			Optional<Prayer> optional = findPrayerBySlot(slotId, book);
			if (!optional.isPresent()) {
				System.out.println("Unable to find prayer... [" + componentId + "," + slotId + "]");
				return;
			}
			if (getPrayerPoints() <= 0) {
				player.getTransmitter().sendMessage("You have ran out of prayer points, you must recharge at an altar.", false);
				return;
			}
			Prayer prayer = optional.get();
			if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < prayer.getPrayerLevelRequired()) {
				player.getTransmitter().sendMessage("You need a prayer level of " + prayer.getPrayerLevelRequired() + " to use " + prayer.getName() + ".", false);
				return;
			}
			if (!prayer.canActivate(player)) {
				return;
			}
			// deactivates if active, otherwise returns false
			// and we turn on the prayer
			boolean activate = false;
			if (!deactivatePrayer(prayer)) {
				closePrayers(prayer, false);
				activePrayers.add(prayer);
				activate = true;
			}
			// so the prayer was activated
			if (activate) {
				prayer.activate(player);
				resetDrainPrayer(prayer.getSlotId());
			}
			refreshActivatedConfigs();
			updateHeadIcon();
		} else if (componentId == 42) {
			Optional<Prayer> optional = findPrayerBySlot(slotId, book);
			if (!optional.isPresent()) {
				System.out.println("Unable to find prayer... [" + componentId + "," + slotId + "]");
				return;
			}
			Prayer prayer = optional.get();
			if (player.getSkills().getLevelForXp(SkillConstants.PRAYER) < prayer.getPrayerLevelRequired()) {
				player.getTransmitter().sendMessage("You need a prayer level of " + prayer.getPrayerLevelRequired() + " to use " + prayer.getName() + ".", false);
				return;
			}
			if (!prayer.canActivate(player)) {
				return;
			}
			// if we're not removing it, we're adding it
			if (!removeQuickPrayer(prayer)) {
				closePrayers(prayer, true);
				quickPrayers.add(prayer);
			}
			refreshActivatedConfigs();
		} else if (componentId == 43) {
			settingQuickPrayers = false;
			sendPrayerSelection();
			if (settingQuickPrayers) {
				player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
			}
			sendAccessMasks();
		}
	}
	
	/**
	 * Gets the amount of prayer points
	 */
	public int getPrayerPoints() {
		return player.getVariables().getPrayerPoints();
	}
	
	/**
	 * Deactivates a prayer if it is active
	 *
	 * @param prayer
	 * 		The prayer
	 * @return {@code Boolean.TRUE} if it was active and we deactivated it.
	 */
	private boolean deactivatePrayer(Prayer prayer) {
		if (prayerOn(prayer)) {
			activePrayers.remove(prayer);
			return true;
		}
		return false;
	}
	
	/**
	 * Closes the prayers that this prayer requires closed
	 *
	 * @param prayer
	 * 		The prayer
	 * @param useQuickPrayers
	 * 		If we are closing quick prayers
	 */
	private void closePrayers(Prayer prayer, boolean useQuickPrayers) {
		int[][] close = prayer.getPrayersToClose();
		Set<Prayer> prayerSet = new TreeSet<>();
		for (int[] closeArray : close) {
			for (int closeId : closeArray) {
				Optional<Prayer> optional = findPrayerBySlot(closeId, prayer.getBook());
				if (!optional.isPresent()) {
					System.out.println("Unable to find prayer by slot " + closeId);
					continue;
				}
				prayerSet.add(optional.get());
			}
		}
		if (prayerSet.isEmpty()) {
			return;
		}
		for (Prayer toClose : prayerSet) {
			((useQuickPrayers ? quickPrayers : activePrayers)).remove(toClose);
		}
	}
	
	/**
	 * Resets the prayer drain statistics
	 *
	 * @param index
	 * 		The index of the prayer
	 */
	private void resetDrainPrayer(int index) {
		long duration = (long) (System.currentTimeMillis() + (PrayerConstants.DRAIN_RATES[book.ordinal()][index] * 1000));
		double bonus = player.getEquipment().getBonus(BonusConstants.PRAYER_BONUS);
		nextDrain[index] = (long) (duration + (bonus * 50));
	}
	
	/**
	 * Refreshes the configs for activated prayers. This will show the colour behind the prayer indicating that it's
	 * on.
	 */
	private void refreshActivatedConfigs() {
		int value = 0;
		for (Prayer prayer : (settingQuickPrayers ? quickPrayers : activePrayers)) {
			if (prayer.getBook() != book) {
				continue;
			}
			value += prayer.getActivationConfig();
		}
		int configId = book == PrayerBook.CURSES ? (settingQuickPrayers ? 1587 : 1582) : (settingQuickPrayers ? 1397 : 1395);
		player.getTransmitter().send(new ConfigPacketBuilder(configId, value).build(player));
		// sets the orb to on/off
		if (!settingQuickPrayers) {
			player.getTransmitter().send(new CS2ConfigBuilder(182, value == 0 ? 0 : 1).build(player));
		}
		refreshStatAdjustments();
	}
	
	/**
	 * Updates the head icon
	 */
	private void updateHeadIcon() {
		if (book == PrayerBook.REGULAR) {
			if (prayerOn(PROTECT_FROM_SUMMONING)) {
				if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.MAGIC_SUMMONING);
					return;
				} else if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.RANGE_SUMMONING);
					return;
				} else if (prayersAreActive(PROTECT_FROM_SUMMONING, PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.MELEE_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.SUMMONING);
					return;
				}
			} else {
				if (prayerOn(PROTECT_FROM_MAGIC)) {
					setIcon(PrayerIcon.PROTECT_FROM_MAGIC);
					return;
				} else if (prayerOn(PROTECT_FROM_MISSILES)) {
					setIcon(PrayerIcon.PROTECT_FROM_RANGE);
					return;
				} else if (prayerOn(PROTECT_FROM_MELEE)) {
					setIcon(PrayerIcon.PROTECT_FROM_MELEE);
					return;
				} else if (prayerOn(RETRIBUTION)) {
					setIcon(PrayerIcon.RETRIBUTION);
					return;
				} else if (prayerOn(REDEMPTION)) {
					setIcon(PrayerIcon.REDEMPTION);
					return;
				} else if (prayerOn(SMITE)) {
					setIcon(PrayerIcon.SMITE);
					return;
				}
			}
		} else {
			if (prayerOn(DEFLECT_SUMMONING)) {
				if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE_AND_SUMMONING);
					return;
				} else if (prayersAreActive(DEFLECT_SUMMONING, DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE_AND_SUMMONING);
					return;
				} else {
					setIcon(PrayerIcon.DEFLECT_SUMMONING);
					return;
				}
			} else {
				if (prayerOn(DEFLECT_MAGIC)) {
					setIcon(PrayerIcon.DEFLECT_MAGIC);
					return;
				} else if (prayerOn(DEFLECT_MISSILES)) {
					setIcon(PrayerIcon.DEFLECT_RANGE);
					return;
				} else if (prayerOn(DEFLECT_MELEE)) {
					setIcon(PrayerIcon.DEFLECT_MELEE);
					return;
				} else if (prayerOn(WRATH)) {
					setIcon(PrayerIcon.WRATH);
					return;
				} else if (prayerOn(SOULSPLIT)) {
					setIcon(PrayerIcon.SOULSPLIT);
					return;
				}
			}
		}
		setIcon(PrayerIcon.NONE);
	}
	
	/**
	 * If the quick prayer was removed
	 *
	 * @param prayer
	 * 		The prayer to remove
	 */
	private boolean removeQuickPrayer(Prayer prayer) {
		return quickPrayers.remove(prayer);
	}
	
	/**
	 * Checks if a prayer is currently on
	 *
	 * @param prayer
	 * 		The prayer
	 */
	public boolean prayerOn(Prayer prayer) {
		return activePrayers.contains(prayer);
	}
	
	/**
	 * Refreshes the stat adjustments
	 */
	private void refreshStatAdjustments() {
		restoreModifiers();
		for (int skillSlot = 0; skillSlot < modifiers.length; skillSlot++) {
			double boost = 0;
			switch (skillSlot) {
				case ATTACK_SLOT:
					boost += getBasePrayerBoost(ATTACK);
					break;
				case STRENGTH_SLOT:
					boost += getBasePrayerBoost(STRENGTH);
					break;
				case DEFENCE_SLOT:
					boost += getBasePrayerBoost(DEFENCE);
					break;
				case RANGE_SLOT:
					boost += getBasePrayerBoost(RANGE);
					break;
				case MAGIC_SLOT:
					boost += getBasePrayerBoost(MAGIC);
					break;
			}
			// the real value to send
			double amount = (modifiers[skillSlot] * 100) + (boost - 1);
			adjustStat(skillSlot, (int) amount);
		}
	}
	
	/**
	 * Checks if a list of prayers are active
	 *
	 * @param prayers
	 * 		The prayers
	 */
	public boolean prayersAreActive(Prayer... prayers) {
		for (Prayer prayer : prayers) {
			if (!prayerOn(prayer)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Sets the icon
	 *
	 * @param icon
	 * 		The icon to set
	 */
	public void setIcon(PrayerIcon icon) {
		this.icon = icon;
		player.getUpdateMasks().register(new AppearanceUpdate(player));
	}
	
	/**
	 * Restores the modifiers at a fair tick rate.
	 */
	private void restoreModifiers() {
		long lastRestoreTick = player.getAttribute("last_prayer_modifier_time", -1L);
		if (lastRestoreTick == -1 || SystemManager.getUpdateWorker().getTicks() - lastRestoreTick >= 25) {
			for (int i = 0; i < modifiers.length; i++) {
				if (modifiers[i] < 0) {
					modifiers[i] += 0.01;
					if (modifiers[i] > 0) {
						modifiers[i] = 0;
					}
				} else if (modifiers[i] > 0) {
					modifiers[i] -= 0.01;
					if (modifiers[i] < 0) {
						modifiers[i] = 0;
					}
				}
			}
			player.putAttribute("last_prayer_modifier_time", SystemManager.getUpdateWorker().getTicks());
		}
	}
	
	/**
	 * Gets the prayer boost for a skill, including the modifiers from the leeches [if applicable]
	 *
	 * @param skill
	 * 		The skill
	 */
	public double getBasePrayerBoost(int skill) {
		double bonus = 1.0;
		switch (skill) {
			case ATTACK:
				if (prayerOn(CLARITY_OF_THOUGHT)) {
					bonus += 0.05;
				} else if (prayerOn(IMPROVED_REFLEXES)) {
					bonus += 0.10;
				} else if (prayerOn(INCREDIBLE_REFLEXES)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.15;
				} else if (prayerOn(PIETY)) {
					bonus += 0.20;
				} else if (prayerOn(LEECH_ATTACK)) {
					bonus += 0.05;
				} else if (prayerOn(TURMOIL)) {
					bonus += 0.15;
				}
				break;
			case STRENGTH:
				if (prayerOn(BURST_OF_STRENGTH)) {
					bonus += 0.05;
				} else if (prayerOn(SUPERHUMAN_STRENGTH)) {
					bonus += 0.10;
				} else if (prayerOn(ULTIMATE_STRENGTH)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.18;
				} else if (prayerOn(PIETY)) {
					bonus += 0.23;
				} else if (prayerOn(LEECH_STRENGTH)) {
					bonus += 0.05;
				} else if (prayerOn(TURMOIL)) {
					bonus += 0.23;
				}
				break;
			case DEFENCE:
				if (prayerOn(THICK_SKIN)) {
					bonus += 0.05;
				} else if (prayerOn(ROCK_SKIN)) {
					bonus += 0.10;
				} else if (prayerOn(STEEL_SKIN)) {
					bonus += 0.15;
				} else if (prayerOn(CHIVALRY)) {
					bonus += 0.15;
				} else if (prayerOn(PIETY)) {
					bonus += 0.20;
				} else if (prayerOn(RIGOUR)) {
					bonus += 0.25;
				} else if (prayerOn(LEECH_DEFENCE)) {
					bonus += 0.05;
				} else if (prayerOn(TURMOIL)) {
					bonus += 0.15;
				}
				break;
			case RANGE:
				if (prayerOn(SHARP_EYE)) {
					bonus += 0.05;
				} else if (prayerOn(HAWK_EYE)) {
					bonus += 0.10;
				} else if (prayerOn(EAGLE_EYE)) {
					bonus += 0.15;
				} else if (prayerOn(RIGOUR)) {
					bonus += 0.20;
				} else if (prayerOn(LEECH_RANGED)) {
					bonus += 0.05;
				}
				break;
			case MAGIC:
				if (prayerOn(MYSTIC_WILL)) {
					bonus += 0.05;
				} else if (prayerOn(MYSTIC_LORE)) {
					bonus += 0.10;
				} else if (prayerOn(MYSTIC_MIGHT)) {
					bonus += 0.15;
				} else if (prayerOn(LEECH_MAGIC)) {
					bonus += 0.05;
				}
				break;
		}
		bonus += getSkillLeechModifier(skill);
		return bonus;
	}
	
	/**
	 * Gets the skill's leech modifier
	 *
	 * @param skill
	 * 		The skill
	 */
	private double getSkillLeechModifier(int skill) {
		switch (skill) {
			case ATTACK:
				return modifiers[ATTACK_SLOT];
			case STRENGTH:
				return modifiers[STRENGTH_SLOT];
			case DEFENCE:
				return modifiers[DEFENCE_SLOT];
			case RANGE:
				return modifiers[RANGE_SLOT];
			case MAGIC:
				return modifiers[MAGIC_SLOT];
			default:
				return 0;
		}
	}
	
	/**
	 * Restores the player's prayer dta
	 */
	public void restore() {
		modifiers = new double[5];
		player.getVariables().setPrayerPoints(player.getSkills().getLevelForXp(SkillConstants.PRAYER) * 10);
		refreshPrayerPoints();
		refreshStatAdjustments();
	}
	
	/**
	 * Refreshes the players prayer points
	 */
	private void refreshPrayerPoints() {
		player.getTransmitter().send(new ConfigPacketBuilder(2382, player.getVariables().getPrayerPoints()).build(player));
	}
	
	/**
	 * This is used to process the draining of prayers
	 */
	public void process() {
		refreshStatAdjustments();
		if (getPrayersActiveCount() == 0) {
			return;
		}
		long time = System.currentTimeMillis();
		int drain = 0;
		int bonus = player.getEquipment().getBonus(BonusConstants.PRAYER_BONUS);
		int hatId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_HAT);
		if (hatId >= 18744 && hatId <= 18746) {
			bonus += 15;
		}
		for (Prayer prayer : activePrayers) {
			int index = prayer.getSlotId();
			long drainTimer = nextDrain[index];
			if (drainTimer == 0 || drainTimer > time) {
				continue;
			}
			int rate = (int) ((PrayerConstants.DRAIN_RATES[book.ordinal()][index] * 1000) + (bonus * 50));
			int passedTime = (int) (time - drainTimer);
			drain++;
			int count = 0;
			while (passedTime >= rate && count++ < 10) {
				drain++;
				passedTime -= rate;
			}
			nextDrain[index] = (time + rate) - passedTime;
		}
		if (drain > 0) {
			drainPrayer(drain);
		}
	}
	
	/**
	 * Toggles the quick prayer setting
	 */
	public void toggleQuickPrayers() {
		if (getPrayerPoints() <= 0) {
			player.getTransmitter().sendMessage("You have ran out of prayer points.");
			return;
		}
		if (getPrayersActiveCount() != 0) {
			activePrayers.forEach(this::deactivatePrayer);
			updateHeadIcon();
			refreshActivatedConfigs();
			return;
		}
		if (getQuickPrayersCount() == 0) {
			player.getTransmitter().sendMessage("You have no quick prayers to activate.");
			return;
		}
		List<Prayer> quickPrayers = this.quickPrayers.stream().filter(prayer -> prayer.getBook().equals(book)).collect(Collectors.toList());
		quickPrayers.forEach(prayer -> {
			closePrayers(prayer, false);
			activePrayers.add(prayer);
			prayer.activate(player);
			resetDrainPrayer(prayer.getSlotId());
			refreshActivatedConfigs();
			updateHeadIcon();
		});
	}
	
	/**
	 * Gets the amount of prayers that are active
	 */
	public int getPrayersActiveCount() {
		return activePrayers.size();
	}
	
	/**
	 * Gets the amount of quick prayers we have
	 */
	public int getQuickPrayersCount() {
		return quickPrayers.size();
	}
	
	/**
	 * Handles the select quick prayers button
	 */
	public void selectQuickPrayers() {
		settingQuickPrayers = !settingQuickPrayers;
		sendPrayerSelection();
		// switchs tab to prayer
		if (settingQuickPrayers) {
			player.getTransmitter().send(new CS2ConfigBuilder(168, 6).build(player));
		}
		sendAccessMasks();
	}
	
	/**
	 * Handles the prayer aspect of a hit being received
	 *
	 * @param hit
	 * 		The hit object
	 */
	public void handleHit(Hit hit) {
		if (hit.getSource().isPlayer() && hit.getSource().toPlayer().getManager().getPrayers().prayerOn(SMITE)) { // smite
			int drain = hit.getDamage() / 4;
			if (drain > 0) {
				drainPrayer(drain);
			}
		} else {
			if (hit.getDamage() == 0) {
				return;
			}
			handleDeflects(hit);
		}
	}
	
	/**
	 * Drains the prayer by the amount
	 *
	 * @param amount
	 * 		The amount to drain
	 * @return {@code Boolean.TRUE} if the prayers were all closed
	 */
	private boolean drainPrayer(int amount) {
		final int points = getPrayerPoints();
		int newAmount = points - amount;
		if (newAmount < 0) {
			newAmount = 0;
		}
		player.getVariables().setPrayerPoints(newAmount);
		refreshPrayerPoints();
		if (newAmount == 0 && points > 0) {
			activePrayers.forEach(this::deactivatePrayer);
			player.getTransmitter().sendMessage("You have ran out of prayer points.");
			updateHeadIcon();
			refreshActivatedConfigs();
			return true;
		}
		return false;
	}
	
	/**
	 * Restores a given amount of prayer points
	 *
	 * @param amount
	 * 		The amount to restore
	 */
	public void restorePrayer(int amount) {
		int maxPrayer = player.getSkills().getLevelForXp(PRAYER) * 10;
		if ((getPrayerPoints() + amount) <= maxPrayer) {
			player.getVariables().setPrayerPoints(getPrayerPoints() + amount);
		} else {
			player.getVariables().setPrayerPoints(maxPrayer);
		}
		refreshPrayerPoints();
	}
	
	/**
	 * Handles the deflection of combat prayers
	 *
	 * @param hit
	 * 		The hit
	 */
	private void handleDeflects(Hit hit) {
		Entity hitter = hit.getSource();
		switch (hit.getSplat()) {
			case MELEE_DAMAGE:
				if (prayerOn(PROTECT_FROM_MELEE)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
				} else if (prayerOn(DEFLECT_MELEE)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendGraphics((2230));
						player.sendAnimation(12573);
					}
				}
				break;
			case RANGE_DAMAGE:
				if (prayerOn(PROTECT_FROM_MISSILES)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
				} else if (prayerOn(DEFLECT_MISSILES)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendAnimation(12573);
						player.sendGraphics((2229));
					}
				}
				break;
			case MAGIC_DAMAGE:
				if (prayerOn(PROTECT_FROM_MAGIC)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					break;
				} else if (prayerOn(DEFLECT_MAGIC)) {
					hit.setDamage(getHitPrayerMultiplier(hitter.isPlayer(), hit.getDamage()));
					int deflectedDamage = (int) (hit.getDamage() * 0.1);
					if (deflectedDamage > 0) {
						hit.getSource().getHitMap().applyHit(new Hit(player, deflectedDamage, HitSplat.REFLECTED_DAMAGE));
						player.sendGraphics((2228));
						player.sendAnimation(12573);
					}
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * Handles leech prayers
	 *
	 * @param hit
	 * 		The hit
	 */
	public void handlePrayerEffects(Hit hit) {
		// soulsplit effect doesnt matter if hit  dint land
		if (hit.getDamage() > 0 && hit.getSource().isPlayer() && hit.getSource().toPlayer().getManager().getPrayers().prayerOn(SOULSPLIT)) {
			handleSoulsplit(player, hit);
		}
		// leeches only apply to players
		// and when the hit lands
		if (!hit.getSource().isPlayer() || hit.getDamage() == 0) {
			return;
		}
		// the instance of the source [to player object]
		Player source = hit.getSource().toPlayer();
		
		// the instance of the sources prayer
		PrayerManager sourcePrayer = source.getManager().getPrayers();
		
		// we only want to find the drain prayers
		List<Prayer> drainers = sourcePrayer.activePrayers.stream().filter(Prayer::isDrainer).collect(Collectors.toList());
		
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
			if (sourcePrayer.maxed(source, prayer, drain.raiseSource()) && maxed(source, prayer, false)) {
				// so we dont spam the chatbox
				message = ("Your opponent has been weakened so much that your " + (prayer.isSap() ? "sap" : "leech") + " curse has no effect.");
			} else {
				source.sendAnimation(drain.startAnimationId());
				if (drain.startGraphicsId() > 0) {
					source.sendGraphics(drain.startGraphicsId());
				}
				modify(source, sourcePrayer, drain.prayerSlots(), drain.amounts(), drain.drainCap(), drain.raiseCap(), drain.raiseSource());
				visualizeLeech(source, player, drain.projectileId(), drain.landingGraphicsId());
			}
			//			System.out.println("receiver{" + Arrays.toString(modifiers) + "},source{" + Arrays.toString(sourcePrayer.modifiers) + "}");
		}
		if (message != null) {
			source.getTransmitter().sendMessage(message, false);
		}
	}
	
	/**
	 * Handles the soulsplit prayer
	 *
	 * @param receiver
	 * 		The entity who received the soulsplit effect
	 * @param hit
	 * 		The hit that landed
	 */
	public static void handleSoulsplit(Entity receiver, Hit hit) {
		Entity source = hit.getSource();
		// actual modifiers
		source.heal((int) (hit.getDamage() * 0.05));
		// drains 10 prayer points if the receiver is a player
		if (receiver.isPlayer()) {
			receiver.toPlayer().getManager().getPrayers().drainPrayer(10);
		}
		
		// the speed of the projectiles
		int speed = ProjectileManager.getSpeedModifier(receiver, source) - 10;
		// the projectile from the player who soulsplitted me to me
		Projectile from = new Projectile(receiver, source, 2263, 11, 5, 10, speed, 10, 0);
		// the projectile from me to the player who soulsplitted me
		Projectile to = new Projectile(source, receiver, 2263, 11, 5, 10, speed, 10, 0);
		// sending the projectiles
		ProjectileManager.sendProjectile(from);
		// a tick after, the next visual effects are done
		int projectileDelay = ProjectileManager.getProjectileDelay(receiver, source);
		// add to the delay
		projectileDelay += ProjectileManager.getDelay(receiver, source, projectileDelay, 0);
		SystemManager.getScheduler().schedule(new ScheduledTask(projectileDelay) {
			@Override
			public void run() {
				ProjectileManager.sendProjectile(to);
				source.sendGraphics(2264);
			}
		});
	}
	
	/**
	 * Gets the hit after the prayer multiplier
	 *
	 * @param player
	 * 		If the receiver is a player
	 * @param damage
	 * 		The amount of damage
	 */
	private int getHitPrayerMultiplier(boolean player, int damage) {
		return (int) (damage * (player ? 0.6D : 0D));
	}
	
	/**
	 * Checks if the prayer's modifiers are maxed
	 *
	 * @param drainTo
	 * 		The player who we're draining to
	 * @param prayer
	 * 		The prayer
	 * @param increasing
	 * 		If the modifiers are increasing or decreasing
	 */
	public boolean maxed(Player drainTo, Prayer prayer, boolean increasing) {
		final double attackModif = modifiers[ATTACK_SLOT];
		final double strengthModif = modifiers[STRENGTH_SLOT];
		final double defenceModif = modifiers[DEFENCE_SLOT];
		final double rangeModif = modifiers[RANGE_SLOT];
		final double magicModif = modifiers[MAGIC_SLOT];
		switch (prayer) {
			case SAP_WARRIOR:
				if (!increasing) {
					if (attackModif <= -0.20 && strengthModif <= 0.20 && defenceModif <= -0.20) {
						return true;
					}
				}
				break;
			case SAP_RANGER:
				if (!increasing && rangeModif <= -0.20) {
					return true;
				}
				break;
			case SAP_MAGE:
				if (!increasing && magicModif <= -0.20) {
					return true;
				}
				break;
			case SAP_SPIRIT:
			case LEECH_SPECIAL_ATTACK:
				if (increasing) {
					if (drainTo != null && drainTo.getCombatDefinitions().getSpecialEnergy() >= 100) {
						return true;
					}
				} else {
					if (player.getCombatDefinitions().getSpecialEnergy() <= 0) {
						return true;
					}
				}
				break;
			case LEECH_ATTACK:
				if (increasing) {
					if (attackModif >= 0.10) {
						return true;
					}
				} else {
					if (attackModif <= -0.25) {
						return true;
					}
				}
				break;
			case LEECH_RANGED:
				if (increasing) {
					if (rangeModif >= 0.10) {
						return true;
					}
				} else if (rangeModif <= -0.25) {
					return true;
				}
				break;
			case LEECH_MAGIC:
				if (increasing) {
					if (magicModif >= 0.10) {
						return true;
					}
				} else if (magicModif <= -0.25) {
					return true;
				}
				break;
			case LEECH_DEFENCE:
				if (increasing) {
					if (defenceModif >= 0.10) {
						return true;
					}
				} else if (defenceModif <= -0.25) {
					return true;
				}
				break;
			case LEECH_STRENGTH:
				if (increasing) {
					if (strengthModif >= 0.10) {
						return true;
					}
				} else if (strengthModif <= -0.25) {
					return true;
				}
				break;
			case LEECH_ENERGY:
				if (increasing) {
					if (drainTo != null && drainTo.getVariables().getRunEnergy() >= 100) {
						return true;
					}
				} else {
					return player.getVariables().getRunEnergy() <= 0;
				}
				break;
			default:
				break;
		}
		return false;
	}
	
	/**
	 * Modifies the prayer {@link #modifiers}
	 *
	 * @param raiser
	 * 		The prayer manager of the player who hit us
	 * @param slots
	 * 		The slots of the prayer that's draining
	 * @param amounts
	 * 		The amounts to drain
	 * @param drainCap
	 * 		The max amount our drain can be set to by this prayer effect
	 */
	public void modify(Player p2, PrayerManager raiser, int[] slots, double[] amounts, double drainCap, double raiseCap, boolean raise) {
		String type = "";
		for (int i = 0; i < slots.length; i++) {
			int slot = slots[i];
			if (slot == ENERGY_SLOT || slot == SPECIAL_ENERGY_SLOT) {
				type = slot == ENERGY_SLOT ? "energy" : "special_energy";
				continue;
			}
			final double amount = amounts[i];
			
			// p2 will only be null in the case of npc leeches
			// if this is not checked we will not have any modifiers when leeching from npcs [same + and - value]
			if (p2 != null) {
				// the player who was hit will receive a reduction
				modifiers[slot] -= amount;
				// if we pass the max amount, we have to make sure it never is out of bounds
				if (modifiers[slot] <= -drainCap) {
					modifiers[slot] = -drainCap;
				}
			}
			
			// some prayers will only have the drain effect and not raise the source's modifiers
			if (raise) {
				// the raiser will receive a boost amount
				raiser.modifiers[slot] += amount;
				System.out.println("raised " + slot + " by " + amount);
				
				// if we pass the max amount, we have to make sure it never is out of bounds [2]
				if (raiser.modifiers[slot] >= raiseCap) {
					raiser.modifiers[slot] = raiseCap;
				}
			}
		}
		switch (type) {
			case "energy":
				double energy = player.getVariables().getRunEnergy();
				final double reductionAmount = energy * amounts[0];
				double newTotal = reductionAmount + (p2 == null ? 100 : p2.getVariables().getRunEnergy());
				if (newTotal >= 100) {
					newTotal = 100;
				}
				// TODO verify this works
				if (p2 != null) {
					player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() - reductionAmount);
					p2.getVariables().setRunEnergy(newTotal);
					player.getTransmitter().refreshEnergy();
					p2.getTransmitter().refreshEnergy();
				} else {
					player.getVariables().setRunEnergy(newTotal);
					player.getTransmitter().refreshEnergy();
				}
				break;
			case "special_energy":
				byte specAmount = player.getCombatDefinitions().getSpecialEnergy();
				final double specReduced = specAmount * amounts[0];
				double newSpec = specReduced + (p2 == null ? 100 : p2.getCombatDefinitions().getSpecialEnergy());
				if (newSpec >= 100) {
					newSpec = 100;
				}
				// TODO verify this works
				if (p2 != null) {
					p2.getCombatDefinitions().setSpecialEnergy((byte) newSpec);
					player.getCombatDefinitions().reduceSpecial((int) specReduced);
				} else {
					player.getCombatDefinitions().reduceSpecial((int) -specReduced);
				}
				break;
		}
	}
	
	/**
	 * Sends the prayer projectile to the target
	 *
	 * @param source
	 * 		The source
	 * @param target
	 * 		The target
	 * @param projectileId
	 * 		The id of the projectile
	 */
	public void visualizeLeech(Entity source, Entity target, int projectileId, int landingGraphicsId) {
		final int projectileDelay = ProjectileManager.getProjectileDelay(source, target);
		double delayCalc = ProjectileManager.getDelay(source, target, projectileDelay, 0);
		int totalDelay = (int) (delayCalc + projectileDelay);
		
		// the additional calculation
		final int speed = ProjectileManager.getSpeedModifier(source, target);
		ProjectileManager.sendProjectile(new Projectile(source, target, projectileId, 0, 10, 0, speed, 15, 0));
		SystemManager.getScheduler().schedule(new ScheduledTask(totalDelay) {
			@Override
			public void run() {
				target.sendGraphics(landingGraphicsId);
			}
		});
	}
	
	/**
	 * Gets the amount of items we keep
	 */
	public int itemKeptCount() {
		boolean skulled = false;
		// TODO: in corp zone we keep 0 base
		int amountToKeep = (/*(controllerManager.getController() instanceof CorpBeastControler) ? 0 : */(skulled ? 0 : 3));
		if (prayerOn(Prayer.PROTECT_ITEM) || prayerOn(PROTECT_ITEM_CURSE)) {
			amountToKeep++;
		}
		return amountToKeep;
	}
}