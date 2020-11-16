package org.redrune.utility.tool;

import java.util.Random;

/**
 * Represents a class used for random methods.
 *
 * @author Vexia
 * @author Tyluur <itstyluur@gmail.com>
 */
public class RandomFunction {
	
	/**
	 * The random instance.
	 */
	public static final Random RANDOM = new Random();
	
	/**
	 * Constructs a new {@code RandomFunction} {@code Object}.
	 */
	private RandomFunction() {
		/**
		 * empty.
		 */
	}
	
	/**
	 * Method used to ease the access of the random class.
	 *
	 * @param min
	 * 		the minium random value.
	 * @param max
	 * 		the maximum random value.
	 * @return the value as an {@link Double}.
	 */
	public static final double random(double min, double max) {
		final double n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random((int) n));
	}
	
	/**
	 * Method used to ease the access of the random class.
	 *
	 * @param min
	 * 		the minium random value.
	 * @param max
	 * 		the maximum random value.
	 * @return the value as an {@link Integer}.
	 */
	public static final int random(int min, int max) {
		final int n = Math.abs(max - min);
		return Math.min(min, max) + (n == 0 ? 0 : random(n));
	}
	
	/**
	 * Method used to return a random integer.
	 *
	 * @param value
	 * 		the value.
	 * @return the integer.
	 */
	public static int getRandomizer(int value) {
		return getRandom(1) == 0 ? value : -value;
	}
	
	/**
	 * Method used to return the integer.
	 *
	 * @param maxValue
	 * 		the value.
	 * @return the value.
	 */
	public static final int getRandom(int maxValue) {
		return (int) (Math.random() * (maxValue + 1));
	}
	
	/**
	 * Method used to return the random double.
	 *
	 * @param maxValue
	 * 		the value.
	 * @return the double.
	 */
	public static final double getRandomDouble(double maxValue) {
		return (Math.random() * (maxValue + 1));
	}
	
	/**
	 * Method used to ease the access of the random class.
	 *
	 * @param maxValue
	 * 		the maximum value.
	 * @return the random integer.
	 */
	public static final int random(int maxValue) {
		if (maxValue <= 0) {
			return 0;
		}
		return RANDOM.nextInt(maxValue);
	}
	
	/**
	 * Gets a random value from the array.
	 *
	 * @param array
	 * 		The array.
	 * @return A random element of the array.
	 */
	public static <T> T getRandomElement(T[] array) {
		return (T) array[randomize(array.length)];
	}
	
	/**
	 * Randomizes the value.
	 *
	 * @param value
	 * 		the value to randomize.
	 * @return The random value.
	 */
	public static int randomize(int value) {
		if (value < 1) {
			return 0;
		}
		return RANDOM.nextInt(value);
	}
	
	/**
	 * Finds out if a certain event should happen, and if it should, return true;
	 *
	 * @param chance
	 * 		The chance of the event happening
	 * @return If the event should happen
	 */
	public static boolean percentageChance(int chance) {
		double x = (Math.random() * 100);
		return x < chance;
	}
}