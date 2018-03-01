package org.obj2openjl.v3.model;

import java.util.List;

import org.obj2openjl.v3.util.FloatArrayConvertible;

public class TexturePoint extends FloatArrayConvertible {
	
	private float x;
	private float y;
	private Float w;
	
	public TexturePoint(float x, float y, Float w) {
		this.x = x;
		this.y = y;
		this.w = w;
	}
	
	public TexturePoint(TexturePoint texturePoint) {
		this.x = texturePoint.x;
		this.y = texturePoint.y;
		this.w = texturePoint.w;
	}
	
	public TexturePoint(List<Float> coordinates) {
		this(coordinates.get(0),
				coordinates.get(1),
				coordinates.get(2));
	}
	
	public Float[] toArray() {
		return new Float[] {this.x, this.y, this.w};
	}
	
	public String toString() {
		return "TexturePoint [" + this.x + ", " + this.y + ", " + this.w + "]";
	}

	@Override
	public Float[] getFloatValues() {
		return new Float[] {this.x, this.y};
	}

}
