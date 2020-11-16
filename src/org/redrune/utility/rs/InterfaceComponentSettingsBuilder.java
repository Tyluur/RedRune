package org.redrune.utility.rs;

/**
 * A.K.A. access mask builder.
 *
 * @author Mangis
 */
public final class InterfaceComponentSettingsBuilder {
	
	/**
	 * Contains the value which should be sended in access mask packet.
	 */
	private int value;
	
	/**
	 * Set's standart option settings.
	 * Great example of standart click option is the Continue button in dialog interface.
	 * If the option is not allowed the packet won't be send to server.
	 */
	public void setStandartClickOptionSettings(boolean allowed) {
		value &= ~(0x1);
		if (allowed) {
			value |= 0x1;
		}
	}
	
	/**
	 * Set's right click option settings.
	 * Great example of right click option is the Dismiss option in summoning orb.
	 * If specified option is not allowed , it will not appear in right click menu and the packet will not be send
	 * to server when clicked.
	 */
	public void setRightClickOptionSettings(int optionID, boolean allowed) {
		if (optionID < 0 || optionID > 9) {
			throw new IllegalArgumentException("optionID must be 0-9.");
		}
		value &= ~(0x1 << (optionID + 1)); // disable
		if (allowed) {
			value |= (0x1 << (optionID + 1));
		}
	}
	
	public void setApplicationSettings(ApplicationSetting... settings) {
		int flags = 0;
		for (ApplicationSetting setting : settings) {
			flags |= 1 << setting.ordinal();
		}
		
		value &= ~(0x7f << 7);
		value |= flags << 7;
	}
	
	/**
	 * Set's interface events depth.
	 * For example, we have inventory interface which is opened
	 * on gameframe interface (548 or 746).
	 * If depth is 1 , then the clicks in inventory will also invoke click event handler scripts
	 * on gameframe interface.
	 */
	public void setInterfaceEventsDepth(int depth) {
		if (depth < 0 || depth > 7) {
			throw new IllegalArgumentException("depth must be 0-7.");
		}
		value &= ~(0x7 << 18);
		value |= (depth << 18);
	}
	
	/**
	 * Set's canUseOnFlag. if it's true then other interface components can do use on this interface component. Example
	 * would be using High alchemy spell on the inventory item. If inventory component where items are stored doesn't
	 * allow the canUseOn , it would not be possible to use High Alchemy on that item.
	 */
	public void setCanUseOn(boolean allow) {
		value &= ~(1 << 22);
		if (allow) {
			value |= (1 << 22);
		}
	}
	
	public int getValue() {
		return value;
	}
	
	enum ApplicationSetting {
		GROUND_ITEMS,
		NPCS,
		OBJECTS,
		OTHER_PLAYERS,
		SELF,
		INTERFACE_COMPONENTS
	}
	
}