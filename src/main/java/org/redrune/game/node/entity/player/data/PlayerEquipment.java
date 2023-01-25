package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.cache.parse.definition.ItemDefinition;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.item.ItemsContainer;
import org.redrune.network.world.packet.outgoing.impl.ContainerPacketBuilder;
import org.redrune.network.world.packet.outgoing.impl.ContainerUpdateBuilder;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.rs.Hit;
import org.redrune.utility.rs.Hit.HitSplat;
import org.redrune.utility.rs.constant.BonusConstants;
import org.redrune.utility.rs.constant.EquipConstants;

import java.util.HashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public class PlayerEquipment implements EquipConstants, BonusConstants {
	
	/**
	 * The container of items
	 */
	@Getter
	private final ItemsContainer<Item> items = new ItemsContainer<>(15, false);
	
	/**
	 * The bonuses of the player
	 */
	private int[] bonuses = new int[18];
	
	/**
	 * The player
	 */
	@Setter
	private transient Player player;
	
	/**
	 * The weight of the player's equipment
	 */
	@Getter
	@Setter
	private transient double weight;
	
	/**
	 * The boost past max health that the equipment gives the player [torva etc]
	 */
	@Getter
	@Setter
	private transient int maxHealthBoost;
	
	/**
	 * Sends the full container of items
	 */
	public void sendContainer() {
		player.getTransmitter().send(new ContainerPacketBuilder(94, items.toArray(), false).build(player));
		double weight = 0;
		for (Item item : items.toArray()) {
			if (item == null) {
				continue;
			}
			weight += ItemRepository.getWeight(item.getId(), true);
		}
		this.weight = weight;
		refresh();
	}
	
	/**
	 * Refreshes an array of slots
	 *
	 * @param slots
	 * 		The slots
	 */
	public void refresh(int... slots) {
		if (slots != null) {
			player.getTransmitter().send(new ContainerUpdateBuilder(94, items.toArray(), slots).build(player));
		}
		updateBonuses();
		updateHealthBoosts();
	}
	
	public void refreshAll() {
		for (int i = 0; i < 15; i++) {
			refresh(i);
		}
	}
	
	/**
	 * Updates the bonuses accurately
	 */
	private void updateBonuses() {
		bonuses = new int[18];
		double weight = 0;
		for (Item item : items.toArray()) {
			if (item == null) {
				continue;
			}
			weight += ItemRepository.getWeight(item.getId(), true);
			int[] bonuses = ItemRepository.getBonuses(item.getId());
			if (bonuses == null) {
				continue;
			}
			for (int id = 0; id < bonuses.length; id++) {
				if (id == RANGED_STRENGTH_BONUS && this.bonuses[RANGED_STRENGTH_BONUS] != 0) {
					continue;
				}
				this.bonuses[id] += bonuses[id];
			}
		}
		this.weight = weight;
		player.getTransmitter().sendWeight();
	}
	
	/**
	 * Updates the health boost
	 */
	private void updateHealthBoosts() {
		double hpIncrease = calculateEquipmentHpBoost();
		// the boosts changed, the player should not be able to maintain boosts
		// int previousBoost = maxHealthBoost;
		if (hpIncrease != maxHealthBoost) {
			maxHealthBoost = (int) hpIncrease;
		}
		/*
		IT WASN'T LIKE THIS ON RS
		// the new boost is less than the old one, so we must reduce if necessary
		// this is so we don't have 1390 hp after removing torva
		if (hpIncrease < previousBoost) {
			if (player.getHealthPoints() >= player.getMaxHealth()) {
				player.setHealthPoints(player.getMaxHealth() + (int) hpIncrease);
				player.getTransmitter().refreshHealthPoints();
			}
		}*/
	}
	
	/**
	 * Checks that the player is equipping a shield
	 */
	public boolean hasShield() {
		return items.get(5) != null;
	}
	
	/**
	 * Gets the render emote of the weapon
	 */
	public int getWeaponRenderEmote() {
		Item weapon = items.get(3);
		if (weapon == null) {
			return 1426;
		}
		if (weapon.getId() == 4565) {
			return 594;
		}
		return weapon.getDefinitions().getRenderAnimId();
	}
	
	/**
	 * Handles the absorption of a hit
	 *
	 * @param hit
	 * 		The hit
	 */
	public void handleAbsorption(Hit hit) {
		if (hit.getDamage() >= 200) {
			if (hit.getSplat() == HitSplat.MELEE_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_MELEE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			} else if (hit.getSplat() == HitSplat.RANGE_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_RANGE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			} else if (hit.getSplat() == HitSplat.MAGIC_DAMAGE) {
				int reducedDamage = hit.getDamage() * getBonus(ABSORB_MAGE_BONUS) / 100;
				if (reducedDamage > 0) {
					hit.setDamage(hit.getDamage() - reducedDamage);
					hit.setSoaked(reducedDamage);
				}
			}
		}
	}
	
	/**
	 * Gets the bonus at an index
	 *
	 * @param index
	 * 		The index
	 */
	public int getBonus(int index) {
		if (index < 0 || index >= bonuses.length) {
			System.out.println("Invalid bonus index expected: " + index);
			return 0;
		}
		return bonuses[index];
	}
	
	/**
	 * Gets the id of the weapon
	 */
	public int getWeaponId() {
		return getIdInSlot(SLOT_WEAPON);
	}
	
	/**
	 * Gets the id of the item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public int getIdInSlot(int slot) {
		Item item = getItem(slot);
		if (item == null) {
			return -1;
		} else {
			return item.getId();
		}
	}
	
	/**
	 * Gets an item in the slot
	 *
	 * @param slot
	 * 		The slot
	 */
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	/**
	 * Checks if the player's cape saves ammo
	 */
	public boolean capeSavesAmmo() {
		int capeId = getIdInSlot(SLOT_CAPE);
		String name = capeId == -1 ? "unarmed" : ItemDefinitionParser.forId(capeId).getName();
		return capeId == 20771 || name.toLowerCase().contains("ava's");
	}
	
	/**
	 * Drains the run energy, based on the weight modifier
	 */
	public void drainRunEnergy() {
		if (player.getMovement().getNextRunDirection() != -1) {
			double toLose = (0.67 + ((player.getEquipment().getWeight() + player.getInventory().getWeight()) / 50)) / 2;
			player.getVariables().setRunEnergy(player.getVariables().getRunEnergy() - toLose);
			player.getTransmitter().refreshEnergy();
		}
	}
	
	/**
	 * Gets the skill weapon requirement of a weapon
	 *
	 * @param skill
	 * 		The skill
	 */
	public int getWeaponRequirement(int skill) {
		int weaponId = getWeaponId();
		if (weaponId == -1) {
			return 1;
		}
		ItemDefinition definition = ItemDefinitionParser.forId(weaponId);
		HashMap<Integer, Integer> requirements = definition.getWearingRequirements();
		if (requirements == null) {
			return 1;
		}
		for (int skillId : requirements.keySet()) {
			if (skillId > 24 || skillId < 0) {
				continue;
			}
			int level = requirements.get(skillId);
			if (level < 0 || level > 120) {
				continue;
			}
			if (skill == skillId) {
				return level;
			}
		}
		return 1;
	}
	
	/**
	 * Calculates the hitpoints from the equipped armour boost
	 */
	private double calculateEquipmentHpBoost() {
		double hpBoost = 0;
		for (int index = 0; index < items.getSize(); index++) {
			Item item = items.get(index);
			if (item == null) {
				continue;
			}
			int id = item.getId();
			if (index == SLOT_HAT) {
				switch (id) {
					case 20135:
					case 20137:
					case 20147:
					case 20149:
					case 20159:
					case 20161:
						hpBoost += 66;
						break;
				}
			} else if (index == SLOT_CHEST) {
				switch (id) {
					case 20139:
					case 20141:
					case 20151:
					case 20153:
					case 20163:
					case 20165:
						hpBoost += 200;
						break;
				}
			} else if (index == SLOT_LEGS) {
				switch (id) {
					case 20143:
					case 20145:
					case 20155:
					case 20157:
					case 20167:
					case 20169:
						hpBoost += 134;
						break;
				}
			}
		}
		return hpBoost;
	}
}
