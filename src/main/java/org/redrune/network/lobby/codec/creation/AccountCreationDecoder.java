package org.redrune.network.lobby.codec.creation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.NetworkConstants;
import org.redrune.network.lobby.ProtocolType;
import org.redrune.network.lobby.codec.PassableDecoder;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.AccountCreationRequestPacketOut;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.backend.CreationResponse;
import org.redrune.utility.rs.buffer.FixedBuffer;
import org.redrune.utility.tool.Misc;

import static org.redrune.network.NetworkConstants.REVISION;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class AccountCreationDecoder extends PassableDecoder {
	
	/**
	 * The session created of the player being in the world
	 */
	private WorldSession session;
	
	@Override
	@SuppressWarnings("unused")
	public void decode(ChannelHandlerContext ctx, ByteBuf in, PacketBuilder builder) throws Exception {
		final Channel channel = ctx.channel();
		
		int size = in.readUnsignedShort();
		if (in.readableBytes() < size) {
			System.out.println("Bad size, we must be able to read at least " + in.readableBytes() + " bytes, size = " + size);
			return;
		}
		byte[] data = new byte[size];
		in.readBytes(data, 0, size);
		FixedBuffer buffer = new FixedBuffer(data);
		
		int revision = buffer.readUnsignedShort();
		if (revision != REVISION) {
			ProtocolType.sendCreationResponse(channel, CreationResponse.NONE);
			return;
		}
		
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		int affiliateId = buffer.readShort();
		String password = buffer.readString();
		long userFlow = buffer.readLong();
		int languageID = buffer.readByte();
		int gameID = buffer.readByte();
		byte[] UID = new byte[24];
		for (int i = 0; i < 24; i++) {
			UID[i] = (byte) buffer.readByte();
		}
		String additionalInfo = null;
		boolean hasAdditionalInfo = buffer.readByte() == 1;
		if (hasAdditionalInfo) {
			additionalInfo = buffer.readString();
		}
		int age = buffer.readByte();
		boolean sendUpdatesToEmail = buffer.readByte() == 1;
		
		// bad credentials
		if (Misc.invalidAccountName(username)) {
			ProtocolType.sendCreationResponse(channel, CreationResponse.INVALID_EMAIL);
			return;
		} else if (password.length() == 0 || password.length() > 20) {
			ProtocolType.sendCreationResponse(channel, CreationResponse.INVALID_PASSWORD);
			return;
		} else if (!MasterCommunication.isConnected()) {
			ProtocolType.sendCreationResponse(channel, CreationResponse.BUSY_SERVER);
			return;
		}
		
		// sets the session so we can write back to this specific session
		setSession(channel);
		
		// write the request id
		MasterCommunication.write(new AccountCreationRequestPacketOut(username, password, session.getUid()));
	}
	
	/**
	 * Sets the session
	 *
	 * @param channel
	 * 		The channel
	 */
	private void setSession(Channel channel) {
		session = new WorldSession(channel);
		channel.attr(NetworkConstants.SESSION_KEY).set(session);
	}
}
