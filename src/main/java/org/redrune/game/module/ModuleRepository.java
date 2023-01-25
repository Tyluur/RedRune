package org.redrune.game.module;

import org.apache.commons.lang3.ArrayUtils;
import org.redrune.game.module.interaction.InteractionModule;
import org.redrune.game.module.type.InterfaceInteractionModule;
import org.redrune.game.module.type.ItemInteractionModule;
import org.redrune.game.module.type.NPCInteractionModule;
import org.redrune.game.module.type.ObjectInteractionModule;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.utility.rs.InteractionOption;
import org.redrune.utility.tool.Misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public class ModuleRepository {
	
	/**
	 * The list of all interface modules
	 */
	private static final List<InterfaceInteractionModule> INTERFACE_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The list of all item modules
	 */
	private static final List<ItemInteractionModule> ITEM_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The list of all modules
	 */
	private static final List<NPCInteractionModule> NPC_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * The list of all modules
	 */
	private static final List<ObjectInteractionModule> OBJECT_MODULES = Collections.synchronizedList(new ArrayList<>());
	
	/**
	 * Registers all the modules
	 *
	 * @param reload
	 * 		If we should clear the current repository
	 */
	public static void registerAllModules(boolean reload) {
		if (reload) {
			empty();
		}
		for (String directory : new ArrayList<>(Misc.getSubDirectories(InteractionModule.class))) {
			Misc.getClassesInDirectory(InteractionModule.class.getPackage().getName() + "." + directory).stream().filter(InteractionModule.class::isInstance).forEach(clazz -> registerBindings((InteractionModule) clazz));
		}
		System.out.println(INTERFACE_MODULES.size() + " interface modules, " + ITEM_MODULES.size() + " item modules, " + NPC_MODULES.size() + " npc modules, and " + OBJECT_MODULES.size() + " object modules loaded.");
	}
	
	/**
	 * Removes all the entries in the module maps
	 */
	private static void empty() {
		INTERFACE_MODULES.clear();
		ITEM_MODULES.clear();
		NPC_MODULES.clear();
		OBJECT_MODULES.clear();
	}
	
	/**
	 * Registers the bindings of a module
	 *
	 * @param module
	 * 		The module
	 */
	private static void registerBindings(InteractionModule module) {
		if (module instanceof InterfaceInteractionModule) {
			INTERFACE_MODULES.add((InterfaceInteractionModule) module);
		}
		if (module instanceof ItemInteractionModule) {
			ITEM_MODULES.add((ItemInteractionModule) module);
		}
		if (module instanceof NPCInteractionModule) {
			NPC_MODULES.add((NPCInteractionModule) module);
		}
		if (module instanceof ObjectInteractionModule) {
			OBJECT_MODULES.add((ObjectInteractionModule) module);
		}
	}
	
	/**
	 * Handles the interface interaction
	 *
	 * @param player
	 * 		The player clicking the interface
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id of the interface
	 * @param itemId
	 * 		The item id on the interface, -1 if none.
	 * @param slotId
	 * 		The slot id on the interface, -1 if none.
	 * @param packetId
	 * 		The packet id of the click, different ids are used for different options
	 * @return {@code True} if it was handled successfully
	 */
	public static boolean handle(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		try {
			for (InterfaceInteractionModule module : getInterfaceModules(interfaceId)) {
				if (module.handle(player, interfaceId, componentId, itemId, slotId, packetId)) {
					return true;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the interface modules for an interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	private static List<InterfaceInteractionModule> getInterfaceModules(int interfaceId) {
		return INTERFACE_MODULES.stream().filter(module -> ArrayUtils.contains(module.interfaceSubscriptionIds(), interfaceId)).collect(Collectors.toList());
	}
	
	/**
	 * Handles the interaction with an npc by looping through all modules and finding which one will handle this.
	 *
	 * @param player
	 * 		The player interacting.
	 * @param npc
	 * 		The npc interacting with.
	 * @param option
	 * 		The option clicked on the npc.
	 * @return {@code True} if successfully interacted.
	 */
	public static boolean handle(Player player, NPC npc, InteractionOption option) {
		try {
			for (NPCInteractionModule module : getNPCModules(npc.getId())) {
				if (module.handle(player, npc, option)) {
					return true;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the npc modules for an nc
	 *
	 * @param npcId
	 * 		The id of the interface
	 */
	private static List<NPCInteractionModule> getNPCModules(int npcId) {
		return NPC_MODULES.stream().filter(module -> ArrayUtils.contains(module.npcSubscriptionIds(), npcId)).collect(Collectors.toList());
	}
	
	/**
	 * Handles the interaction with an item
	 *
	 * @param player
	 * 		The player
	 * @param item
	 * 		The item we're interacting with
	 * @param slotId
	 * 		The slot id in the inventory the item was clicked on
	 * @param option
	 * 		The option we clicked  @return {@code True} if successfully interacted.
	 */
	public static boolean handle(Player player, Item item, int slotId, InteractionOption option) {
		try {
			for (ItemInteractionModule module : getItemModules(item.getId())) {
				if (module.handle(player, item, slotId, option)) {
					return true;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the npc modules for an nc
	 *
	 * @param itemId
	 * 		The id of the interface
	 */
	private static List<ItemInteractionModule> getItemModules(int itemId) {
		return ITEM_MODULES.stream().filter(module -> ArrayUtils.contains(module.itemSubscriptionIds(), itemId)).collect(Collectors.toList());
	}
	
	/**
	 * Handles the interaction with the module
	 *
	 * @param player
	 * 		The player interacting
	 * @param object
	 * 		The object interacting with
	 * @param option
	 * 		The option clicked on the object
	 * @return {@code True} if the interaction was successful
	 */
	public static boolean handle(Player player, GameObject object, InteractionOption option) {
		try {
			for (ObjectInteractionModule module : getObjectModules(object.getId())) {
				if (module.handle(player, object, option)) {
					return true;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the npc modules for an nc
	 *
	 * @param objectId
	 * 		The id of the object
	 */
	private static List<ObjectInteractionModule> getObjectModules(int objectId) {
		return OBJECT_MODULES.stream().filter(module -> ArrayUtils.contains(module.objectSubscriptionIds(), objectId)).collect(Collectors.toList());
	}
	
}