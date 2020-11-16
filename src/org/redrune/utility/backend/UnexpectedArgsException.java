package org.redrune.utility.backend;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/20/2017
 */
public class UnexpectedArgsException extends Exception {
	
	public UnexpectedArgsException() {
		super("Unexpected jvm arguments!");
		System.err.println("args[0]=[true/false] - debug mode");
		System.err.println("args[1]=[byte] - worldId");
		System.err.println("args[2]=[true/false] - web integrated");
	}
	
	public static void push() {
		new UnexpectedArgsException().printStackTrace();
	}
	
}
