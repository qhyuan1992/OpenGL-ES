package org.obj2openjl.v3.matcher.implementation;

import java.util.List;
import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;
import org.obj2openjl.v3.matcher.primitive.FloatMatcher;
import org.obj2openjl.v3.model.Normal;

public class NormalMatcher extends MatchHandler<Normal> {
	
	
	private Pattern normalLinePattern = Pattern.compile("^vn.*$");

	@Override
	protected Pattern getPattern() {
		return this.normalLinePattern;
	}
	
	@Override
	protected void handleMatch(String group) {
		FloatMatcher floatMatcher = new FloatMatcher();
		floatMatcher.matchString(group);
		
		List<Float> coordinates = floatMatcher.getMatches();
		
		this.addMatch(new Normal(coordinates));
	}
	
}
