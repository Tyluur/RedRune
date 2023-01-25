package org.redrune.game.world.list;

import org.redrune.network.NetworkConstants;
import org.redrune.network.world.packet.Packet.PacketType;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.rs.constant.WorldConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Holds all the current worlds.
 *
 * @author Dementhium development team
 */
public class WorldList implements WorldConstants {
	
	/**
	 * A list holding all the currently loaded worlds.
	 */
	private static final List<WorldDefinition> WORLD_LIST = new ArrayList<>();
	
	/* Populates the world list. */
	static {
		WORLD_LIST.add(new WorldDefinition(1, 0, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGHLIGHT, "Main World", NetworkConstants.MAIN_WORLD_IP, "USA", COUNTRY_CANADA));
		WORLD_LIST.add(new WorldDefinition(2, 0, FLAG_MEMBERS | FLAG_LOOTSHARE | FLAG_HIGH_RISK, "PvP World", NetworkConstants.PVP_WORLD_IP, "USA", COUNTRY_USA));
	}
	
	/**
	 * Gets the packet to update the world list in the lobby.
	 *
	 * @param worldConfiguration
	 * 		If the configuration should be added.
	 * @param worldStatus
	 * 		If the status should be added.
	 * @return The {@code OutgoingPacket} to write.
	 */
	public static PacketBuilder getData(boolean worldConfiguration, boolean worldStatus) {
		PacketBuilder bldr = new PacketBuilder(23, PacketType.VAR_SHORT);
		bldr.writeByte(1);
		bldr.writeByte(2);
		bldr.writeByte(1);
		if (worldConfiguration) {
			populateConfiguration(bldr);
		}
		if (worldStatus) {
			populateStatus(bldr);
		}
		return bldr;
	}
	
	/**
	 * Adds the world configuration on the packet.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	public static void populateConfiguration(PacketBuilder buffer) {
		buffer.writeSmart(WORLD_LIST.size());
		setCountry(buffer);
		buffer.writeSmart(0);
		buffer.writeSmart(WORLD_LIST.size() + 1);
		buffer.writeSmart(WORLD_LIST.size());
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeByte(0);
			buffer.writeInt(w.getFlag());
			buffer.writeGJString(w.getActivity());
			buffer.writeGJString(w.getIp());
		}
		buffer.writeInt(0x94DA4A87);
	}
	
	/**
	 * Adds the world status on the packet.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	public static void populateStatus(PacketBuilder buffer) {
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getWorldId());
			buffer.writeShort(w.getSize());
		}
	}
	
	/**
	 * Sets the countries for each world.
	 *
	 * @param buffer
	 * 		The current packet.
	 */
	private static void setCountry(PacketBuilder buffer) {
		for (WorldDefinition w : WORLD_LIST) {
			buffer.writeSmart(w.getCountry());
			buffer.writeGJString(w.getRegion());
		}
	}
	
	/**
	 * Updates the amount of players in the world
	 *
	 * @param worldId
	 * 		The world id
	 * @param value
	 * 		The amount of players
	 */
	public static void updateSize(int worldId, int value) {
		Optional<WorldDefinition> definition = findDefinitionById(worldId);
		if (!definition.isPresent()) {
			System.out.println("unable to find world by " + worldId + ", supposed to update size....");
			return;
		}
		definition.get().setSize((short) value);
	}
	
	/**
	 * Finds the world definition by the worlds id
	 *
	 * @param worldId
	 * 		The world's id
	 */
	private static Optional<WorldDefinition> findDefinitionById(int worldId) {
		return WORLD_LIST.stream().filter(definition -> definition.getWorldId() == worldId).findFirst();
	}
	
}
