package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.content.dialogue.impl.misc.WorldMapDialogue;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.NetworkConstants;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.utility.rs.constant.InterfaceConstants;
import org.redrune.game.content.action.interaction.PlayerRestAction;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.utility.rs.GameTab;

import static org.redrune.utility.rs.constant.InterfaceConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class GameframeInteractionModule implements InterfaceInteractionModule, NetworkConstants {
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(CHAT_SETUP_INTERFACE_ID, InterfaceConstants.SCREEN_RESIZABLE_WINDOW_ID, InterfaceConstants.SCREEN_FIXED_WINDOW_ID, OPTIONS_INTERFACE_ID, PRAYER_ORB_INTERFACE_ID, RUN_ORB_INTERACE_ID, InterfaceConstants.LOGOUT_INTERFACE_ID, InterfaceConstants.GAMEFRAME_INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		switch (interfaceId) {
			case InterfaceConstants.SCREEN_FIXED_WINDOW_ID:
			case InterfaceConstants.SCREEN_RESIZABLE_WINDOW_ID:
				if (componentId == 179) {
					player.getManager().getDialogues().startDialogue(new WorldMapDialogue());
					return true;
				} else if (componentId == 0) {
					if (packetId == FIRST_PACKET_ID) {
						return true;
					} else if (packetId == DROP_PACKET_ID) {
						player.getSkills().resetExperienceCounter();
						return true;
					}
				}
				break;
			case RUN_ORB_INTERACE_ID:
				if (componentId == 1) {
					if (packetId == FIRST_PACKET_ID) {
						player.getVariables().setRunToggled(!player.getVariables().isRunToggled());
						player.getTransmitter().sendSettings();
						return true;
					} else if (packetId == SECOND_PACKET_ID) {
						player.getManager().getActions().startAction(new PlayerRestAction());
						return true;
					}
				}
				break;
			case PRAYER_ORB_INTERFACE_ID:
				if (componentId == 1) {
					if (packetId == FIRST_PACKET_ID) {
						player.getManager().getPrayers().toggleQuickPrayers();
						return true;
					} else if (packetId == SECOND_PACKET_ID) {
						player.getManager().getPrayers().selectQuickPrayers();
						return true;
					}
				}
				break;
			case CHAT_SETUP_INTERFACE_ID:
				switch (componentId) {
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, GameTab.OPTIONS.getInterfaceId());
						return true;
				}
				break;
			case InterfaceConstants.LOGOUT_INTERFACE_ID:
				player.logout(componentId == 6);
				return true;
			case OPTIONS_INTERFACE_ID:
				switch (componentId) {
					case 3:
						player.getVariables().putAttribute(AttributeKey.FILTERING_PROFANITY, !player.getVariables().getAttribute(AttributeKey.FILTERING_PROFANITY, false));
						player.getTransmitter().sendSettings();
						return true;
					case 4:
						player.getVariables().putAttribute(AttributeKey.CHAT_EFFECTS, !player.getVariables().getAttribute(AttributeKey.CHAT_EFFECTS, false));
						player.getTransmitter().sendSettings();
						return true;
					case 5:
						player.getManager().getInterfaces().sendTab(GameTab.OPTIONS, CHAT_SETUP_INTERFACE_ID);
						return true;
					case 6:
						player.getVariables().putAttribute(AttributeKey.DUAL_MOUSE_BUTTONS, !player.getVariables().getAttribute(AttributeKey.DUAL_MOUSE_BUTTONS, false));
						player.getTransmitter().sendSettings();
						return true;
					case 7:
						player.getVariables().putAttribute(AttributeKey.ACCEPTING_AID, !player.getVariables().getAttribute(AttributeKey.ACCEPTING_AID, true));
						player.getTransmitter().sendSettings();
						return true;
					case 14:
						player.getManager().getInterfaces().sendInterface(742, false);
						return true;
					case 16:
						player.getManager().getInterfaces().sendInterface(743, false);
						return true;
				}
				break;
			case InterfaceConstants.GAMEFRAME_INTERFACE_ID:
				if (componentId == 31) {
					// game
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.FILTER, GameBarStatus.NO_FILTER);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.FILTER, GameBarStatus.FILTER);
						return true;
					}
				} else if (componentId == 28) {
					// public
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.PUBLIC, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.PUBLIC, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.PUBLIC, GameBarStatus.OFF);
						return true;
					} else if (packetId == FIFTH_PACKET_ID) {
						updateGameBar(player, AttributeKey.PUBLIC, GameBarStatus.HIDE);
						return true;
					}
				} else if (componentId == 25) {
					 // private
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.PRIVATE, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.PRIVATE, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.PRIVATE, GameBarStatus.OFF);
						return true;
					}
				} else if (componentId == 8) {
					// friends
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.FRIENDS, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.FRIENDS, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.FRIENDS, GameBarStatus.OFF);
						return true;
					}
				} else if (componentId == 22) {
					//clan
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.CLAN, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.CLAN, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.CLAN, GameBarStatus.OFF);
						return true;
					}
				} else if (componentId == 19) {
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.TRADE, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.TRADE, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.TRADE, GameBarStatus.OFF);
						return true;
					}
					// trade
				}  else if (componentId == 16) {
					// assist
					if (packetId == SECOND_PACKET_ID) {
						updateGameBar(player, AttributeKey.ASSIST, GameBarStatus.ON);
						return true;
					} else if (packetId == THIRD_PACKET_ID) {
						updateGameBar(player, AttributeKey.ASSIST, GameBarStatus.FRIENDS);
						return true;
					} else if (packetId == LAST_PACKET_ID) {
						updateGameBar(player, AttributeKey.ASSIST, GameBarStatus.OFF);
						return true;
					}
				}
				break;
		}
		return false;
	}
	
	/**
	 * Updates the game bar
	 *
	 * @param player
	 * 		The player
	 * @param bar
	 * 		The bar
	 * @param status
	 * 		The status of the bar
	 */
	public static void updateGameBar(Player player, AttributeKey bar, GameBarStatus status) {
		player.getVariables().putAttribute(bar, status);
		player.getManager().getInterfaces().sendGameBar();
	}
}
