package org.obj2openjl.v3.model.transform.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.TexturePoint;
import org.obj2openjl.v3.model.Vertex;

public class CombineTransformation extends ArrayTransformation {
	
	private List<ArrayTransformation> transformations;
	
	public CombineTransformation(List<ArrayTransformation> transformations) {
		super(0);
		this.transformations = transformations;
	}

	@Override
	public List<Vertex> transformVertices(List<Vertex> vertices) {
		Iterator<ArrayTransformation> transformationIterator = this.transformations.iterator();
		List<Vertex> result = new ArrayList<Vertex>();
		while(transformationIterator.hasNext()) {
			ArrayTransformation tf = transformationIterator.next();
			System.out.println("performing " + tf.getClass().getSimpleName());
			List<Vertex> temp = tf.transformVertices(vertices);
			Iterator<Vertex> it = temp.iterator();
			result.addAll(temp);
			while(it.hasNext()) {
				System.out.print(it.next() + " ");
			}
			System.out.println("performed " + tf.getClass().getSimpleName());
			System.out.println(result.size());
		}
		return result;
	}

	@Override
	public List<Normal> transformNormals(List<Normal> normals) {
		Iterator<ArrayTransformation> transformationIterator = this.transformations.iterator();
		List<Normal> result = new ArrayList<Normal>(); 
		while(transformationIterator.hasNext()) {
			result.addAll(transformationIterator.next().transformNormals(normals));
		}
		return result;
	}

	@Override
	public List<TexturePoint> transformTextureCoordinates(List<TexturePoint> textureCoordinates) {
		Iterator<ArrayTransformation> transformationIterator = this.transformations.iterator();
		List<TexturePoint> result = new ArrayList<TexturePoint>(); 
		while(transformationIterator.hasNext()) {
			result.addAll(transformationIterator.next().transformTextureCoordinates(textureCoordinates));
		}
		return result;
	}

	@Override
	public List<Short> transformIndices(List<Short> indices, int maxIndex) {
		Iterator<ArrayTransformation> transformationIterator = this.transformations.iterator();
		List<Short> result = new ArrayList<Short>();
		int indexOffset = maxIndex;
		while(transformationIterator.hasNext()) {
			ArrayTransformation transformation = transformationIterator.next();
			result.addAll(transformation.transformIndices(indices, indexOffset));
			indexOffset += transformation.getNumberOfInstances() * maxIndex;
		}
		return result;
	}

}
