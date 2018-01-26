package org.obj2openjl.v3.model.transform.matrix;


public class RotationMatrix extends TransformationMatrix {
	
	private double angleRadians;
	
	public RotationMatrix(float angleDegrees, float x, float y, float z) {
		super();
		this.angleRadians = Math.toRadians(angleDegrees);
		this.x = x;
		this.y = y;
		this.z = z;
		this.matrix = this.buildMatrix();
	}
	
	@Override
	protected double[][] buildMatrix() {
		double angleCos = Math.cos(this.angleRadians);
		double angleSin = Math.sin(this.angleRadians);
		float x = this.x;
		float y = this.y;
		float z = this.z;
		return new double[][] {
				{epsilonCheck(angleCos + x*x*(1-angleCos)),		epsilonCheck(x*y*(1-angleCos) - z*angleSin),	epsilonCheck(x*z*(1 - angleCos) + y*angleSin), 	0},
				{epsilonCheck(x*y*(1 - angleCos) + z*angleSin),	epsilonCheck(angleCos + y*y*(1 - angleCos)),	epsilonCheck(y*z*(1 - angleCos) - x*angleSin),	0},
				{epsilonCheck(x*z*(1 - angleCos) - y*angleSin),	epsilonCheck(y*z*(1 - angleCos) + x*angleSin),	epsilonCheck(angleCos + z*z*(1 - angleCos)),	0},
				{0,												0,												0,												1}
		};
	}
	
}
