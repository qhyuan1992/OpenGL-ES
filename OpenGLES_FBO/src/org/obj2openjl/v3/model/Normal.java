package org.obj2openjl.v3.model;

import java.util.List;

import org.obj2openjl.v3.model.transform.Transformable;
import org.obj2openjl.v3.util.FloatArrayConvertible;

public class Normal extends FloatArrayConvertible implements Transformable {
	
	private float x;
	private float y;
	private float z;
	
	public Normal(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Normal(Normal normal) {
		this.x = normal.x;
		this.y = normal.y;
		this.z = normal.z;
	}
	
	public Normal(List<Float> coordinates) {
		this(coordinates.get(0),
				coordinates.get(1),
				coordinates.get(2));
	}
	
	public String toString() {
		return "Normal [" + this.x + ", " + this.y + ", " + this.z + "]";
	}

	@Override
	public Float[] getFloatValues() {
		return new Float[] {this.x, this.y, this.z};
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

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getZ() {
		return z;
	}
	
	
	
}
