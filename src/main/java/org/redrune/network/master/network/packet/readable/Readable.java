package org.redrune.network.master.network.packet.readable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/11/2017
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Readable {
	
	/**
	 * The ids of the packet that are readable
	 */
	int[] packetIds();
	
}
