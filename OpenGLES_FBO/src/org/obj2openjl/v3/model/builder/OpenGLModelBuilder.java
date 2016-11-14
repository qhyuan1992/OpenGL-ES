package org.obj2openjl.v3.model.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.obj2openjl.v3.model.DirectionalVertex;
import org.obj2openjl.v3.model.Face;
import org.obj2openjl.v3.model.FaceProperty;
import org.obj2openjl.v3.model.Normal;
import org.obj2openjl.v3.model.RawOpenGLModel;
import org.obj2openjl.v3.model.TexturePoint;
import org.obj2openjl.v3.model.Vertex;


public class OpenGLModelBuilder {
	
	public RawOpenGLModel buildOpenGLModel(List<Vertex> vertices,
			List<Normal> normals,
			List<TexturePoint> texturePoints,
			List<List<FaceProperty>> faceProperties) {
		
		Iterator<List<FaceProperty>> facePropertyIterator = faceProperties.iterator();
		FaceBuilder faceBuilder = new FaceBuilder(vertices, normals, texturePoints);
		
		while(facePropertyIterator.hasNext()) {
			faceBuilder.buildFace(facePropertyIterator.next());
		}
		
		return new RawOpenGLModel(faceBuilder.getFaces(),
				faceBuilder.vertexManager.getVertexBoundingBox(),
				vertices);
	}
	
	private static class FaceBuilder {
		
		private VertexManager vertexManager;
		private List<Face> faces;
		
		public FaceBuilder(List<Vertex> vertices,
				List<Normal> normals,
				List<TexturePoint> texturePoints) {
			this.vertexManager = new VertexManager(vertices, normals, texturePoints);
			this.faces = new ArrayList<Face>();
		}
		
		public void buildFace(List<FaceProperty> faceProperties) {
			Iterator<FaceProperty> facePropertyIterator = faceProperties.iterator();
			List<DirectionalVertex> vertices = new ArrayList<DirectionalVertex>();
			
			while(facePropertyIterator.hasNext()) {
				vertices.add(this.vertexManager.buildAndRegisterVertex(facePropertyIterator.next()));
			}
			
			faces.add(new Face(vertices));
		}
		
		public List<Face> getFaces() {
			return this.faces;
		}
	}
	
}
