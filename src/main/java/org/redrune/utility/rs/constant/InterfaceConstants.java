package org.redrune.utility.rs.constant;

import org.redrune.utility.tool.Misc;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.CS2ScriptBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface InterfaceConstants extends InterfaceRepository {
	
	/**
	 * The id of the fixed screen window
	 */
	int SCREEN_FIXED_WINDOW_ID = 548;
	
	/**
	 * The id of the resizable screen window
	 */
	int SCREEN_RESIZABLE_WINDOW_ID = 746;
	
	/**
	 * The window id of the chatbox interface
	 */
	int CHATBOX_WINDOW_ID = 752;
	
	/**
	 * The child id that regular interfaces are displayed on (using fixed mode).
	 */
	int DISPLAY_FIXED_CHILD_ID = 18;
	
	/**
	 * The child id that regular interfaces are displayed on (using resizable mode).
	 */
	int DISPLAY_RESIZABLE_CHILD_ID = 11;
	
	/**
	 * The child id for inventory interfaces on fixed mode
	 */
	int INVENTORY_FIXED_CHILD_ID = 198;
	
	/**
	 * The child id for inventory interfaces on resizable mode
	 */
	int INVENTORY_RESIZABLE_CHILD_ID = 86;
	
	/**
	 * The interface id of the regular chatbox
	 */
	int REGULAR_CHATBOX_INTERFACE_ID = 137;
	
	/**
	 * The component id of the chatbox in fixed mode
	 */
	int FIXED_CHATBOX_COMPONENT_ID = 192;
	
	/**
	 * The component id of the chatbox in resizable mode
	 */
	int RESIZABLE_CHATBOX_COMPONENT_ID = 71;
	
	/**
	 * The id of the inventory interface
	 */
	int INVENTORY_INTERFACE_ID = 679;
	
	/**
	 * The id of the equipment interface
	 */
	int EQUIPMENT_INTERFACE_ID = 387;
	
	/**
	 * The id of the logout interface
	 */
	int LOGOUT_INTERFACE_ID = 182;
	
	/**
	 * The interface id of the gameframe
	 */
	int GAMEFRAME_INTERFACE_ID = 751;
	
	/**
	 * The id of the first trade interface [containers]
	 */
	int FIRST_TRADE_INTERFACE_ID = 335;
	
	/**
	 * The id of the second trade interface [confirmation]
	 */
	int SECOND_TRADE_INTERFACE_ID = 334;
	
	/**
	 * The id of the trade inventory interface
	 */
	int TRADE_INVENTORY_INTERFACE_ID = 336;
	
	/**
	 * Sends the quest interface to the player with the parameterized title and
	 * list of messages. The messages will be formatted to never overlap one
	 * line, but to go to the next one if it passes the limit of characters on a
	 * line.
	 *
	 * @param player
	 * 		The player
	 * @param title
	 * 		The title of the quest interface
	 * @param messageList
	 * 		The list of messages to send. a {@code String} {@code Array} {@code Object}
	 */
	static void sendQuestScroll(Player player, String title, String... messageList) {
		final int interfaceId = 275;
		final int endLine = 309;
		
		Misc.clearInterface(player, interfaceId);
		
		int startLine = 16;
		for (String message : messageList) {
			if (startLine > endLine) {
				break;
			}
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, startLine, message);
			startLine++;
		}
		
		player.getTransmitter().send(new CS2ScriptBuilder(1207, messageList.length).build(player));
		player.getManager().getInterfaces().sendInterfaceText(interfaceId, 2, title);
		player.getManager().getInterfaces().sendInterface(interfaceId, true);
	}
}
