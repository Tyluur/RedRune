package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.content.action.interaction.PlayerCombatAction;
import org.redrune.game.content.action.interaction.PlayerFollowAction;
import org.redrune.game.content.combat.StaticCombatFormulae;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.node.entity.link.interaction.TradeInteraction;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.impl.FaceLocationUpdate;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.rs.InteractionOption;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/20/2017
 */
public class PlayerInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The attack player opcode
	 */
	private static final byte ATTACK_PLAYER = 43;
	
	/**
	 * The follow player opcode
	 */
	private static final byte FOLLOW_PLAYER = 44;
	
	/**
	 * The trade player opcode
	 */
	private static final byte PLAYER_TRADE_REQUEST = 90;
	
	/**
	 * The interface on player packet
	 */
	private static final byte PLAYER_INTERFACE_USAGE = 65;
	
	/**
	 * The request accept opcode
	 */
	private static final byte PLAYER_REQUEST_ACCEPT = 83;
	
	@Override
	public int[] bindings() {
		return arguments(ATTACK_PLAYER, FOLLOW_PLAYER, PLAYER_TRADE_REQUEST, PLAYER_INTERFACE_USAGE, PLAYER_REQUEST_ACCEPT);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case PLAYER_INTERFACE_USAGE:
				readPlayerInterfaceUsage(player, packet);
				break;
			case PLAYER_REQUEST_ACCEPT:
				readPlayerRequestAcceptPacket(player, packet);
				break;
			default:
				readPlayerOptionPacket(player, packet);
				break;
		}
	}
	
	/**
	 * Reads the packet received when an interface is used on a player
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPlayerInterfaceUsage(Player player, Packet packet) {
		int index = packet.readLEShort();
		boolean running = packet.readByte() == 1;
		int id = packet.readShortA();
		int interfaceHash = packet.readLEInt();
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash & 0xFFFF;
		int slot = packet.readLEShortA();
		if (index < 0 || index > 2048) {
			return;
		}
		Player p2 = World.get().getPlayers().get(index);
		if (p2 == null) {
			return;
		}
		switch (interfaceId) {
			case 192: // regular
			case 193: // ancients
				// we put them all into one switch statement because the actual logic is in the
				// CombatRegistry#checkCombatSpell
				switch (componentId) {
					case 25: // air strike
					case 28: // water strike
					case 30: // earth strike
					case 32: // fire strike
					case 34: // air bolt
					case 42: // earth bolt
					case 45: // fire bolt
					case 49: // air blast
					case 52: // water blast
					case 58: // earth blast
					case 63: // fire blast
					case 70: // air wave
					case 73: // water wave
					case 77: // earth wave
					case 80: // fire wave
					case 84: // air surge
					case 87: // water surge
					case 89: // earth surge
					case 66: // Sara Strike
					case 67: // Guthix Claws
					case 68: // Flame of Zammy
					case 93:
					case 91: // fire surge
					case 99: // storm of Armadyl
					case 55: // snare
					case 81: // entangle
					case 24:
					case 20:
					case 26:
					case 22:
					case 29:
					case 33:
					case 21:
					case 31:
					case 35:
					case 27:
					case 23:
					case 75:
					case 78:
					case 82:
					case 86: // teleblock
					case 36: // bind
					case 37:
					case 38:
					case 39: // water bolt
						if (CombatRegistry.checkCombatSpell(player, componentId, 1, false)) {
							if (!StaticCombatFormulae.canFight(player, p2)) {
								return;
							}
							player.getManager().getActions().startAction(new PlayerCombatAction(p2));
						}
						break;
				}
				break;
		}
	}
	
	/**
	 * Reads the packet that is sent when a player option is clicked
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPlayerOptionPacket(Player player, Packet packet) {
		int index = packet.readShort();
		boolean running = packet.readByte() == 1;
		if (index > 2047 || index < 1) {
			return;
		}
		Player p2 = World.get().getPlayers().get(index);
		if (p2 == null) {
			return;
		}
		switch (packet.getOpcode()) {
			case ATTACK_PLAYER:
				decodePlayerAttack(player, p2);
				break;
			case FOLLOW_PLAYER:
				decodePlayerFollow(player, p2);
				break;
			case PLAYER_TRADE_REQUEST:
				handleTradeRequest(player, p2);
				break;
		}
	}
	
	/**
	 * Reads the player request accept packet
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readPlayerRequestAcceptPacket(Player player, Packet packet) {
		int index = packet.readShort();
		int type = packet.readByte();
		if (index < 1 || index > 2047) {
			return;
		}
		Player p2 = World.get().getPlayers().get(index);
		if (p2 == null) {
			return;
		}
		switch(type) {
			// trade request type
			case 0:
				handleTradeRequest(player, p2);
				break;
		}
	}
	
	/**
	 * Decodes the player attack packet
	 *
	 * @param player
	 * 		The player
	 * @param p2
	 * 		The other player we're attacking
	 */
	private void decodePlayerAttack(Player player, Player p2) {
		// stop everything
		player.stop(true, true, true, false);
		// face the player
		player.getUpdateMasks().register(new FaceLocationUpdate(player, p2.getLocation()));
		
		if (!player.getVariables().isInFightArea()) {
			return;
		}
		if (!player.getVariables().isInFightArea() || !p2.getVariables().isInFightArea()) {
			player.getTransmitter().sendMessage("You can only attack players in a player-vs-player area.");
			return;
		}
		if (!p2.isAtMultiArea() || !player.isAtMultiArea()) {
			if (player.getAttackedBy() != p2 && player.getAttackedByDelay() > System.currentTimeMillis()) {
				player.getTransmitter().sendMessage("You are already in combat.");
				return;
			}
			if (p2.getAttackedBy() != player && p2.getAttackedByDelay() > System.currentTimeMillis()) {
				if (p2.getAttackedBy() instanceof NPC) {
					System.out.println("set the p2 to attack " + player);
					p2.setAttackedBy(player);
				} else {
					player.getTransmitter().sendMessage("That player is already in combat.");
					return;
				}
			}
		}
		
		// make sure we can fight in the activity
		if (player.getManager().getActivities().handlesNodeInteraction(p2, InteractionOption.ATTACK_OPTION)) {
			return;
		}
		player.getManager().getActions().startAction(new PlayerCombatAction(p2));
	}
	
	/**
	 * Decodes the player follow packet
	 *
	 * @param player
	 * 		The player
	 * @param other
	 * 		The other player we're following
	 */
	private void decodePlayerFollow(Player player, Player other) {
		player.stop(true, true, true, false);
		player.getManager().getActions().startAction(new PlayerFollowAction(other));
	}
	
	/**
	 * Decodes the player request packet
	 *
	 * @param player
	 * 		The player
	 * @param other
	 * 		The other player we're requesting
	 */
	private void handleTradeRequest(Player player, Player other) {
		player.stop(true, true, true, false);
		int distance = player.getLocation().getDistance(other.getLocation());
		if (distance == 0 || distance > 1) {
			EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(other, () -> {
				if (!other.isRenderable()) {
					return;
				}
				TradeInteraction.handleTradeRequesting(player, other);
			}));
		} else {
			TradeInteraction.handleTradeRequesting(player, other);
		}
	}
	
}
