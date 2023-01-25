package org.redrune.utility.backend;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class modifies previous {@link System#out} logging and prints them with information. We need to know which class
 * printed data and at what time at all times.
 *
 * @author Tyluur<itstyluur@gmail.com>
 * @since Apr 9, 2015
 */
public class OutLogger extends PrintStream {
	
	/**
	 * The format of a date
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss.SSS");
	
	public OutLogger(OutputStream out) {
		super(out);
	}
	
	@Override
	public void print(boolean message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	@Override
	public void print(int message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	@Override
	public void print(long message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	@Override
	public void print(double message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	@Override
	public void print(String message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	@Override
	public void print(Object message) {
		StackTraceElement element = getProperElement(Thread.currentThread().getStackTrace());
		prettyLog(element.getFileName() + ":" + element.getLineNumber() + "#" + element.getMethodName(), "" + message);
	}
	
	/**
	 * Gets the proper stacktrace element
	 *
	 * @param elements
	 * 		The elements
	 */
	private StackTraceElement getProperElement(StackTraceElement[] elements) {
		for (int i = 0; i < elements.length; i++) {
			StackTraceElement element = elements[i];
			if (element.toString().contains("java.io.PrintStream")) {
				int newIndex = i + 1;
				if (newIndex >= elements.length) {
					continue;
				}
				return elements[newIndex];
			}
		}
		return elements[elements.length - 2];
	}
	
	/**
	 * Outputs a pretty log
	 *
	 * @param description
	 * 		The description of where its coming from
	 * @param text
	 * 		The text that is being outputted
	 */
	private void prettyLog(String description, String text) {
		String pretext = "[" + description + "][" + getFormattedDate() + "]  " + text;
		super.print(pretext);
	}
	
	/**
	 * Gets the date in a formatted string.
	 *
	 * @return The date
	 */
	private String getFormattedDate() {
		return DATE_FORMAT.format(new Date());
	}
}