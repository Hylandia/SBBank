package com.gavingogaming.skyblockbanker.managers;

public class EasyManager {
	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		}catch(NumberFormatException e) {
			return false;
		}
	}
}
