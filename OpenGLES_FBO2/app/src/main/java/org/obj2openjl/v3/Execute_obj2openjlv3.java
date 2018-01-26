package org.obj2openjl.v3;

import java.io.FileNotFoundException;

import org.obj2openjl.v3.model.OpenGLModelData;
import org.obj2openjl.v3.model.RawOpenGLModel;

public class Execute_obj2openjlv3 {
	
	public static void main(String [] args) {
		OpenGLModelData openGLModelData;
		try {
			RawOpenGLModel openGLModel = new Obj2OpenJL().convert("src/org/obj2openjl/boxes");
//			List<ArrayTransformation> transformations = new ArrayList<ArrayTransformation>();
//			transformations.add(new TranslationTransformation(2, 1.f, 0.f, 0.f));
//			transformations.add(new TranslationTransformation(2, 0.f, 1.f, 0.f));
//			transformations.add(new RotationTransformation(2, 360.f, 0.f, 0.f, 1.f));
//			CombineTransformation transformation = new CombineTransformation(transformations);
//			openGLModel.setTransformation(transformation);
			openGLModelData = openGLModel.normalize().center().getDataForGLDrawElements();
			
			printArray(openGLModelData.getVertices(), "Vertices");
			printArray(openGLModelData.getNormals(), "Normals");
			printArray(openGLModelData.getTextureCoordinates(), "Texture coordinates");
			printArray(openGLModelData.getIndices(), "Indices");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void printArray(float[] array, String label) {
		if(array == null) return;
		System.out.println(label + ": " + array.length);
		int c = 0;
		for(int i = 0; i < array.length; i++) {
			if(c != 0 && c % 10 == 0) {
				System.out.println();
			}
			System.out.print(array[i] + "f");
			if(i < array.length - 1) {
				System.out.print(", ");
			}
			c++;
		}
		System.out.println();
		System.out.println();
	}
	
	public static void printArray(int[] array, String label) {
		if(array == null) return;
		System.out.println(label + ": " + array.length);
		int c = 0;
		for(int i = 0; i < array.length; i++) {
			if(c != 0 && c % 3 == 0) {
				System.out.println();
			}
			System.out.print(array[i]);
			if(i < array.length - 1) {
				System.out.print(", ");
			}
			c++;
		}
		System.out.println();
		System.out.println();
	}
	
	public static void printArray(short[] array, String label) {
		if(array == null) return;
		System.out.println(label + ": " + array.length);
		int c = 0;
		for(int i = 0; i < array.length; i++) {
			if(c != 0 && c % 3 == 0) {
				System.out.println();
			}
			System.out.print(array[i]);
			if(i < array.length - 1) {
				System.out.print(", ");
			}
			c++;
		}
		System.out.println();
		System.out.println();
	}

}
