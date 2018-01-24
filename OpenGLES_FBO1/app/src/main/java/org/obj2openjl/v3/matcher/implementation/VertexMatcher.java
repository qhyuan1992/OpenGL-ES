package org.obj2openjl.v3.matcher.implementation;

import java.util.List;
import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;
import org.obj2openjl.v3.matcher.primitive.FloatMatcher;
import org.obj2openjl.v3.model.Vertex;

public class VertexMatcher extends MatchHandler<Vertex> {
	
	private Pattern vertexLinePattern = Pattern.compile("^v .*$");

	@Override
	protected Pattern getPattern() {
		return this.vertexLinePattern;
	}
	
	
	@Override
	protected void handleMatch(String group) {
		FloatMatcher floatMatcher = new FloatMatcher();
		floatMatcher.matchString(group);
		List<Float> xyzwCoordinates = floatMatcher.getMatches();
		// w is missing in xyzw
		if(xyzwCoordinates.size() < 4) {
			xyzwCoordinates.add(null);
		}
		
		this.addMatch(new Vertex(
				xyzwCoordinates.get(0),
				xyzwCoordinates.get(1),
				xyzwCoordinates.get(2),
				xyzwCoordinates.get(3)));
	}
	
}
