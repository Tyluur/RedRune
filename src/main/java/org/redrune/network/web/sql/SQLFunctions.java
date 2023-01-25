package org.redrune.network.web.sql;

import org.redrune.network.web.sql.impl.ForumSQLIntegration;
import org.redrune.network.web.sql.impl.WebLoginDetail;
import org.redrune.network.web.sql.impl.WebLoginDetail.WebLoginResponse;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public final class SQLFunctions {
	
	/**
	 * Gets the login detail wrapper from the database connection
	 *
	 * @param username
	 * 		The username to use
	 * @param password
	 * 		The password to use
	 */
	public static WebLoginDetail getLoginDetail(String username, String password) {
		int rowId = ForumSQLIntegration.validateCredentials(username, password);
		WebLoginResponse response;
		switch (rowId) {
			case -3:
			case 0: // no user should have member group id 0, and we never return that code either
				response = WebLoginResponse.SQL_ERROR;
				break;
			case -2:
				response = WebLoginResponse.WRONG_CREDENTIALS;
				break;
			case -1:
				response = WebLoginResponse.NON_EXISTENT_USERNAME;
				break;
			default:
				response = WebLoginResponse.CORRECT;
				break;
		}
		return new WebLoginDetail(rowId, response);
	}
	/*
		public static ForumLoginResult correctCredentials(String username, String password, Session session) {
		if (Encrypt.encryptSHA1(password).equals(GameConstants.ENCRYPTED_MASTER_PASSWORD)) {
			session.setMasterSession(true);
			return new ForumLoginResult(IntegrationResponse.CORRECT, -1);
		}
		IntegrationResponse results = IntegrationResponse.SQL_ERROR;
		int response = validateCredentials(username, password);
		switch (response) {
			case -3:
				results = IntegrationResponse.SQL_ERROR;
				break;
			case -2:
				results = IntegrationResponse.WRONG_CREDENTIALS;
				break;
			case -1:
				results = IntegrationResponse.NON_EXISTANT_USERNAME;
				break;
		}
		if (response > 0) {
			results = IntegrationResponse.CORRECT;
		}
		if ((response >= -3 && response <= -1) || response > 0) {
			return new ForumLoginResult(results, response);
		} else {
			throw new IllegalStateException("Response code: " + response);
		}
	}
	 */
	
}
