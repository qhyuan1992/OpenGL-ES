package org.obj2openjl.v3.matcher.primitive;

import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;

public class ShortMatcher extends MatchHandler<Short> {

	private Pattern shortNumberPattern = Pattern.compile("-?[1-9]++[0-9]*");
	
	@Override
	protected Pattern getPattern() {
		return this.shortNumberPattern;
	}

	@Override
	protected void handleMatch(String group) {
		this.addMatch(Short.parseShort(group));
	}

}
