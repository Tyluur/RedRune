package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.CommandEventContext;
import org.redrune.game.content.event.impl.CommandEvent;
import org.redrune.game.module.interaction.rsinterface.GameframeInteractionModule;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.game.world.punishment.PunishmentType;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.network.world.packet.outgoing.impl.PublicChatBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.utility.tool.BufferUtils;
import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/26/2017
 */
public class CommunicationsPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The public chat message opcode.
	 */
	private static final int PUBLIC_CHAT = 19;
	
	/**
	 * The private chat message opcode
	 */
	public static final int PRIVATE_MESSAGE = 13;
	
	/**
	 * The  opcode of the game bar settings flag
	 */
	public static final int BAR_SETTINGS = 78;
	
	@Override
	public int[] bindings() {
		return arguments(PUBLIC_CHAT, PRIVATE_MESSAGE, BAR_SETTINGS);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case PUBLIC_CHAT:
				readPublicChatPacket(player, packet);
				break;
			case PRIVATE_MESSAGE:
				readPrivateMessagePacket(player, packet);
				break;
			case BAR_SETTINGS:
				readGameBarPacket(player, packet);
				break;
		}
	}
	
	/**
	 * Reads the packet for a public chat message
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPublicChatPacket(Player player, Packet packet) {
		int effects = packet.readShort();
		int length = packet.readByte() & 0xFF;
		String text = Misc.formatTextToSentence(BufferUtils.decompressHuffman(packet, length));
		if (text.startsWith("::")) {
			EventRepository.executeEvent(player, CommandEvent.class, new CommandEventContext(text.replaceFirst("::", "").split(" "), false));
			return;
		}
		if (player.getVariables().hasPunishment(PunishmentType.MUTE) || player.getVariables().hasPunishment(PunishmentType.ADDRESS_MUTE)) {
			player.getTransmitter().sendUnrepeatingMessages("You are muted.");
			return;
		}
		for (Player p : World.get().getPlayers()) {
			if (p == null || p.getLocation().getRegionId() != player.getLocation().getRegionId()) {
				continue;
			}
			p.getSession().write(new PublicChatBuilder(player.getIndex(), player.getDetails().getDominantRight().getClientRight(), text, effects).build(p));
		}
	}
	
	/**
	 * Decodes the packet saying that we should send a private message
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPrivateMessagePacket(Player player, Packet packet) {
		String name = Misc.formatPlayerNameForProtocol(packet.readRS2String());
		byte length = packet.readByte();
		String message = BufferUtils.decompressHuffman(packet, length);
		
		if (player.getVariables().hasPunishment(PunishmentType.MUTE) || player.getVariables().hasPunishment(PunishmentType.ADDRESS_MUTE)) {
			player.getTransmitter().sendUnrepeatingMessages("You are muted.");
			return;
		}
		
		player.getManager().getContacts().sendPrivateMessage(name, message);
	}
	
	/**
	 * Reads the packet for the game bar settings.
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	public void readGameBarPacket(Player player, Packet packet) {
		byte publicFlag = packet.readByte();
		byte privateFlag = packet.readByte();
		byte friendsFlag = packet.readByte();
		
		if (publicFlag < 0 || publicFlag > 3) {
			publicFlag = 0;
		}
		if (privateFlag < 0 || privateFlag > 2) {
			privateFlag = 0;
		}
		if (friendsFlag < 0 || friendsFlag > 2) {
			friendsFlag = 0;
		}
		
		final GameBarStatus publicStatus = GameBarStatus.byValue(publicFlag).orElse(GameBarStatus.ON);
		final GameBarStatus privateStatus = GameBarStatus.byValue(privateFlag).orElse(GameBarStatus.ON);
		final GameBarStatus friendsStatus = GameBarStatus.byValue(friendsFlag).orElse(GameBarStatus.ON);
		
		GameframeInteractionModule.updateGameBar(player, AttributeKey.PUBLIC, publicStatus);
		GameframeInteractionModule.updateGameBar(player, AttributeKey.PRIVATE, privateStatus);
		GameframeInteractionModule.updateGameBar(player, AttributeKey.FRIENDS, friendsStatus);
		
		player.getManager().getContacts().sendMyStatusChange(!privateStatus.equals(GameBarStatus.OFF));
	}
}
