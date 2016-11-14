package org.obj2openjl.v3.matcher.primitive;

import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;

public class FloatMatcher extends MatchHandler<Float> {
	
	private Pattern floatNumberPattern = Pattern.compile("[+-]?[0-9]*\\.[0-9]*");

	@Override
	protected Pattern getPattern() {
		return this.floatNumberPattern;
	}
	
	boolean print = false;
	
	@Override
	protected void handleMatch(String group) {
		Float f = Float.parseFloat(group);
		this.addMatch(f);
	}

}
