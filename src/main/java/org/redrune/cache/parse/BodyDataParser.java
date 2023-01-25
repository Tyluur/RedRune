package org.redrune.cache.parse;

import lombok.Getter;
import lombok.Setter;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.definition.BodyData;
import org.redrune.cache.stream.RSInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/21/2017
 */
public final class BodyDataParser {
	
	/**
	 * The array of body data
	 */
	@Getter
	@Setter
	private static int[] bodyData;
	
	public static void loadAll() {
		BodyData read = null;
		try {
			read = BodyDataParser.read();
		} catch (IOException e) {
			System.out.println("Unable to parse body data!");
			e.printStackTrace();
		}
		if (read == null) {
			return;
		}
		setBodyData(read.getPartsData());
	}
	
	/**
	 * Reads body data from the cache.
	 *
	 * @return The body data object, or null if it failed.
	 */
	public static BodyData read() throws IOException {
		BodyData data = new BodyData();
		byte[] buff = CacheManager.getData(28, 6, 0);
		RSInputStream reader = new RSInputStream(new ByteArrayInputStream(buff));
		data.parse(reader);
		reader.close();
		return data;
	}
}
