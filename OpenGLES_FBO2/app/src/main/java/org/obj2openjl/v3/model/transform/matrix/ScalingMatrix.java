package org.obj2openjl.v3.model.transform.matrix;


public class ScalingMatrix extends TransformationMatrix {
	
	public ScalingMatrix(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	protected double[][] buildMatrix() {
		float x = this.x;
		float y = this.y;
		float z = this.z;
		return new double[][] {
				{x,	0,	0,	0},
				{0,	y,	0,	0},
				{0,	0,	z,	0},
				{0,	0,	0,	1}
		};
	}
	
}
