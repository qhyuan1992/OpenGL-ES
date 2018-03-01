package org.obj2openjl.v3.model.transform.array;

import java.util.ArrayList;
import java.util.List;

import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.Vertex;
import org.obj2openjl.v3.model.transform.Transformation;
import org.obj2openjl.v3.model.transform.matrix.RotationMatrix;

public class RotationTransformation extends ArrayTransformation {
	
	private float x;
	private float y;
	private float z;
	private float step;

	public RotationTransformation(int numberOfInstances, float angleDegrees, float x, float y, float z) {
		super(numberOfInstances > 0 ? numberOfInstances - 1 : 0);
		this.x = x;
		this.y = y;
		this.z = z;
		this.step = this.getNumberOfInstances() == 0 ? 0 : angleDegrees / this.getNumberOfInstances();
	}

	@Override
	public List<Vertex> transformVertices(List<Vertex> vertices) {
		if(this.getNumberOfInstances() == 0) return new ArrayList<Vertex>();
		
		// Skip original object - combine transformation would produce many duplicates
		List<Vertex> result = new ArrayList<Vertex>();
		for(int i = 1; i <= this.getNumberOfInstances(); i++) {
				RotationMatrix rotationMatrix = new RotationMatrix(i * this.step, this.x, this.y, this.z);
				Transformation rotation = new Transformation(rotationMatrix);
				
				result.addAll(rotation.copyAndApplyToVertices(vertices));
		}
		return result;
	}
	
	@Override
	public List<Normal> transformNormals(List<Normal> normals) {
		if(this.getNumberOfInstances() == 0) return new ArrayList<Normal>();
		
		// Skip original object - combine transformation would produce many duplicates
		List<Normal> result = new ArrayList<Normal>();
		for(int i = 1; i <= this.getNumberOfInstances(); i++) {
				RotationMatrix rotationMatrix = new RotationMatrix(i * this.step, this.x, this.y, this.z);
				Transformation rotation = new Transformation(rotationMatrix);
				
				result.addAll(rotation.copyAndApplyToNormals(normals));
		}
		return result;
	}

}
