package org.redrune.game.content.event.context;

import lombok.Getter;
import org.redrune.game.content.event.EventContext;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/29/2017
 */
public final class CommandEventContext implements EventContext {
	
	/**
	 * The arguments of the command
	 */
	@Getter
	private final String[] arguments;
	
	/**
	 * If the command was sent over the console
	 */
	@Getter
	private final boolean console;
	
	public CommandEventContext(String[] arguments, boolean console) {
		this.arguments = arguments;
		this.console = console;
	}
}
