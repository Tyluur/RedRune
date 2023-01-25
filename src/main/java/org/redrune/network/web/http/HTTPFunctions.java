package org.redrune.network.web.http;

import org.redrune.core.EngineWorkingSet;
import org.redrune.network.web.WebConstants;
import org.redrune.utility.tool.Misc;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 8/15/2017
 */
public final class HTTPFunctions {
	
	/**
	 * Registers the username into the forum
	 *
	 * @param name
	 * 		The name
	 * @param password
	 * 		The password
	 */
	public static boolean registerUser(String name, String password) {
		Callable<Boolean> callable = () -> {
			StringBuilder bldr = new StringBuilder();
			String username = Misc.formatPlayerNameForURL(name);
			bldr.append(WebConstants.WEBSITE_URL+ "/create_member.php");
			bldr.append("?username=").append(Misc.formatPlayerNameForDisplay(username));
			bldr.append("&password=").append(password);
			try {
				WebPage page = new WebPage(bldr.toString());
				page.load(false);
				List<String> results = page.getLines();
				StringBuilder result = new StringBuilder();
				for (String resultLine : results) {
					result.append(resultLine).append("\n");
				}
				return result.toString().trim().equalsIgnoreCase("SUCCESS");
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		};
		Future<Boolean> resultsFuture = EngineWorkingSet.WEB_WORKER.submit(callable);
		try {
			return resultsFuture.get();
		} catch (ExecutionException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
