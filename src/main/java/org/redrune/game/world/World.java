package org.redrune.game.world;

import com.google.common.base.Stopwatch;
import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.Cache;
import org.redrune.cache.parse.BodyDataParser;
import org.redrune.cache.parse.ItemDefinitionParser;
import org.redrune.core.boot.BootHandler;
import org.redrune.core.system.SystemManager;
import org.redrune.core.task.impl.*;
import org.redrune.game.GameConstants;
import org.redrune.game.GameFlags;
import org.redrune.game.content.activity.impl.pvp.PvPLocation;
import org.redrune.game.content.combat.player.CombatRegistry;
import org.redrune.game.content.dialogue.DialogueRepository;
import org.redrune.game.content.event.EventRepository;
import org.redrune.game.content.market.shop.ShopRepository;
import org.redrune.game.module.ModuleRepository;
import org.redrune.game.module.command.CommandRepository;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.EntityList;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.npc.extension.RockCrabNPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.RegionBuilder;
import org.redrune.game.world.region.RegionDeletion;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.MasterConstants;
import org.redrune.network.web.sql.SQLRepository;
import org.redrune.network.world.WorldNetwork;
import org.redrune.network.world.packet.incoming.IncomingPacketRepository;
import org.redrune.network.world.packet.incoming.impl.WalkPacketDecoder;
import org.redrune.utility.backend.MapKeyRepository;
import org.redrune.utility.backend.SequentialService;
import org.redrune.utility.backend.UnexpectedArgsException;
import org.redrune.utility.repository.item.ItemRepository;
import org.redrune.utility.repository.npc.combat.NPCCombatSwingRepository;
import org.redrune.utility.repository.npc.spawn.NPCSpawn;
import org.redrune.utility.repository.object.ObjectSpawnRepository;
import org.redrune.utility.rs.constant.Directions.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.fill;

