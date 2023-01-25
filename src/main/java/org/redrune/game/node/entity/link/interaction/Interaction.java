package org.redrune.game.node.entity.link.interaction;

import lombok.Getter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public abstract class Interaction {
	
	/**
	 * Starts the interaction
	 */
	public abstract void start();
	
	/**
	 * Sends the request to start the interaction
	 */
	public abstract void request();
	
	/**
	 * Ends the interaction
	 */
	public abstract void end();
	
	/**
	 * If we can request the target
	 */
	public boolean canRequest() {
		return true;
	}
	
	/**
	 * The source of the interaction, the entity that started the interaction
	 */
	@Getter
	protected final Entity source;
	
	/**
	 * The target of the interaction, the entity that was requested to interact with us
	 */
	@Getter
	protected final Entity target;
	
	public Interaction(Entity source, Entity target) {
		this.source = source;
		this.target = target;
	}
	
	/**
	 * Handles the interface during the interaction
	 */
	public void handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
	
	}
}
