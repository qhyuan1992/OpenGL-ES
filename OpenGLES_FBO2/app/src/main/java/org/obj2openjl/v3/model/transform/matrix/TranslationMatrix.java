package org.obj2openjl.v3.model.transform.matrix;


public class TranslationMatrix extends TransformationMatrix {
	
	public TranslationMatrix(float x, float y, float z) {
		super(x, y, z);
	}
	
	@Override
	protected double[][] buildMatrix() {
		float x = this.x;
		float y = this.y;
		float z = this.z;
		return new double[][] {
				{1,	0,	0,	x},
				{0,	1,	0,	y},
				{0,	0,	1,	z},
				{0,	0,	0,	1}
		};
	}

}
