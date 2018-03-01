package org.obj2openjl.v3.model;


public class OpenGLModelData {
	
	private float[] vertices;
	private float[] normals;
	private float[] textureCoordinates;
	private short[] indices;
	
	public OpenGLModelData(
			float[] vertices,
			float[] normals,
			float[] textureCoordinates,
			short[] indices) {
		this.vertices = vertices;
		this.normals = normals;
		this.textureCoordinates = textureCoordinates;
		this.indices = indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getNormals() {
		return normals;
	}

	public float[] getTextureCoordinates() {
		return textureCoordinates;
	}

	public short[] getIndices() {
		return indices;
	}
	
}
