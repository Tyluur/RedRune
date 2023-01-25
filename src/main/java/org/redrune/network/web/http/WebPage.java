package org.redrune.network.web.http;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public final class WebPage {
	
	/**
	 * The url on which this web page was loaded
	 */
	@Getter
	private final String url;
	
	/**
	 * The lines of code that were on the webpage
	 */
	@Getter
	private final List<String> lines;
	
	/**
	 * Constructs a new webpage instance
	 *
	 * @param url
	 * 		The url to load
	 */
	public WebPage(String url) {
		if (!url.startsWith("http")) {
			url = "http://" + url;
		}
		url = url.replaceAll(" ", "%20");
		this.url = url;
		this.lines = new ArrayList<>();
	}
	
	/**
	 * Loads the data from the webpage
	 *
	 * @param advanced
	 * 		If we should load the data using an advanced protocol instead of the simple one.
	 */
	public WebPage load(boolean advanced) throws IOException {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			if (advanced) {
				connection.setRequestMethod("GET");
				connection.setRequestProperty("User-Agent", "Mozilla Firefox");
				connection.setDoOutput(true);
				connection.setDoInput(true);
			} else {
				connection.setConnectTimeout(3000);
				connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			}
			InputStream input;
			if (advanced) {
				if (connection.getResponseCode() >= 400) {
					input = connection.getErrorStream();
				} else {
					input = connection.getInputStream();
				}
			} else {
				input = connection.getInputStream();
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String line;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	/**
	 * Gets the text on the webpage as a single line formatted by the '\n' split
	 */
	public String asSingleLine() {
		StringBuilder text = new StringBuilder();
		for (String line : lines) {
			text.append(line).append("\n");
		}
		return text.toString();
	}
}
