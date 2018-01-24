package org.obj2openjl.v3.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ListUtils {
	
	public static float[] toFloat(List<? extends FloatArrayConvertible> convertibles) {
		Iterator<? extends FloatArrayConvertible> convertibleIterator = convertibles.iterator();
		List<Float> floats = new ArrayList<Float>();
		
		while(convertibleIterator.hasNext()) {
			FloatArrayConvertible convertible = convertibleIterator.next();
			if(convertible != null) {
				floats.addAll(Arrays.asList(
						convertible.getFloatValues()));
			}
		}
		
		return ListUtils.toPrimitiveFloat(floats);
	}
	
	public static float[] toPrimitiveFloat(List<Float> floats) {
		int nullValueCount = 0;
		float[] primitives = new float[floats.size()];
		
		for(int i = 0; i < floats.size(); i++) {
			Float floatObject = floats.get(i);
			if(floatObject == null) {
				nullValueCount++;
			} else {
				primitives[i - nullValueCount] = floatObject;
			}
		}
		
		float[] result = primitives;
		if(nullValueCount > 0) {
			result = new float[primitives.length - nullValueCount];
			System.arraycopy(primitives, 0, result, 0, result.length);
		}
		
		return result;
	}
	
	public static int[] toPrimitiveInteger(List<Integer> ints) {
		int nullValueCount = 0;
		int[] primitives = new int[ints.size()];
		
		for(int i = 0; i < ints.size(); i++) {
			Integer intObject = ints.get(i);
			if(intObject == null) {
				nullValueCount++;
			} else {
				primitives[i - nullValueCount] = intObject;
			}
		}
		
		int[] result = primitives;
		if(nullValueCount > 0) {
			result = new int[primitives.length - nullValueCount];
			System.arraycopy(primitives, 0, result, 0, result.length);
		}
		
		return result;
	}
	
	public static short[] toPrimitiveShort(List<Short> shorts) {
		int nullValueCount = 0;
		short[] primitives = new short[shorts.size()];
		
		for(int i = 0; i < shorts.size(); i++) {
			Short shortObject = shorts.get(i);
			if(shortObject == null) {
				nullValueCount++;
			} else {
				primitives[i - nullValueCount] = shortObject;
			}
		}
		
		short[] result = primitives;
		if(nullValueCount > 0) {
			result = new short[primitives.length - nullValueCount];
			System.arraycopy(primitives, 0, result, 0, result.length);
		}
		
		return result;
	}
	
	public static float[] mergeArrays(float[] ar1, float[] ar2) {
		float[] result = new float[ar1.length + ar2.length];
		System.arraycopy(ar1, 0, result, 0, ar1.length);
		System.arraycopy(ar2, 0, result, ar1.length, ar2.length);
		return result;
	}
	
	public static short[] mergeArrays(short[] ar1, short[] ar2) {
		short[] result = new short[ar1.length + ar2.length];
		System.arraycopy(ar1, 0, result, 0, ar1.length);
		System.arraycopy(ar2, 0, result, ar1.length, ar2.length);
		return result;
	}
	
	public static float getMax(float[] array) {
		Float result = null;
		for(float f : array) {
			result = result == null ? f : result < f ? f : result;
		}
		return result != null ? result : 0;
	}
	
	public static short getMax(short[] array) {
		Short result = null;
		for(short f : array) {
			result = result == null ? f : result < f ? f : result;
		}
		return result != null ? result : 0;
	}
	
	public static short getMax(List<Short> shorts) {
		Iterator<Short> shortIterator = shorts.iterator();
		Short result = null;
		while(shortIterator.hasNext()) {
			short currentShort = shortIterator.next();
			result = result == null ? currentShort : result < currentShort ? currentShort : result;
		}
		return result != null ? result : 0;
	}

}
