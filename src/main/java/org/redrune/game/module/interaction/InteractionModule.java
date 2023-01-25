package org.redrune.game.module.interaction;

import org.redrune.utility.tool.Misc;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/27/2017
 */
public interface InteractionModule {
	
	/**
	 * Converts the varargs to an array
	 *
	 * @param parameters
	 * 		The varargs
	 */
	default int[] arguments(int... parameters) {
		return Misc.arguments(parameters);
	}
}
