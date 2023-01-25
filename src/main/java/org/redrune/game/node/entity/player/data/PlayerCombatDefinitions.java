package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatType;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.GameTab;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.MagicConstants.MagicBook;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/21/2017
 */
public class PlayerCombatDefinitions {
	
	/**
	 * The attack style the entity is using. Melee for npcs is always on stab [0].
	 */
	@Getter
	private byte attackStyle = 0;
	
	/**
	 * The amount of special energy we have
	 */
	@Getter
	private byte specialEnergy = 100;
	
	/**
	 * If we are to fight back the player who hits us
	 */
	@Getter
	private boolean retaliating = true;
	
	/**
	 * The id of the spellbook
	 */
	@Getter
	private MagicBook spellbook = MagicBook.REGULAR;
	
	/**
	 * The spell id we're auto casting
	 */
	@Getter
	private int autocastId = -1;
	
	/**
	 * If we are defensive casting
	 */
	@Getter
	private boolean defensiveCasting;
	
	/**
	 * The sort spellbook value
	 */
	private byte sortSpellBook;
	
	/**
	 * If combat spells should be shown, the default is true.
	 */
	private boolean showCombatSpells = true;
	
	/**
	 * If skill spells should be shown, the default is true.
	 */
	private boolean showSkillSpells = true;
	
	/**
	 * If miscellaneous spells should be shown, the default is true.
	 */
	private boolean showMiscellaneousSpells = true;
	
	/**
	 * If teleport spells should be shown, the default is true.
	 */
	private boolean showTeleportSpells = true;
	
	/**
	 * Sets the special activated flag
	 */
	@Getter
	private transient boolean specialActivated = false;
	
	/**
	 * The player whose definitions these are for
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Sends the login refreshing
	 */
	public void sendLogin() {
		refreshAttackStyle();
		refreshRetaliate();
		refreshSpecialEnergy();
		refreshAutoCastSpell();
		refreshDefensiveCasting();
		refreshSpellbook();
	}
	
	/**
	 * Refreshes the attack style
	 */
	private void refreshAttackStyle() {
		player.getTransmitter().send(new ConfigPacketBuilder(43, autocastId > 0 ? 4 : attackStyle).build(player));
	}
	
	/**
	 * Refreshes the retaliate button
	 */
	private void refreshRetaliate() {
		player.getTransmitter().send(new ConfigPacketBuilder(172, retaliating ? 0 : 1).build(player));
	}
	
	/**
	 * Refreshes the special energy
	 */
	private void refreshSpecialEnergy() {
		player.getTransmitter().send(new ConfigPacketBuilder(300, specialEnergy * 10).build(player));
	}
	
	/**
	 * Refreshes the currently selected autocast spell
	 */
	private void refreshAutoCastSpell() {
		refreshAttackStyle();
		player.getTransmitter().send(new ConfigPacketBuilder(108, getSpellAutoCastConfigValue()).build(player));
	}
	
	/**
	 * Refreshes the defensive casting button
	 */
	private void refreshDefensiveCasting() {
		player.getTransmitter().send(new ConfigPacketBuilder(439, spellbook.getInterfaceId() + (!defensiveCasting ? 0 : 1 << 8)).build(player));
	}
	
	/**
	 * Refreshes the spellbook we're on.
	 */
	public void refreshSpellbook() {
		GameTab data = GameTab.MAGIC_SPELLBOOK;
		player.getManager().getInterfaces().sendInterface(player.getManager().getInterfaces().usingFixedMode() ? data.getFixedChildId() : data.getResizedChildId(), spellbook.getInterfaceId());
		refreshBookConfiguration();
	}
	
