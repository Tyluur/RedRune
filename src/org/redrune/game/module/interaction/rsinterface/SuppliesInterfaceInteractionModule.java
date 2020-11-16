package org.redrune.game.module.interaction.rsinterface;

import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.link.InterfaceManager;
import org.redrune.game.node.item.Item;
import org.redrune.network.world.Transmitter;
import org.redrune.network.world.packet.outgoing.impl.AccessMaskBuilder;
import org.redrune.network.world.packet.outgoing.impl.CS2ScriptBuilder;
import org.redrune.network.world.packet.outgoing.impl.ContainerPacketBuilder;

import java.util.Arrays;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/19/2017
 */
public class SuppliesInterfaceInteractionModule implements InterfaceInteractionModule {
	
	/**
	 * The id of the supplies interface
	 */
	public static final int INTERFACE_ID = 860;
	
	@Override
	public int[] interfaceSubscriptionIds() {
		return arguments(INTERFACE_ID);
	}
	
	@Override
	public boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		return true;
	}
	
	/**
	 * Displays the supplies interface
	 *
	 * @param player
	 * 		player
	 */
	public static void display(Player player) {
		int[] hideComponents = { 20, 21, 26 };
		int titleLine = 18;
		int descriptionLine = 19;
		int itemsKey = 91;
		
		Item[] items = new Item[] { new Item(11694), null, new Item(4151) };
		InterfaceManager interfaces = player.getManager().getInterfaces();
		Transmitter transmitter = player.getTransmitter();
		
		Arrays.stream(hideComponents).forEach(value -> interfaces.sendInterfaceComponentChange(INTERFACE_ID, value, true));
		
		transmitter.send(new ContainerPacketBuilder(itemsKey, items).build(player));
		transmitter.send(new AccessMaskBuilder(INTERFACE_ID, 23, 0, items.length, 0, 1, 2, 3, 4, 5, 6).build(player));
		transmitter.send(CS2ScriptBuilder.getInterfaceUnlockScript(INTERFACE_ID, 23, 91, 8, 150, "Value", "Buy 1", "Buy 10", "Buy 100", "Buy X").build(player));
		
		interfaces.sendInterfaceText(INTERFACE_ID, titleLine, "Supplies");
		interfaces.sendInterfaceText(INTERFACE_ID, descriptionLine, "All supplies you need for the wilderness can be found here.<br>You can use the search function at the bottom to find specifics.");
		interfaces.sendInterface(INTERFACE_ID, true);
	}
	
}
