package org.redrune.game.node.entity.render;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.node.entity.player.render.flag.impl.*;
import org.redrune.game.node.entity.render.flag.UpdateFlag;
import org.redrune.game.node.entity.render.flag.impl.*;
import org.redrune.utility.backend.Priority;
import org.redrune.utility.rs.Graphics;

import java.util.PriorityQueue;

/**
 * Represents an Entity's update masks.
 *
 * @author Emperor
 */
public class UpdateMasks {
	
	/**
	 * Our priority queue used.
	 */
	@Getter
	private final PriorityQueue<UpdateFlag> flagQueue = new PriorityQueue<>();
	
	/**
	 * The mask data.
	 */
	@Getter
	private int maskData = 0;
	
	/**
	 * The current animation priority.
	 */
	@Getter
	@Setter
	private Priority animationPriority;
	
	/**
	 * The time the last animation ended
	 */
	@Getter
	@Setter
	private long lastAnimationEndTime = -1;
	
	/**
	 * Prepares the outgoing packet for updating.
	 *
	 * @param entity
	 * 		The entity who's using this update mask instance.
	 */
	public void prepare(Entity entity) {
		if (entity.isPlayer()) {
			final Player toPlayer = entity.toPlayer();
			
			if (toPlayer.getDetails().getAppearance() != null) {
				toPlayer.getDetails().getAppearance().prepareBodyData(toPlayer);
			}
			if (entity.getMovement().getNextWalkDirection() != -1 || entity.getMovement().getNextRunDirection() != -1) {
				register(new MovementUpdate(toPlayer));
			}
		}
		if (entity.getHitMap().getHitList().size() > 0) {
			register(new HitUpdate(entity));
		}
	}
	
	/**
	 * Registers an update flag.
	 *
	 * @param updateFlag
	 * 		The update flag.
	 */
	public void register(UpdateFlag updateFlag) {
		if (!updateFlag.canRegister(this)) {
			return;
		}
		// we're attempting to register a graphic
		if (updateFlag instanceof Graphics) {
			UpdateFlag graphicFlag = canRegisterGraphic(updateFlag);
			if (graphicFlag != null) {
				updateFlag = graphicFlag;
			}
		}
		if ((maskData & updateFlag.getMaskData()) != 0) {
			flagQueue.remove(updateFlag);
		}
		maskData |= updateFlag.getMaskData();
		flagQueue.add(updateFlag);
	}
	
	/**
	 * Checks if we can register a graphic
	 *
	 * @param updateFlag
	 * 		The graphic flag
	 */
	private UpdateFlag canRegisterGraphic(UpdateFlag updateFlag) {
		Graphics graphic = (Graphics) updateFlag;
		if (!hasFlag(Graphic.class)) {
			return new Graphic(graphic.id(), graphic.height(), graphic.speed(), graphic.npc());
		} else if (!hasFlag(Graphic2.class)) {
			return new Graphic2(graphic.id(), graphic.height(), graphic.speed(), graphic.npc());
		} else if (!hasFlag(Graphic3.class)) {
			return new Graphic3(graphic.id(), graphic.height(), graphic.speed(), graphic.npc());
		} else if (!hasFlag(Graphic4.class)) {
			return new Graphic4(graphic.id(), graphic.height(), graphic.speed(), graphic.npc());
		} else {
			return null;
		}
	}
	
	/**
	 * Checks if we have a flag registered
	 *
	 * @param clazz
	 * 		The class of the flag
	 */
	private boolean hasFlag(Class<?> clazz) {
		for (UpdateFlag flag : flagQueue) {
			if (flag.getClass().getSimpleName().equalsIgnoreCase(clazz.getSimpleName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Finishes the updating.
	 */
	public void finish() {
		animationPriority = Priority.LOWEST;
		maskData = 0;
		flagQueue.clear();
	}
	
	/**
	 * Checks if an update is required.
	 *
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isUpdateRequired() {
		return maskData != 0;
	}
	
	/**
	 * Checks if an update flag was registered.
	 *
	 * @param data
	 * 		The mask data of the update flag.
	 * @return {@code True} if the update flag was registered, {@code false} if not.
	 */
	public boolean get(int data) {
		return (maskData & data) != 0;
	}
	
}