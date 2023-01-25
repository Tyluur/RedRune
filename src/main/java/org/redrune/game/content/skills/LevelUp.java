package org.redrune.game.content.skills;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.ConfigFilePacketBuilder;
import org.redrune.utility.rs.constant.SkillConstants;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public final class LevelUp implements SkillConstants {
	
	/**
	 * Sends a notification to the player that they have levelled up in a skill
	 *
	 * @param player
	 * 		The player
	 * @param skillId
	 * 		The id of the skill
	 */
	public static void sendCongratulations(Player player, int skillId) {
		int level = player.getSkills().getLevelForXp(skillId);
		String name = SKILL_NAME[skillId];
		
		player.putAttribute("leveledUp", skillId);
		player.putAttribute("leveledUp[" + skillId + "]", Boolean.TRUE);
		player.sendGraphics(199, 100, 0);
		player.getManager().getInterfaces().sendInterfaceText(740, 0, "Congratulations, you have just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level!");
		player.getManager().getInterfaces().sendInterfaceText(740, 1, "You have now reached level " + level + ".");
		player.getManager().getInterfaces().sendChatboxInterface(740);
		player.getTransmitter().send(new ConfigFilePacketBuilder(4757, getIconValue(skillId)).build(player));
		switchFlash(player, skillId, true);
	}
	
	/**
	 * Switches the flash for a skill
	 *
	 * @param player
	 * 		The player
	 * @param skill
	 * 		The id of the skill
	 * @param on
	 * 		If the flash should be on or off
	 */
	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		if (skill == ATTACK) {
			id = 4732;
		} else if (skill == STRENGTH) {
			id = 4733;
		} else if (skill == DEFENCE) {
			id = 4734;
		} else if (skill == RANGE) {
			id = 4735;
		} else if (skill == PRAYER) {
			id = 4736;
		} else if (skill == MAGIC) {
			id = 4737;
		} else if (skill == HITPOINTS) {
			id = 4738;
		} else if (skill == AGILITY) {
			id = 4739;
		} else if (skill == HERBLORE) {
			id = 4740;
		} else if (skill == THIEVING) {
			id = 4741;
		} else if (skill == CRAFTING) {
			id = 4742;
		} else if (skill == FLETCHING) {
			id = 4743;
		} else if (skill == MINING) {
			id = 4744;
		} else if (skill == SMITHING) {
			id = 4745;
		} else if (skill == FISHING) {
			id = 4746;
		} else if (skill == COOKING) {
			id = 4747;
		} else if (skill == FIREMAKING) {
			id = 4748;
		} else if (skill == WOODCUTTING) {
			id = 4749;
		} else if (skill == RUNECRAFTING) {
			id = 4750;
		} else if (skill == SLAYER) {
			id = 4751;
		} else if (skill == FARMING) {
			id = 4752;
		} else if (skill == CONSTRUCTION) {
			id = 4754;
		} else if (skill == HUNTER) {
			id = 4753;
		} else if (skill == SUMMONING) {
			id = 4755;
		} else {
			id = 7756;
		}
		player.getTransmitter().send(new ConfigFilePacketBuilder(id, on ? 1 : 0).build(player));
	}
	
	/**
	 * Gets the icon value of a skill
	 *
	 * @param skill
	 * 		The skill
	 */
	private static int getIconValue(int skill) {
		if (skill == ATTACK) {
			return 1;
		}
		if (skill == STRENGTH) {
			return 2;
		}
		if (skill == RANGE) {
			return 3;
		}
		if (skill == MAGIC) {
			return 4;
		}
		if (skill == DEFENCE) {
			return 5;
		}
		if (skill == HITPOINTS) {
			return 6;
		}
		if (skill == PRAYER) {
			return 7;
		}
		if (skill == AGILITY) {
			return 8;
		}
		if (skill == HERBLORE) {
			return 9;
		}
		if (skill == THIEVING) {
			return 10;
		}
		if (skill == CRAFTING) {
			return 11;
		}
		if (skill == RUNECRAFTING) {
			return 12;
		}
		if (skill == MINING) {
			return 13;
		}
		if (skill == SMITHING) {
			return 14;
		}
		if (skill == FISHING) {
			return 15;
		}
		if (skill == COOKING) {
			return 16;
		}
		if (skill == FIREMAKING) {
			return 17;
		}
		if (skill == WOODCUTTING) {
			return 18;
		}
		if (skill == FLETCHING) {
			return 19;
		}
		if (skill == SLAYER) {
			return 20;
		}
		if (skill == FARMING) {
			return 21;
		}
		if (skill == CONSTRUCTION) {
			return 22;
		}
		if (skill == HUNTER) {
			return 23;
		}
		if (skill == SUMMONING) {
			return 24;
		}
		return 25;
	}
	
}
