package org.redrune.game.content.skills.firemaking;

import org.redrune.core.task.ScheduledTask;
import org.redrune.core.system.SystemManager;
import org.redrune.game.content.action.Action;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.render.flag.impl.FaceLocationUpdate;
import org.redrune.game.node.item.FloorItem;
import org.redrune.game.node.item.Item;
import org.redrune.game.node.object.GameObject;
import org.redrune.game.world.region.RegionManager;
import org.redrune.utility.rs.constant.EquipConstants;
import org.redrune.utility.rs.constant.SkillConstants;

import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/8/2017
 */
public class FiremakingAction implements Action {
	
	/**
	 * The id of a tinderbox
	 */
	private static final int TINDERBOX_ID = 590;
	
	/**
	 * The fire we are lighting
	 */
	private final Fire fire;
	
	/**
	 * If the log is already on the ground
	 */
	private final boolean ground;
	
	public FiremakingAction(Fire fire, boolean ground) {
		this.fire = fire;
		this.ground = ground;
	}
	
	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getTransmitter().sendMessage("You attempt to light the logs.", true);
		// in the case the item is already on the ground we don't need to delete
		// nor add another floor item
		if (!ground) {
			player.getInventory().deleteItem(fire.getLogId(), 1);
			RegionManager.addFloorItem(fire.getLogId(), 1, 200, player.getLocation(), player.getDetails().getUsername());
		}
		
		Long time = player.removeAttribute("Fire");
		boolean quickFire = time != null && time > System.currentTimeMillis();
		setDelay(player, quickFire ? 1 : 2);
		if (!quickFire) {
			player.sendAnimation(733);
		}
		return true;
	}
	
	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}
	
	@Override
	public int processOnTicks(Player player) {
		final Location tile = new Location(player.getLocation());
		if (!player.getMovement().addWalkSteps(player.getLocation().getX() - 1, player.getLocation().getY())) {
			if (!player.getMovement().addWalkSteps(player.getLocation().getX() + 1, player.getLocation().getY())) {
				if (!player.getMovement().addWalkSteps(player.getLocation().getX(), player.getLocation().getY() + 1)) {
					player.getMovement().addWalkSteps(player.getLocation().getX(), player.getLocation().getY() - 1);
				}
			}
		}
		player.getTransmitter().sendMessage("The fire catches and the logs begin to burn.", true);
		SystemManager.getScheduler().schedule(new ScheduledTask(1) {
			@Override
			public void run() {
				Optional<FloorItem> optional = player.getRegion().getFloorItem(fire.getLogId(), tile.getX(), tile.getY(), tile.getPlane(), null);
				if (!optional.isPresent()) {
					return;
				}
				if (!checkAll(player)) {
					return;
				}
				FloorItem item = optional.get();
				if (!player.getRegion().removeFloorItem(item)) {
					return;
				}
				RegionManager.addTimedGamedObject(new GameObject(fire.getObjectId(), 10, 0, tile), 592, 1, fire.getLife());
				player.getSkills().addExperienceWithMultiplier(SkillConstants.FIREMAKING, increasedExperience(player, fire.getXp()));
				player.getUpdateMasks().register(new FaceLocationUpdate(player, tile));
			}
		});
		player.putAttribute("Fire", System.currentTimeMillis() + 1800);
		return -1;
	}
	
	@Override
	public void stop(Player player) {
		setDelay(player, 3);
	}
	
	/**
	 * Gets the increased experience
	 *
	 * @param player
	 * 		The player
	 * @param base
	 * 		The amount of base experience
	 */
	private static double increasedExperience(Player player, double base) {
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_HANDS) == 13660) {
			base *= 1.025;
		}
		if (player.getEquipment().getIdInSlot(EquipConstants.SLOT_RING) == 13659) {
			base *= 1.025;
		}
		return base;
	}
	
	/**
	 * Checks to ensure we can still fire make
	 *
	 * @param player
	 * 		The player
	 */
	private boolean checkAll(Player player) {
		if (!player.getInventory().getItems().contains(590)) {
			player.getTransmitter().sendMessage("You do not have the required items to light this.");
			return false;
		}
		if (player.getSkills().getLevelForXp(SkillConstants.FIREMAKING) < fire.getLevel()) {
			player.getTransmitter().sendMessage("You do not have the required level to light this.");
			return false;
		}
		if (badFireLocation(player)) {
			player.getTransmitter().sendMessage("You can't light a fire here.");
			return false;
		}
		return true;
	}
	
	/**
	 * If the player's location is a bad location to light a fire
	 *
	 * @param player
	 * 		The player
	 */
	public static boolean badFireLocation(Player player) {
		return !RegionManager.canMoveNPC(player.getLocation().getPlane(), player.getLocation().getX(), player.getLocation().getY(), player.getSize()) || player.getRegion().findSpawnedGameObject(-1, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getPlane(), -1).isPresent();
	}
	
	/**
	 * Checks if the player is firemaking
	 *
	 * @param player
	 * 		The player
	 * @param used
	 * 		The item used
	 * @param with
	 * 		The item used with
	 */
	public static Fire getFire(Player player, Item used, Item with) {
		Item tinderbox = player.getInventory().getItem(TINDERBOX_ID, used, with);
		if (tinderbox == null) {
			return null;
		}
		Item log = tinderbox.getId() == used.getId() ? with : used;
		if (log == null) {
			return null;
		}
		return Fire.getFireInstance(log.getId());
	}
}
