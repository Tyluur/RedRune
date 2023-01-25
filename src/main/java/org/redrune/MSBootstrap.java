package org.redrune;

import org.redrune.cache.Cache;
import org.redrune.core.system.SystemManager;
import org.redrune.network.master.server.engine.MSEngineFactory;
import org.redrune.network.master.server.network.MSNetworkSystem;
import org.redrune.network.web.sql.SQLRepository;

/**
 * The lobby server bootstrap, used to start the master server on its own.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/10/2017
 */
public class MSBootstrap {
	
	/**
	 * The network system
	 */
	private static final MSNetworkSystem NETWORK_SYSTEM = new MSNetworkSystem();
	
	/**
	 * The main method that starts the master server
	 *
	 * @param args
	 * 		The jvm arguments
	 */
	public static void main(String[] args) {
		try {
			Cache.init();
			SystemManager.setDefaults(args);
			SQLRepository.storeConfiguration();
			MSEngineFactory.startUp();
			NETWORK_SYSTEM.bind();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}