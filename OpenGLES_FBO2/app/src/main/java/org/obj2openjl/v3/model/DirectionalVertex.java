package org.obj2openjl.v3.model;


public class DirectionalVertex {

	private short index;
	private Vertex vertex;
	private Normal normal;
	private TexturePoint texturePoint;
	
	public DirectionalVertex(short index, Vertex vertex, Normal normal, TexturePoint texturePoint) {
		this.index = index;
		this.vertex = vertex;
		this.normal = normal;
		this.texturePoint = texturePoint;
	}
	
	public short getIndex() {
		return this.index;
	}
	
	public Vertex getVertex() {
		return this.vertex;
	}
	
	public Normal getNormal() {
		return normal;
	}

	public void setNormal(Normal normal) {
		this.normal = normal;
	}

	public TexturePoint getTexturePoint() {
		return texturePoint;
	}

	public void setTexturePoint(TexturePoint texturePoint) {
		this.texturePoint = texturePoint;
	}

}
