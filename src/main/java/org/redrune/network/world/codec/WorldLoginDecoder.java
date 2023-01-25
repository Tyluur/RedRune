package org.redrune.network.world.codec;

import com.alex.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.CacheFileStore;
import org.redrune.cache.crypto.ISAACCipher;
import org.redrune.game.GameFlags;
import org.redrune.network.NetworkConstants;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.packet.out.LoginRequestPacketOut;
import org.redrune.network.world.WorldSession;
import org.redrune.network.world.codec.io.RSPacketDecoder;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.rs.buffer.FixedBuffer;
import org.redrune.utility.tool.Misc;

import java.util.Arrays;
import java.util.List;

import static org.redrune.network.NetworkConstants.*;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class WorldLoginDecoder extends ByteToMessageDecoder {
	
	/**
	 * The session created of the player being in the world
	 */
	private WorldSession session;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 3) {
			return;
		}
		int opcode = in.readUnsignedByte();
		int size = in.readUnsignedShort();
		if (in.readableBytes() != size) {
			ctx.close();
			return;
		}
		if (opcode != 16 && opcode != 18) {
			System.out.println("Received unexpected world login opcode: " + opcode);
			setSession(ctx.channel());
			session.sendLoginResponse(10);
			return;
		}
		int revision = in.readInt();
		if (revision != REVISION) {
			setSession(ctx.channel());
			session.sendLoginResponse(10);
			return;
		}
		byte[] data = new byte[size - 4];
		// store the data into the buffer
		in.readBytes(data);
		// convert the buffer into a readable object
		FixedBuffer buffer = new FixedBuffer(data);
		
		// handle the correct login case
		setSession(ctx.channel());
		decodeWorldLogin(ctx, buffer, out);
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
	
	/**
	 * Decode the world login from buffer.
	 *
	 * @param ctx
	 * 		the channel context.
	 * @param buffer
	 * 		the buffer with data
	 * @param out
	 * 		The outgoing response
	 */
	@SuppressWarnings("unused")
	private void decodeWorldLogin(ChannelHandlerContext ctx, FixedBuffer buffer, List<Object> out) {
		boolean reconnecting = buffer.readBoolean();
		int rsaSize = buffer.readUnsignedShort();
		if (rsaSize > buffer.getRemaining()) {
			session.sendLoginResponse(10);
			return;
		}
		byte[] rsaData = new byte[rsaSize];
		buffer.read(rsaData);
		FixedBuffer rsaBuffer = new FixedBuffer(Utils.cryptRSA(rsaData, LOGIN_EXPONENT, LOGIN_MODULUS));
		if (rsaBuffer.readUnsignedByte() != 10) {
			session.sendLoginResponse(10);
			return;
		}
		int[] isaacSeed = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			isaacSeed[i] = rsaBuffer.readInt();
		}
		if (rsaBuffer.readLong() != 0) {
			session.sendLoginResponse(10);
			return;
		}
		String password = rsaBuffer.readString();
		rsaBuffer.readLong();
		rsaBuffer.readLong();
		buffer.decodeXTEA(isaacSeed, buffer.getOffset(), buffer.getLength());
		String username = Misc.formatPlayerNameForProtocol(buffer.readString());
		buffer.readByte();
		int mode = buffer.readByte();
		int width = buffer.readShort();
		int height = buffer.readShort();
		int displayMode = buffer.readByte();
		byte[] userId = new byte[24];
		// that's the content of random.dat , which is generated depending on user's hardware and software.
		for (int i = 0; i < 24; i++) {
			userId[i] = (byte) buffer.readByte();
		}
		String settings = buffer.readString();
		int affiliateId = buffer.readInt(); // used for adverts.
		int settingsBufferLength = buffer.readByte();
		byte[] settingsBuffer = new byte[settingsBufferLength];
		for (int i = 0; i < settingsBufferLength; i++) {
			settingsBuffer[i] = (byte) buffer.readByte();
		}
		
		// hardware block
		int hwMagic = buffer.readByte();
		if (hwMagic != 5) {
			session.sendLoginResponse(ReturnCode.MALFORMED_LOGIN_PACKET.getValue()).addListener(ChannelFutureListener.CLOSE);
			return;
		}
		
		int osId = buffer.readByte(); // [1 - windows, 2 - mac, 3 - linux, 4 - other]
		boolean is64Bit = buffer.readByte() == 1;
		int osVersion = buffer.readByte();
		int javaVendorID = buffer.readByte();
		byte[] javaVersion = new byte[3];
		for (int i = 0; i < 3; i++) {
			javaVersion[i] = (byte) buffer.readByte();
		}
		boolean webclient = buffer.readByte() == 1;
		int heapSize = buffer.readShort();
		int availableProcessors = buffer.readByte();
		
		buffer.read24BitInt(); // raw cpu info MEDINT,USHORT,UBYTE,UBYTE,UBYTE
		buffer.readShort();
		buffer.readByte();
		buffer.readByte();
		buffer.readByte();
		for (int i = 0; i < 4; i++) {
			buffer.readVString(); // those 4 are always empty and not set in client ( propably for future use )
		}
		buffer.readByte(); // same for theese two
		buffer.readShort();
		
		int specialPacketCounter = buffer.readInt();
		long userFlow = buffer.readLong();
		
		boolean additionalExists = buffer.readByte() == 1;
		String additionalInfo = "";
		if (additionalExists) {
			additionalInfo = buffer.readString();
		}
		
		boolean jagtheoraLoaded = buffer.readByte() == 1;
		boolean supportsJavaScript = buffer.readByte() == 1;
		
		for (int index = 0; index < 36; index++) {
			int crc = CacheFileStore.STORE.getIndexes()[index] == null ? 0 : CacheFileStore.STORE.getIndexes()[index].getCRC();
			int receivedCrc = buffer.readInt();
			if (crc != receivedCrc && index < 32) {
				session.sendLoginResponse(6);
				return;
			}
		}
		if (Misc.invalidAccountName(username)) {
			session.sendLoginResponse(ReturnCode.INVALID_CREDENTIALS.getValue());
			return;
		}
		if (password.length() >= 30) {
			session.sendLoginResponse(ReturnCode.INVALID_CREDENTIALS.getValue());
			return;
		}
		if (!MasterCommunication.isConnected()) {
			session.sendLoginResponse(ReturnCode.LOGIN_SERVER_OFFLINE.getValue());
			return;
		}
		// build the isaac ciphers
		int[] inCipher = Arrays.copyOf(isaacSeed, isaacSeed.length);
		int[] outCipher = new int[4];
		for (int i = 0; i < isaacSeed.length; i++) {
			outCipher[i] = isaacSeed[i] + 50;
		}
		
		// finished decoding now we can build the session
		session.setInLobby(false);
		session.getViewComponents().setScreenSizeMode(mode);
		session.getViewComponents().setScreenSizeX(width);
		session.getViewComponents().setScreenSizeY(height);
		session.getViewComponents().setDisplayMode(displayMode);
		session.buildCiphers(new ISAACCipher(inCipher), new ISAACCipher(outCipher));
		
		// change the decoders now
		ctx.pipeline().replace("decoder", "decoder", new RSPacketDecoder(session));
		
		// tell the master server we this session to log in
		MasterCommunication.write(new LoginRequestPacketOut(GameFlags.worldId, false, username, password, session.getUid()));
	}
}
