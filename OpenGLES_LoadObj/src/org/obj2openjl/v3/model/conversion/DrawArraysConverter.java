package org.obj2openjl.v3.model.conversion;

import org.obj2openjl.v3.model.DirectionalVertex;

public class DrawArraysConverter extends OpenGLModelConverterImpl {
	
	//private short index = 0;
	
	@Override
	protected void handleDirectionalVertex(DirectionalVertex vertex) {
		this.addVertex(vertex.getVertex());
		this.addNormal(vertex.getNormal());
		this.addTextureCoordinates(vertex.getTexturePoint());
	}
	
}