	/**
	 * Gets the config value of the spell we're autocasting
	 */
	private int getSpellAutoCastConfigValue() {
		if (spellbook == MagicBook.REGULAR) {
			switch (autocastId) {
				case 98:
					return 143;
				case 25:
					return 3;
				case 28:
					return 5;
				case 30:
					return 7;
				case 32:
					return 9;
				case 34:
					return 11; // air bolt
				case 39:
					return 13;// water bolt
				case 42:
					return 15;// earth bolt
				case 45:
					return 17; // fire bolt
				case 49:
					return 19;// air blast
				case 52:
					return 21;// water blast
				case 58:
					return 23;// earth blast
				case 63:
					return 25;// fire blast
				case 66: // Saradomin Strike
					return 41;
				case 67:// Claws of Guthix
					return 39;
				case 68:// Flames of Zammorak
					return 43;
				case 70:
					return 27;// air wave
				case 73:
					return 29;// water wave
				case 77:
					return 31;// earth wave
				case 80:
					return 33;// fire wave
				case 84:
					return 47;
				case 87:
					return 49;
				case 89:
					return 51;
				case 91:
					return 53;
				case 99:
					return 145;
				default:
					return 0;
			}
		} else if (spellbook == MagicBook.ANCIENTS) {
			switch (autocastId) {
				case 28:
					return 63;
				case 32:
					return 65;
				case 24:
					return 67;
				case 20:
					return 69;
				case 30:
					return 71;
				case 34:
					return 73;
				case 26:
					return 75;
				case 22:
					return 77;
				case 29:
					return 79;
				case 33:
					return 81;
				case 25:
					return 83;
				case 21:
					return 85;
				case 31:
					return 87;
				case 35:
					return 89;
				case 27:
					return 91;
				case 23:
					return 93;
				case 36:
					return 95;
				case 37:
					return 99;
				case 38:
					return 97;
				case 39:
					return 101;
				default:
					return 0;
			}
		} else {
			return 0;
		}
	}
	
	/**
	 * Refreshes the book configuration
	 */
	private void refreshBookConfiguration() {
		int value = 0;
		if (spellbook == MagicBook.REGULAR) {
			value = sortSpellBook | (showCombatSpells ? 0 : 1 << 9) | (showSkillSpells ? 0 : 1 << 10) | (showMiscellaneousSpells ? 0 : 1 << 11) | (showTeleportSpells ? 0 : 1 << 12);
		} else if (spellbook == MagicBook.ANCIENTS) {
			value = sortSpellBook << 3 | (showCombatSpells ? 0 : 1 << 16) | (showTeleportSpells ? 0 : 1 << 17);
		} else if (spellbook == MagicBook.LUNARS) {
			value = sortSpellBook << 6 | (showCombatSpells ? 0 : 1 << 13) | (showMiscellaneousSpells ? 0 : 1 << 14) | (showTeleportSpells ? 0 : 1 << 15);
		}
		player.getTransmitter().send(new ConfigPacketBuilder(1376, value).build(player));
	}
	
	/**
	 * Changes the attack style
	 *
	 * @param attackStyle
	 * 		The attack style
	 */
	public void changeAttackStyle(byte attackStyle) {
		byte maxSize = 3;
		int weaponId = player.getEquipment().getIdInSlot(EquipConstants.SLOT_WEAPON);
		CombatType type = StaticCombatFormulae.getCombatType(player);
		String name = weaponId == -1 ? "" : ItemDefinitionParser.forId(weaponId).getName().toLowerCase();
		// whips, halberds, range, and magic combat styles only have 3 styles.
		if (weaponId == -1 || type != CombatType.MELEE || name.contains("whip") || name.contains("halberd")) {
			maxSize = 2;
		}
		if (attackStyle > maxSize) {
			attackStyle = maxSize;
		}
		if (this.attackStyle != attackStyle) {
			this.attackStyle = attackStyle;
			if (autocastId > 1) {
				resetSpells(true);
			} else {
				refreshAttackStyle();
			}
		} else if (autocastId > 1) {
			resetSpells(true);
		}
	}
	
	/**
	 * Resets spell information
	 *
	 * @param removeAutoCast
	 * 		If we should remove the auto cast spell
	 */
	public void resetSpells(boolean removeAutoCast) {
		player.removeAttribute("spell_cast_id");
		if (removeAutoCast) {
			autocastId = -1;
			refreshAutoCastSpell();
		}
	}
	
