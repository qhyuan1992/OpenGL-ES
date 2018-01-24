package org.obj2openjl.v3.matcher.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.obj2openjl.v3.matcher.MatchHandler;
import org.obj2openjl.v3.model.FaceProperty;

public class FaceMatcher extends MatchHandler<List<FaceProperty>> {
	
	private Pattern faceLinePattern = Pattern.compile("^f.*$");

	@Override
	protected Pattern getPattern() {
		return this.faceLinePattern;
	}

	@Override
	protected void handleMatch(String group) {
		FacePropertyExtractor facePropertyExtractor = new FacePropertyExtractor();
		this.addMatch(facePropertyExtractor.extractFacePropertiesFrom(group));
	}
	
	private static class FacePropertyExtractor {
		private List<Integer> propertyList = new ArrayList<Integer>();
		
		public List<FaceProperty> extractFacePropertiesFrom(String matchedLine) {
			String[] properties = matchedLine.split(" ");
			this.addProperties(properties);
			return this.getFacePropertyList();
		}
		
		/**
		 * Takes the split face property list and iterates through
		 * the array, passing each tuple to addProperty() and
		 * addMissingValues(); 
		 * 
		 * @param properties
		 */
		private void addProperties(String[] properties) {
			for(int i = 1; i < properties.length; i++) {
				String[] property = properties[i].split("/");
				this.addProperty(property);
				this.addMissingValues(property);
			}
		}
		
		/**
		 * Iterates through the split property tuple values
		 * and passes them to addValue();
		 * 
		 * @param property
		 */
		private void addProperty(String[] property) {
			for(String i : property) {
				this.addValue(i);
			}
			
		}
		
		/**
		 * Checks each value if it's a valid short number
		 * and adds null if not so.
		 * 
		 * @param value
		 */
		private void addValue(String value) {
			if(!"".equals(value)) {
				this.propertyList.add(Integer.decode(value));
			} else {
				this.propertyList.add(null);
			}
		}
		
		/**
		 * Fills gaps in the value list with null, ensuring that
		 * each face has 4 values.
		 * 
		 * @param property
		 */
		private void addMissingValues(String[] property) {
			for(int k = property.length; k < 3; k++)  {
				this.propertyList.add(null);
			}
		}
		
		private List<FaceProperty> getFacePropertyList() {
			List<FaceProperty> result = new ArrayList<FaceProperty>();
			
			for(int i = 0; i < this.propertyList.size() - 2; i+=3) {
				int vertexIndex = this.propertyList.get(i);
				Integer texturePointIndex = this.propertyList.get(i + 1);
				Integer normalIndex = this.propertyList.get(i + 2);
				
				result.add(new FaceProperty(vertexIndex,
						normalIndex,
						texturePointIndex));
			}
			
			return result;
		}

	}
	
}
