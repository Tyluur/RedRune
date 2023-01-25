package org.redrune.cache.parse;

import org.redrune.cache.CacheConstants;
import org.redrune.cache.CacheManager;
import org.redrune.cache.parse.definition.AnimationDefinition;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/1/2017
 */
public final class AnimationDefinitionParser {
	
	/**
	 * The map of definitions
	 */
	private static final ConcurrentHashMap<Integer, AnimationDefinition> DEFINITIONS = new ConcurrentHashMap<>();
	
	/**
	 * Gets animation definitions by an animation id
	 *
	 * @param animationId
	 * 		The animation id
	 */
	public static AnimationDefinition forId(int animationId) {
		if (animationId < 0) {
			return null;
		}
		AnimationDefinition definitions = DEFINITIONS.get(animationId);
		if (definitions != null) {
			return definitions;
		} else {
			byte[] is = null;
			try {
				is = CacheManager.getData(CacheConstants.ANIM_IDX_ID, animationId >>> 7, animationId & 0x7f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			definitions = new AnimationDefinition();
			try {
				if (is != null) {
					definitions.init(ByteBuffer.wrap(is));
				}
			} catch (Exception e) {
				throw new IllegalStateException("Unable to get definition for animation id " + animationId + ".", e);
			}
			definitions.setId(animationId);
			definitions.method544();
			DEFINITIONS.put(animationId, definitions);
			return definitions;
		}
	}
	
}