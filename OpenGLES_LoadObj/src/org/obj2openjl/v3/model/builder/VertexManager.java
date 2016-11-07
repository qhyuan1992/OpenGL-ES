package org.obj2openjl.v3.model.builder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.obj2openjl.v3.model.BoundingBox3D;
import org.obj2openjl.v3.model.DirectionalVertex;
import org.obj2openjl.v3.model.FaceProperty;
import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.TexturePoint;
import org.obj2openjl.v3.model.Vertex;

public class VertexManager {
	
	private short currentVertexID;
	private List<Vertex> vertices;
	private List<Normal> normals;
	private List<TexturePoint> textureCoordinates;
	private Map<String, DirectionalVertex> idVertexMap = new HashMap<String, DirectionalVertex>();
	
	private Float minX = null;
	private Float minY = null;
	private Float minZ = null;
	private Float maxX = null;
	private Float maxY = null;
	private Float maxZ = null;
	
	public VertexManager(List<Vertex> vertices, List<Normal> normals, List<TexturePoint> textureCoordinates) {
		this.currentVertexID = 0;
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
	}
	
	public DirectionalVertex buildAndRegisterVertex(FaceProperty faceProperty) {
		int vertexID = faceProperty.getVertexIndex();
		Integer normalID = faceProperty.getNormalIndex();
		Integer textureCoordinateID = faceProperty.getTexturePointIndex();
		
		String globalVertexIdentifier = this.buildGlobalVertexIdentifierFrom(vertexID, normalID);
		DirectionalVertex vertex = this.idVertexMap.get(globalVertexIdentifier);
		if(vertex == null) {
			this.checkBounds(vertexID);
			vertex = new DirectionalVertex(
					this.currentVertexID++,
					this.getVertexAt(vertexID),
					this.getNormalAt(normalID),
					this.getTextureCoordinatesAt(textureCoordinateID)); 
			this.idVertexMap.put(globalVertexIdentifier, vertex);
		}
		
		return vertex;
	}
	
	private void checkBounds(int vertexID) {
		Vertex vertex = this.getVertexAt(vertexID);
		float x = vertex.getFloatValues()[0];
		float y = vertex.getFloatValues()[1];
		float z = vertex.getFloatValues()[2];
		if(this.minX == null) {
			this.minX = x;
			this.maxX = x;
			this.minY = y;
			this.maxY = y;
			this.minZ = z;
			this.maxZ = z;
		} else {
			this.checkBoundsX(x);
			this.checkBoundsY(y);
			this.checkBoundsZ(z);
		}
	}
	
	private void checkBoundsX(float x) {
		if(x < this.minX) {
			this.minX = x;
		}
		if(x > this.maxX) {
			this.maxX = x;
		}
	}
	
	private void checkBoundsY(float y) {
		if(y < this.minY) {
			this.minY = y;
		}
		if(y > this.maxY) {
			this.maxY = y;
		}
	}
	
	private void checkBoundsZ(float z) {
		if(z < this.minZ) {
			this.minZ = z;
		}
		if(z > this.maxZ) {
			this.maxZ = z;
		}
	}
	
	private Vertex getVertexAt(int index) {
		return this.vertices.get(index);
	}
	
	private Normal getNormalAt(Integer index) {
		return index == null ? null : this.normals.get(index);
	}
	
	private TexturePoint getTextureCoordinatesAt(Integer index) {
		return index == null ? null : this.textureCoordinates.get(index);
	}
	
	private String buildGlobalVertexIdentifierFrom(int vertexID, int normalID) {
		return vertexID + "." + normalID;
	}
	
	public BoundingBox3D getVertexBoundingBox() {
		return new BoundingBox3D(this.minX,
				this.maxX,
				this.minY,
				this.maxY,
				this.minZ,
				this.maxZ);
	}
	
}
