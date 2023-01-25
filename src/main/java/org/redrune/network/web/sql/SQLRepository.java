package org.redrune.network.web.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.redrune.game.GameConstants;
import org.redrune.utility.backend.configuration.ConfigurationNode;
import org.redrune.utility.backend.configuration.ConfigurationParser;
import org.redrune.utility.backend.configuration.MySQLDatabaseConfiguration;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class SQLRepository {
	
	/**
	 * The source from which the database will be connected
	 */
	private static DataSource dataSource;
	
	/**
	 * The database configuration
	 */
	private static MySQLDatabaseConfiguration configuration = new MySQLDatabaseConfiguration();
	
	/**
	 * Gets the data source
	 */
	public static DataSource getDataSource() {
		if (dataSource == null) {
			HikariConfig config = new HikariConfig();
			
			config.setJdbcUrl("jdbc:mysql://" + configuration.getHost() + ":" + configuration.getPort() + "/" + configuration.getDatabase());
			config.setUsername(configuration.getUsername());
			config.setPassword(configuration.getPassword());
			
			config.setConnectionTimeout(8000);
			config.setAutoCommit(false);
			config.setMinimumIdle(0);
			config.setMaximumPoolSize(10);
			
			config.addDataSourceProperty("cachePrepStmts", "true");
			config.addDataSourceProperty("prepStmtCacheSize", "256");
			config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
			config.addDataSourceProperty("useServerPrepStmts", true);
			dataSource = new HikariDataSource(config);
		}
		return dataSource;
	}
	
	/**
	 * Loads server configuration.
	 */
	public static void storeConfiguration() {
		try (FileInputStream fis = new FileInputStream(GameConstants.SQL_CONFIGURATION_FILE)) {
			ConfigurationParser parser = new ConfigurationParser(fis);
			ConfigurationNode mainNode = parser.parse();
			if (!mainNode.has("database")) {
				System.out.println("Unable to identify database key");
				return;
			}
			ConfigurationNode databaseNode = mainNode.nodeFor("database");
			configuration.setHost(databaseNode.getString("host"));
			configuration.setPort(databaseNode.getInteger("port"));
			configuration.setDatabase(databaseNode.getString("database"));
			configuration.setUsername(databaseNode.getString("username"));
			configuration.setPassword(databaseNode.getString("password"));
			System.out.println("Stored sql database configuration from " + GameConstants.SQL_CONFIGURATION_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
