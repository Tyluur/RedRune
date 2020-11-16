package org.redrune.network.master.server.network;

import lombok.Getter;
import org.redrune.network.master.MasterConstants;

/**
 * The possible states that the network can hold
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public enum MSNetworkStatus {
	
	// the default state
	NOT_CONNECTED,
	
	// the connected state
	CONNECTED(() -> System.out.println("Successfully bound the server to port " + MasterConstants.PORT_ID)),
	
	// the state that the connection is no longer valid
	CONNECTION_DROPPED(() -> System.out.println("The lobby server was just dropped."));
	
	/**
	 * The event that is fired when the state is changed
	 */
	@Getter
	private final StatusChangeEvent event;
	
	/**
	 * Constructs a status with no event
	 */
	MSNetworkStatus() {
		this.event = null;
	}
	
	/**
	 * Constructs a status with an event
	 *
	 * @param event
	 * 		The event
	 */
	MSNetworkStatus(StatusChangeEvent event) {
		this.event = event;
	}
	
	/**
	 * The event that is executed when the status is changed
	 */
	@FunctionalInterface
	public interface StatusChangeEvent {
		
		/**
		 * Executes the event
		 */
		void execute();
	}
}
