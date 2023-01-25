package org.redrune.utility.tool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Classes that are readable by gson will implement this class, this is due to the non-generic loading of gson objects.
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/16/2017
 */
public interface GsonReadable<K> {
	
	/**
	 * Handles the loading from a file
	 *
	 * @param file
	 * 		The file
	 */
	K load(File file);
	
	/**
	 * Saves the object
	 *
	 * @param file
	 * 		The file to save to
	 * @param k
	 * 		The object to save
	 */
	default void save(File file, K k) {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
			Gson gson = builder.create();
			gson.toJson(k, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
