package org.redrune.network.lobby.codec.creation;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.redrune.network.lobby.ProtocolType;
import org.redrune.network.lobby.codec.PassableDecoder;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.utility.backend.CreationResponse;
import org.redrune.utility.rs.buffer.FixedBuffer;
import org.redrune.utility.tool.Misc;

import static org.redrune.network.NetworkConstants.REVISION;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public class NameVerificationDecoder extends PassableDecoder {
	
	@Override
	@SuppressWarnings("unused")
	public void decode(ChannelHandlerContext ctx, ByteBuf in, PacketBuilder builder) throws Exception {
		if (in.readableBytes() < 2) {
			ctx.disconnect();
			return;
		}
		
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
		byte languageId = (byte) buffer.readByte();
		
		if (Misc.invalidAccountName(username)) {
			ProtocolType.sendCreationResponse(channel, CreationResponse.INVALID_EMAIL);
			return;
		}
		
		// as long as the username is acceptable, we can continue
		ProtocolType.sendCreationResponse(channel, CreationResponse.SUCCESSFUL);
	}
	
}
