package org.redrune.network.master.client.network;

import lombok.Getter;
import lombok.Setter;
import org.redrune.game.world.World;
import org.redrune.network.master.client.packet.out.ReconnectionPacketOut;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public enum MCNetworkStatus {
	
	/**
	 * The not connected [default] status, this is never returned to after we have ever connected.
	 */
	NOT_CONNECTED,
	
	/**
	 * The connected status, only set once, afterwards any connections that are dropped and reconnected are set to
	 * {@link MCNetworkStatus#RECONNECTED}
	 */
	CONNECTED,
	
	/**
	 * The reconnected status
	 */
	RECONNECTED {
		@Override
		public StatusChangeEvent setEvent() {
			return () -> this.system.write(new ReconnectionPacketOut(this.system.getWorldId(), World.get().getPlayersAsString()));
		}
	},
	
	/**
	 * The disconnected status
	 */
	DISCONNECTED;
	
	/**
	 * The system this status is for, set before the status event is fired
	 */
	@Getter
	@Setter
	protected MCNetworkSystem system;
	
	/**
	 * The event that is invoked when the status is changed to this flag.
	 */
	public StatusChangeEvent setEvent() {
		return null;
	}
	
	/**
	 * The event that is invoked when the status leaves this current state.
	 */
	public StatusChangeEvent leaveEvent() {
		return null;
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
