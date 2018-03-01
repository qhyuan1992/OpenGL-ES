package org.obj2openjl.v3.model;

import org.obj2openjl.v3.model.transform.Transformable;
import org.obj2openjl.v3.util.FloatArrayConvertible;

public class Vertex extends FloatArrayConvertible implements Transformable {
	
	private float x;
	private float y;
	private float z;
	private Float w;
	
	public Vertex(float x, float y, float z, Float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}

	public Float getW() {
		return w;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public Vertex(Vertex vertex) {
		this.x = vertex.x;
		this.y = vertex.y;
		this.z = vertex.z;
		this.w = vertex.w;
	}
	
	public Float[] toArray() {
		return new Float[] {this.x, this.y, this.z, this.w};
	}
	
	public String toString() {
		return "Vertex [" + this.x + ", " + this.y + ", " + this.z + ", " + this.w + "]";
	}

	@Override
	public Float[] getFloatValues() {
		return new Float[] {this.x, this.y, this.z};//, this.w};
	}
	
}
