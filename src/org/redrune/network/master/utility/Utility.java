package org.redrune.network.master.utility;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.netty.channel.socket.SocketChannel;
import org.redrune.network.master.utility.rs.LoginConstants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
public class Utility {
	
	/**
	 * Gets the host address of the user logging in.
	 *
	 * @param channel
	 * 		The channel.
	 * @return The host address of this connection.
	 */
	public static String getHost(SocketChannel channel) {
		try {
			return channel.remoteAddress().getAddress().getHostAddress();
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * Gets all of the classes in a directory
	 *
	 * @param directory
	 * 		The directory to iterate through
	 * @return The list of classes
	 */
	public static List<Object> getClassesInDirectory(String directory) {
		List<Object> classes = new ArrayList<>();
		final File[] files = new File(String.format("./bin/%s", directory.replace(".", "/"))).listFiles();
		if (files == null) {
			return classes;
		}
		for (File file : files) {
			if (file.getName().contains("$") || file.getName().contains("dropbox")) {
				continue;
			}
			try {
				Object objectEvent = (Class.forName(directory + "." + file.getName().replace(".class", "")).newInstance());
				classes.add(objectEvent);
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return classes;
	}
	
	/**
	 * If the player file exists
	 *
	 * @param username
	 * 		The name of the file
	 */
	public static boolean playerFileExists(String username) {
		return new File(LoginConstants.getLocation(username)).exists();
	}
	
	/**
	 * Saves data using json
	 *
	 * @param file
	 * 		The file to save to
	 * @param json
	 * 		The data to save
	 */
	public static void saveData(File file, String json) {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
			Gson gson = builder.create();
			
			JsonParser jp = new JsonParser();
			JsonElement je = jp.parse(json);
			String prettyJsonString = gson.toJson(je);
			
			writer.write(prettyJsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the json text from an object
	 *
	 * @param object
	 * 		The object
	 * @param collapse
	 * 		If the text should be collapsed
	 */
	public static String getJsonText(Object object, boolean collapse) {
		GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
		Gson gson = builder.create();
		
		String toJson = gson.toJson(object);
		if (collapse) {
			toJson = toJson.replaceAll("\n", "");
		}
		return toJson;
	}
	
	/**
	 * Getting the text in the file as a formatted {@code String} {@code Object}
	 *
	 * @param location
	 * 		The location of the file
	 */
	public static String getCollapsedText(String location) {
		File file = new File(location);
		if (!file.exists()) {
			throw new IllegalStateException("File doesn't exist:\t" + file.getAbsolutePath());
		}
		StringBuilder text = new StringBuilder();
		for (String fileText : getFileText(location)) {
			text.append(fileText);
		}
		return text.toString();
	}
	
	/**
	 * Gets the text from a file.
	 *
	 * @param file
	 * 		The location of the file.
	 * @return A list of the text in the file. Different lines are separated by different list indexes.
	 */
	private static List<String> getFileText(String file) {
		List<String> text = new ArrayList<>();
		File realFile = new File(file);
		if (!realFile.exists()) {
			return text;
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.equals("") || line.equals(" ")) {
					continue;
				}
				text.add(line);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
}
