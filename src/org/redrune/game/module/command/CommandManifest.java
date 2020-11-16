package org.redrune.game.module.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/31/2017
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandManifest {
	
	/**
	 * The description of the command
	 */
	String description() default "";
	
	/**
	 * The parameter types of the command
	 */
	Class[] types() default { };
	
}
