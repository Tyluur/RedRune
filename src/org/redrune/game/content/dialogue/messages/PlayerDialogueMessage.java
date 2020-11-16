package org.redrune.game.content.dialogue.messages;

import org.redrune.game.content.dialogue.DialogueMessage;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.InterfaceAnimationBuilder;
import org.redrune.network.world.packet.outgoing.impl.InterfaceEntityBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class PlayerDialogueMessage extends DialogueMessage {
	
	/**
	 * The animation the player is doing
	 */
	private final int animationId;
	
	/**
	 * The messages to send
	 */
	private final String[] messages;
	
	public PlayerDialogueMessage(int animationId, String... messages) {
		this.animationId = animationId;
		this.messages = messages;
	}
	
	@Override
	public void send(Player player) {
		int interfaceId = 63 + this.messages.length;
		int[] componentOptions = getIComponentsIds(interfaceId);
		String title = player.getDetails().getDisplayName();
		String[] messages = getMessages(title, this.messages);
		if (componentOptions == null || (messages.length) != componentOptions.length) {
			return;
		}
		player.getManager().getInterfaces().sendChatboxInterface(interfaceId);
		for (int i = 0; i < componentOptions.length; i++) {
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, componentOptions[i], messages[i]);
		}
		player.getTransmitter().send(new InterfaceEntityBuilder(interfaceId, 2, -1).build(player));
		player.getTransmitter().send(new InterfaceAnimationBuilder(interfaceId, 2, animationId).build(player));
	}
	
}
