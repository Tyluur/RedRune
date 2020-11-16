package org.redrune.utility.rs;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 6/13/2017
 */
public class CensorUtility {
	
	/**
	 * The words that are censored
	 */
	private static final String[] CENSORED_WORDS = { "fuck", "bitch", "hoe", "ass", "cock", "dick", "nigger" };
	
	public static void main(String[] args) {
		System.out.println(getCensoredSentence("yo sup guys its your fucking boy tyluur with a nice censoring system, this one will censor any fcking word that sets off the cnt flag fuckufuffu haha llo fucktest cocktest cock"));
	}
	
	public static String getCensoredSentence(String sentence) {
		String newSentence = "";
		String[] split = sentence.split(" ");
		for (String word : split) {
			String newWord = censor(word);
			newSentence += newWord + " ";
		}
		return newSentence;
	}
	
	private static String censor(String word) {
		int wordLength = word.length();
		for (String censored : CENSORED_WORDS) {
			if (word.toLowerCase().contains(censored.toLowerCase())) {
				return getAsterikWord(wordLength);
			}
		}
		return word;
	}
	
	private static String getAsterikWord(int length) {
		String text = "";
		for (int i = 0; i < length; i++) {
			text += "*";
		}
		return text;
	}
	
}
