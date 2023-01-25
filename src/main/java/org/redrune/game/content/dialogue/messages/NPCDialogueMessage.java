package org.redrune.game.content.dialogue.messages;

import org.redrune.cache.parse.NPCDefinitionParser;
import org.redrune.cache.parse.definition.NPCDefinition;
import org.redrune.game.content.dialogue.DialogueMessage;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.InterfaceAnimationBuilder;
import org.redrune.network.world.packet.outgoing.impl.InterfaceEntityBuilder;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
public class NPCDialogueMessage extends DialogueMessage {
	
	/**
	 * The id of the npc
	 */
	private final int npcId;
	
	/**
	 * The animation the npc face is doing
	 */
	private final int animationId;
	
	/**
	 * The messages to send
	 */
	private final String[] messages;
	
	public NPCDialogueMessage(int npcId, int animationId, String... messages) {
		this.npcId = npcId;
		this.animationId = animationId;
		this.messages = messages;
	}
	
	@Override
	public void send(Player player) {
		int interfaceId = 240 + messages.length;
		int[] componentOptions = getIComponentsIds((short) interfaceId);
		final NPCDefinition definition = NPCDefinitionParser.forId(npcId);
		if (definition == null) {
			throw new IllegalStateException("Unable to find npc definition #" + npcId);
		}
		String title = definition.getName();
		String[] messages = getMessages(title, this.messages);
		if (componentOptions == null || (messages.length) != componentOptions.length) {
			return;
		}
		player.getManager().getInterfaces().sendChatboxInterface(interfaceId);
		for (int i = 0; i < componentOptions.length; i++) {
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, componentOptions[i], messages[i]);
		}
		player.getTransmitter().send(new InterfaceEntityBuilder(interfaceId, 2, npcId).build(player));
		player.getTransmitter().send(new InterfaceAnimationBuilder(interfaceId, 2, animationId).build(player));
	}
	
}
