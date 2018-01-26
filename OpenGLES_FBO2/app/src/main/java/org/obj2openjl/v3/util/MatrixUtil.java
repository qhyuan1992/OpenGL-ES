package org.obj2openjl.v3.util;

public class MatrixUtil {
	
	public double[][] multiply(double[][] left, double[][] top) {
		if(left[0].length != top.length) return null;
		
		int resultHeight = left.length;
		int resultWidth = top[0].length;
		int leftWidth = left[0].length;
		int topHeight = top.length;
		double[][] result = new double[resultHeight][resultWidth];
		
		for(int resultRow = 0; resultRow < resultHeight; resultRow++) {
			for(int resultColumn = 0; resultColumn < resultWidth; resultColumn++) {
				double resultAtCurrentPosition = 0;
				for(int leftColumn = 0; leftColumn < leftWidth; leftColumn++) {
					for(int topRow = 0; topRow < topHeight; topRow++) {
						resultAtCurrentPosition += left[resultRow][leftColumn] * top[topRow][resultColumn];
					}
				}
				result[resultRow][resultColumn] = resultAtCurrentPosition;
			}
		}
		
		return result;
	}
	
	public void print(double[][] matrix) {
		for(int i = 0; i < matrix.length; i++) {
			for(int j = 0; j < matrix.length; j++) {
				System.out.println(matrix[i][j] + "\t");
			}
			System.out.println();
		}
	}

}