/**
 * Contains all the collections and data to handle a world.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public final class World implements SequentialService {
	
	/**
	 * The world singleton
	 */
	private static World singleton = null;
	
	/**
	 * The id of the world
	 */
	@Getter
	private final byte id;
	
	/**
	 * The list of all players in the world
	 */
	@Getter
	private final EntityList<Player> players = new EntityList<>(GameConstants.PLAYERS_LIMIT, true);
	
	/**
	 * The {@code EntityList} of all npcs that exist.
	 */
	@Getter
	private final EntityList<NPC> npcs = new EntityList<>(GameConstants.NPCS_LIMIT, false);
	
	/**
	 * The instance of the packet repository
	 */
	@Getter
	private final IncomingPacketRepository packetRepository = new IncomingPacketRepository(WalkPacketDecoder.class.getPackage().getName());
	
	/**
	 * The instance of the stopwatch
	 */
	@Getter
	private final Stopwatch stopwatch = Stopwatch.createUnstarted();
	
	/**
	 * If the world is alive
	 */
	@Getter
	@Setter
	private boolean isAlive;
	
	/**
	 * The array of global player hash information
	 */
	private Location[] locationHashInformation = new Location[2048];
	
	/**
	 * Constructs a new world object
	 */
	public World(String[] args) {
		try {
			SystemManager.setDefaults(args);
		} catch (UnexpectedArgsException e) {
			UnexpectedArgsException.push();
			System.exit(1);
		}
		this.id = GameFlags.worldId;
		if (id != MasterConstants.LOBBY_WORLD_ID) {
			this.packetRepository.storeAll();
		}
		fill(locationHashInformation, Location.create(0, 0, 0));
		setAlive(true);
	}
	
	@Override
	public void start() {
		// startup necessities
		stopwatch.start();
	}
	
	@Override
	public void execute() {
		if (!isLobby()) {
			BootHandler.addWork(() -> {
				Cache.init();
				BodyDataParser.loadAll();
				RegionBuilder.init();
				CombatRegistry.registerAll();
				ItemDefinitionParser.loadEquipmentConfiguration();
				ShopRepository.load();
			}, () -> {
				ItemRepository.initialize(false);
				MapKeyRepository.readAll();
			}, () -> {
				RegionDeletion.prepare();
				ObjectSpawnRepository.get().loadAll();
			}, () -> {
				DialogueRepository.loadSubscriptions();
				NPCCombatSwingRepository.loadAll();
			}, () -> {
				EventRepository.registerEvents(false);
				SQLRepository.storeConfiguration();
			}, () -> {
				ModuleRepository.registerAllModules(false);
				CommandRepository.populate(false);
			});
		} else {
			BootHandler.addWork();
		}
		BootHandler.await();
	}
	
	@Override
	public void end() {
		SystemManager.start();
		if (isLobby()) {
			return;
		}
		System.out.println("Started world " + id + " in " + stopwatch.elapsed(TimeUnit.MILLISECONDS) + " ms.");
		// master server can now listen
		MasterCommunication.start();
		// start the world tasks now that everything has loaded
		generateWorldTasks();
		try {
			// this waits for the session to close, so anything after this method will not execute until shutdown
			WorldNetwork.bind();
		} catch (Throwable e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Generates tasks that operate for the world
	 */
	private void generateWorldTasks() {
		SystemManager.getScheduler().schedule(new EnergyRestorationTask());
		SystemManager.getScheduler().schedule(new SkillRestorationTask());
		SystemManager.getScheduler().schedule(new HitpointsRestorationTask());
		SystemManager.getScheduler().schedule(new PlayerSavingTask());
		SystemManager.getScheduler().schedule(new InformationTabTask());
		SystemManager.getScheduler().schedule(new SpecialEnergyRestorationTask());
	}
	
	/**
	 * Creates a new world
	 *
	 * @param args
	 * 		The arguments of the world
	 */
	public static World create(String[] args) {
		synchronized (World.class) {
			return singleton = new World(args);
		}
	}
	
	/**
	 * Gets the singleton instance
	 *
	 * @return A {@code World} {@code Object}
	 */
	public static World get() {
		return singleton;
	}
	
	/**
	 * Gets the hash of a player
	 *
	 * @param playerIndex
	 * 		The index of the player
	 */
	public Location getHash(short playerIndex) {
		return this.locationHashInformation[playerIndex];
	}
	
	/**
	 * Updates the hash of the player
	 *
	 * @param playerIndex
	 * 		The index of the player
	 * @param loc
	 * 		The location of the player
	 */
	public void updateHash(short playerIndex, Location loc) {
		this.locationHashInformation[playerIndex] = loc;
	}
	
	/**
	 * Adds an npc to the world, in the form of a {@link NPCSpawn} {@code Object}
	 *
	 * @param spawn
	 * 		The {@code NPCSpawn} object
	 */
	public World addSpawn(NPCSpawn spawn) {
		addNPC(spawn.getNpcId(), spawn.getTile(), spawn.getDirection());
		return this;
	}
	
	/**
	 * Adds an npc to the world
	 *
	 * @param id
	 * 		The id of the npc
	 * @param location
	 * 		The location of the npc
	 * @return The npc that was constructed
	 */
	public NPC addNPC(int id, Location location, Direction direction) {
		final NPC npc;
		if (id == 1266 || id == 1268 || id == 2453 || id == 2886) {
			npc = new RockCrabNPC(id, location, direction);
		} else {
			npc = new NPC(id, location, direction);
		}
		npc.register();
		npcs.add(npc);
		return npc;
	}
	
	/**
	 * Handles the removal of a player
	 *
	 * @param player
	 * 		The player to remove
	 */
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	/**
	 * Finds a player by their username
	 *
	 * @param username
	 * 		The username of the player
	 */
	public Optional<Player> getPlayerByUsername(String username) {
		return players.stream().filter(player -> player.getDetails().getUsername().equalsIgnoreCase(username)).findAny();
	}
	
	/**
	 * Checks if the tile is a pvp area
	 *
	 * @param location
	 * 		The tile
	 */
	public boolean isPvpArea(Location location) {
		if (id == GameConstants.PVP_WORLD_ID) {
			return PvPLocation.isAtPvpLocation(location);
		} else {
			return PvPLocation.isAtWild(location);
		}
	}
	
	/**
	 * Constructs a new list of all the players in the world and adds their names and their uid into the string entry.
	 */
	public List<String> getPlayersAsString() {
		List<String> playerList = new ArrayList<>();
		for (Player player : players) {
			playerList.add(player.getDetails().getUsername() + ":::::" + player.getSession().getUid());
		}
		return playerList;
	}
	
	/**
	 * Gets the amount of players
	 */
	public int getPlayerCount() {
		return players.size();
	}
	
	/**
	 * If this world is a lobby
	 */
	public boolean isLobby() {
		return id == MasterConstants.LOBBY_WORLD_ID;
	}
}
