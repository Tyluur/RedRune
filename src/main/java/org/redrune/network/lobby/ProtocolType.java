package org.redrune.network.lobby;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.Getter;
import org.redrune.network.lobby.codec.LobbyLoginDecoder;
import org.redrune.network.lobby.codec.PassableDecoder;
import org.redrune.network.lobby.codec.creation.AccountCreationDecoder;
import org.redrune.network.lobby.codec.creation.NameVerificationDecoder;
import org.redrune.network.lobby.codec.download.DownloadDecoder;
import org.redrune.network.world.codec.WorldLoginDecoder;
import org.redrune.utility.backend.CreationResponse;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public enum ProtocolType {
	
	LOGIN_REQUEST(14) {
		@Override
		public ByteToMessageDecoder getDecoder(boolean world) {
			return world ? new WorldLoginDecoder() : new LobbyLoginDecoder();
		}
	},
	JS5_REQUEST(15) {
		@Override
		public ByteToMessageDecoder getDecoder(boolean world) {
			return new DownloadDecoder();
		}
	},
	ACCOUNT_CREATION(22) {
		@Override
		public PassableDecoder getPassableDecoder() {
			return new AccountCreationDecoder();
		}
	},
	NAME_VERIFICATION(28) {
		@Override
		public PassableDecoder getPassableDecoder() {
			return new NameVerificationDecoder();
		}
	};
	
	/**
	 * The id of the protocol
	 */
	@Getter
	private final int protocolId;
	
	/**
	 * Gets the decoder
	 *
	 * @param world
	 * 		If the decoder is for the world
	 */
	public ByteToMessageDecoder getDecoder(boolean world) {
		return null;
	}
	
	/**
	 * Gets the passable decoder
	 */
	public PassableDecoder getPassableDecoder() {
		return null;
	}
	
	ProtocolType(int protocolId) {
		this.protocolId = protocolId;
	}
	
	/**
	 * Gets the type of protocol by the id
	 */
	public static Optional<ProtocolType> getType(int protocolId) {
		return Arrays.stream(values()).filter(type -> type.getProtocolId() == protocolId).findAny();
	}
	
	/**
	 * Sends a account creation response
	 *
	 * @param channel
	 * 		The channel
	 * @param response
	 * 		The response
	 */
	public static void sendCreationResponse(Channel channel, CreationResponse response) {
		channel.writeAndFlush(Unpooled.buffer().writeByte(response.getValue())).addListener(ChannelFutureListener.CLOSE);
	}
}
