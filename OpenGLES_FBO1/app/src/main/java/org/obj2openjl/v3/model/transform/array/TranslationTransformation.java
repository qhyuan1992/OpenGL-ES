package org.obj2openjl.v3.model.transform.array;

import java.util.ArrayList;
import java.util.List;

import org.obj2openjl.v3.model.Vertex;
import org.obj2openjl.v3.model.transform.Transformation;
import org.obj2openjl.v3.model.transform.matrix.TransformationMatrix;
import org.obj2openjl.v3.model.transform.matrix.TranslationMatrix;


public class TranslationTransformation extends ArrayTransformation {
	
	private float distanceX;
	private float distanceY;
	private float distanceZ;
	
	public TranslationTransformation(int numberOfInstances, float distanceX, float distanceY, float distanceZ) {
		super(numberOfInstances > 0 ? numberOfInstances - 1 : 0);
		this.distanceX = distanceX;
		this.distanceY = distanceY;
		this.distanceZ = distanceZ;
	}

	@Override
	public List<Vertex> transformVertices(List<Vertex> vertices) {
		if(this.getNumberOfInstances() == 0) return new ArrayList<Vertex>();
		
		float stepX = this.distanceX / (this.getNumberOfInstances());
		float stepY = this.distanceY / (this.getNumberOfInstances());
		float stepZ = this.distanceZ / (this.getNumberOfInstances());
		
		// Skip original object - combine transformation would produce many duplicates
		List<Vertex> result = new ArrayList<Vertex>();
		for(int i = 1; i <= this.getNumberOfInstances(); i++) {
				TransformationMatrix translationMatrix = new TranslationMatrix(i*stepX, i*stepY, i*stepZ);
				Transformation translation = new Transformation(translationMatrix);
				
				result.addAll(translation.copyAndApplyToVertices(vertices));
		}
		return result;
	}

}
