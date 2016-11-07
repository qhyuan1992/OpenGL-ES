package org.obj2openjl.v3.matcher.implementation;

import java.util.List;
import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;
import org.obj2openjl.v3.matcher.primitive.FloatMatcher;
import org.obj2openjl.v3.model.TexturePoint;

public class TextureMatcher extends MatchHandler<TexturePoint> {
	
	private Pattern textureLinePattern = Pattern.compile("^vt.*$");

	@Override
	protected Pattern getPattern() {
		return this.textureLinePattern;
	}
	
	@Override
	protected void handleMatch(String group) {
		FloatMatcher floatMatcher = new FloatMatcher();
		floatMatcher.matchString(group);
		
		List<Float> coordinates = floatMatcher.getMatches();
		if(coordinates.size() < 3) {
			coordinates.add(null);
		}
		
		this.addMatch(new TexturePoint(coordinates));
	}
	
}
