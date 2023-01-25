package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.game.world.World;
import org.redrune.network.lobby.LobbyNetwork;
import org.redrune.network.master.MasterCommunication;
import org.redrune.network.web.sql.SQLRepository;
import org.redrune.utility.backend.UnexpectedArgsException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class LSBootstrap {

    public static void main(String[] args) {
        try {
            SystemManager.setDefaults(args);
        } catch (UnexpectedArgsException e) {
            UnexpectedArgsException.push();
            System.exit(1);
            return;
        }
        Cache.init();
        try {
            // starts the communication to the master server
            MasterCommunication.start();
            // stores the sql configuration data
            SQLRepository.storeConfiguration();
            // loads all lobby packets
            LobbyNetwork.PACKET_REPOSITORY.storeAll();
            // create a new world
            World world = World.create(args);
            // runs the procedure
            world.run();
            // binds the network port
            LobbyNetwork.bind();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
