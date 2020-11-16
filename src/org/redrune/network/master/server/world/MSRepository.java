package org.redrune.network.master.server.world;

import org.redrune.network.master.MasterConstants;
import org.redrune.network.master.network.packet.OutgoingPacket;
import org.redrune.network.master.server.network.MSSession;
import org.redrune.utility.tool.Misc;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
public final class MSRepository implements MasterConstants {
	
	/**
	 * The array of worlds that we hold
	 */
	private static final MSWorld[] WORLDS = new MSWorld[20];
	
	/**
	 * Creates a new world
	 *
	 * @param worldId
	 * 		The id of the world
	 */
	public static MSWorld createNewWorld(byte worldId) {
		final int index = worldId;
		if (index < 0 || index >= WORLDS.length) {
			throw new IllegalStateException("Unexpected world id: " + worldId);
		}
		// we've already made this world.
		if (WORLDS[index] != null) {
			System.out.println("Attempted to create a new world when it was already made: " + worldId);
			return null;
		}
		// creates a new world
		final MSWorld world = new MSWorld(worldId);
		// saves the index of the world
		WORLDS[index] = world;
		
		System.out.println("World " + worldId + " was just registered & verified.");
		return world;
	}
	
	/**
	 * Checks if a player is online on any of the worlds
	 *
	 * @param username
	 * 		The username of the player
	 * @param checkLobby
	 * 		If we should check the lobby players
	 */
	public static boolean isOnline(String username, boolean checkLobby) {
		boolean online = false;
		
		if (checkLobby) {
			Optional<MSWorld> optional = getWorld(LOBBY_WORLD_ID);
			if (optional.isPresent()) {
				online = optional.get().isOnline(username);
			}
		}
		
		// we found them in the lobby! don't check worlds unnecessarily
		if (online) {
			return true;
		}
		
		// loop through all the worlds
		for (MSWorld world : WORLDS) {
			if (world == null || world.getId() == LOBBY_WORLD_ID) {
				continue;
			}
			// found the player is online, loop doesn't need to continue.
			if (world.isOnline(username)) {
				online = true;
				break;
			}
		}
		return online;
	}
	
	/**
	 * Gets a world by ids id
	 *
	 * @param worldId
	 * 		The world id
	 */
	public static Optional<MSWorld> getWorld(int worldId) {
		final int index = worldId;
		if (index < 0 || index >= WORLDS.length) {
			throw new IllegalStateException("Unexpected world id: " + worldId);
		}
		MSWorld world = WORLDS[index];
		if (world == null) {
			return Optional.empty();
		}
		return Optional.of(world);
	}
	
	/**
	 * Unregisters the world
	 *
	 * @param world
	 * 		The world
	 */
	public static void unregister(MSWorld world) {
		WORLDS[world.getId()] = null;
		world.unregister();
		System.out.println("World " + world.getId() + " was just unregistered.");
	}
	
	/**
	 * If the player exists in a world, we return the master session that that player is in
	 *
	 * @param username
	 * 		The name of the player
	 */
	public static Optional<MSSession> getSessionByUsername(String username) {
		String formattedName = Misc.formatPlayerNameForProtocol(username);
		// loop through all the worlds
		for (MSWorld world : WORLDS) {
			if (world == null) {
				continue;
			}
			// there is a player by that name in this world so this is the right world
			if (world.playerRegistered(formattedName)) {
				return Optional.of(world.getSession());
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Gets a player by their username from any of the worlds
	 *
	 * @param username
	 * 		The username of the player
	 */
	public static Optional<MSPlayer> getPlayer(String username) {
		for (MSWorld world : WORLDS) {
			if (world == null) {
				continue;
			}
			Optional<MSPlayer> optional = world.getPlayerByName(username);
			if (!optional.isPresent()) {
				continue;
			}
			return optional;
		}
		return Optional.empty();
	}
	
	/**
	 * Gets the player details in an object array. [0] = username, [1] = online/offline, [2] = world id
	 *
	 * @param username
	 * 		The name of the player
	 */
	public static Object[] getPlayerDetails(String username) {
		Optional<MSPlayer> optional = getPlayer(username);
		return optional.map(player -> new Object[] { username, true, player.getWorldId() }).orElseGet(() -> new Object[] { username, false, (byte) 0 });
	}
	
	/**
	 * Gets the amount of worlds that are active
	 */
	public static byte getWorldCount() {
		byte count = 0;
		for (MSWorld world : WORLDS) {
			if (world == null || world.isLobby()) {
				continue;
			}
			count++;
		}
		return count;
	}
	
	/**
	 * Gets all the worlds
	 */
	public static MSWorld[] getWorlds() {
		return WORLDS;
	}
}