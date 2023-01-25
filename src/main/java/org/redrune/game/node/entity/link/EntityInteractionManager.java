package org.redrune.game.node.entity.link;

import lombok.Setter;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.link.interaction.Interaction;
import org.redrune.game.node.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/3/2017
 */
public class EntityInteractionManager {
	
	/**
	 * The entity who owns this manager
	 */
	@Setter
	private Entity entity;
	
	/**
	 * The current interaction the entity is in
	 */
	private Interaction interaction;
	
	/**
	 * The map of requests, the key being the type of request, and the value being the last player who requested
	 */
	private Map<String, Entity> requestMap = new HashMap<>();
	
	/**
	 * Gets the last entity who requested a specific interaction
	 *
	 * @param aClass
	 * 		The interaction class
	 */
	public Entity getLastRequested(Class aClass) {
		Entity last = requestMap.get(aClass.getSimpleName());
		if (last == null) {
			return null;
		} else {
			return last;
		}
	}
	
	/**
	 * Requests the target to start an interaction with us
	 *
	 * @param interaction
	 * 		The interaction
	 */
	public void requestInteraction(Interaction interaction) {
		if (!interaction.canRequest()) {
			if (interaction.getSource().isPlayer()) {
				interaction.getSource().toPlayer().getTransmitter().sendMessage("The other player is busy.");
			}
			return;
		}
		interaction.getSource().getInteractionManager().requestMap.put(interaction.getClass().getSimpleName(), interaction.getTarget());
		interaction.request();
	}
	
	/**
	 * Starts the interaction
	 *
	 * @param interaction
	 * 		The interaction
	 */
	public void startInteraction(Interaction interaction) {
		this.interaction = interaction;
		this.interaction.start();
		this.interaction.getTarget().getInteractionManager().interaction = interaction;
		this.interaction.getTarget().getInteractionManager().requestMap.remove(interaction.getClass().getSimpleName());
		this.interaction.getSource().getInteractionManager().requestMap.remove(interaction.getClass().getSimpleName());
	}
	
	/**
	 * Checks if the interaction is a specific class
	 *
	 * @param aClass
	 * 		The class
	 */
	public boolean interactionIs(Class aClass) {
		return interaction != null && interaction.getClass().getSimpleName().equals(aClass.getSimpleName());
	}
	
	/**
	 * Uses the current interaction to handle the interface
	 */
	public void handleInterface(Player player, int interfaceId, int componentId, int itemId, int slotId, int packetId) {
		if (interaction == null) {
			return;
		}
		interaction.handleInterface(player, interfaceId, componentId, itemId, slotId, packetId);
	}
	
	/**
	 * If we have an interaction
	 */
	public boolean hasInteraction() {
		return interaction != null;
	}
	
	/**
	 * Removes the interaction
	 */
	public void end() {
		this.interaction = null;
	}
}