	/**
	 * Toggles the retaliate button
	 */
	public void toggleAutoRetaliate() {
		retaliating = !retaliating;
		player.stop(true, true, true, false);
		refreshRetaliate();
	}
	
	/**
	 * Reduces the special attack energy by the given amount. This also verifies that we never have < 0 special energy.
	 * This can be used with a negative number because we have upper and lower bounds [to add instead of reduce]
	 *
	 * @param amount
	 * 		The amount to reduce it by.
	 */
	public void reduceSpecial(int amount) {
		this.specialEnergy -= amount;
		if (this.specialEnergy <= 0) {
			this.specialEnergy = 0;
		} else if (this.specialEnergy >= 100) {
			this.specialEnergy = 100;
		}
		refreshSpecialEnergy();
	}
	
	/**
	 * Sets if the special attack is activated or not
	 *
	 * @param specialActivated
	 * 		The special attack being activated
	 */
	public void setSpecialActivated(boolean specialActivated) {
		this.specialActivated = specialActivated;
		refreshSpecialActivated();
	}
	
	/**
	 * Refreshes the special attack bar, sending it on or off to the client.
	 */
	private void refreshSpecialActivated() {
		player.getTransmitter().send(new ConfigPacketBuilder(301, specialActivated ? 1 : 0).build(player));
	}
	
	/**
	 * Sets the amount of special energy we have
	 *
	 * @param specialEnergy
	 * 		The amount
	 */
	public void setSpecialEnergy(byte specialEnergy) {
		this.specialEnergy = specialEnergy;
		refreshSpecialEnergy();
	}
	
	/**
	 * Sets the auto-cast spell id
	 *
	 * @param autocastId
	 * 		The id
	 */
	public void setAutocastId(int autocastId) {
		this.autocastId = autocastId;
		refreshAutoCastSpell();
	}
	
	/**
	 * Sets the defensive casting flag
	 *
	 * @param defensiveCasting
	 * 		The defensive casting flag
	 */
	public void setDefensiveCasting(boolean defensiveCasting) {
		this.defensiveCasting = defensiveCasting;
		refreshDefensiveCasting();
	}
	
	/**
	 * Toggles combat spells being shown
	 */
	public void switchShowCombatSpells() {
		showCombatSpells = !showCombatSpells;
		refreshBookConfiguration();
	}
	
	/**
	 * Toggles skill spells being shown
	 */
	public void switchShowSkillSpells() {
		showSkillSpells = !showSkillSpells;
		refreshBookConfiguration();
	}
	
	/**
	 * Toggles miscellaneous spells being shown
	 */
	public void switchShowMiscellaneousSpells() {
		showMiscellaneousSpells = !showMiscellaneousSpells;
		refreshBookConfiguration();
	}
	
	/**
	 * Toggles the teleport spells being shown
	 */
	public void switchShowTeleportSkillSpells() {
		showTeleportSpells = !showTeleportSpells;
		refreshBookConfiguration();
	}
	
	/**
	 * Sets the spellbook sort value
	 *
	 * @param sortId
	 * 		The sort value
	 */
	public void setSortSpellBook(int sortId) {
		this.sortSpellBook = (byte) sortId;
		refreshBookConfiguration();
	}
	
	/**
	 * Sets the spellbook we're on and refreshes the visual aspects
	 *
	 * @param spellbook
	 * 		The spellbook to set
	 */
	public void setSpellbook(MagicBook spellbook) {
		this.spellbook = spellbook;
		refreshAutoCastSpell();
		refreshDefensiveCasting();
		refreshSpellbook();
	}
	
	/**
	 * Gets the id of the spell we're using. If we're autocasting, we will use that id. Otherwise if we set a spell via
	 * magic book -> spell -> target, we will set that id. Otherwise we will not have a spell
	 *
	 * @return -1 if we have no spell, or the id of the spell we're using
	 */
	public int getSpellId() {
		if (autocastId != -1) {
			return autocastId;
		}
		return player.getAttribute("spell_cast_id", -1);
	}
}


