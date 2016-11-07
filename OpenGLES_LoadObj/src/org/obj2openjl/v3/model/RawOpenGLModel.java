package org.obj2openjl.v3.model;

import java.util.ArrayList;
import java.util.List;

import org.obj2openjl.v3.model.conversion.DrawArraysConverter;
import org.obj2openjl.v3.model.conversion.DrawElementsConverter;
import org.obj2openjl.v3.model.transform.Transformation;
import org.obj2openjl.v3.model.transform.array.ArrayTransformation;
import org.obj2openjl.v3.model.transform.array.CombineTransformation;
import org.obj2openjl.v3.model.transform.array.DefaultTransformation;
import org.obj2openjl.v3.model.transform.matrix.RotationMatrix;
import org.obj2openjl.v3.model.transform.matrix.ScalingMatrix;
import org.obj2openjl.v3.model.transform.matrix.TransformationMatrix;
import org.obj2openjl.v3.model.transform.matrix.TranslationMatrix;

public class RawOpenGLModel {
	
	private List<Face> faces;
	private BoundingBox3D boundingBox;
	private final List<Vertex> vertexPool;
	private ArrayTransformation transformation;
	
	/**
	 * Creates a raw openGL model that holds model data model-independently. Operations performed on these
	 * objects do not affect OpenGLModelData instances that have been retrieved before.
	 * 
	 * 
	 * @param faces A list of faces, that defines the surface of a model.
	 * @param boundingBox The bounding box enclosing the original model.
	 * @param vertexPool The vertex object directly extracted from the .obj file, used for minimizing coordinate changes when scaling and translating.
	 */
	public RawOpenGLModel(List<Face> faces,
			BoundingBox3D boundingBox,
			List<Vertex> vertexPool) {
		this.faces = faces;
		this.boundingBox = boundingBox;
		this.vertexPool = vertexPool;
		this.transformation = new DefaultTransformation();
	}
	
	public RawOpenGLModel setArrayTransformation(ArrayTransformation transformation) {
		List<ArrayTransformation> transformations = new ArrayList<ArrayTransformation>();
		// Make sure the original object data is included
		transformations.add(new DefaultTransformation());
		transformations.add(transformation);
		this.transformation = new CombineTransformation(transformations);
		return this;
	}

	public List<Face> getFaces() {
		return faces;
	}

	public OpenGLModelData getDataForGLDrawArrays() {
		return new DrawArraysConverter().convert(this.transformation.transformFaces(this.faces));
	}
	
	public OpenGLModelData getDataForGLDrawElements() {
		return new DrawElementsConverter().convert(this.transformation.transformFaces(this.faces));
	}
	
	public RawOpenGLModel scale(float scaleFactor) {
		TransformationMatrix scalingMatrix = new ScalingMatrix(scaleFactor, scaleFactor, scaleFactor);
		Transformation scaling = new Transformation(scalingMatrix);
		
		scaling.applyToVertices(this.vertexPool);
		this.boundingBox.scale(scaleFactor);
		
		return this;
	}
	
	public RawOpenGLModel center() {
		float[] formerCenter = this.boundingBox.center();
		
		TransformationMatrix translationMatrix = new TranslationMatrix(-formerCenter[0], -formerCenter[1], -formerCenter[2]);
		Transformation centering = new Transformation(translationMatrix);
		
		centering.applyToVertices(this.vertexPool);
		
		return this;
	}
	
	public RawOpenGLModel normalize() {
		float scaleFactor = this.boundingBox.getLengthX();
		scaleFactor = this.boundingBox.getLengthY() > scaleFactor ?
				this.boundingBox.getLengthY() : scaleFactor;
		scaleFactor = this.boundingBox.getLengthZ() > scaleFactor ?
				this.boundingBox.getLengthZ() : scaleFactor;
		scaleFactor = scaleFactor == 0 ? 1 : 1/scaleFactor;
		
		return this.scale(scaleFactor);
	}
	
	public BoundingBox3D getBoundingBox() {
		return new BoundingBox3D(this.boundingBox);
	}
	
	public RawOpenGLModel rotate(float angleDegrees, float x, float y, float z) {
		TransformationMatrix rotationMatrix = new RotationMatrix(angleDegrees, x, y, z);
		Transformation rotation = new Transformation(rotationMatrix);
		
		rotation.applyToVertices(this.vertexPool);
		this.boundingBox = new BoundingBox3D(this.vertexPool);
		
		return this;
	}
	
	public RawOpenGLModel translate(float distanceX, float distanceY, float distanceZ) {
		TransformationMatrix translationMatrix = new TranslationMatrix(distanceX, distanceY, distanceZ);
		Transformation translation = new Transformation(translationMatrix);
		
		translation.applyToVertices(this.vertexPool);
		this.boundingBox.translate(distanceX, distanceY, distanceZ);
		
		return this;
	}
	
	public List<Vertex> getVertexPool() {
		return this.vertexPool;
	}
	
}