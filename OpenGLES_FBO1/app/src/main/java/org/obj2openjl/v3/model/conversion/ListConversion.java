package org.obj2openjl.v3.model.conversion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ListConversion {
	
	public static float[] convert(List<? extends FloatArrayConvertible> convertibles) {
		Iterator<? extends FloatArrayConvertible> convertibleIterator = convertibles.iterator();
		List<Float> floats = new ArrayList<Float>();
		
		while(convertibleIterator.hasNext()) {
			floats.addAll(Arrays.asList(
					convertibleIterator.next().getFloatValues()));
		}
		
		return ListConversion.toPrimitiveFloat(floats);
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

}
