package org.redrune.network.lobby.packet.outgoing;

import org.redrune.game.node.entity.player.Player;
import org.redrune.network.world.packet.Packet;
import org.redrune.network.world.packet.PacketBuilder;
import org.redrune.network.world.packet.outgoing.OutgoingPacketBuilder;
import org.redrune.utility.AttributeKey;
import org.redrune.utility.tool.Misc;
import org.redrune.game.GameFlags;

import java.util.Date;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class LobbyResponseBuilder implements OutgoingPacketBuilder {
	
	@Override
	public Packet build(Player player) {
		PacketBuilder builder = new PacketBuilder();
		builder.writeByte(player.getDetails().getDominantRight().getClientRight());
		builder.writeByte(0); // blackmarks
		builder.writeByte(0); // muted? (bool)
		builder.writeByte(0); // dunno (bool)
		builder.writeByte(0);// dunno (bool)
		final long nowDate = new Date().getTime();
		
		builder.writeLong((nowDate - new Date(1970, 1, 1).getTime()));
		builder.writeByte5((6 * (60000)) + 20000);
		builder.writeByte(player.getDetails().isDonator() ? 0x2 : 0);
		builder.writeInt(0);// unused in client
		builder.writeByte(0); // unused in client (bool)
		builder.writeInt(0); // unused in client
		builder.writeShort(1); // recovery questions, 0 - not set,
		// otherwise goes bitencoded date.
		builder.writeShort(player.getManager().getWebManager().getUnreadMessages()); // unread messages
		
		// leave the cast because of json boxing.
		long lastLogin = ((Number) player.getVariables().getAttribute(AttributeKey.LAST_LONGIN_STAMP, System.currentTimeMillis())).longValue();
		
		long now = System.currentTimeMillis();
		long jag = 1014753880308L;
		long since_jag = (now - jag) / 1000 / 60 / 60 / 24;
		long since_log = (now - lastLogin) / 1000 / 60 / 60 / 24;
		
		builder.writeShort((int) (since_jag - since_log)); // last logged in date
		String lastIp = player.getDetails().getLastIp();
		if (lastIp == null) {
			lastIp = Misc.getIpAddress(player.getSession().getChannel());
		}
		builder.writeInt(Misc.IPAddressToNumber(lastIp)); // last login ip
		builder.writeByte(player.getManager().getWebManager().isEmailVerified() ? 3 : 0);
		// 0 - no email
		// 1 - pending parental confirmation
		// 2 - pending confirmation.
		// 3 - registered.
		// 4 - no longer registered.
		builder.writeShort(0); // credit card expiration time
		builder.writeShort(0); // credit card loyality expiration time
		builder.writeByte(0); // unused in client
		builder.writeGJString2(player.getDetails().getUsername());
		builder.writeByte(0); // unused in client
		builder.writeInt(player.getDetails().getUsername().startsWith("#") ? 0 : 1);
		builder.writeByte(1); // dunno (bool)
		builder.writeShort(GameFlags.worldId); // current world id
		builder.writeGJString2("127.0.0.1");
		
		PacketBuilder lobbyResponse = new PacketBuilder();
		lobbyResponse.writeByte(builder.getBuffer().writerIndex());
		lobbyResponse.writeBytes(builder.getBuffer());
		return lobbyResponse.toPacket();
	}
}