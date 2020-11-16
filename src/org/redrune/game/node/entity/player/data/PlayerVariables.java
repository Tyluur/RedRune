package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.game.world.punishment.Punishment;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.HeadIcons.SkullIcon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The class used to store important player variables.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class PlayerVariables {
	
	public PlayerVariables() {
		this.storedAttributes = new ConcurrentHashMap<>();
		putAttributeIfEmpty(AttributeKey.FILTERING_PROFANITY, false);
		putAttributeIfEmpty(AttributeKey.CHAT_EFFECTS, true);
		putAttributeIfEmpty(AttributeKey.ACCEPTING_AID, false);
		putAttributeIfEmpty(AttributeKey.DUAL_MOUSE_BUTTONS, true);
	}
	
	/**
	 * The map of saved attributes
	 */
	private final ConcurrentHashMap<AttributeKey, Object> storedAttributes;
	
	/**
	 * The health points of the player
	 */
	@Getter
	@Setter
	private int healthPoints = 100;
	
	/**
	 * The player points
	 */
	@Getter
	@Setter
	private int prayerPoints = 10;
	
	/**
	 * The amount of run energy the player has
	 */
	@Getter
	@Setter
	private double runEnergy = 100;
	
	/**
	 * If the run button has been toggled on
	 */
	@Getter
	@Setter
	private boolean runToggled = false;
	
	/**
	 * If the experience is locked
	 */
	@Getter
	@Setter
	private boolean experienceLocked = false;
	
	/**
	 * If we are in an area we can fight in
	 */
	@Getter
	@Setter
	private boolean inFightArea = false;
	
	/**
	 * The skull icon
	 */
	@Getter
	private SkullIcon skullIcon = SkullIcon.NONE;
	
	/**
	 * The time until the player is unskulled
	 */
	@Getter
	@Setter
	private long skullIconTimer = -1L;
	
	/**
	 * The amount of players that we have killed
	 */
	@Getter
	@Setter
	private int playersKilled = 0;
	
	/**
	 * The amount of times we have die from another player
	 */
	@Getter
	@Setter
	private int playerDeaths = 0;
	
	/**
	 * The kill streak the player is on
	 */
	@Getter
	@Setter
	private int killstreak = 0;
	
	/**
	 * The id of the row in the sql database that the player's data is in
	 */
	@Getter
	@Setter
	private int rowId;
	
	/**
	 * The amount of earning potential the player has
	 */
	@Getter
	@Setter
	private int earningPotential = 0;
	
	/**
	 * The list of punishments the player has
	 */
	private final List<Punishment> punishmentList = new ArrayList<>();
	
	/**
	 * Gets a stored attribute
	 *
	 * @param key
	 * 		The key of the attribute
	 */
	@SuppressWarnings("unchecked")
	public <K> K getAttribute(AttributeKey key) {
		return (K) storedAttributes.get(key);
	}
	
	/**
	 * Removes the value for the attribute
	 *
	 * @param key
	 * 		The key
	 * @return The value that was removed
	 */
	@SuppressWarnings("unchecked")
	public <K> K removeAttribute(AttributeKey key) {
		return (K) storedAttributes.remove(key);
	}
	
	/**
	 * Removes an attribute and returns the default value parameter if the key wasnt in the map.
	 *
	 * @param key
	 * 		The key
	 * @param defaultValue
	 * 		The value to return
	 * @return The attribute value, or the default value if nothing existed
	 */
	@SuppressWarnings("unchecked")
	public <K> K removeAttribute(AttributeKey key, K defaultValue) {
		K value = (K) storedAttributes.remove(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * Increments an attribute's value by 1 if they're a number
	 *
	 * @param key
	 * 		The key
	 * @return The new value
	 */
	public int incrementIntegerAttribute(AttributeKey key) {
		Object value = storedAttributes.get(key);
		int digit = -1;
		if (value instanceof Number) {
			digit = ((Number) value).intValue();
		}
		digit++;
		storedAttributes.put(key, digit);
		return digit;
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putAttributeIfEmpty(AttributeKey key, K value) {
		if (!storedAttributes.containsKey(key)) {
			storedAttributes.put(key, value);
		}
		return value;
	}
	
	/**
	 * Puts the key into the attributes map
	 *
	 * @param key
	 * 		The key
	 * @param value
	 * 		The value
	 */
	public <K> K putAttribute(AttributeKey key, K value) {
		storedAttributes.put(key, value);
		return value;
	}
	
	/**
	 * Sets a player's skull icon
	 *
	 * @param player
	 * 		The player
	 * @param skullIcon
	 * 		The icon
	 */
	public void setSkullIcon(Player player, SkullIcon skullIcon) {
		this.skullIcon = skullIcon;
		if (player != null) {
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}
	}
	
	/**
	 * Reduces the amount of health points we have
	 *
	 * @param amount
	 * 		The amount to reduce by
	 */
	public boolean reduceHealth(int amount) {
		healthPoints = healthPoints - amount;
		if (healthPoints <= 0) {
			healthPoints = 0;
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * If the player is accepting aid
	 */
	public Boolean isAcceptingAid() {
		return getAttribute(AttributeKey.ACCEPTING_AID, true);
	}
	
	/**
	 * Gets an attribute and returns the default value if it doesn't exist
	 *
	 * @param key
	 * 		The key of the attribute
	 * @param defaultValue
	 * 		The value to return if the key doesnt exist in the map
	 */
	@SuppressWarnings("unchecked")
	
	public <K> K getAttribute(AttributeKey key, K defaultValue) {
		K value = (K) storedAttributes.get(key);
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	/**
	 * If the player is filtering profanity
	 */
	public Boolean isFilteringProfanity() {
		return getAttribute(AttributeKey.FILTERING_PROFANITY, false);
	}
	
	/**
	 * If the skull timer is more than the current time
	 */
	public boolean isSkulled() {
		return skullIconTimer > System.currentTimeMillis();
	}
	
	/**
	 * Gets the formatted amount of earning potential
	 */
	public String getFormattedEarningPotential() {
		String colour;
		if (earningPotential < 25) {
			colour = "990000";
		} else if (earningPotential >= 25 && earningPotential < 50) {
			colour = "FF6633";
		} else if (earningPotential >= 50 && earningPotential < 75) {
			colour = "FFCC33";
		} else {
			colour = "33FF33";
		}
		return "EP: <col=" + colour + ">" + earningPotential + "%</col>";
	}
	
	/**
	 * Checks if the player has a punishment of a certain type
	 *
	 * @param type
	 * 		The type of punishment
	 */
	public boolean hasPunishment(PunishmentType type) {
		for (Punishment punishment : punishmentList) {
			if (punishment.getType() != type) {
				continue;
			}
			if (punishment.getTime() > System.currentTimeMillis()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a punishment to the list
	 *
	 * @param punishment
	 * 		The punishment
	 */
	public boolean addPunishment(Punishment punishment) {
		return punishmentList.add(punishment);
	}
	
	/**
	 * Handles the removal of a punishment
	 *
	 * @param punishment
	 * 		The punishment to remove
	 */
	public boolean removePunishment(Punishment punishment) {
		boolean removed = false;
		for (Iterator<Punishment> iterator = punishmentList.iterator(); iterator.hasNext(); ) {
			Punishment p = iterator.next();
			if (p.equals(punishment)) {
				iterator.remove();
				removed = true;
			}
		}
		return removed;
	}
}

