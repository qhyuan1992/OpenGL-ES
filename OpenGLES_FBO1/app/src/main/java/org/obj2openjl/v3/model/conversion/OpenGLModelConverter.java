package org.obj2openjl.v3.model.conversion;

import java.util.List;

import org.obj2openjl.v3.model.Face;
import org.obj2openjl.v3.model.OpenGLModelData;

public interface OpenGLModelConverter {
	
	public OpenGLModelData convert(List<Face> faces);

}