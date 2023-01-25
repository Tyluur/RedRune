package org.redrune.game.module.interaction.rsinterface;

import org.redrune.utility.tool.Misc;
import org.redrune.game.content.skills.LevelUp;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.data.PlayerSkills;
import org.redrune.game.node.entity.player.render.flag.impl.AppearanceUpdate;
import org.redrune.network.world.packet.outgoing.impl.ConfigPacketBuilder;
import org.redrune.utility.rs.constant.SkillConstants;
import org.redrune.utility.rs.input.InputResponse;
import org.redrune.utility.rs.input.InputType;

import static org.redrune.utility.AttributeKey.SKILL_MENU;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/28/2017
 */
public class SkillTabInteractionModule implements InterfaceInteractionModule {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return Misc.arguments(320);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (packetId == 85) { // first click
			int lvlupSkill = -1;
			int skillMenu = -1;
			switch (componentId) {
				case 200: // Attack
					skillMenu = 1;
					if (player.removeAttribute("leveledUp[0]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 1).build(player));
					} else {
						lvlupSkill = 0;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 10).build(player));
					}
					break;
				case 11: // Strength
					skillMenu = 2;
					if (player.removeAttribute("leveledUp[2]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 2).build(player));
					} else {
						lvlupSkill = 2;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 20).build(player));
					}
					break;
				case 28: // Defence
					skillMenu = 5;
					if (player.removeAttribute("leveledUp[1]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 5).build(player));
					} else {
						lvlupSkill = 1;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 40).build(player));
					}
					break;
				case 52: // Ranged
					skillMenu = 3;
					if (player.removeAttribute("leveledUp[4]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 3).build(player));
					} else {
						lvlupSkill = 4;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 30).build(player));
					}
					break;
				case 76: // Prayer
					if (player.removeAttribute("leveledUp[5]") == null) {
						skillMenu = 7;
						player.getTransmitter().send(new ConfigPacketBuilder(965, 7).build(player));
					} else {
						lvlupSkill = 5;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 60).build(player));
					}
					break;
				case 93: // Magic
					if (player.removeAttribute("leveledUp[6]") == null) {
						skillMenu = 4;
						player.getTransmitter().send(new ConfigPacketBuilder(965, 4).build(player));
					} else {
						lvlupSkill = 6;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 33).build(player));
					}
					break;
				case 110: // Runecrafting
					if (player.removeAttribute("leveledUp[20]") == null) {
						skillMenu = 12;
						player.getTransmitter().send(new ConfigPacketBuilder(965, 12).build(player));
					} else {
						lvlupSkill = 20;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 100).build(player));
					}
					break;
				case 134: // Construction
					skillMenu = 22;
					if (player.removeAttribute("leveledUp[21]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 22).build(player));
					} else {
						lvlupSkill = 21;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 698).build(player));
					}
					break;
				case 193: // Hitpoints
					skillMenu = 6;
					if (player.removeAttribute("leveledUp[3]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 6).build(player));
					} else {
						lvlupSkill = 3;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 50).build(player));
					}
					break;
				case 19: // Agility
					skillMenu = 8;
					if (player.removeAttribute("leveledUp[16]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 8).build(player));
					} else {
						lvlupSkill = 16;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 65).build(player));
					}
					break;
				case 36: // Herblore
					skillMenu = 9;
					if (player.removeAttribute("leveledUp[15]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 9).build(player));
					} else {
						lvlupSkill = 15;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 75).build(player));
					}
					break;
				case 60: // Thieving
					skillMenu = 10;
					if (player.removeAttribute("leveledUp[17]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 10).build(player));
					} else {
						lvlupSkill = 17;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 80).build(player));
					}
					break;
				case 84: // Crafting
					skillMenu = 11;
					if (player.removeAttribute("leveledUp[12]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 11).build(player));
					} else {
						lvlupSkill = 12;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 90).build(player));
					}
					break;
				case 101: // Fletching
					skillMenu = 19;
					if (player.removeAttribute("leveledUp[9]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 19).build(player));
					} else {
						lvlupSkill = 9;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 665).build(player));
					}
					break;
				case 118: // Slayer
					skillMenu = 20;
					if (player.removeAttribute("leveledUp[18]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 20).build(player));
					} else {
						lvlupSkill = 18;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 673).build(player));
					}
					break;
				case 142: // Hunter
					skillMenu = 23;
					if (player.removeAttribute("leveledUp[22]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 23).build(player));
					} else {
						lvlupSkill = 22;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 689).build(player));
					}
					break;
				case 186: // Mining
					skillMenu = 13;
					if (player.removeAttribute("leveledUp[14]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 13).build(player));
					} else {
						lvlupSkill = 14;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 110).build(player));
					}
					break;
				case 179: // Smithing
					skillMenu = 14;
					if (player.removeAttribute("leveledUp[13]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 14).build(player));
					} else {
						lvlupSkill = 13;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 115).build(player));
					}
					break;
				case 44: // Fishing
					skillMenu = 15;
					if (player.removeAttribute("leveledUp[10]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 15).build(player));
					} else {
						lvlupSkill = 10;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 120).build(player));
					}
					break;
				case 68: // Cooking
					skillMenu = 16;
					if (player.removeAttribute("leveledUp[7]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 16).build(player));
					} else {
						lvlupSkill = 7;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 641).build(player));
					}
					break;
				case 172: // Firemaking
					skillMenu = 17;
					if (player.removeAttribute("leveledUp[11]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 17).build(player));
					} else {
						lvlupSkill = 11;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 649).build(player));
					}
					break;
				case 165: // Woodcutting
					skillMenu = 18;
					if (player.removeAttribute("leveledUp[8]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 18).build(player));
					} else {
						lvlupSkill = 8;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 660).build(player));
					}
					break;
				case 126: // Farming
					skillMenu = 21;
					if (player.removeAttribute("leveledUp[19]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 21).build(player));
					} else {
						lvlupSkill = 19;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 681).build(player));
					}
					break;
				case 150: // Summoning
					skillMenu = 24;
					if (player.removeAttribute("leveledUp[23]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 24).build(player));
					} else {
						lvlupSkill = 23;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 705).build(player));
					}
					break;
				case 158: // Dung
					skillMenu = 25;
					if (player.removeAttribute("leveledUp[24]") == null) {
						player.getTransmitter().send(new ConfigPacketBuilder(965, 25).build(player));
					} else {
						lvlupSkill = 24;
						player.getTransmitter().send(new ConfigPacketBuilder(1230, 705).build(player));
					}
					break;
			}
			// so we won't have level up if we click set level skills
			if (lvlupSkill != -1) {
				LevelUp.switchFlash(player, lvlupSkill, false);
			}
			if (skillMenu != -1) {
				player.putAttribute(SKILL_MENU, skillMenu);
			}
			player.getManager().getInterfaces().closeAll();
			if (getSkillId(componentId) != -1) {
				handleSkillSetting(player, getSkillId(componentId));
				return true;
			}
			if (lvlupSkill != -1) {
				player.getManager().getInterfaces().sendInterfaceText(741, 4, "You have just advanced " + player.getSkills().getLevelsAdvanced((short) lvlupSkill, true) + " " + SkillConstants.SKILL_NAME[lvlupSkill] + " levels!");
			}
			player.getManager().getInterfaces().sendInterface(lvlupSkill != -1 ? 741 : 499, true);
		}
		return true;
	}
	
	/**
	 * Gets the component id of a skill
	 *
	 * @param componentId
	 * 		The component id
	 */
	private static int getSkillId(int componentId) {
		switch (componentId) {
			case 200:
				return SkillConstants.ATTACK;
			case 11:
				return SkillConstants.STRENGTH;
			case 28:
				return SkillConstants.DEFENCE;
			case 52:
				return SkillConstants.RANGE;
		/*	case 76:
				return Skills.PRAYER;*/
			case 93:
				return SkillConstants.MAGIC;
			case 193:
				return SkillConstants.HITPOINTS;
			default:
				return -1;
		}
	}
	
	/**
	 * Handles the skill level setting
	 *
	 * @param player
	 * 		The player
	 * @param skillId
	 * 		The id of the skill to set a level to
	 */
	private void handleSkillSetting(Player player, int skillId) {
		if (player.getManager().getActivities().getActivity().isPresent()) {
			player.getTransmitter().sendMessage("You can't set levels in this place.");
			return;
		}
		player.getTransmitter().requestInput(input -> {
			int level = InputResponse.getInput(input);
			if (level <= 0) {
				level = 1;
			} else if (level > 99) {
				level = 99;
			}
			int exp = PlayerSkills.getXPForLevel(level);
			player.getSkills().setLevel(skillId, level);
			player.getSkills().setXp(skillId, exp);
			player.getUpdateMasks().register(new AppearanceUpdate(player));
		}, InputType.INTEGER, "Enter your desired level in " + SkillConstants.SKILL_NAME[skillId].toLowerCase() + ".");
	}
}
