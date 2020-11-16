package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.utility.rs.constant.EquipConstants;

/**
 * The appearance of the player.
 *
 * @author Emperor
 * @author Tyluur <itstyluur@gmail.com>
 */
public final class PlayerAppearance {
	
	/**
	 * The player's body parts data.
	 */
	private int[] bodyParts;
	
	/**
	 * The look.
	 */
	private int[] look;
	
	/**
	 * An array containing all the player's colours.
	 */
	private int[] colors;
	
	/**
	 * If the player is male or female.
	 */
	@Getter
	@Setter
	private boolean male;
	
	/**
	 * The NPC id of the player. (In case of being an NPC.)
	 */
	@Getter
	private int npcId;
	
	/**
	 * The player's render emote.
	 */
	@Getter
	@Setter
	private int renderEmote = 1426;
	
	/**
	 * The constructor.
	 */
	public PlayerAppearance() {
		setDefaultAppearance();
	}
	
	/**
	 * Sets the default appearance.
	 */
	public void setDefaultAppearance() {
		this.look = new int[7];
		this.bodyParts = new int[15];
		this.colors = new int[10];
		setLook(0, 1);
		setLook(1, 10);
		setLook(2, 18);
		setLook(3, 28);
		setLook(4, 34);
		setLook(5, 39);
		setLook(6, 42);
		for (int i = 0; i < 10; i++) {
			colors[i] = i * 6 + 2;
		}
		this.colors[0] = 1;
		this.colors[1] = 7;
		this.colors[2] = 6;
		this.male = true;
		this.npcId = -1;
	}
	
	/**
	 * @param look
	 * 		the look to set
	 */
	public void setLook(int slot, int look) {
		this.look[slot] = look;
	}
	
	/**
	 * Gets a body part.
	 *
	 * @param part
	 * 		The part.
	 * @return The body part data.
	 */
	public int getBodyPart(int part) {
		return this.bodyParts[part];
	}
	
	/**
	 * Prepares the body parts data.
	 */
	public void prepareBodyData(Player player) {
		Item chest = player.getEquipment().getItems().get(EquipConstants.SLOT_CHEST);
		Item shield = player.getEquipment().getItems().get(EquipConstants.SLOT_SHIELD);
		Item legs = player.getEquipment().getItems().get(EquipConstants.SLOT_LEGS);
		Item hat = player.getEquipment().getItems().get(EquipConstants.SLOT_HAT);
		Item hands = player.getEquipment().getItems().get(EquipConstants.SLOT_HANDS);
		Item feet = player.getEquipment().getItems().get(EquipConstants.SLOT_FEET);
		Item aura = player.getEquipment().getItems().get(EquipConstants.SLOT_AURA);
		Item cape = player.getEquipment().getItems().get(EquipConstants.SLOT_CAPE);
		Item amulet = player.getEquipment().getItems().get(EquipConstants.SLOT_AMULET);
		Item weapon = player.getEquipment().getItems().get(EquipConstants.SLOT_WEAPON);
		if (hat != null) {
			drawItem(0, hat);
		} else {
			clearBodyPart(0);
		}
		if (cape != null) {
			drawItem(1, cape);
		} else {
			clearBodyPart(1);
		}
		if (amulet != null) {
			drawItem(2, amulet);
		} else {
			clearBodyPart(2);
		}
		if (weapon != null) {
			if (npcId == -1) {
				renderEmote = weapon.getDefinitions().getRenderAnimId();
			}
			drawItem(3, weapon);
		} else {
			if (npcId == -1) {
				renderEmote = 1426;
			}
			clearBodyPart(3);
		}
		if (chest != null) {
			drawItem(4, chest);
		} else {
			drawClothes(4, getLook(2));
		}
		if (shield != null) {
			drawItem(5, shield);
		} else {
			clearBodyPart(5);
		}
		if (chest != null && EquipConstants.isFullBody(chest.getDefinitions())) {
			clearBodyPart(6);
		} else {
			drawClothes(6, getLook(3));
		}
		if (legs != null) {
			drawItem(7, legs);
		} else {
			drawClothes(7, getLook(5));
		}
		if (hat != null && (EquipConstants.isFullHat(hat.getDefinitions()) || EquipConstants.isFullMask(hat.getDefinitions()))) {
			clearBodyPart(8);
		} else {
			drawClothes(8, getLook(0));
		}
		if (hands != null) {
			drawItem(9, hands);
		} else {
			drawClothes(9, getLook(4));
		}
		if (feet != null) {
			drawItem(10, feet);
		} else {
			drawClothes(10, getLook(6));
		}
		if (hat != null && EquipConstants.isFullMask(hat.getDefinitions())) {
			clearBodyPart(11);
		} else {
			drawClothes(11, getLook(1));
		}
		if (aura != null) {
			drawItem(14, aura);
		} else {
			clearBodyPart(14);
		}
	}
	
	/**
	 * Draws an item on a body part.
	 *
	 * @param part
	 * 		The body part.
	 * @param item
	 * 		The item to draw.
	 */
	public void drawItem(int part, Item item) {
		this.bodyParts[part] = item.getDefinitions().getEquipId() + 0x8000;
	}
	
	/**
	 * Clears a body part.
	 *
	 * @param part
	 * 		The part to clear.
	 */
	public void clearBodyPart(int part) {
		this.bodyParts[part] = 0;
	}
	
	/**
	 * Draws clothing on a body part.
	 *
	 * @param part
	 * 		The body part.
	 * @param clothesId
	 * 		The clothes id.
	 */
	public void drawClothes(int part, int clothesId) {
		this.bodyParts[part] = clothesId + 0x100;
	}
	
	/**
	 * @return the look
	 */
	public int getLook(int i) {
		return look[i];
	}
	
	/**
	 * Sets a colour.
	 *
	 * @param slot
	 * 		The slot.
	 * @param color
	 * 		The colour.
	 */
	public void setColor(int slot, int color) {
		this.colors[slot] = color;
	}
	
	/**
	 * Gets the colour on the given slot.
	 *
	 * @param slot
	 * 		The slot.
	 * @return The colour.
	 */
	public int getColor(int slot) {
		return colors[slot];
	}
	
	/**
	 * @param npcId
	 * 		the npcId to set
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
		if (npcId > 0) {
			this.renderEmote = NPCDefinitionParser.forId(npcId).getRenderEmote();
		} else {
			this.renderEmote = 1426;
		}
	}
	
}
