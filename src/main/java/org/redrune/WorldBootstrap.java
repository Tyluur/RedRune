package org.redrune;

import org.redrune.game.world.World;

/**
 * The class used to run the game
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class WorldBootstrap {
	
	private WorldBootstrap() {
	
	}
	
	/**
	 * The main method executed from the JVM
	 *
	 * @param args
	 * 		Program arguments
	 */
	public static void main(String[] args) {
		try {
			// create a new world
			World world = World.create(args);
			// runs the procedure
			world.run();
		} catch (Exception e) {
			System.out.println("Unexpected error on initialization");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
}
