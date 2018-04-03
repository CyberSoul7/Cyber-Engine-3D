package com.cybersgames.engine3.engine;

import java.io.InputStream;
import java.util.Scanner;

public class Utils {
	
	public static String loadResource(String fileName) throws Exception {
		String result;
		try (InputStream in = Class.forName(Utils.class.getName()).getResourceAsStream(fileName);
			 Scanner scanner = new Scanner(in, "UTF-8")) {
			result = scanner.useDelimiter("\\A").next();
		}
		return result;
	}
	
	public static void arrayByScalar(int[] array, int scalar) {
		for (int i = 0; i < array.length; i++) {
			array[i] *= scalar;
		}
	}
	
	public static void arrayByScalar(float[] array, int scalar) {
		for (int i = 0; i < array.length; i++) {
			array[i] *= scalar;
		}
	}
	
	public static float clamp(float value, float min, float max) {
		return (Math.max(Math.min(value, max), min));
	}
	
	public static float map(float value, float oldMin, float oldMax, float newMin, float newMax) {
		return ((value-oldMin)/(oldMax-oldMin) * (newMax-newMin) + newMin);
	}
	
}
