package org.redrune.network.web.sql.impl;

import org.redrune.network.web.sql.SQLRepository;
import org.redrune.utility.backend.BCryptService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class ForumSQLIntegration {
	
	/**
	 * Validates the users credentials and returns the user's member group id upon successful validation.
	 *
	 * @param username
	 * 		The username
	 * @param password
	 * 		The password
	 * @return <ul><li>-3 in the case of a database connection error.</li><li>-2 if the set did not match</li><li>-1 if
	 * the user did not exist.</li><li>Otherwise, any number greater than 0 is a valid member id.</li></ul>
	 */
	public static int validateCredentials(String username, String password) {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = SQLRepository.getDataSource().getConnection();
			if (connection == null) {
				return -3;
			}
			statement = connection.prepareStatement("SELECT * FROM `core_members` WHERE name=? LIMIT 1");
			statement.setString(1, username);
			int responseCode;
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String salt = resultSet.getString("members_pass_salt");
				String encryptedHash = encryptPassword(password, salt);
				String storedHash = resultSet.getString("members_pass_hash");
				if (storedHash.equals(encryptedHash)) {
					responseCode = resultSet.getInt("member_id");
				} else {
					responseCode = -2;
				}
			} else {
				responseCode = -1;
			}
			connection.close();
			statement.close();
			return responseCode;
		} catch (Throwable t) {
			t.printStackTrace();
			return -3;
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
				if (statement != null) {
					statement.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Encrypts the password to IPB4 format
	 *
	 * @param password
	 * 		The password
	 * @param salt
	 * 		The salt
	 */
	private static String encryptPassword(String password, String salt) {
		return BCryptService.hashpw(password, "$2a$13$" + salt);
	}
}
