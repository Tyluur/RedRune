package org.redrune.utility.rs;

import lombok.Getter;
import org.redrune.game.node.Location;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public final class HintIcon {
	
	/**
	 * The distance from the floor
	 */
	public static final int DEFAULT_PLAYER_FLOOR_DISTANCE = 100;
	
	/**
	 * The slot of the hint icon
	 */
	@Getter
	private final int slot;
	
	/**
	 * The type of icon to send. For entity icons, the type is 10 for player, 1 for npc.
	 */
	@Getter
	private final HintIconType iconType;
	
	/**
	 * The type of arrow. The most common icon shown here is 0.
	 */
	@Getter
	private final HintIconArrow arrowType;
	
	/**
	 * The model id of the icon
	 */
	@Getter
	private final int modelId;
	
	/**
	 * The index of the target
	 */
	@Getter
	private final int targetIndex;
	
	/**
	 * The location of the target
	 */
	@Getter
	private final Location location;
	
	/**
	 * The distance of the icon from the floor
	 */
	@Getter
	private final int floorDistance;
	
	/**
	 * Constructs a hint icon on a tile
	 *
	 * @param slot
	 * 		The slot of the icon
	 * @param iconType
	 * 		The type of icon. This is used for directions in this case. <ul><li>2 - center</li><li>3 - west<li>4 -
	 * 		east</li><li>5 - south</li><li>6 - north</li></ul>
	 * @param arrowType
	 * 		The type of arrow
	 * @param modelId
	 * 		The model of the icon
	 * @param location
	 * 		The location to send the icon
	 * @param floorDistance
	 * 		The distance from the floor of the icon
	 */
	public HintIcon(int slot, HintIconType iconType, HintIconArrow arrowType, int modelId, Location location, int floorDistance) {
		this.slot = slot;
		this.iconType = iconType;
		this.arrowType = arrowType;
		this.modelId = modelId;
		this.targetIndex = -1;
		this.location = location;
		this.floorDistance = floorDistance;
	}
	
	/**
	 * Constructs a hint icon on an entity
	 *
	 * @param slot
	 * 		The slot of the icon
	 * @param iconType
	 * 		The type of icon
	 * @param arrowType
	 * 		The type of arrow
	 * @param modelId
	 * 		The model sent
	 * @param targetIndex
	 * 		The index of the target
	 */
	public HintIcon(int slot, HintIconType iconType, HintIconArrow arrowType, int modelId, int targetIndex) {
		this.slot = slot;
		this.iconType = iconType;
		this.arrowType = arrowType;
		this.modelId = modelId;
		this.targetIndex = targetIndex;
		this.location = null;
		this.floorDistance = -1;
	}
	
	/**
	 * If the icon is an entity icon
	 */
	public boolean isEntityIcon() {
		return location == null;
	}
	
	/**
	 * The enum of the types of icons there are. Different types are handled differently in the client.
	 */
	public enum HintIconType {
		REMOVAL(0),
		PLAYER(10),
		NPC(1),
		CENTER(2),
		WEST(3),
		EAST(4),
		SOUTH(5),
		NORTH(6);
		
		/**
		 * The value of this type
		 */
		@Getter
		private final int value;
		
		HintIconType(int value) {
			this.value = value;
		}
	}
	
	/**
	 * The enum of the types of arrows to show
	 */
	public enum HintIconArrow {
		
		DEFAULT_YELLOW(0),
		YELLOW_SEETHROUGH(1),
		BLUE(2),
		GREEN(3),
		SECONDARY_YELLOW(4),
		RED(5),
		MINIMAP_ONLY_BLUE(6),
		MINIMAP_ONLY_WHITE(7),
		MINIMAP_ONLY_RED(8),;
		
		/**
		 * The value of this arrow
		 */
		@Getter
		private final int value;
		
		HintIconArrow(int value) {
			this.value = value;
		}
	}
}
