package org.redrune.game.content.dialogue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/6/2017
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DialogueSubscription {
	
	/**
	 * The names of npcs that this dialogue subscribes to
	 */
	String[] npcNames() default "";
	
	/**
	 * The names of objects that this dialgoue subscribes to
	 */
	String[] objectNames() default "";
	
}
