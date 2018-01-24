package org.obj2openjl.v3.model;

import java.util.Iterator;
import java.util.List;

public class Face {
	
	private List<DirectionalVertex> vertices;
	
	public Face(List<DirectionalVertex> vertices) {
		this.vertices = vertices;
	}

	public List<DirectionalVertex> getVertices() {
		return vertices;
	}
	
	public String toString() {
		Iterator<DirectionalVertex> it = this.vertices.iterator();
		
		String res = "";
		while(it.hasNext()) {
			res += it.next().toString() + "\n";
		}
		
		return res;
	}
	
}
