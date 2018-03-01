package org.obj2openjl.v3.model.transform.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.obj2openjl.v3.model.DirectionalVertex;
import org.obj2openjl.v3.model.Face;
import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.TexturePoint;
import org.obj2openjl.v3.model.Vertex;
import org.obj2openjl.v3.util.ListUtils;

public abstract class ArrayTransformation {
	
	private int numberOfInstances;
	protected static int currentIndex = 0;
	
	private class Helper {
		
		public int numberOfVertices;
		public List<Vertex> vertices;
		public List<Normal> normals;
		public List<TexturePoint> texturePoints;
		public List<Short> indices;
		
		public Helper() {
			this.numberOfVertices = 0;
			this.indices = new ArrayList<Short>();
			this.vertices = new ArrayList<Vertex>();
			this.texturePoints = new ArrayList<TexturePoint>();
			this.normals = new ArrayList<Normal>();
		}
		
		public void addValuesOf(DirectionalVertex vertex) {
			this.vertices.add(new Vertex(vertex.getVertex()));
			if(vertex.getNormal() != null) {
				this.normals.add(new Normal(vertex.getNormal()));				
			}
			if(vertex.getTexturePoint() != null) {
				this.texturePoints.add(new TexturePoint(vertex.getTexturePoint()));
			}
			this.indices.add(vertex.getIndex());
		}
	}
	
	public ArrayTransformation(int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}
	
	public int getNumberOfInstances() {
		return this.numberOfInstances;
	}
	
	public List<Face> transformFaces(List<Face> originalFaces) {
		List<Face> result = new ArrayList<Face>();
		Iterator<Face> faceIterator = originalFaces.iterator();
		while(faceIterator.hasNext()) {
			result.addAll(this.transformFace(faceIterator.next()));
		}
		return result;
	}
	
	private List<Face> transformFace(Face face) {
		Helper helper = this.gatherLists(face);
		
		helper.vertices = this.transformVertices(helper.vertices);
		helper.normals = this.transformNormals(helper.normals);
		helper.texturePoints = this.transformTextureCoordinates(helper.texturePoints);
		helper.indices = this.transformIndices(helper.indices, ListUtils.getMax(helper.indices));
		
		return this.split(helper);
		
	}
	
	private List<Face> split(Helper helper) {
		List<Face> result = new ArrayList<Face>();
		for(int i = 0; i < (helper.vertices.size() / helper.numberOfVertices); i++) {
			List<DirectionalVertex> vertices = new ArrayList<DirectionalVertex>();
			for(int j = 0; j < helper.numberOfVertices; j++) {
				int currentPos = i*helper.numberOfVertices + j;
				Normal normal = helper.normals.size() - 1 < currentPos ? null : helper.normals.get(currentPos);
				TexturePoint texturePoint = helper.texturePoints.size() - 1 < currentPos ? null : helper.texturePoints.get(currentPos);
				vertices.add(new DirectionalVertex(helper.indices.get(currentPos),
						helper.vertices.get(currentPos),
						normal,
						texturePoint));
			}
			result.add(new Face(vertices));
		}
		return result;
	}
	
	private Helper gatherLists(Face face) {
		Iterator<DirectionalVertex> vertexIterator = face.getVertices().iterator();
		Helper helper = new Helper();
		helper.numberOfVertices = face.getVertices().size();
		while(vertexIterator.hasNext()) {
			helper.addValuesOf(vertexIterator.next());
		}
		return helper;
	}
	
	public abstract List<Vertex> transformVertices(List<Vertex> vertices);
	
	// Default: copy normals
	public List<Normal> transformNormals(List<Normal> normals) {
		List<Normal> result = new ArrayList<Normal>();
		for(int i = 0; i < this.numberOfInstances; i++) {
			Iterator<Normal> normalIterator = normals.iterator();
			while(normalIterator.hasNext()) {
				result.add(normalIterator.next());
			}
		}
		return result;
	}
	
	// Default: copy texture points
	public List<TexturePoint> transformTextureCoordinates(List<TexturePoint> textureCoordinates) {
		List<TexturePoint> result = new ArrayList<TexturePoint>();
		for(int i = 0; i < this.numberOfInstances; i++) {
			Iterator<TexturePoint> textureCoordinateIterator = textureCoordinates.iterator();
			while(textureCoordinateIterator.hasNext()) {
				result.add(textureCoordinateIterator.next());
			}
		}
		return result;
	}
	
	// Default: copy indices, unique indices for each object
	public List<Short> transformIndices(List<Short> indices, int maxIndex) {
		List<Short> result = new ArrayList<Short>();
		for(short i = 1; i <= this.numberOfInstances; i++) {
			for(short j = 0; j < indices.size(); j++) {
				result.add((short)(indices.get(j) + i*(maxIndex + 1)));
			}
		}
		return result;
	}
	
}
