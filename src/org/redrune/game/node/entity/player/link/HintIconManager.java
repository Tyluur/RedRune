package org.redrune.game.node.entity.player.link;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.outgoing.impl.HintIconPacketBuilder;
import org.redrune.utility.tool.Misc;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.world.World;
import org.redrune.utility.rs.HintIcon;
import org.redrune.utility.rs.HintIcon.HintIconArrow;
import org.redrune.utility.rs.HintIcon.HintIconType;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public final class HintIconManager {
	
	/**
	 * The list of entities that have an icon following them.
	 */
	private final Map<Integer, FollowingEntityIcon> followingIconList = new HashMap<>();
	
	/**
	 * The hint icons
	 */
	private final HintIcon[] icons = new HintIcon[7];
	
	/**
	 * The player this class manages hint icons for
	 */
	@Setter
	private Player player;
	
	/**
	 * Adds an icon thats following the entity
	 *
	 * @param entity
	 * 		The entity
	 * @param iconArrow
	 * 		The icon arrow
	 */
	public boolean addFollowingEntityIcon(Entity entity, HintIconArrow iconArrow) {
		int freeSlot = getFreeIndex();
		if (freeSlot == -1) {
			System.out.println("Unable to add a new entity icon for player {" + player + "}");
			return false;
		}
		followingIconList.put(freeSlot, new FollowingEntityIcon(entity, iconArrow));
		sendEntityIcon(entity, iconArrow, freeSlot);
		return true;
	}
	
	/**
	 * Gets the first free index from the {@link #icons} array
	 */
	private int getFreeIndex() {
		for (int index = 0; index < icons.length; index++) {
			if (icons[index] == null) {
				return index;
			}
		}
		return -1;
	}
	
	/**
	 * Adds a hint icon to the entity
	 *
	 * @param entity
	 * 		The entity
	 * @param iconArrow
	 * 		The arrow to send
	 * @param freeSlot
	 * 		The slot to send
	 */
	private void sendEntityIcon(Entity entity, HintIconArrow iconArrow, int freeSlot) {
		HintIcon icon = new HintIcon(freeSlot, entity.isPlayer() ? HintIconType.PLAYER : HintIconType.NPC, iconArrow, 0, entity.getIndex());
		icons[freeSlot] = icon;
		player.getTransmitter().send(new HintIconPacketBuilder(icon).build(player));
	}
	
	/**
	 * Adda an icon to the location
	 *
	 * @param location
	 * 		The location of the icon
	 * @param type
	 * 		The type of icon
	 * @param iconArrow
	 * 		The arrow to send
	 * @param floorDistance
	 * 		The distance from the floor
	 */
	public boolean addLocationIcon(Location location, HintIconType type, HintIconArrow iconArrow, int floorDistance) {
		int freeSlot = getFreeIndex();
		if (freeSlot == -1) {
			System.out.println("Unable to add a new location icon for player {" + player + "}");
			return false;
		}
		sendLocationIcon(location, type, iconArrow, floorDistance, freeSlot);
		return true;
	}
	
	/**
	 * Adda an icon to the location
	 *
	 * @param location
	 * 		The location of the icon
	 * @param type
	 * 		The type of icon
	 * @param iconArrow
	 * 		The arrow to send
	 * @param floorDistance
	 * 		The distance from the floor
	 * @param freeSlot
	 * 		The slot of the icon
	 */
	private void sendLocationIcon(Location location, HintIconType type, HintIconArrow iconArrow, int floorDistance, int freeSlot) {
		HintIcon icon = new HintIcon(freeSlot, type, iconArrow, 0, location, floorDistance);
		icons[freeSlot] = icon;
		player.getTransmitter().send(new HintIconPacketBuilder(icon).build(player));
	}
	
	/**
	 * Processes the hint icons
	 */
	public void process() {
		if (followingIconList.isEmpty()) {
			return;
		}
		List<Integer> removalSlots = new ArrayList<>();
		for (Entry<Integer, FollowingEntityIcon> entry : followingIconList.entrySet()) {
			Integer slot = entry.getKey();
			FollowingEntityIcon icon = entry.getValue();
			
			Entity e;
			// updating the entity location
			{
				if (icon.isPlayer()) {
					e = World.get().getPlayers().get(icon.getEntityIndex());
				} else {
					e = World.get().getNpcs().get(icon.getEntityIndex());
				}
				// couldn't find the entity to trail, lets remove it.
				if (e == null) {
					removalSlots.add(slot);
					continue;
				}
				if (icon.isIconLocation() && !icon.getLastUpdatedLocation().equals(e.getLocation())) {
					sendLocationIcon(icon.getLastUpdatedLocation(), HintIconType.CENTER, icon.getIconArrow(), HintIcon.DEFAULT_PLAYER_FLOOR_DISTANCE, slot);
				}
				icon.setLastUpdatedLocation(e.getLocation());
			}
			
			if (icon.isIconLocation() && icon.getLastUpdatedLocation().getDistance(player.getLocation()) < 16) {
				sendEntityIcon(e, icon.getIconArrow(), slot);
				icon.setIconLocation(false);
			} else if (!icon.isIconLocation() && icon.getLastUpdatedLocation().getDistance(player.getLocation()) >= 16) {
				sendLocationIcon(icon.getLastUpdatedLocation(), HintIconType.CENTER, icon.getIconArrow(), HintIcon.DEFAULT_PLAYER_FLOOR_DISTANCE, slot);
				icon.setIconLocation(true);
			}
		}
		if (removalSlots.isEmpty()) {
			return;
		}
		for (Integer slot : removalSlots) {
			removeIconAtSlot(slot);
		}
	}
	
	/**
	 * Removes the icon at a slot
	 */
	private void removeIconAtSlot(int slot) {
		HintIcon icon = Misc.getArrayEntry(icons, slot);
		if (icon == null) {
			System.out.println("Unable to remove icon at slot #" + slot + " for player{" + player + "}");
			return;
		}
		player.getTransmitter().send(new HintIconPacketBuilder(new HintIcon(slot, HintIconType.REMOVAL, HintIconArrow.DEFAULT_YELLOW, 0, player.getLocation(), 65)).build(player));
		followingIconList.remove(slot);
		icons[slot] = null;
	}
	
	/**
	 * Removes all the icons shown
	 */
	public void removeAll() {
		for (Entry<Integer, FollowingEntityIcon> entry : followingIconList.entrySet()) {
			removeIconAtSlot(entry.getKey());
		}
		for (int i = 0; i < icons.length; i++) {
			if (icons[i] == null) {
				continue;
			}
			removeIconAtSlot(i);
		}
		Arrays.fill(icons, null);
		followingIconList.clear();
	}
	
	public static class FollowingEntityIcon {
		
		/**
		 * The index of the entity
		 */
		@Getter
		private final int entityIndex;
		
		/**
		 * If the entity is a player
		 */
		@Getter
		private final boolean isPlayer;
		
		/**
		 * The icon arrow
		 */
		@Getter
		private final HintIconArrow iconArrow;
		
		/**
		 * If the icon being shown is a location icon
		 */
		@Getter
		@Setter
		private boolean iconLocation;
		
		/**
		 * The last location of the entity that was updated.
		 */
		@Getter
		@Setter
		private Location lastUpdatedLocation;
		
		FollowingEntityIcon(Entity entity, HintIconArrow iconArrow) {
			this.entityIndex = entity.getIndex();
			this.isPlayer = entity.isPlayer();
			this.iconLocation = false;
			this.lastUpdatedLocation = entity.getLocation();
			this.iconArrow = iconArrow;
		}
	}
}
