package org.obj2openjl.v3.model;

public class FaceProperty {
	
	private int vertexIndex;
	private Integer normalIndex;
	private Integer texturePointIndex;
	
	public FaceProperty(Integer vertexIndex, Integer normalIndex, Integer texturePointIndex) {
		this.vertexIndex = (int) (vertexIndex - 1);
		this.normalIndex = normalIndex;
		this.texturePointIndex = texturePointIndex;
		
		if(normalIndex != null) {
			this.normalIndex = (int) (normalIndex - 1);
		}
		
		if(texturePointIndex != null) {
			this.texturePointIndex = (int) (texturePointIndex - 1);
		}
	}

	public int getVertexIndex() {
		return vertexIndex;
	}

	public Integer getNormalIndex() {
		return normalIndex;
	}

	public Integer getTexturePointIndex() {
		return texturePointIndex;
	}

}
