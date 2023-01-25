package org.redrune.game.content.dialogue;

import org.redrune.game.node.entity.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public abstract class DialogueMessage {
	
	/**
	 * Sends the dialogue message
	 *
	 * @param player
	 * 		The message
	 */
	public abstract void send(Player player);
	
	/**
	 * Sends the dialogue
	 *
	 * @param player
	 * 		The player clicking the option
	 * @param option
	 * 		The option clicked
	 */
	public void handleOption(Player player, int option) {
	
	}
	
	/**
	 * Gets the component ids of a dialogue interface
	 *
	 * @param interfaceId
	 * 		The interface
	 */
	protected int[] getIComponentsIds(int interfaceId) {
		int childOptions[];
		switch (interfaceId) {
			case 458:
				childOptions = new int[4];
				for (int i = 0; i < childOptions.length; i++) {
					childOptions[i] = i;
				}
				break;
			case 210:
				childOptions = new int[1];
				childOptions[0] = 1;
				break;
			case 211:
				childOptions = new int[2];
				childOptions[0] = 1;
				childOptions[1] = 2;
				break;
			
			case 212:
				childOptions = new int[3];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				break;
			
			case 213:
				childOptions = new int[4];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				childOptions[3] = 4;
				break;
			
			case 229:
				childOptions = new int[3];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				break;
			
			case 230:
				childOptions = new int[4];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				childOptions[3] = 4;
				break;
			
			case 231:
				childOptions = new int[4];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				childOptions[3] = 4;
				break;
			
			case 235:
				childOptions = new int[4];
				childOptions[0] = 1;
				childOptions[1] = 2;
				childOptions[2] = 3;
				childOptions[3] = 4;
				break;
			
			case 236:
				childOptions = new int[3];
				childOptions[0] = 0;
				childOptions[1] = 1;
				childOptions[2] = 2;
				break;
			
			case 237:
				childOptions = new int[5];
				childOptions[0] = 0;
				childOptions[1] = 1;
				childOptions[2] = 2;
				childOptions[3] = 3;
				childOptions[4] = 4;
				break;
			
			case 238:
				childOptions = new int[6];
				childOptions[0] = 0;
				childOptions[1] = 1;
				childOptions[2] = 2;
				childOptions[3] = 3;
				childOptions[4] = 4;
				childOptions[5] = 5;
				break;
			
			case 64:
				childOptions = new int[2];
				childOptions[0] = 3;
				childOptions[1] = 4;
				break;
			
			case 65:
				childOptions = new int[3];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				break;
			
			case 66:
				childOptions = new int[4];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				childOptions[3] = 6;
				break;
			
			case 67:
				childOptions = new int[5];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				childOptions[3] = 6;
				childOptions[4] = 7;
				break;
			
			case 241:
			case 245:
				childOptions = new int[2];
				childOptions[0] = 3;
				childOptions[1] = 4;
				break;
			
			case 242:
			case 246:
				childOptions = new int[3];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				break;
			
			case 243:
			case 247:
				childOptions = new int[4];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				childOptions[3] = 6;
				break;
			
			case 244:
			case 248:
				childOptions = new int[5];
				childOptions[0] = 3;
				childOptions[1] = 4;
				childOptions[2] = 5;
				childOptions[3] = 6;
				childOptions[4] = 7;
				break;
			
			case 214:
			case 215:
			case 216:
			case 217:
			case 218:
			case 219:
			case 220:
			case 221:
			case 222:
			case 223:
			case 224:
			case 225:
			case 226:
			case 227:
			case 228:
			case 232:
			case 233:
			case 234:
			case 239:
			case 240:
			default:
				return null;
		}
		return childOptions;
	}
	
	/**
	 * Constructs an array of the full dialogue with the title and messages
	 *
	 * @param title
	 * 		The title of the dialogue
	 * @param message
	 * 		The message of the dialogue
	 */
	protected String[] getMessages(String title, String[] message) {
		List<String> textList = new ArrayList<>();
		textList.add(title);
		Collections.addAll(textList, message);
		return textList.toArray(new String[textList.size()]);
	}
}
