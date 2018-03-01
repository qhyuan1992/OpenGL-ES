package org.obj2openjl.v3.model.transform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.Vertex;
import org.obj2openjl.v3.model.transform.matrix.TransformationMatrix;

public class Transformation {
	
	private TransformationMatrix transformationMatrix;
	
	public Transformation(TransformationMatrix transformationMatrix) {
		this.transformationMatrix = transformationMatrix;
	}
	
	public void applyToVertices(List<Vertex> transformables) {
		if(transformables == null || this.transformationMatrix == null) return;
		Iterator<Vertex> vertexIterator = transformables.iterator();
		
		while(vertexIterator.hasNext()) {
			this.applyTo(vertexIterator.next());
		}
	}
	
	public List<Vertex> copyAndApplyToVertices(List<Vertex> transformables) {
		List<Vertex> result = new ArrayList<Vertex>();
		if(transformables == null || this.transformationMatrix == null) return result;
		Iterator<Vertex> vertexIterator = transformables.iterator();
		
		while(vertexIterator.hasNext()) {
			Vertex vertex = new Vertex(vertexIterator.next()); 
			this.applyTo(vertex);
			result.add(vertex);
		}
		
		return result;
	}
	
	public List<Normal> copyAndApplyToNormals(List<Normal> transformables) {
		List<Normal> result = new ArrayList<Normal>();
		if(transformables == null || this.transformationMatrix == null) return result;
		Iterator<Normal> vertexIterator = transformables.iterator();
		
		while(vertexIterator.hasNext()) {
			Normal vertex = new Normal(vertexIterator.next()); 
			this.applyTo(vertex);
			result.add(vertex);
		}
		
		return result;
	}
	
	public void applyToNormals(List<Normal> transformables) {
		if(transformables == null || this.transformationMatrix == null) return;
		Iterator<Normal> normalIterator = transformables.iterator();
		
		while(normalIterator.hasNext()) {
			this.applyTo(normalIterator.next());
		}
	}
	
	public void applyTo(Transformable transformable) {
		if(transformable == null || this.transformationMatrix == null) return;
		
		float x = this.getXHomogeneous(transformable.getX(), transformable.getY(), transformable.getZ());
		float y = this.getYHomogeneous(transformable.getX(), transformable.getY(), transformable.getZ());
		float z = this.getZHomogeneous(transformable.getX(), transformable.getY(), transformable.getZ());
		float w = this.getWHomogeneous(transformable.getX(), transformable.getY(), transformable.getZ());
		
		x = x / w;
		y = y / w;
		z = z / w;
		
		transformable.setX(x);
		transformable.setY(y);
		transformable.setZ(z);
	}
	
	
	private float getXHomogeneous(float x, float y, float z) {
		return (float)transformationMatrix.getMatrix()[0][0] * x + 
				(float)transformationMatrix.getMatrix()[0][1] * y +
				(float)transformationMatrix.getMatrix()[0][2] * z +
				(float)transformationMatrix.getMatrix()[0][3] * 1;
	}
	
	private float getYHomogeneous(float x, float y, float z) {
		return (float)transformationMatrix.getMatrix()[1][0] * x + 
				(float)transformationMatrix.getMatrix()[1][1] * y +
				(float)transformationMatrix.getMatrix()[1][2] * z +
				(float)transformationMatrix.getMatrix()[1][3] * 1;
	}
	
	private float getZHomogeneous(float x, float y, float z) {
		return (float)transformationMatrix.getMatrix()[2][0] * x + 
				(float)transformationMatrix.getMatrix()[2][1] * y +
				(float)transformationMatrix.getMatrix()[2][2] * z +
				(float)transformationMatrix.getMatrix()[2][3] * 1;
	}
	
	private float getWHomogeneous(float x, float y, float z) {
		return (float)transformationMatrix.getMatrix()[3][0] * x + 
				(float)transformationMatrix.getMatrix()[3][1] * y +
				(float)transformationMatrix.getMatrix()[3][2] * z +
				(float)transformationMatrix.getMatrix()[3][3] * 1;
	}

}
