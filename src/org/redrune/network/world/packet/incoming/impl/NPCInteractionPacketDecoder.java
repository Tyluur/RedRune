package org.redrune.network.world.packet.incoming.impl;

import org.redrune.game.GameFlags;
import org.redrune.game.content.action.interaction.PlayerCombatAction;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.event.context.NPCEventContext;
import org.redrune.game.content.event.context.NodeReachEventContext;
import org.redrune.game.content.event.impl.NPCEvent;
import org.redrune.game.content.event.impl.NodeReachEvent;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.impl.FaceLocationUpdate;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.incoming.IncomingPacketDecoder;
import org.redrune.utility.repository.npc.spawn.NPCSpawnRepository;
import org.redrune.utility.rs.InteractionOption;

import static org.redrune.utility.rs.InteractionOption.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
public class NPCInteractionPacketDecoder implements IncomingPacketDecoder {
	
	/**
	 * The packet id for option
	 */
	private static final int FIRST_NPC_OPTION = 29, SECOND_NPC_OPTION = 10, THIRD_NPC_OPTION = 69, FOURTH_NPC_OPTION = 61, ATTACK_NPC_OPTION = 70, LAST_NPC_OPTION = 27, NPC_INTERFACE_USAGE = 57;
	
	@Override
	public int[] bindings() {
		return arguments(FIRST_NPC_OPTION, SECOND_NPC_OPTION, THIRD_NPC_OPTION, FOURTH_NPC_OPTION, ATTACK_NPC_OPTION, LAST_NPC_OPTION, NPC_INTERFACE_USAGE);
	}
	
	@Override
	public void read(Player player, Packet packet) {
		switch (packet.getOpcode()) {
			case NPC_INTERFACE_USAGE:
				readInterfaceUsagePacket(player, packet);
				break;
			default:
				readNPCOptionPacket(player, packet);
				break;
		}
	}
	
	/**
	 * Reads the packet that is sent when using an interface on an npc
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	@SuppressWarnings("unused")
	private void readInterfaceUsagePacket(Player player, Packet packet) {
		boolean running = packet.readByteS() == 1;
		int slot = packet.readLEShort();
		int interfaceHash = packet.readInt1();
		int id = packet.readLEShort();
		int index = packet.readLEShort();
		
		int interfaceId = interfaceHash >> 16;
		int componentId = interfaceHash & 0xFFFF;
		
		if (index < 0 || index > Short.MAX_VALUE) {
			return;
		}
		NPC npc = World.get().getNpcs().get(index);
		if (npc == null || npc.isDead() || !npc.isRenderable() || !player.getMapRegionsIds().contains(npc.getRegion().getRegionId())) {
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
							player.turnToLocation(Location.create(npc.getLocation().getCoordFaceX(npc.getSize()), npc.getLocation().getCoordFaceY(npc.getSize()), npc.getLocation().getPlane()));
							if (player.getManager().getActivities().handlesNodeInteraction(npc, InteractionOption.ATTACK_OPTION)) {
								return;
							}
							if (!npc.getCombatManager().isForceMultiAttacked()) {
								if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
									if (player.getAttackedBy() != npc && player.getAttackedByDelay() > System.currentTimeMillis()) {
										player.getTransmitter().sendMessage("You are already in combat.");
										return;
									}
									if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > System.currentTimeMillis()) {
										player.getTransmitter().sendMessage("This npc is already in combat.");
										return;
									}
								}
							}
							player.getManager().getActions().startAction(new PlayerCombatAction(npc));
						}
						break;
					default:
						System.out.println("Spell " + componentId + " was not added");
						break;
				}
				break;
			default:
				System.out.println("Interface on npc [" + interfaceId + "," + componentId + "] unhandled");
				break;
		}
	}
	
	/**
	 * Reads the packet that is sent when clicking an option on an npc
	 *
	 * @param player
	 * 		The player
	 * @param packet
	 * 		The packet
	 */
	private void readNPCOptionPacket(Player player, Packet packet) {
		int index = packet.readLEShortA();
		boolean forceRun = packet.readByteC() == 1;
		
		if (index < 0) {
			System.out.println("Invalid npc index found: " + index);
			return;
		}
		
		NPC npc = World.get().getNpcs().get(index);
		
		// for some reason this npc was found, we still shouldn't interact with it...
		if (npc == null || !npc.isRenderable()) {
			return;
		}
		InteractionOption option = getOptionByOpcode(packet.getOpcode());
		if (option == null) {
			System.out.println("Unable to identify interaction option for opcode " + packet.getOpcode());
			return;
		}
		// different options handled differently
		switch (option) {
			case ATTACK_OPTION:
				// stop everything
				player.stop(true, true, true, false);
				// face the player
				player.getUpdateMasks().register(new FaceLocationUpdate(player, npc.getLocation()));
				// make sure we aren't at multi
				
				if (!npc.getCombatManager().isForceMultiAttacked()) {
					if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
						if (player.getAttackedBy() != npc && player.getAttackedByDelay() > System.currentTimeMillis()) {
							player.getTransmitter().sendMessage("You are already in combat.");
							return;
						}
						if (npc.getAttackedBy() != player && npc.getAttackedByDelay() > System.currentTimeMillis()) {
							player.getTransmitter().sendMessage("This npc is already in combat.");
							return;
						}
					}
				}
				// make sure we can fight in the activity
				if (player.getManager().getActivities().handlesNodeInteraction(npc, InteractionOption.ATTACK_OPTION)) {
					return;
				}
				player.getManager().getActions().startAction(new PlayerCombatAction(npc));
				break;
			case EXAMINE:
				if (player.getAttribute("remove_npc_spawns", false)) {
					if (NPCSpawnRepository.removeNPC(npc)) {
						npc.deregister();
					} else {
						player.getTransmitter().sendMessage("Couldn't remove this npc!");
					}
				} else {
					if (GameFlags.debugMode) {
						player.getTransmitter().sendMessage(npc.toString(), true);
						System.out.println(npc.toString());
					}
				}
				break;
			default:
				player.getMovement().reset(forceRun);
				EventRepository.executeEvent(player, NodeReachEvent.class, new NodeReachEventContext(npc, () -> {
					// executing the npc interaction event on arrival
					EventRepository.executeEvent(player, NPCEvent.class, new NPCEventContext(npc, option));
				}));
				break;
		}
	}
	
	/**
	 * Gets the {@link InteractionOption} {@code Object} for the option clicked on the {@code NPC}
	 *
	 * @param opcode
	 * 		The option's opcode
	 */
	private InteractionOption getOptionByOpcode(int opcode) {
		switch (opcode) {
			case ATTACK_NPC_OPTION:
				return ATTACK_OPTION;
			case FIRST_NPC_OPTION:
				return FIRST_OPTION;
			case SECOND_NPC_OPTION:
				return SECOND_OPTION;
			case THIRD_NPC_OPTION:
				return THIRD_OPTION;
			case FOURTH_NPC_OPTION:
				return FOURTH_OPTION;
			case LAST_NPC_OPTION:
				return EXAMINE;
			default:
				return null;
		}
	}
}
