package org.redrune.utility.tool;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import io.netty.channel.Channel;
import org.redrune.cache.CacheFileStore;
import org.redrune.game.node.Location;
import org.redrune.game.node.entity.Entity;
import org.redrune.game.node.entity.npc.NPC;
import org.redrune.game.node.entity.player.Player;
import org.redrune.game.world.region.Region;
import org.redrune.game.world.region.RegionManager;

import java.io.*;
import java.net.SocketAddress;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class Misc {
	
	private static final char[] VALID_CHARS = { '_', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	
	/**
	 * The gson instance
	 */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	/**
	 * The random instance
	 */
	private static final Random RANDOM = new Random();
	
	/**
	 * Checks if a character is valid to use.
	 *
	 * @param c
	 * 		The character.
	 * @return {@code True} if so.
	 */
	public static boolean allowed(char c) {
		return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '_' || c == ' ';
	}
	
	/**
	 * Collapses a wide array of numbers
	 *
	 * @param numbers
	 * 		The numbers
	 */
	public static int[] arguments(int... numbers) {
		return numbers;
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
		String format = String.format("./bin/%s", directory.replace(".", "/"));
		final File[] files = new File(format).listFiles();
		if (files == null) {
			return classes;
		}
		for (File file : files) {
			if (file.getName().contains("$") || file.getName().contains("dropbox")) {
				continue;
			}
			// the file is a directory
			if (file.isDirectory()) {
				String fileToDirectory = file.getPath().replace("\\", ".").replace("/", ".");
				fileToDirectory = fileToDirectory.substring(fileToDirectory.indexOf(".org") + 1, fileToDirectory.length());
				List<Object> classesInDirectory = getClassesInDirectory(fileToDirectory);
				classes.addAll(classesInDirectory);
			} else {
				try {
					String className = directory + "." + file.getName().replace(".class", "");
					Object objectEvent = (Class.forName(className).newInstance());
					classes.add(objectEvent);
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return classes;
	}
	
	/**
	 * Gets all of the sub directories of a folder
	 */
	public static List<String> getSubDirectories(Object object) {
		String firstDirectory;
		if (object instanceof Class) {
			firstDirectory = ((Class<?>) object).getPackage().getName();
		} else if (object instanceof String) {
			firstDirectory = ((String) object);
		} else {
			throw new IllegalStateException();
		}
		String directory = "./bin/" + firstDirectory.replace(".", "/");
		File file = new File(directory);
		String[] directories = file.list((current, name) -> new File(current, name).isDirectory());
		return Arrays.asList(directories != null ? directories : new String[0]);
	}
	
	/**
	 * Converts an IP-Address as string to Integer.
	 *
	 * @return The Integer.
	 */
	public static int IPAddressToNumber(String ipAddress) {
		StringTokenizer st = new StringTokenizer(ipAddress, ".");
		int[] ip = new int[4];
		int i = 0;
		while (st.hasMoreTokens()) {
			ip[i++] = Integer.parseInt(st.nextToken());
		}
		return ((ip[0] << 24) | (ip[1] << 16) | (ip[2] << 8) | (ip[3]));
	}
	
	/**
	 * Gets the direction the player is running
	 *
	 * @param dx
	 * 		The x direction
	 * @param dy
	 * 		The y direction
	 */
	
	public static int getRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2) {
			return 0;
		}
		if (dx == -1 && dy == -2) {
			return 1;
		}
		if (dx == 0 && dy == -2) {
			return 2;
		}
		if (dx == 1 && dy == -2) {
			return 3;
		}
		if (dx == 2 && dy == -2) {
			return 4;
		}
		if (dx == -2 && dy == -1) {
			return 5;
		}
		if (dx == 2 && dy == -1) {
			return 6;
		}
		if (dx == -2 && dy == 0) {
			return 7;
		}
		if (dx == 2 && dy == 0) {
			return 8;
		}
		if (dx == -2 && dy == 1) {
			return 9;
		}
		if (dx == 2 && dy == 1) {
			return 10;
		}
		if (dx == -2 && dy == 2) {
			return 11;
		}
		if (dx == -1 && dy == 2) {
			return 12;
		}
		if (dx == 0 && dy == 2) {
			return 13;
		}
		if (dx == 1 && dy == 2) {
			return 14;
		}
		if (dx == 2 && dy == 2) {
			return 15;
		}
		return -1;
	}
	
	/**
	 * Gets the direction the player is wlking
	 *
	 * @param dx
	 * 		The x direction
	 * @param dy
	 * 		The y direction
	 */
	public static int getWalkDirection(int dx, int dy) {
		if (dx < 0 && dy < 0) {
			return 0;
		}
		if (dx == 0 && dy < 0) {
			return 1;
		}
		if (dx > 0 && dy < 0) {
			return 2;
		}
		if (dx < 0 && dy == 0) {
			return 3;
		}
		if (dx > 0 && dy == 0) {
			return 4;
		}
		if (dx < 0 && dy > 0) {
			return 5;
		}
		if (dx == 0 && dy > 0) {
			return 6;
		}
		if (dx > 0 && dy > 0) {
			return 7;
		}
		return -1;
	}
	
	/**
	 * Format a player's name for display.
	 *
	 * @param name
	 * 		The name to be formatted.
	 * @return The formatted string.
	 */
	public static String formatPlayerNameForDisplay(String name) {
		final StringBuilder builder = new StringBuilder();
		name = name.replaceAll("_", " ").toLowerCase();
		boolean wasSpace = true;
		for (int i = 0; i < name.length(); i++) {
			if (wasSpace) {
				builder.append(("" + name.charAt(i)).toUpperCase());
				wasSpace = false;
			} else {
				builder.append(name.charAt(i));
			}
			if (name.charAt(i) == ' ') {
				wasSpace = true;
			}
		}
		return builder.toString();
	}
	
	/**
	 * Optimizes the text for a chat message
	 *
	 * @param message
	 * 		The text
	 */
	public static String formatTextToSentence(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		boolean exception = false;
		for (int i = 0; i < message.length(); i++) {
			if (!exception) {
				if (wasSpace) {
					newText.append(("" + message.charAt(i)).toUpperCase());
					if (!String.valueOf(message.charAt(i)).equals(" ")) {
						wasSpace = false;
					}
				} else {
					newText.append(("" + message.charAt(i)).toLowerCase());
				}
			} else {
				newText.append("").append(message.charAt(i));
			}
			if (String.valueOf(message.charAt(i)).contains(".") || String.valueOf(message.charAt(i)).contains("!") || String.valueOf(message.charAt(i)).contains("?")) {
				wasSpace = true;
			}
		}
		return newText.toString();
	}
	
	/**
	 * Polls every element within the specified {@link Queue} and performs the specified {@link Consumer} event for each
	 * element.
	 *
	 * @param queue
	 * 		The {@link Queue} to poll elements from. Must not be {@code null}.
	 * @param consumer
	 * 		The {@link Consumer} to execute for each polled element. Must not be {@code null}.
	 */
	public static <T> void pollAll(Queue<T> queue, Consumer<T> consumer) {
		Preconditions.checkNotNull(queue, "Queue may not be null");
		Preconditions.checkNotNull(consumer, "Consumer may not be null");
		
		T element;
		while ((element = queue.poll()) != null) {
			consumer.accept(element);
		}
	}
	
	public static int getMoveDirection(int xOffset, int yOffset) {
		if (xOffset < 0) {
			if (yOffset < 0) {
				return 5;
			} else if (yOffset > 0) {
				return 0;
			} else {
				return 3;
			}
		} else if (xOffset > 0) {
			if (yOffset < 0) {
				return 7;
			} else if (yOffset > 0) {
				return 2;
			} else {
				return 4;
			}
		} else {
			if (yOffset < 0) {
				return 6;
			} else if (yOffset > 0) {
				return 1;
			} else {
				return -1;
			}
		}
	}
	
	/**
	 * Gets the entry of an array at a slot, if there is nothing, it will return null. If the slot is too small/big it
	 * will return null
	 */
	@SuppressWarnings("unchecked")
	public static <K> K getArrayEntry(K[] array, int slot) {
		if (slot >= array.length || slot < 0) {
			return null;
		}
		return array[slot];
	}
	
	/**
	 * Gets the class type from a character string
	 *
	 * @param characters
	 * 		The characters
	 */
	public static Class getClassType(String characters) {
		if (isDigit(characters)) {
			return Integer.class;
		} else if (isBoolean(characters)) {
			return Boolean.class;
		} else {
			return String.class;
		}
	}
	
	/**
	 * Checks if the characters are numbers
	 */
	public static boolean isDigit(String characters) {
		Integer digit;
		try {
			digit = Integer.parseInt(characters);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Checks if the characters are a boolean
	 */
	public static boolean isBoolean(String characters) {
		return "true".equals(characters) || "false".equals(characters);
	}
	
	public static void clearInterface(Player player, int interfaceId) {
		int componentLength = CacheFileStore.getAmountOfComponents(interfaceId);
		for (int i = 0; i < componentLength; i++) {
			player.getManager().getInterfaces().sendInterfaceText(interfaceId, i, "");
		}
	}
	
	/**
	 * This method clears all the text inside a file
	 *
	 * @param file
	 * 		The file location
	 */
	public static void clearFile(String file) {
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.print("");
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the direction the player is facing from walking. <ul> <li>0 - southwest</li> <li>1 - south</li> <li>2 -
	 * southeast</li> <li>3 - west</li> <li>4 - east</li> <li>5 - northwest</li> <li>6 - north</li> <li>7 -
	 * northeast</li> </ul>
	 *
	 * @param dx
	 * 		The delta x
	 * @param dy
	 * 		The delta y
	 */
	public static int getPlayerWalkingDirection(int dx, int dy) {
		if (dx == -1 && dy == -1) {
			return 0;
		}
		if (dx == 0 && dy == -1) {
			return 1;
		}
		if (dx == 1 && dy == -1) {
			return 2;
		}
		if (dx == -1 && dy == 0) {
			return 3;
		}
		if (dx == 1 && dy == 0) {
			return 4;
		}
		if (dx == -1 && dy == 1) {
			return 5;
		}
		if (dx == 0 && dy == 1) {
			return 6;
		}
		if (dx == 1 && dy == 1) {
			return 7;
		}
		return -1;
	}
	
	/**
	 * Gets the direction for the player to face, based on their running direction. <ul> <li>0 - southwest</li> <li>1 -
	 * south</li> <li>2 - southeast</li> <li>3 - west</li> <li>4 - east</li> <li>5 - northwest</li> <li>6 - north</li>
	 * <li>7 - northeast</li> </ul>
	 *
	 * @param dx
	 * 		The delta x
	 * @param dy
	 * 		The delta y
	 */
	public static int getPlayerRunningDirection(int dx, int dy) {
		if (dx == -2 && dy == -2) {
			return 0;
		}
		if (dx == -1 && dy == -2) {
			return 1;
		}
		if (dx == 0 && dy == -2) {
			return 2;
		}
		if (dx == 1 && dy == -2) {
			return 3;
		}
		if (dx == 2 && dy == -2) {
			return 4;
		}
		if (dx == -2 && dy == -1) {
			return 5;
		}
		if (dx == 2 && dy == -1) {
			return 6;
		}
		if (dx == -2 && dy == 0) {
			return 7;
		}
		if (dx == 2 && dy == 0) {
			return 8;
		}
		if (dx == -2 && dy == 1) {
			return 9;
		}
		if (dx == 2 && dy == 1) {
			return 10;
		}
		if (dx == -2 && dy == 2) {
			return 11;
		}
		if (dx == -1 && dy == 2) {
			return 12;
		}
		if (dx == 0 && dy == 2) {
			return 13;
		}
		if (dx == 1 && dy == 2) {
			return 14;
		}
		if (dx == 2 && dy == 2) {
			return 15;
		}
		return -1;
	}
	
	/**
	 * Saves data using gson
	 *
	 * @param file
	 * 		The file to save to
	 * @param data
	 * 		The data to save
	 */
	public static void saveData(File file, Object data) {
		try (Writer writer = new FileWriter(file.getAbsolutePath())) {
			GsonBuilder builder = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping();
			Gson gson = builder.create();
			gson.toJson(data, writer);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes text to the file
	 *
	 * @param file
	 * 		The file
	 * @param text
	 * 		The text to write
	 * @param append
	 * 		If we should append text
	 */
	public static void writeTextToFile(String file, String text, boolean append) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, append))) {
			writer.write(text);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int getFaceDirection(int xOffset, int yOffset) {
		return ((int) (Math.atan2(-xOffset, -yOffset) * 2607.5945876176133)) & 0x3fff;
	}
	
	public static int getRandom(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}
	
	public static int random(int maxValue) {
		if (maxValue <= 0) {
			return 0;
		}
		return RANDOM.nextInt(maxValue);
	}
	
	public static int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	
	public static String formatPlayerNameForProtocol(String name) {
		if (name == null) {
			return "";
		}
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		return name;
	}
	
	public static String formatPlayerNameForURL(String name) {
		name = name.replaceAll(" ", "_");
		name = name.toLowerCase();
		String newName = "";
		boolean uppercased = false;
		for (int i = 0; i < name.toCharArray().length; i++) {
			char c = name.toCharArray()[i];
			if (!uppercased && name.toCharArray()[i] != '_') {
				c = Character.toUpperCase(c);
				uppercased = true;
			}
			newName = newName + "" + c;
		}
		return newName;
	}
	
	/**
	 * Loads the file data
	 *
	 * @param file
	 * 		The file to load data from
	 */
	public static <K> K loadGsonData(File file) {
		if (!file.exists()) {
			return null;
		}
		return Misc.getGSON().fromJson(Misc.getText(file.getAbsolutePath()), new TypeToken<K>() {
		}.getType());
	}
	
	/**
	 * Loads a player from the file contents
	 *
	 * @param fileContents
	 * 		The contents of the file
	 */
	public static Player loadPlayer(String fileContents) {
		return Misc.getGSON().fromJson(fileContents, new TypeToken<Player>() {
		}.getType());
	}
	
	public static Gson getGSON() {
		return GSON;
	}
	
	/**
	 * Getting the text in the file as a formatted {@code String} {@code Object}
	 *
	 * @param location
	 * 		The location of the file
	 */
	public static String getText(String location) {
		File file = new File(location);
		if (!file.exists()) {
			throw new IllegalStateException("File doesn't exist:\t" + file.getAbsolutePath());
		}
		StringBuilder text = new StringBuilder();
		for (String fileText : getFileText(location)) {
			text.append(fileText).append("\n");
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
	public static List<String> getFileText(String file) {
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
	
	public static String format(Number number) {
		return NumberFormat.getIntegerInstance().format(number);
	}
	
	/**
	 * Gets the ip address of a channel
	 *
	 * @param channel
	 * 		The channel
	 */
	public static String getIpAddress(Channel channel) {
		SocketAddress socketAddress = channel.remoteAddress();
		if (socketAddress == null) {
			return "127.0.0.1";
		} else {
			return formatIp(socketAddress.toString());
		}
	}
	
	/**
	 * Formats the IP-Address.
	 *
	 * @param unformatted
	 * 		The unformatted IP.
	 * @return The formatted IP.
	 */
	public static final String formatIp(String unformatted) {
		String ipAddress = unformatted;
		ipAddress = ipAddress.replaceAll("/", "").replaceAll(" ", "");
		ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
		return ipAddress;
	}
	
	public static int getDistance(int coordX1, int coordY1, int coordX2, int coordY2) {
		int deltaX = Math.abs(coordX2 - coordX1);
		int deltaY = Math.abs(coordY2 - coordY1);
		return ((int) Math.ceil(Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2))));
	}
	
	public static boolean colides(Entity entity, Entity target) {
		return entity.getLocation().getPlane() == target.getLocation().getPlane() && colides(entity.getLocation().getX(), entity.getLocation().getY(), entity.getSize(), target.getLocation().getX(), target.getLocation().getY(), target.getSize());
	}
	
	public static boolean colides(int x1, int y1, int size1, int x2, int y2, int size2) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		return distanceX < size2 && distanceX > -size1 && distanceY < size2 && distanceY > -size1;
	}
	
	public static boolean isOnRange(Entity entity, Entity target, int rangeRatio) {
		return entity.getLocation().getPlane() == target.getLocation().getPlane() && isOnRange(entity.getLocation().getX(), entity.getLocation().getY(), entity.getSize(), target.getLocation().getX(), target.getLocation().getY(), target.getSize(), rangeRatio);
	}
	
	public static boolean isOnRange(int x1, int y1, int size1, int x2, int y2, int size2, int maxDistance) {
		int distanceX = x1 - x2;
		int distanceY = y1 - y2;
		return !(distanceX > size2 + maxDistance || distanceX < -size1 - maxDistance || distanceY > size2 + maxDistance || distanceY < -size1 - maxDistance);
	}
	
	public static boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gets the simplified type from a class name
	 *
	 * @param className
	 * 		The class name
	 */
	public static String getSimplifiedType(String className) {
		switch (className) {
			case "String":
				return "Text";
			case "Integer":
				return "#";
			default:
				return className;
		}
	}
	
	/**
	 * Gets a string format of the amount of memory we're using.
	 */
	public static String getMemoryUsageInformation() {
		DecimalFormat decimalFormat = new DecimalFormat("0.0#%");
		NumberFormat memoryFormat = NumberFormat.getInstance();
		Runtime runtime = Runtime.getRuntime();
		long totalMemory = runtime.totalMemory();
		long freeMemory = runtime.freeMemory();
		long usedMemory = (totalMemory - freeMemory);
		return "Total Used JVM Allocated Memory: " + memoryFormat.format(usedMemory / (1024L * 1024L)) + "/" + memoryFormat.format(totalMemory / (1024L * 1024L)) + " MB, " + decimalFormat.format((double) usedMemory / (double) totalMemory) + " - Free JVM Allocated Memory: " + memoryFormat.format(freeMemory / (1024L * 1024L)) + " MB, " + decimalFormat.format((double) freeMemory / (double) totalMemory);
	}
	
	/**
	 * Finds an npc by the name, in a region
	 *
	 * @param region
	 * 		The region the npc is in
	 * @param name
	 * 		The name
	 * @param defaultId
	 * 		The default id if we couldn't find it
	 * @param closestTo
	 * 		The closest tile to the npc we know of.
	 */
	public static int findNPCByName(Region region, String name, int defaultId, Location closestTo) {
		if (closestTo == null) {
			Optional<NPC> optional = region.getNpcs().stream().filter(npc -> npc.getDefinitions().getName().equalsIgnoreCase(name)).findFirst();
			return optional.map(NPC::getId).orElse(defaultId);
		} else {
			List<NPC> npcs = new ArrayList<>(region.getNpcs());
			npcs.sort(Comparator.comparingInt(o -> o.getLocation().getDistance(closestTo)));
			Optional<NPC> optional = npcs.stream().filter(npc -> npc.getDefinitions().getName().equalsIgnoreCase(name)).findFirst();
			return optional.map(NPC::getId).orElse(defaultId);
		}
	}
	
	public static boolean invalidAccountName(String name) {
		return name.length() < 2 || name.length() > 12 || name.startsWith("_") || name.endsWith("_") || name.contains("__") || containsInvalidCharacter(name);
	}
	
	public static boolean containsInvalidCharacter(String name) {
		for (char c : name.toCharArray()) {
			if (containsInvalidCharacter(c)) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean containsInvalidCharacter(char c) {
		for (char vc : VALID_CHARS) {
			if (vc == c) {
				return false;
			}
		}
		return true;
	}
	
	public static int getNpcMoveDirection(int dd) {
		if (dd < 0) {
			return -1;
		}
		return getNpcMoveDirection(RegionManager.DIRECTION_DELTA_X[dd], RegionManager.DIRECTION_DELTA_Y[dd]);
	}
	
	public static int getNpcMoveDirection(int dx, int dy) {
		if (dx == 0 && dy > 0) {
			return 0;
		}
		if (dx > 0 && dy > 0) {
			return 1;
		}
		if (dx > 0 && dy == 0) {
			return 2;
		}
		if (dx > 0 && dy < 0) {
			return 3;
		}
		if (dx == 0 && dy < 0) {
			return 4;
		}
		if (dx < 0 && dy < 0) {
			return 5;
		}
		if (dx < 0 && dy == 0) {
			return 6;
		}
		if (dx < 0 && dy > 0) {
			return 7;
		}
		return -1;
	}
	
	public static boolean inCircle(Location location, Location center, int radius) {
		return getDistance(center, location) < radius;
	}
	
	public static int getDistance(Location t1, Location t2) {
		return getDistance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
	}
	
	public static String convertMillisecondsToTime(long milliseconds) {
		long days = (milliseconds / 86400000L);
		long hours = ((milliseconds / 3600000L) % 24L);
		long minutes = ((milliseconds / 60000L) % 60L);
		//long seconds = ((milliseconds / 1000L) % 60L);
		String string = "";
		if (days > 0) {
			String s = days == 1 ? " day " : " days ";
			string += days + s;
		}
		if (hours > 0) {
			String s = hours == 1 ? " hour " : " hours ";
			string += hours + s;
		}
		if (minutes > 0) {
			String s = minutes == 1 ? " min " : " mins ";
			string += minutes + s;
		}
	/*	if (seconds > 0) {
			String s = seconds == 1 ? " sec " : " secs ";
			string += seconds + s;
		}*/
		if (string.equals("")) {
			string = "1 minute ";
		}
		return string;
	}
	
	public static String getLagPercentage(long lastCycleDuration) {
		double percentage = ((double) (lastCycleDuration == 0 ? 600 : lastCycleDuration) / 600) * 100;
		double total = 100 - percentage;
		NumberFormat formatter = new DecimalFormat("#0.00");
		//System.out.println("[speed=" + speed + ", percentage=" + percentage + "]");
		return formatter.format(total);
	}
	
	public static double round(double value, int precision) {
		StringBuilder zeros = new StringBuilder();
		for (int i = 0; i < precision; i++) {
			zeros.append("" + 0);
		}
		return Double.parseDouble(new DecimalFormat("#." + zeros).format(value));
	}
	
	public static <K> K randomArraySlot(K[] array) {
		return array[random(array.length)];
	}
	
	public static NPC findLocalNPC(Player player, int npcId) {
		for (NPC npc : player.getRegion().getNpcs()) {
			if (npc == null || !npc.isRenderable()) {
				continue;
			}
			if (npc.getId() == npcId) {
				return npc;
			}
		}
		return null;
	}
	
	/**
	 * Formats a number to a long
	 *
	 * @param value
	 * 		The number
	 */
	public static long formatNumber(Object value) {
		String string = value.toString();
		string = string.replace(".", "");
		if (string.contains("E")) {
			string = string.substring(0, string.indexOf("E"));
		}
		return Long.parseLong(string);
	}
	
	public static String fixChatMessage(String message) {
		StringBuilder newText = new StringBuilder();
		boolean wasSpace = true;
		boolean exception = false;
		for (int i = 0; i < message.length(); i++) {
			if (!exception) {
				if (wasSpace) {
					newText.append(("" + message.charAt(i)).toUpperCase());
					if (!String.valueOf(message.charAt(i)).equals(" ")) {
						wasSpace = false;
					}
				} else {
					newText.append(("" + message.charAt(i)).toLowerCase());
				}
			} else {
				newText.append("").append(message.charAt(i));
			}
			if (String.valueOf(message.charAt(i)).contains(".") || String.valueOf(message.charAt(i)).contains("!") || String.valueOf(message.charAt(i)).contains("?")) {
				wasSpace = true;
			}
		}
		return newText.toString();
	}
}
