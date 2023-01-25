package org.redrune.game.node.entity.player.data;

import lombok.Getter;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.World;
import org.redrune.network.world.packet.PacketBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * Holds the player's rendering data.
 *
 * @author Jolt environment v2 development team
 * @author Emperor (converted to Java + NPC information).
 * @author Tyluur
 */
public class RenderInformation {
	
	/**
	 * Holds the players' hash locations.
	 */
	@Getter
	private final int[] hashLocations = new int[2048];
	
	/**
	 * The local player indexes.
	 */
	@Getter
	private final short[] locals = new short[2048];
	
	/**
	 * The global player indexes.
	 */
	@Getter
	private final short[] globals = new short[2048];
	
	/**
	 * The local players.
	 */
	@Getter
	private final boolean[] isLocal = new boolean[2048];
	
	/**
	 * The skipped player indexes.
	 */
	@Getter
	private final byte[] skips = new byte[2048];
	
	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The amount of local players.
	 */
	public int localsCount = 0;
	
	/**
	 * The amount of global players.
	 */
	public int globalsCount = 0;
	
	/**
	 * The list of local NPCs.
	 */
	@Getter
	private List<NPC> localNpcs = new LinkedList<>();
	
	/**
	 * The player's last location.
	 */
	@Getter
	private Location lastLocation;
	
	/**
	 * If the player has just logged in.
	 */
	@Getter
	private boolean onFirstCycle;
	
	/**
	 * The amount of added players in the current update cycle.
	 */
	private int added;
	
	/**
	 * Constructs a new {@code RenderInformation} {@code Object}.
	 *
	 * @param player
	 * 		The player.
	 */
	public RenderInformation(Player player) {
		this.player = player;
		this.onFirstCycle = true;
	}
	
	/**
	 * Updates the player's map region packet with player information.
	 *
	 * @param packet
	 * 		The packet.
	 */
	public void enterWorld(PacketBuilder packet) {
		int myIndex = player.getIndex();
		locals[localsCount++] = (short) myIndex;
		isLocal[myIndex] = true;
		hashLocations[myIndex] = 0;
		packet.startBitAccess();
		packet.writeBits(30, player.getLocation().get30BitsHash());
		for (short index = 1; index < 2048; index++) {
			if (index == myIndex) {
				continue;
			}
			globals[globalsCount++] = index;
			Player p = World.get().getPlayers().get(index);
			if (p == null || !p.isRenderable()) {
				packet.writeBits(18, 0);
				continue;
			}
			packet.writeBits(18, p.getLocation().get18BitsHash());
		}
		packet.finishBitAccess();
	}
	
	/**
	 * Updates the player rendering information.
	 */
	public void updateInformation() {
		localsCount = 0;
		globalsCount = 0;
		added = 0;
		onFirstCycle = false;
		for (short i = 1; i < 2048; i++) {
			skips[i] >>= 1;
			if (isLocal[i]) {
				locals[localsCount++] = i;
			} else {
				globals[globalsCount++] = i;
			}
			Player p = World.get().getPlayers().get(i);
			if (p != null && p.isRenderable()) {
				hashLocations[i] = p.getLocation().get18BitsHash();
			}
		}
		World.get().updateHash((short) player.getIndex(), player.getLocation().getRegionLocation());
		lastLocation = player.getLocation().copy();
	}
	
	/**
	 * Gets the amount of currently added players in this cycle.
	 *
	 * @return The amount, incremented.
	 */
	public int getAddedIncr() {
		return added++;
	}
	
}