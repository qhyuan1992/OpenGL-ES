package org.obj2openjl.v3.model.conversion;

import java.util.HashSet;
import java.util.Set;

import org.obj2openjl.v3.model.DirectionalVertex;

public class DrawElementsConverter extends OpenGLModelConverterImpl {
	
	private Set<Short> handledIndices;
	
	public DrawElementsConverter() {
		super();
		this.handledIndices = new HashSet<Short>();
	}

	@Override
	protected void handleDirectionalVertex(DirectionalVertex vertex) {
		if(this.handledIndices.add(vertex.getIndex())) {
			this.addVertex(vertex.getVertex());
			this.addNormal(vertex.getNormal());
			this.addTextureCoordinates(vertex.getTexturePoint());
		}
		this.addIndex(vertex.getIndex());
	}

}