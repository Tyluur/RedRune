package org.redrune.network.web.sql.impl;

import lombok.Getter;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/16/2017
 */
public class WebLoginDetail {
	
	/**
	 * The member group id that was identified in this process
	 */
	@Getter
	private final int rowId;
	
	/**
	 * The response that we received
	 */
	@Getter
	private final WebLoginResponse response;
	
	public WebLoginDetail(int rowId, WebLoginResponse response) {
		this.rowId = rowId;
		this.response = response;
	}
	
	@Override
	public String toString() {
		return "WebLoginDetail{" + "rowId=" + rowId + ", response=" + response + '}';
	}
	
	/**
	 * The enumeration of possible responses from the forum when logging in
	 */
	public enum WebLoginResponse {
		
		/** The credentials are correct */
		CORRECT,
		
		/** The username entered did not exist */
		NON_EXISTENT_USERNAME,
		
		/** The credentials you entered were not valid */
		WRONG_CREDENTIALS,
		
		/** There was an error in the sql aspect */
		SQL_ERROR
	}
	
}