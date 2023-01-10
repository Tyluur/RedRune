package org.redrune.network.master.client.packet.in;

import org.redrune.game.GameConstants;
import org.redrune.network.NetworkSession;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.master.client.network.MCSession;
import org.redrune.network.master.client.packet.responsive.ResponsiveLoginPacket;
import org.redrune.network.master.network.packet.IncomingPacket;
import org.redrune.network.master.network.packet.PacketConstants;
import org.redrune.network.master.network.packet.readable.Readable;
import org.redrune.network.master.network.packet.readable.ReadablePacket;
import org.redrune.network.world.packet.outgoing.impl.LoginResponseCodeBuilder;
import org.redrune.utility.backend.ReturnCode;
import org.redrune.utility.backend.SecureOperations;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/12/2017
 */
@Readable(packetIds = {PacketConstants.LOGIN_RESPONSE_PACKET_ID})
public class LoginResponsePacketIn implements ReadablePacket<MCSession> {

    @Override
    public void read(MCSession session, IncomingPacket packet) {
        // decode first
        String uuid = packet.readString();
        byte responseCode = (byte) packet.readByte();
        boolean lobby = (byte) packet.readByte() == 1;
        String username = packet.readString();
        int rowId = packet.readInt();
        int dataLength = packet.readInt();
        byte[] data = new byte[dataLength];
        for (int i = 0; i < dataLength; i++) {
            data[i] = (byte) packet.readByte();
        }

        String fileText;
        try {
            // convert to text now
            fileText = new String(SecureOperations.getDecryptedDecompressed(data, GameConstants.FILE_ENCRYPTION_KEY), "UTF-8");
            // handle the reading now
            MasterCommunication.read(new ResponsiveLoginPacket(username, fileText, uuid, lobby, responseCode, rowId));
        } catch (UnsupportedEncodingException e) {
            sendSessionResponse(uuid, ReturnCode.ERROR_LOADING_PROFILE.getValue());
            e.printStackTrace();
        }
    }

    /**
     * Sends a response to the session
     *
     * @param uuid         The uuid of the session
     * @param responseCode The response code id
     */
    private void sendSessionResponse(String uuid, int responseCode) {
        Optional<NetworkSession> optional = NetworkSession.findByUid(uuid);
        if (!optional.isPresent()) {
            System.err.println("Unable to find session by id " + uuid);
            return;
        }
        NetworkSession session = optional.get();
        // sends the response code
        session.write(new LoginResponseCodeBuilder(responseCode).build(null));
    }
}
