package org.redrune.network.world.packet.incoming.impl;

import org.redrune.utility.tool.Misc;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/22/2017
 */
public class InputDevicePacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The opcode for mouse clicking
	 */
	private static final int MOUSE_CLICK = 36;
	
	/**
	 * Thet opcode for mouse motions
	 */
	private static final int MOUSE_MOTION = 88;
	
	/**
	 * The opcode for keyboard typing
	 */
	private static final int KEYPRESS = 89;
	
	/**
	 * The opcode for window focusing
	 */
	private static final int WINDOW_FOCUS = 60;
	
	/**
	 * This opcode is unknown at this point
	 */
	private static final int UNKNOWN_STREAM = 21;
	
	@Override
	public int[] bindings() {
		return Misc.arguments(MOUSE_MOTION, MOUSE_CLICK, KEYPRESS, WINDOW_FOCUS, UNKNOWN_STREAM);
	}
	
	@Override
	@SuppressWarnings("unused")
	public void read(Player player, Packet packet) {
		final int opcode = packet.getOpcode();
		if (opcode == MOUSE_CLICK) {
			short clickData = (short) packet.readLEShortA();
			short timePassed = (short) (clickData & 0x7FFF);
			boolean leftClick = (clickData >> 15) == 0;
			int positionData = packet.readLEInt();
			short screenClickX = (short) (positionData >> 16);
			short screenClickY = (short) (positionData & 0xFFFF);
		} else if (opcode == MOUSE_MOTION) {
//			packet.readByte();
		} else if (opcode == KEYPRESS) {
			while (packet.remaining() >= 3) {
				byte keyId = packet.readByte();
				short timePassed = (short) packet.readShort();
				fireKeyListeners(player, keyId);
			}
		} else if (opcode == WINDOW_FOCUS) {
			boolean focus = packet.readByte() == 1;
		} else if (opcode == UNKNOWN_STREAM) {
			final int unknown = packet.readShort();
		}
	}
	
	/**
	 * Fires the event listeners for the key pressed
	 *
	 * @param player
	 * 		The player
	 * @param keyId
	 * 		The id of the key pressed
	 */
	private void fireKeyListeners(Player player, byte keyId) {
		int chatboxId = player.getManager().getInterfaces().getChatboxInterface();
		boolean threeOptionDialogue = chatboxId == 230;
		switch (keyId) {
			case 13: // esc
				player.stop(false, false, true, false);
				break;
			case 16: // 1
				player.getManager().getDialogues().handleOption(chatboxId, threeOptionDialogue ? 2 : 1);
				break;
			case 17: // 2
				player.getManager().getDialogues().handleOption(chatboxId, threeOptionDialogue ? 3 : 2);
				break;
			case 18: // 3
				player.getManager().getDialogues().handleOption(chatboxId, threeOptionDialogue ? 4 : 3);
				break;
			case 19: // 4
				player.getManager().getDialogues().handleOption(chatboxId, 4);
				break;
			case 20: // 5
				player.getManager().getDialogues().handleOption(chatboxId, 5);
				break;
			case 83: // space
				if (chatboxId != -1) {
					player.getManager().getDialogues().handleOption(chatboxId, -1);
				}
				break;
			default:
				break;
		}
	}
}
