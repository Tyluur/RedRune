package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheFileStore;
import org.redrune.game.content.event.EventListener;
import org.redrune.game.content.event.EventListener.EventType;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.*;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.rs.GameTab;
import org.redrune.utility.rs.constant.GameBarStatus;
import org.redrune.utility.rs.constant.InterfaceConstants;
import org.redrune.utility.rs.input.InputType;
import org.redrune.utility.tool.ColorConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class InterfaceManager implements InterfaceConstants {
	
	/**
	 * The bindings that store the data of the currently active interfaces. The key is the component id that an
	 * interface is drawn on. The values are an integer array. array[0] = interfaceId, array[1] = paneId.
	 */
	private final Map<Integer, int[]> interfaceBindings = new HashMap<>();
	
	/**
	 * The pane to draw the components on
	 */
	@Setter
	@Getter
	private int paneId;
	
	/**
	 * The player that owns this manager
	 */
	@Setter
	private transient Player player;
	
	/**
	 * Sends all the login configurations
	 */
	public void sendLogin() {
		sendMainComponents();
		if (usingFixedMode()) {
			player.getTransmitter().sendFixedAMasks();
		} else {
			player.getTransmitter().sendFullScreenAMasks();
		}
		EmoteManager.sendUnlockConfigs(player);
		sendGameBar();
		player.getManager().getContacts().sendLogin();
		player.getCombatDefinitions().sendLogin();
		player.getManager().getPrayers().sendLoginConfigurations();
	}
	
	/**
	 * Sends the main components
	 */
	private InterfaceManager sendMainComponents() {
		if (usingFixedMode()) {
			sendWindowPane(SCREEN_FIXED_WINDOW_ID);
			sendInterface(67, 751);
			sendInterface(FIXED_CHATBOX_COMPONENT_ID, CHATBOX_WINDOW_ID);
			sendInterface(16, 754);
			sendInterface(182, 748);
			sendInterface(184, 749);
			sendInterface(185, 750);
			sendInterface(187, 747);
			sendInterface(14, 745);
		} else {
			sendWindowPane(SCREEN_RESIZABLE_WINDOW_ID);
			sendInterface(18, 751);
			sendInterface(RESIZABLE_CHATBOX_COMPONENT_ID, CHATBOX_WINDOW_ID);
			sendInterface(72, 754);
			sendInterface(176, 748);
			sendInterface(177, 749);
			sendInterface(178, 750);
			sendInterface(179, 747);
			sendInterface(14, 745);
		}
		switch (player.getSession().getViewComponents().getScreenSizeMode()) {
			case 0:
			case 1:
				break;
			case 2:
			case 3:
				break;
		}
		sendInterface(CHATBOX_WINDOW_ID, 9, REGULAR_CHATBOX_INTERFACE_ID, false).sendDefaultTabs();
		player.getCombatDefinitions().refreshSpellbook();
		return this;
	}
	
	/**
	 * Sends the default tabs.
	 */
	private InterfaceManager sendDefaultTabs() {
		for (GameTab data : GameTab.values()) {
			sendInterface(usingFixedMode() ? data.getFixedChildId() : data.getResizedChildId(), data.getInterfaceId());
			switch (data) {
				case ACHIEVEMENT_TAB:
					int interfaceId = data.getInterfaceId();
					sendInterfaceText(interfaceId, 10, "<col=" + ColorConstants.RED + ">Information");
					sendInterfaceText(interfaceId, 16, "");
					for (byte i = 17; i < 25; i++) {
						sendInterfaceComponentChange(930, i, true);
					}
					break;
				default:
					break;
			}
		}
		return this;
	}
	
	/**
	 * If we have an interface open
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public boolean hasInterfaceOpen(int interfaceId) {
		if (interfaceId == paneId) {
			return true;
		}
		for (int[] values : interfaceBindings.values()) {
			if (values[0] == interfaceId) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Sends a window pane
	 *
	 * @param paneId
	 * 		The id of the pane
	 */
	public InterfaceManager sendWindowPane(int paneId, int subWindowId) {
		player.getTransmitter().send(new GameWindowBuilder(this.paneId = paneId, subWindowId).build(player));
		return this;
	}
	
	/**
	 * Sends an interface over the chatbox
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendChatboxInterface(int interfaceId) {
		return sendInterface(CHATBOX_WINDOW_ID, 13, interfaceId, false);
	}
	
	/**
	 * Sends an interface on the specified component id
	 *
	 * @param paneId
	 * 		The pane id to draw the interface on
	 * @param componentId
	 * 		The component id that the interface will be drawn on.
	 * @param interfaceId
	 * 		The id of the interface
	 * @param walkable
	 * 		If the interface should be walkable
	 */
	public InterfaceManager sendInterface(int paneId, int componentId, int interfaceId, boolean walkable) {
		if (interfaceId >= CacheFileStore.getInterfaceDefinitionsSize()) {
			throw new IllegalStateException("Unable to send an interface with id " + interfaceId);
		}
		if (interfaceBindings.get(componentId) != null) {
			closeInterface(paneId, componentId);
		}
		interfaceBindings.put(componentId, new int[] { interfaceId, paneId, walkable ? 1 : 0 });
		flushComponent(componentId);
		return this;
	}
	
	/**
	 * Closes an interface
	 *
	 * @param paneId
	 * 		The pane the interface is on
	 * @param componentId
	 * 		The component the interface is on
	 */
	public InterfaceManager closeInterface(int paneId, int componentId) {
		interfaceBindings.remove(componentId);
		player.getTransmitter().send(new CloseInterfaceBuilder(paneId, componentId).build(player));
		return this;
	}
	
	/**
	 * Sends the interface to the client, using the component key to get the other data to send.
	 *
	 * @param componentId
	 * 		The component key
	 */
	private InterfaceManager flushComponent(int componentId) {
		int[] values = interfaceBindings.get(componentId);
		if (values == null) {
			return this;
		}
		// the walk flag
		final int walkFlag = values[2];
		// if the walk flag is on [set to 1]
		boolean walkable = walkFlag == 1;
		// the interface should be sent differently from other ones because it is force walkable
		if (walkable) {
			player.getTransmitter().send(new InterfaceDisplayBuilder(values[1], componentId, values[0], true).build(player));
		} else {
			boolean forceWalkable = componentId == getScreenComponentId(usingFixedMode());
			player.getTransmitter().send(new InterfaceDisplayBuilder(values[1], componentId, values[0], !forceWalkable).build(player));
		}
		return this;
	}
	
	/**
	 * Gets the component id of the screen
	 *
	 * @param fixedMode
	 * 		If we are using fixed mode.
	 */
	private static int getScreenComponentId(boolean fixedMode) {
		return fixedMode ? DISPLAY_FIXED_CHILD_ID : DISPLAY_RESIZABLE_CHILD_ID;
	}
	
	/**
	 * Gets the component id of the screen
	 *
	 * @param fixedMode
	 * 		If we are using fixed mode.
	 */
	private static int getPrimaryOverlayComponentId(boolean fixedMode) {
		return fixedMode ? 7 : 9;
	}
	
	/**
	 * Gets the component id of the combat overlay
	 *
	 * @param fixedMode
	 * 		If we are on fixed mode
	 */
	private static int getCombatOverlayComponentId(boolean fixedMode) {
		return fixedMode ? 240 : 245;
	}
	
	/**
	 * If the player is using the fixed client mode.
	 */
	public boolean usingFixedMode() {
		return player.getSession().getViewComponents().usingFixedMode();
	}
	
	/**
	 * Sends a regular screen interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param force
	 * 		If we should force the interface to be shown (this means if we have a screen interface open, and force = false,
	 * 		it will not be shown)
	 */
	public InterfaceManager sendInterface(int interfaceId, boolean force) {
		if (!force && getScreenInterface() != -1) {
			player.getTransmitter().sendMessage("You need to close the interface you have open before doing this.", false);
			return this;
		}
		closeInputBox();
		EventListener.fireListener(player, EventType.SCREEN_INTERFACE_OPEN);
		return sendInterface(getScreenPaneId(usingFixedMode()), getScreenComponentId(usingFixedMode()), interfaceId, false);
	}
	
	/**
	 * Gets the current interface that we are displaying on the screen
	 */
	public int getScreenInterface() {
		int[] values = interfaceBindings.get(getScreenComponentId(usingFixedMode()));
		if (values == null) {
			return -1;
		} else {
			return values[0];
		}
	}
	
	/**
	 * Gets the primary interface we are displaying on the screen
	 */
	public int getPrimaryOverlayInterface() {
		int[] values = interfaceBindings.get(getPrimaryOverlayComponentId(usingFixedMode()));
		if (values == null) {
			return -1;
		} else {
			return values[0];
		}
	}
	
	/**
	 * Gets the primary interface we are displaying on the screen
	 */
	public int getCombatOverlayInterface() {
		int[] values = interfaceBindings.get(getCombatOverlayComponentId(usingFixedMode()));
		if (values == null) {
			return -1;
		} else {
			return values[0];
		}
	}
	
	/**
	 * Closes the input boxes
	 */
	public void closeInputBox() {
		Arrays.stream(InputType.values()).forEach(type -> player.removeAttribute(type.getName()));
		player.getTransmitter().closeInputBox();
	}
	
	/**
	 * Gets the pane id for the mode we're on
	 *
	 * @param usingFixedMode
	 * 		If we are on fixed mode
	 */
	private static int getScreenPaneId(boolean usingFixedMode) {
		return usingFixedMode ? SCREEN_FIXED_WINDOW_ID : SCREEN_RESIZABLE_WINDOW_ID;
	}
	
	/**
	 * Sends text on an interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id
	 * @param text
	 * 		The text
	 * @throws IllegalStateException
	 * 		When the component/interface id is out of bounds
	 */
	public InterfaceManager sendInterfaceText(int interfaceId, int componentId, String text) {
		if (interfaceId >= CacheFileStore.getInterfaceDefinitionsSize()) {
			throw new IllegalStateException("Unable to send an interface with id " + interfaceId);
		}
		if (componentId >= CacheFileStore.getAmountOfComponents(interfaceId)) {
			throw new IllegalStateException("Unable to send text on component " + componentId + " using interface " + interfaceId);
		}
		player.getTransmitter().send(new InterfaceStringBuilder(interfaceId, componentId, text).build(player));
		return this;
	}
	
	/**
	 * Sends an interface change packet
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 * @param componentId
	 * 		The component id of the interface
	 * @param hide
	 * 		If the button should be hidden
	 */
	public InterfaceManager sendInterfaceComponentChange(int interfaceId, int componentId, boolean hide) {
		if (interfaceId >= CacheFileStore.getInterfaceDefinitionsSize()) {
			throw new IllegalStateException("Unable to send an interface with id " + interfaceId);
		}
		if (componentId >= CacheFileStore.getAmountOfComponents(interfaceId)) {
			throw new IllegalStateException("Unable to send text on component " + componentId + " using interface " + interfaceId);
		}
		player.getTransmitter().send(new InterfaceChangeBuilder(interfaceId, componentId, hide).build(player));
		return this;
	}
	
	/**
	 * Sends an interface on a tab
	 *
	 * @param tab
	 * 		The tab to send it on
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendTab(GameTab tab, int interfaceId) {
		return sendInterface(usingFixedMode() ? tab.getFixedChildId() : tab.getResizedChildId(), interfaceId);
	}
	
	/**
	 * Sends the primary overlay
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendPrimaryOverlay(int interfaceId) {
		return sendWalkableInterface(getPrimaryOverlayComponentId(usingFixedMode()), interfaceId);
	}
	
	/**
	 * Closes the primary overlay
	 */
	public InterfaceManager closePrimaryOverlay() {
		return closeInterface(getPaneId(), getPrimaryOverlayComponentId(usingFixedMode()));
	}
	
	/**
	 * Sends an interface over the combat overlay component
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendCombatOverlay(int interfaceId) {
		return sendInterface(getCombatOverlayComponentId(usingFixedMode()), interfaceId);
	}
	
	/**
	 * Closes the primary overlay
	 */
	public InterfaceManager closeCombatOverlay() {
		return closeInterface(getPaneId(), getCombatOverlayComponentId(usingFixedMode()));
	}
	
	/**
	 * Sends an interface on the specified component id
	 *
	 * @param componentId
	 * 		The component id that the interface will be drawn on.
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendInterface(int componentId, int interfaceId) {
		return sendInterface(getScreenPaneId(usingFixedMode()), componentId, interfaceId, false);
	}
	
	/**
	 * Sends an interface on the specified component id
	 *
	 * @param componentId
	 * 		The component id that the interface will be drawn on.
	 * @param interfaceId
	 * 		The id of the interface
	 */
	private InterfaceManager sendWalkableInterface(int componentId, int interfaceId) {
		return sendInterface(getScreenPaneId(usingFixedMode()), componentId, interfaceId, true);
	}
	
	/**
	 * Sends the inventory interface
	 *
	 * @param interfaceId
	 * 		The id of the interface
	 */
	public InterfaceManager sendInventoryInterface(int interfaceId) {
		return sendInterface(usingFixedMode() ? INVENTORY_FIXED_CHILD_ID : INVENTORY_RESIZABLE_CHILD_ID, interfaceId);
	}
	
	/**
	 * Opens the world map
	 */
	public InterfaceManager openWorldMap() {
		sendWindowPane(755);
		int posHash = player.getLocation().getX() << 14 | player.getLocation().getY();
		player.getTransmitter().send(new CS2ConfigBuilder(622, posHash).build(player));
		player.getTransmitter().send(new CS2ConfigBuilder(674, posHash).build(player));
		return this;
	}
	
	/**
	 * Sends a window pane
	 *
	 * @param paneId
	 * 		The id of the pane
	 */
	public InterfaceManager sendWindowPane(int paneId) {
		player.getTransmitter().send(new GameWindowBuilder(this.paneId = paneId, 0).build(player));
		return this;
	}
	
	/**
	 * Closes all interfaces
	 */
	public InterfaceManager closeAll() {
		closeInputBox();
		closeAllInterfaces();
		return this;
	}
	
	/**
	 * Closes all the interfaces visible
	 */
	public InterfaceManager closeAllInterfaces() {
		if (getScreenInterface() != -1) {
			closeScreenInterface();
			EventListener.fireListener(player, EventType.SCREEN_INTERFACE_CLOSE);
			//			System.out.println("closed screen");
		}
		if (getChatboxInterface() != -1) {
			closeChatboxInterface();
			player.getManager().getDialogues().end();
			//			System.out.println("closed chatbox");
		}
		if (getInventoryInterface() != -1) {
			closeInventoryInterface();
			//			System.out.println("Closed inventory");
		}
		return this;
	}
	
	/**
	 * Closes the interface we have open on the screen
	 */
	public InterfaceManager closeScreenInterface() {
		int componentId = getScreenComponentId(usingFixedMode());
		int[] values = interfaceBindings.get(componentId);
		if (values == null) {
			return this;
		}
		EventListener.fireListener(player, EventType.SCREEN_INTERFACE_CLOSE);
		return closeInterface(getScreenPaneId(usingFixedMode()), componentId);
	}
	
	/**
	 * Gets the chatbox interface id
	 */
	public int getChatboxInterface() {
		for (Entry<Integer, int[]> entry : interfaceBindings.entrySet()) {
			if (entry.getKey() == 13 && entry.getValue()[1] == CHATBOX_WINDOW_ID) {
				return entry.getValue()[0];
			}
		}
		return -1;
	}
	
	/**
	 * Closes the chatbox interface and sends the regular one
	 */
	public InterfaceManager closeChatboxInterface() {
		return closeInterface(CHATBOX_WINDOW_ID, 13);
	}
	
	/**
	 * Gets  the id of the inventory interface
	 */
	public int getInventoryInterface() {
		int[] values = interfaceBindings.get(getInventoryComponentId(usingFixedMode()));
		if (values == null) {
			return -1;
		} else {
			return values[0];
		}
	}
	
	/**
	 * Closes the inventory interface
	 */
	public InterfaceManager closeInventoryInterface() {
		int componentId = getInventoryComponentId(usingFixedMode());
		int[] values = interfaceBindings.get(componentId);
		if (values == null) {
			return this;
		}
		return closeInterface(getScreenPaneId(usingFixedMode()), componentId);
	}
	
	/**
	 * Gets the component id of the screen
	 *
	 * @param fixedMode
	 * 		If we are using fixed mode.
	 */
	private static int getInventoryComponentId(boolean fixedMode) {
		return fixedMode ? INVENTORY_FIXED_CHILD_ID : INVENTORY_RESIZABLE_CHILD_ID;
	}
	
	/**
	 * Sends the game bar settings
	 */
	public void sendGameBar() {
		Object filterData = player.getVariables().getAttribute(AttributeKey.FILTER, GameBarStatus.NO_FILTER);
		Object publicData = player.getVariables().getAttribute(AttributeKey.PUBLIC, GameBarStatus.ON);
		Object privateData = player.getVariables().getAttribute(AttributeKey.PRIVATE, GameBarStatus.ON);
		Object friendsData = player.getVariables().getAttribute(AttributeKey.FRIENDS, GameBarStatus.ON);
		Object clanData = player.getVariables().getAttribute(AttributeKey.CLAN, GameBarStatus.ON);
		Object tradeData = player.getVariables().getAttribute(AttributeKey.TRADE, GameBarStatus.ON);
		Object assistData = player.getVariables().getAttribute(AttributeKey.ASSIST, GameBarStatus.ON);
		GameBarStatus filter = GameBarStatus.NO_FILTER;
		if (filterData != null) {
			if (filterData.getClass().equals(String.class)) {
				filter = GameBarStatus.valueOf(filterData.toString());
			} else {
				filter = (GameBarStatus) filterData;
			}
		}
		GameBarStatus publicStatus = GameBarStatus.ON;
		if (publicData != null) {
			if (publicData.getClass().equals(String.class)) {
				publicStatus = GameBarStatus.valueOf(publicData.toString());
			} else {
				publicStatus = (GameBarStatus) publicData;
			}
		}
		GameBarStatus privateStatus = GameBarStatus.ON;
		if (privateData != null) {
			if (privateData.getClass().equals(String.class)) {
				privateStatus = GameBarStatus.valueOf(privateData.toString());
			} else {
				privateStatus = (GameBarStatus) privateData;
			}
		}
		GameBarStatus friends = GameBarStatus.ON;
		if (friendsData != null) {
			if (friendsData.getClass().equals(String.class)) {
				friends = GameBarStatus.valueOf(friendsData.toString());
			} else {
				friends = (GameBarStatus) friendsData;
			}
		}
		GameBarStatus clan = GameBarStatus.ON;
		if (clanData != null) {
			if (clanData.getClass().equals(String.class)) {
				clan = GameBarStatus.valueOf(clanData.toString());
			} else {
				clan = (GameBarStatus) clanData;
			}
		}
		GameBarStatus trade = GameBarStatus.ON;
		if (tradeData != null) {
			if (tradeData.getClass().equals(String.class)) {
				trade = GameBarStatus.valueOf(tradeData.toString());
			} else {
				trade = (GameBarStatus) tradeData;
			}
		}
		GameBarStatus assist = GameBarStatus.ON;
		if (assistData != null) {
			if (assistData.getClass().equals(String.class)) {
				assist = GameBarStatus.valueOf(assistData.toString());
			} else {
				assist = (GameBarStatus) assistData;
			}
		}
		player.getTransmitter().sendGameStatuses(filter, clan, assist, friends);
		player.getTransmitter().send(new PrivateStatusBuilder(privateStatus).build(player));
		player.getTransmitter().send(new GameStatusBuilder(publicStatus, trade).build(player));
	}
	
}