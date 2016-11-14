package org.obj2openjl.v3.model.transform.matrix;

public abstract class TransformationMatrix {
	
	protected float x;
	protected float y;
	protected float z;
	protected double[][] matrix;
	
	public TransformationMatrix(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.matrix = this.buildMatrix();
	}
	
	protected TransformationMatrix() {
		this.x = 0.f;
		this.y = 0.f;
		this.z = 0.f;
		this.matrix = null;
	}
	
	protected abstract double[][] buildMatrix();
	
	public double[][] getMatrix() {
		return this.matrix;		
	}
	
	protected double epsilonCheck(double value) {
		if(Math.abs(value) - 1.0E-10 < 0) {
			return 0;
		} else {
			return value;
		}
	}
	
	protected void printMatrix() {
		if(this.getMatrix() == null) return;
		for(double[] rows: this.getMatrix()) {
			for(double val: rows) {
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}

}
